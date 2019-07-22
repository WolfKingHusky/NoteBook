package com.huang.notetool.frame.method;

import com.huang.notetool.frame.exception.ManageExceptionFrame;
import com.huang.notetool.frame.start.Note;
import com.huang.notetool.service.MethodService;
import com.huang.notetool.tool.ChooseWayFrame;
import com.huang.notetool.util.InitialComponent;
import com.huang.notetool.util.StringUtils;
import com.huang.notetool.util.ToolUtil;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 管理方法面板
 *
 * @author huang
 */
public class ManageMethodFrame extends JFrame {
    private JList msgList;
    /**
     * Service
     */
    private MethodService methodService;
    /**
     * 数据量,打开关闭数据的标志，0：是关闭数据
     */
    private int methodNum;
    private static Logger logger = Logger.getLogger(ManageMethodFrame.class);

    /**
     * 构造方法
     */
    public ManageMethodFrame(Note noteFrame) {
        logger.info("打开 Method 管理页面 ...");
        //非Windows主题才能设置透明度
        if (!InitialComponent.isWindowsLookAndFeel()) {
            this.setUndecorated(true);
            this.getRootPane().setWindowDecorationStyle(JRootPane.WHEN_IN_FOCUSED_WINDOW);
            this.setOpacity(noteFrame.getOpacity());
        }
        this.setCursor(noteFrame.getCursor());
        this.setTitle("双击可以修改");
        //容器
        Container container = this.getContentPane();

        Point point = ToolUtil.getMethodMannerFramePoint();
        container.setSize(point.x, point.y);

        Double x = noteFrame.getX() + noteFrame.getWidth() / 2 - point.getX() / 2 + 25;
        Double y = noteFrame.getY() + noteFrame.getHeight() / 2 - point.getY() / 2 + 25;
        this.setBounds(x.intValue(), y.intValue(), point.x - 50, point.y - 50);

        container.setLayout(new BorderLayout());
        //model 设置为false，在弹出此窗口还可以点击主窗口
        //this.setModal(false);
        //可以改变大小
        this.setResizable(true);
        init(noteFrame);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * 初始化
     */
    private void init(Note noteFrame) {
        methodService = new MethodService();

        JPanel btnPanel = new JPanel();
        btnPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        JButton openDataBtn = new JButton("打开数据");
        JButton closeBtn = new JButton("关闭数据");
        JButton manageDBBtn = new JButton("管理数据库连接");
        JButton exitBtn = new JButton("退出");
        JButton delAllBtn = new JButton("清空");
        JButton exportBtn = new JButton("导出");
        JButton refreshBtn = new JButton("刷新");

        btnPanel.add(openDataBtn);
        btnPanel.add(exportBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(manageDBBtn);
        btnPanel.add(closeBtn);
        btnPanel.add(delAllBtn);
        btnPanel.add(exitBtn);

        this.add(btnPanel, "South");

        //打开数据
        openDataBtn.addActionListener(openDateAction(noteFrame));
        //退出
        exitBtn.addActionListener(exitAction());
        //关闭数据
        closeBtn.addActionListener(closeDataAction());
        //导出
        exportBtn.addActionListener(exportMethodAction());
        //清空
        delAllBtn.addActionListener(delAllBtnAction());
        //刷新
        refreshBtn.addActionListener(refreshBtnAction());
    }

    /**
     * 刷新
     */
    private ActionListener refreshBtnAction() {
        return e -> refreshJList();
    }

    /**
     * 刷新
     */
    private void refreshJList() {
        if (0 != methodNum) {
            msgList.removeAll();
            String[] allMethodArr = methodService.getAllMethods();
            if (null != allMethodArr && allMethodArr.length > 0 && null != msgList) {
                msgList.setListData(allMethodArr);
            } else {
                allMethodArr = new String[]{""};
                msgList.setListData(allMethodArr);
            }
        } else {
            logger.info("没有打开数据...");
            JOptionPane.showMessageDialog(null, "没有打开数据");
        }
    }

    /**
     * 清空
     *
     * @return
     */
    private ActionListener delAllBtnAction() {
        return e -> {
            if (0 != methodNum) {
                int isDelMsg = JOptionPane.showConfirmDialog(ManageMethodFrame.this, "确定删除");
                if (isDelMsg == 0) {
                    logger.info("已确认清空 Method");
                    methodService.deleteAll();
                    refreshJList();
                }
            } else {
                logger.info("没有打开数据...");
                JOptionPane.showMessageDialog(null, "没有打开数据");
            }
        };
    }

    /**
     * 导出方法
     *
     * @return ActionListener
     */
    private ActionListener exportMethodAction() {
        return e -> {
            if (0 != methodNum) {
                logger.info("正在导出数据... ");
                String filePath = ChooseWayFrame.chooseFile(null, null,
                        JFileChooser.FILES_AND_DIRECTORIES, false, ManageMethodFrame.this);
                if (StringUtils.isNotEmpty(filePath)) {
                    String fileType = ChooseWayFrame.getExportFileType(this);
                    if (methodService.exportMethod(filePath, fileType)) {
                        logger.info("导出数据成功...");
                        JOptionPane.showMessageDialog(null, "导出数据成功");
                    } else {
                        logger.info("导出数据失败...");
                        JOptionPane.showMessageDialog(null, "导出数据失败");
                    }
                }
            } else {
                logger.info("没有打开数据...");
                JOptionPane.showMessageDialog(null, "没有打开数据");
            }
        };
    }

    /**
     * 关闭数据
     *
     * @return ActionListener
     */
    private ActionListener closeDataAction() {
        return e -> {
            logger.info("关闭数据 [" + methodNum + "] 条");
            String[] nullValue = {""};
            msgList.setListData(nullValue);
            if (methodNum != 0) {
                msgList.removeAll();
                this.remove(msgList);
            }
            JPanel jPanel = new JPanel();
            this.add(jPanel, "Center");
            this.setVisible(true);
            methodNum = 0;
        };
    }

    /**
     * 退出
     *
     * @return ActionListener
     */
    private ActionListener exitAction() {
        return e -> {
            logger.info("退出管理界面");
            this.dispose();
        };
    }

    /**
     * 打开数据
     *
     * @param noteFrame
     * @return ActionListener
     */
    private ActionListener openDateAction(Note noteFrame) {
        return e -> {
            logger.info("打开数据");
            String[] methodArr = methodService.getAllMethods();
            if (null != methodArr && methodArr.length > 0) {
                methodNum = methodArr.length;
                msgList = new JList(methodArr);
                JScrollPane js = new JScrollPane(msgList);
                this.add(js, "Center");
                this.setVisible(true);
                logger.info("打开数据成功 ");
                //列表事件
                msgList.addMouseListener(dataListAction(noteFrame));
            }
        };
    }

    /**
     * 列表事件
     *
     * @param noteFrame
     * @return
     */
    private MouseListener dataListAction(Note noteFrame) {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JList msgLists = (JList) e.getSource();
                    int selectIndex = msgLists.getSelectedIndex();
                    // 两种方法   int b = msgLists.locationToIndex(e.getPoint());
                    String selectValue = (String) msgLists.getSelectedValue();
                    logger.info("准备进入修改页面 选中第 [" + selectIndex + "] 条信息 [" + selectValue + "] --> ManageMethodFrame");
                    UpdateMethodFrame updateMethodFrame = new UpdateMethodFrame(selectValue, noteFrame);
                    updateMethodFrame.setVisible(true);
                    if (updateMethodFrame.isSuccess()) {
                        msgList.removeAll();
                        msgList.setListData(methodService.getAllMethods());
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }
}
