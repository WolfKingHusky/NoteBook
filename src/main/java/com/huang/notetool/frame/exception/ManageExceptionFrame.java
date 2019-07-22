package com.huang.notetool.frame.exception;

import com.huang.notetool.frame.common.ShowMsgSinglePanel;
import com.huang.notetool.frame.start.Note;
import com.huang.notetool.service.ExceptionService;
import com.huang.notetool.tool.ChooseWayFrame;
import com.huang.notetool.util.InitialComponent;
import com.huang.notetool.util.StringUtils;
import com.huang.notetool.util.ToolUtil;
import com.huang.notetool.util.TxtFileFilter;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 管理异常信息
 *
 * @author 黄
 */
public class ManageExceptionFrame extends JFrame {
    /**
     * 数据列表
     */
    private JList msgList;
    private ExceptionService exceptionService;
    private static Logger logger = Logger.getLogger(ManageExceptionFrame.class);

    /**
     * 构造方法
     *
     * @param noteFrame       主面板
     * @param allExceptionArr 所有的异常信息
     */
    public ManageExceptionFrame(Note noteFrame, String[] allExceptionArr) {
        //非Windows主题才能设置透明度
        if (!InitialComponent.isWindowsLookAndFeel()) {
            this.setUndecorated(true);
            this.getRootPane().setWindowDecorationStyle(JRootPane.WHEN_IN_FOCUSED_WINDOW);
            this.setOpacity(noteFrame.getOpacity());
        }
        this.setCursor(noteFrame.getCursor());
        logger.info("打开 Exception 管理页面 ");
        this.setTitle("双击可以修改");
        //容器
        Container container = this.getContentPane();
        Point point = ToolUtil.getExceptionFramePoint();
        Double x = noteFrame.getX() + noteFrame.getWidth() / 2 - point.getX() / 2 + 25;
        Double y = noteFrame.getY() + noteFrame.getHeight() / 2 - point.getY() / 2 + 25;
        this.setBounds(x.intValue(), y.intValue(), point.x - 50, point.y - 50);
        container.setLayout(new BorderLayout());
        initial(noteFrame, allExceptionArr);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * 初始化组件
     *
     * @param allExceptionArr 所有的异常信息
     * @param noteFrame       主面板
     */
    private void initial(Note noteFrame, String[] allExceptionArr) {
        exceptionService = new ExceptionService();
        JPanel btnPanel = new JPanel();
        JButton manageDBBtn = new JButton("管理数据库连接");
        JButton exitBtn = new JButton("退出");
        JButton exportBtn = new JButton("导出");
        JButton importBtn = new JButton("导入");
        JButton delAllBtn = new JButton("清空");
        JButton refreshBtn = new JButton("刷新");
        btnPanel.add(refreshBtn);
        btnPanel.add(exportBtn);
        btnPanel.add(manageDBBtn);
        btnPanel.add(importBtn);
        btnPanel.add(exitBtn);
        btnPanel.add(delAllBtn);
        //边框
        btnPanel.setBorder(BorderFactory.createEtchedBorder());
        this.add(btnPanel, "South");

        logger.info("打开数据 ");
        exceptionService = new ExceptionService();

        if (null != allExceptionArr && allExceptionArr.length > 0) {
            msgList = new JList(allExceptionArr);
        } else {
            msgList = new JList();
        }
        JScrollPane jScrollPane = new JScrollPane(msgList);
        jScrollPane.setBorder(BorderFactory.createEtchedBorder());
        this.add(jScrollPane, "Center");
        if (null != msgList.getSelectedValue()) {
            logger.info("打开数据成功 ");
        }

        // 退出
        exitBtn.addActionListener(exitAction());

        //导出
        exportBtn.addActionListener(exportAction());
        //导入
        importBtn.addActionListener(importAction(noteFrame));

        // 列表事件
        msgList.addMouseListener(exceptionListClickAction(noteFrame));

        //刷新
        refreshBtn.addActionListener(refreshAction());

        //清空
        delAllBtn.addActionListener(delAllActionListener());
    }

    private ActionListener delAllActionListener() {
        return e -> {
            int isDelMsg = JOptionPane.showConfirmDialog(ManageExceptionFrame.this, "确定删除");
            if (isDelMsg == 0) {
                logger.info("已确认删除 ");
                exceptionService.deleteAll();
                refreshJList();
            }
        };
    }

    /**
     * 导入
     *
     * @param noteFrame
     * @return ActionListener
     */
    private ActionListener importAction(Note noteFrame) {
        return e -> {
            TxtFileFilter txtFileFilter = new TxtFileFilter();
            String filePath = ChooseWayFrame.chooseFile(super.getContentPane(), txtFileFilter,
                    JFileChooser.FILES_ONLY, true, ManageExceptionFrame.this);
            if (null != filePath && !"".equals(filePath)) {
                logger.info("准备导入数据... ");
                String returnMsg = exceptionService.importMethod(filePath);
                new ShowMsgSinglePanel(returnMsg, noteFrame);
                refreshJList();
            }
        };
    }

    /**
     * 刷新
     *
     * @return
     */
    private ActionListener refreshAction() {
        return e -> refreshJList();
    }

    /**
     * 刷新
     */
    private void refreshJList() {
        msgList.removeAll();
        String[] allExceptionArr = exceptionService.getAllExceptions();
        if (null != allExceptionArr && allExceptionArr.length > 0 && null != msgList) {
            msgList.setListData(allExceptionArr);
        } else {
            allExceptionArr = new String[]{""};
            msgList.setListData(allExceptionArr);
        }
    }

    private MouseListener exceptionListClickAction(Note noteFrame) {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    clickWay(mouseEvent, noteFrame);
                }
            }

