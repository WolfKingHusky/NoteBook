package com.huang.notetool.tool;

import com.huang.notetool.frame.common.JRootParentDiag;
import com.huang.notetool.frame.common.JRootParentFrame;
import com.huang.notetool.frame.common.ShowMsgSinglePanel;
import com.huang.notetool.util.Constants;
import com.huang.notetool.util.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 文件搜索
 *
 * @author huang
 */
public class NormalSearch extends JRootParentFrame implements Runnable {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(NormalSearch.class);
    /**
     * 搜索过的文件数量
     */
    private static int countFiles = 0;
    /**
     * 搜索过的文件夹数量
     */
    private static int countFolders = 0;
    /**
     * 搜索到的符合的文件数量
     */
    private int countSearchFiles = 0;
    /**
     * 合格的文件路径，通过\n分隔
     */
    private StringBuilder searchFilePath;
    /**
     * 给其他类提供的调用对象
     */
    public String returns;

    public NormalSearch() {

    }

    /**
     * 构造方法，启动此子线程
     *
     * @param str 无关信息
     */
    private NormalSearch(String str) {
        //得到容器
        Container container = getContentPane();
        new JRootParentDiag(null, "由于本程序权限不够，所以最好不搜索C盘", Constants.NOTICE_IMAGE);
        //初始化
        searchFilePath = new StringBuilder();
        //查找文件
        String fileNameListStr = findSearchFilePath();
        //查找到数据
        if (StringUtils.isNotEmpty(fileNameListStr)) {
            //初始化面板
            initialFileListShowFrame(container, fileNameListStr);
        } else {
            dispose();
        }
        //关闭窗口不退出
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * 初始化搜索到数据的显示面板
     *
     * @param container       容器
     * @param fileNameListStr 搜索到的文件列表
     */
    private void initialFileListShowFrame(Container container, String fileNameListStr) {
        //文件路径列表
        String[] filePathArray = fileNameListStr.split("\n");

        //去掉null的数据
        List<String> filePathList = Arrays.asList(filePathArray);
        filePathList.removeAll(Collections.singleton(null));
        filePathArray = filePathList.toArray(new String[0]);

        //        2016-2017刚开始写的根据\n分隔字符串的半底层java代码
        //        for (int jig = 0; jig < fileNameListStr.length(); jig++) {
        //            if (fileNameListStr.charAt(jig) == '\n') {
        //                filePathList[getjis++] = fileNameListStr.substring(fenduan, jig);
        //                fenduan = jig;
        //            }
        //        }
        //        去掉null的数据
        //        for (int i = 0; i < filePathList.length; i++) {
        //            if (filePathList[i] == null) {
        //                nullsize = i;
        //                break;
        //            }
        //        }

        logger.info("查询到的文件数量：" + filePathArray.length);

        JList searchFilePathList = new JList(filePathArray);
        //下拉的边框
        JScrollPane jsp = new JScrollPane(searchFilePathList);
        //设置边界
        jsp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        container.add(jsp, BorderLayout.CENTER);
        //初始化最后的显示列表
        setSize(400, 400);
        setLocation(450, 200);
        setVisible(true);
        searchFilePathList.addMouseListener(getSearchPathListMouseAdapter(searchFilePathList));
    }

    /**
     * 列表点击事件
     *
     * @param searchFilePathList 列表
     * @return MouseAdapter
     */
    private MouseAdapter getSearchPathListMouseAdapter(JList searchFilePathList) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (searchFilePathList.getSelectedIndex() != -1) {
                    if (mouseEvent.getClickCount() == 2) {
                        twoClick(mouseEvent);
                    }
                }
            }

