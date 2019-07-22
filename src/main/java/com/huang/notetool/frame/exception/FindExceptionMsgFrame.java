package com.huang.notetool.frame.exception;

import com.huang.notetool.frame.start.Note;
import com.huang.notetool.service.ExceptionService;
import com.huang.notetool.util.InitialComponent;
import com.huang.notetool.util.LimitKeyIn;
import com.huang.notetool.util.StringUtils;
import com.huang.notetool.util.ToolUtil;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;

/**
 * 查询异常信息
 *
 * @author 黄
 */
public class FindExceptionMsgFrame extends JDialog {
    /**
     * 异常信息
     */
    private JTextArea exceptionMsg;
    /**
     * 异常信息类型
     */
    private JList exceptionTypeList;
    /**
     * 返回给输入框的信息
     */
    private String returnAnswerMsg;
    /**
     * 监听异常信息输入框换行计数
     */
    private int exceptionMsgClickNum;
    /**
     * 点击次数
     */
    private int windowSizeClick;

    private ExceptionService exceptionService;
    /**
     * 日志
     */
    private static Logger logger = Logger.getLogger(FindExceptionMsgFrame.class);
    /**
     * 支持撤销操作
     */
    private UndoManager undoManager = new UndoManager();

    public FindExceptionMsgFrame(Note noteFrame) {
        //非Windows主题才能设置透明度
        if (!InitialComponent.isWindowsLookAndFeel()) {
            this.setUndecorated(true);
            this.getRootPane().setWindowDecorationStyle(JRootPane.WHEN_IN_FOCUSED_WINDOW);
            this.setOpacity(noteFrame.getOpacity());
        }
        this.setCursor(noteFrame.getCursor());
        this.setTitle("异常查询,点击三次按钮旁边的空白处可以放大和缩小,输入框点击两次实现换行与否");
        logger.info("进入异常查询页面 FindExceptionMsgFrame ....... ");
        Point point = ToolUtil.getExceptionFramePoint();
        Double x = noteFrame.getX() + noteFrame.getWidth() / 2 - point.getX() / 2 + 25;
        Double y = noteFrame.getY() + noteFrame.getHeight() / 2 - point.getY() / 2 + 25;
        this.setBounds(x.intValue(), y.intValue(), point.x - 50, point.y - 50);
        this.setLayout(new BorderLayout());
        initial(noteFrame);
        setShortsKeys();
        this.setModal(true);
        this.setResizable(true);
        this.addMouseListener(containerClickEvent(this, noteFrame));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * 快捷键
     */
    private void setShortsKeys() {
        InputMap inputMap = exceptionMsg.getInputMap();
        InitialComponent.initialCtrlZAndB(inputMap, undoManager);
    }

    private void initial(Note noteFrame) {
        exceptionService = new ExceptionService();

        JLabel exceptionLabel = new JLabel("出错信息(注意左边的type),异常信息第一行后面引号以及冒号后的内容全部改为1！！！");
        exceptionMsg = InitialComponent.setJTextArea(15, Color.RED, null, true, "宋体", false, null, true);
        exceptionMsg.addKeyListener(new LimitKeyIn());
        exceptionMsg.getDocument().addUndoableEditListener(undoManager);
        JButton goBackBtn = new JButton("返回");
        JButton searchAnswerByExceptionBtn = new JButton("查找答案");
        JButton addExceptionAndAnswerMsg = new JButton("添加信息");
        JButton managerBtn = new JButton("敬请期待");

        JPanel showExceptionMsgPanel = new JPanel();
        showExceptionMsgPanel.setBorder(BorderFactory.createEtchedBorder());
        showExceptionMsgPanel.add(addExceptionAndAnswerMsg);
        showExceptionMsgPanel.add(searchAnswerByExceptionBtn);
        showExceptionMsgPanel.add(managerBtn);
        showExceptionMsgPanel.add(goBackBtn);
        JPanel answerMsgPanel = new JPanel();

        answerMsgPanel.add(exceptionLabel);
        JScrollPane jScrollPane = new JScrollPane(exceptionMsg);
        jScrollPane.setBorder(BorderFactory.createEtchedBorder());
        exceptionTypeList = new JList(ToolUtil.exceptionType);
        exceptionTypeList.setSelectedIndex(0);
        JScrollPane jListPane = new JScrollPane(exceptionTypeList);
        jListPane.setBorder(BorderFactory.createEtchedBorder());
        this.add(answerMsgPanel, "North");
        //西方,East东方
        this.add(jListPane, "West");
        this.add(jScrollPane, "Center");
        this.add(showExceptionMsgPanel, "South");

        this.addWindowFocusListener(windowsFocusListener());
        goBackBtn.addActionListener(exitSearchAction());

        searchAnswerByExceptionBtn.addActionListener(searchExceptionAction());

        addExceptionAndAnswerMsg.addActionListener(addExceptionActionListener(noteFrame));

        managerBtn.addActionListener(managerActionListener());

        exceptionMsg.addMouseListener(exceptionMsgClickListener(exceptionMsg));

    }

    /**
     * 异常信息点击事件
     *
     * @return
     */
    private MouseAdapter exceptionMsgClickListener(JTextArea jTextArea) {
        return new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param mouseEvent
             */
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    if (exceptionMsgClickNum == 0) {
                        jTextArea.setLineWrap(true);
                        jTextArea.setWrapStyleWord(true);
                        exceptionMsgClickNum++;
                    } else {
                        jTextArea.setLineWrap(false);
                        jTextArea.setWrapStyleWord(false);
                        exceptionMsgClickNum = 0;
                    }
                }
            }
        };
    }

    private MouseAdapter containerClickEvent(FindExceptionMsgFrame findExceptionMsgFrame, JFrame noteFrame) {
        return new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 3) {
                    if (windowSizeClick == 0) {
                        //得到屏幕尺寸
                        Dimension screen = getToolkit().getScreenSize();
                        findExceptionMsgFrame.setBounds(0, 30, screen.width, screen.height - 26);
                        windowSizeClick++;
                    } else {
                        findExceptionMsgFrame.setBounds(noteFrame.getX(), noteFrame.getY(), noteFrame.getWidth(), noteFrame.getHeight());
                        windowSizeClick = 0;
                    }
                }
            }
        };
    }


    /**
     * 获取焦点事件
     *
     * @return
     */
    private WindowFocusListener windowsFocusListener() {
        return new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                exceptionMsg.requestFocus();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {

            }
        };
    }

    private ActionListener managerActionListener() {
        return e -> JOptionPane.showMessageDialog(null, "敬请期待");
    }

    private ActionListener addExceptionActionListener(Note noteFrame) {
        return e -> {
            AddExceptionFrame addExceptionFrame = new AddExceptionFrame(noteFrame);
            addExceptionFrame.setVisible(true);
            this.dispose();
        };
    }

    /**
     * 搜索
     *
     * @return
     */
    private ActionListener searchExceptionAction() {
        return e -> {
            logger.info("异常查询准备 ....... ");
            String msg = exceptionMsg.getText().trim();
            if ("".equals(msg)) {
                JOptionPane.showMessageDialog(null, "还没有输入哦");
            } else {
                String keyWords = JOptionPane.showInputDialog(null, "格式：关键字,关键字").trim();
                returnAnswerMsg = exceptionService.getExceptionAnswer(new String[]{(String) exceptionTypeList.getSelectedValue(), keyWords, exceptionMsg.getText().trim()});
                if (StringUtils.isNotEmpty(returnAnswerMsg)) {
//                returnExceptionAnswer();
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "没有查询到信息，期待您的加入");
                }
            }
        };
    }

    private ActionListener exitSearchAction() {
        return e -> {
            logger.info("退出异常查询 .......");
            this.dispose();
        };
    }

    /**
     * 获取返回数据
     *
     * @return returnAnswerMsg
     */
    public String getExceptionAnswer() {
        return returnAnswerMsg;
    }
}