            /**两种方法  获取点击的value
             *  int b = msgLists.locationToIndex(e.getPoint());
             *  String selectValue = (String) msgLists.getSelectedValue();
             * 点击方法
             * @param mouseEvent
             * @param noteFrame
             */
            private void clickWay(MouseEvent mouseEvent, Note noteFrame) {
                JList msgLists = (JList) mouseEvent.getSource();
                int selectIndex = msgLists.getSelectedIndex();
                String selectValue = (String) msgLists.getSelectedValue();
                logger.info("准备进入修改页面 选中第 [" + selectIndex + "] 条信息 [" + selectValue.length() + "] ");
                if (StringUtils.isEmpty(selectValue)) {
                    return;
                }
                UpdateExceptionFrame updateExceptionFrame = new UpdateExceptionFrame(selectValue, noteFrame);
                updateExceptionFrame.setVisible(true);
                String returnMsg = updateExceptionFrame.getUpdateMsg();
                if (null != returnMsg && !"".equals(returnMsg) && !"".equals(returnMsg.trim())) {
                    //JOptionPane.showMessageDialog(null, returnMsg);
                    refreshJList();
                }
            }

            /**
             * Invoked when a mouse button has been pressed on a component.
             *
             * @param e
             */
            @Override
            public void mousePressed(MouseEvent e) {

            }

            /**
             * Invoked when a mouse button has been released on a component.
             *
             * @param e
             */
            @Override
            public void mouseReleased(MouseEvent e) {

            }

            /**
             * Invoked when the mouse enters a component.
             *
             * @param e
             */
            @Override
            public void mouseEntered(MouseEvent e) {

            }

            /**
             * Invoked when the mouse exits a component.
             *
             * @param e
             */
            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }

    /**
     * 导出数据
     *
     * @return ActionListener
     */
    private ActionListener exportAction() {
        return e -> {
            logger.info("正在导出数据... ");
            String filePath = ChooseWayFrame.chooseFile(null, null,
                    JFileChooser.FILES_AND_DIRECTORIES, false, ManageExceptionFrame.this);
            if (StringUtils.isNotEmpty(filePath)) {
                String fileType = ChooseWayFrame.getExportFileType(this);
                if (exceptionService.exportException(filePath, fileType)) {
                    logger.info("导出数据成功... ");
                    JOptionPane.showMessageDialog(null, "导出数据成功");
                } else {
                    logger.info("导出数据失败... ");
                    JOptionPane.showMessageDialog(null, "导出数据失败");
                }
            }
        };
    }


    private ActionListener exitAction() {
        return e -> {
            logger.info("退出管理界面 ");
            this.dispose();
        };
    }
}