            /**
             * 点击事件
             *
             * @param mouseEvent 鼠标事件
             */
            void twoClick(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                int index = theList.locationToIndex(mouseEvent.getPoint());
                if (index >= 0) {
                    String fileName = (String) theList.getModel().getElementAt(index);
                    fileName = fileName.replace("\n", "");
                    if (fileName.contains(".exe")) {
                        try {
                            Runtime.getRuntime().exec(fileName);
                        } catch (IOException e1) {
                            //显示消息
                            new JRootParentDiag(NormalSearch.this, "没有权限打开",
                                    Constants.NOTICE_IMAGE);
                            logger.warn("没有权限打开文件" + e1);
                        }
                    } else {
                        File file = new File(fileName);
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (IOException e) {
                            //显示消息
                            new JRootParentDiag(NormalSearch.this, e.getMessage(),
                                    Constants.NOTICE_IMAGE);
                            logger.warn(e);
                        }
                    }
                    setVisible(true);
                }
            }
        };
    }

    /**
     * 查找文件
     *
     * @return 查找到的文件列表字符串，通过\n 分隔
     */
    private String findSearchFilePath() {
        //需要搜索的内容
        String searchName = JOptionPane.showInputDialog(NormalSearch.this, "搜索内容", "搜索",
                JOptionPane.PLAIN_MESSAGE);
        //需要搜索的盘符
        String searchPath = JOptionPane.showInputDialog(NormalSearch.this, "搜索路径", "搜索",
                JOptionPane.PLAIN_MESSAGE);
        if (StringUtils.isNotEmpty(searchPath) && StringUtils.isNotEmpty(searchName)) {
            //用路径实例化一个文件对象
            File searchFile = new File(searchPath);
            StringBuilder fileNameList = new StringBuilder();
            if (searchFile.exists()) {
                logger.info("开始搜索文件");
                //查找文件
                searchFileAndDir(searchName, searchFile, fileNameList);
                if (countSearchFiles != 0) {
                    new ShowMsgSinglePanel(" 查找了 " + countFiles + " 个文件，" + countFolders + " " +
                            "个文件夹，共找到 " + countSearchFiles + " 个符合标准的文件 ",NormalSearch.this);
                    return fileNameList.toString();
                } else {
                    logger.warn("没有搜索到文件");
                    //显示消息
                    new JRootParentDiag(NormalSearch.this,"没有找到",Constants.NOTICE_IMAGE);
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        logger.warn("没有搜索到文件" + e);
                    }
                    dispose();
                }
            }
            return null;
        } else {
            logger.warn("文件路径不正确");
            new JRootParentDiag(NormalSearch.this,"文件路径不正确",Constants.NOTICE_IMAGE);
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                logger.warn("文件路径不正确" + e);
            }
            dispose();
            return null;
        }
    }

    /**
     * 查找文件
     *
     * @param searchName   查找的文件名
     * @param searchFile   文件
     * @param fileNameList 文件名列表
     */
    private void searchFileAndDir(String searchName, File searchFile, StringBuilder fileNameList) {
        if (!searchFile.exists()) {
            new JRootParentDiag(NormalSearch.this,searchFile.getAbsolutePath()+"路径不存在",
                    Constants.NOTICE_IMAGE);
            return;
        }
        //重点:取得目录内所有文件列表
        File[] fileList = searchFile.listFiles();
        if (null != fileList && fileList.length > 0) {
            //遍历文件夹
            for (File file : fileList) {
                if (file.isDirectory()) {
                    //计数文件夹
                    countFolders++;
                    //遍历文件夹
                    searchFileAndDir(searchName, file, fileNameList);
                } else if (file.isFile()) {
                    countFiles++;
                    //找到包含搜索内容的文件
                    if (file.getAbsolutePath().contains(searchName)) {
                        countSearchFiles++;
                        fileNameList.append(file.getAbsolutePath()).append("\n");
                    }
                }
            }
        }
    }

    /**
     * 遍历文件夹和文件，计数查询的文件夹和文件的个数以及符合条件的文件
     *
     * @param folder  文件夹
     * @param keyWord 查找的数据
     */
    private File[] searchFolder(File folder, String keyWord) {
        if (!folder.exists()) {
            new JRootParentDiag(NormalSearch.this,"目录不存在",Constants.NOTICE_IMAGE);
            return null;
        }
        //遍历文件夹,此处用于计数
        File[] subFolders = folder.listFiles(fileList -> {
            if (fileList.isFile()) {
                countFiles++;
            } else {
                countFolders++;
            }
            return fileList.isDirectory() || (fileList.isFile() && fileList.getName().toLowerCase().contains(keyWord.toLowerCase()));
        });
        //存放文件列表
        List<File> result = new ArrayList<>();
        if (null != subFolders && subFolders.length > 0) {
            //遍历文件夹
            for (File subFolder : subFolders) {
                if (subFolder.isFile()) {
                    result.add(subFolder);
                } else {
                    File[] foldResult = searchFolder(subFolder, keyWord);
                    if (null != foldResult) {
                        result.addAll(Arrays.asList(foldResult));
                    }
                }
            }
        }
        //将List转为Array
        File[] returnFileList = result.toArray(new File[0]);
        //计数合格的文件
        countSearchFiles += result.size();
        for (File resultFile : result) {
            searchFilePath.append(resultFile.getAbsolutePath()).append("\n");
        }
        return returnFileList;
    }

    //    public static void main(String[] rgs) {
    //        new NormalSearch();
    //    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        new NormalSearch(null);
    }
}
