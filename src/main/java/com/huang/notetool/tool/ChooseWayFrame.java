package com.huang.notetool.tool;

import com.huang.notetool.frame.common.JRootParentDiag;
import com.huang.notetool.util.Constants;
import com.huang.notetool.util.FilePath;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

/**
 * 导入文件框
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-05-24 21:19:30
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-24   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class ChooseWayFrame {
    /**
     * 日志
     */
    private static Logger logger = Logger.getLogger(ChooseWayFrame.class);

    /**
     * 导入文件
     *
     * @param container   底层面板容器
     * @param fileFilter  文件过滤器
     * @param selectModel 文件选择模型
     * @param isFile      是选择文件还是文件夹
     * @param jFrame      面板
     * @return 返回路径
     */
    public static String chooseFile(Container container, FileFilter fileFilter, int selectModel,
                                    boolean isFile, JFrame jFrame) {
        //JFileChooser控件
        JFileChooser jFileChooser = new JFileChooser();
        //设置默认访问路径
        jFileChooser.setCurrentDirectory(new File(FilePath.getImportMethodDefaultPath()));
        if (null != fileFilter) {
            jFileChooser.addChoosableFileFilter(fileFilter);
            jFileChooser.setFileFilter(fileFilter);
        }
        //选择文件和文件夹：JFileChooser.FILES_AND_DIRECTORIES
        jFileChooser.setFileSelectionMode(selectModel);
        int index;
        if (null != container) {
            index = jFileChooser.showOpenDialog(container);
        } else {
            index = jFileChooser.showDialog(new JLabel(), "选择");
        }
        if (index == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser.getSelectedFile();
            logger.info("导入文件绝对路径 [" + selectedFile.getAbsolutePath() + "]  ");
            if (isFile) {
                if (selectedFile.isFile()) {
                    return selectedFile.getAbsolutePath();
                } else {
                    new JRootParentDiag(jFrame, "未选择文件", Constants.NOTICE_IMAGE);
                    return null;
                }
            } else {
                if (selectedFile.isFile() || selectedFile.isDirectory()) {
                    return selectedFile.getAbsolutePath();
                } else {
                    return null;
                }
            }
        } else {
            logger.info("取消操作 ");
            new JRootParentDiag(jFrame, "取消操作", Constants.NOTICE_IMAGE);
            return null;
        }
    }

    /**
     * 获取导出文件类型
     *
     * @return
     */
    public static String getExportFileType(Component component) {
        Object[] fileType = {"txt", "xls", "xlsx", "csv", "dox", "docx", "pdf"};
        return (String) JOptionPane.showInputDialog(component
                , "请选择导出文件类型:\n"
                , "文件类型"
                , JOptionPane.PLAIN_MESSAGE
                , new ImageIcon("icon.png")
                , fileType
                , "txt");
    }

    /**
     * 导出文件
     *
     * @param filePath 文件路径
     * @param fileType 文件类型
     * @param dataArr  导出数据
     * @return 成功与否
     */
    public static boolean exportChooseFile(String filePath, String fileType, String[] dataArr,
                                           Class objectClass) {
        switch (fileType) {
            case "txt":
                return WriteFile.writeTxtFile(dataArr, objectClass, filePath);
            case "xls":
                return WriteFile.writeXlsFile(dataArr, objectClass, filePath);
            case "csv":
                return false;
            case "dox":
                return false;
            case "xlsx":
                return false;
            case "docx":
                return false;
            case "pdf":
                return false;
            default:
                return false;
        }
    }
}
