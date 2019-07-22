package com.huang.notetool.frame.exception;

import com.huang.notetool.frame.common.JRootParentDiag;
import com.huang.notetool.frame.start.Note;
import com.huang.notetool.po.ExceptionWay;
import com.huang.notetool.service.ExceptionService;
import com.huang.notetool.util.*;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * 查询异常信息输入框
 *
 * @author 黄
 */
public class AddExceptionFrame extends JFrame {
    /**
     * 异常信息
     * protected主要为了给继承的类使用
     */
    protected JTextArea exceptionMsg;
    /**
     * 异常信息解决方法
     */
    protected JTextArea exceptionAnswerMsg;
    /**
     * 异常信息根本原因
     */
    protected JTextArea exceptionCause;
    /**
     * 异常信息列表
     */
    protected JList exceptionTypeList;
    /**
     * 异常信息语言
     */
    protected JComboBox exceptionLanguageList;
    /**
     * 关键字
     */
    protected JTextField keyFiled;
    /**
     * 存放按钮的面板，继承此类的子类会重写此面板
     */
    protected JPanel superBtnPanel;

    private ExceptionService exceptionService;
    /**
     * 监听计数
     */
    private int iniNum;
    private int windowSizeClick;
    /**
     * 监听异常信息输入框换行计数
     */
    private int exceptionMsgClickNum;
    /**
     * 日志
     */
    private static Logger logger = Logger.getLogger(AddExceptionFrame.class);
    /**
     * 支持撤销操作
     */
    private UndoManager undoExceptMsgManager = new UndoManager();
    /**
     * 支持撤销操作
     */
    private UndoManager undoExceptAnswerMsgManager = new UndoManager();
    /**
     * 支持撤销操作
     */
    private UndoManager undoExceptCauseMsgManager = new UndoManager();

    public AddExceptionFrame(Note jFrame) {
        //非Windows主题才能设置透明度
        if (!InitialComponent.isWindowsLookAndFeel()) {
            this.setUndecorated(true);
            this.getRootPane().setWindowDecorationStyle(JRootPane.WHEN_IN_FOCUSED_WINDOW);
            this.setOpacity(jFrame.getOpacity());
        }
        this.setCursor(jFrame.getCursor());
        //model 设置为false，在弹出此窗口还可以点击主窗口
        this.setTitle("添加异常信息,点击三次按钮旁边的空白处可以放大和缩小,输入框点击两次实现换行与否");
        logger.info("进入异常添加页面  ....... ");
        Point point = ToolUtil.getExceptionFramePoint();
        Double x = jFrame.getX() + jFrame.getWidth() / 2 - point.getX() / 2 + 25;
        Double y = jFrame.getY() + jFrame.getHeight() / 2 - point.getY() / 2 + 25;
        this.setBounds(x.intValue(), y.intValue(), point.x - 50, point.y - 50);
        this.setLayout(new BorderLayout());
        this.addWindowFocusListener(windowsFocusListener());
        initial();
        //快捷键
        setShortsKeys();
        this.addMouseListener(containerClickEvent(this, jFrame));
        this.setResizable(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }


    /**
     * 初始化
     */
    private void initial() {
        exceptionService = new ExceptionService();
        JLabel exceptionLabel = new JLabel("出错信息(注意左边的type)，异常信息的第一行后面引号以及冒号后的内容全部改为1！！！");
        //初始化文本框
        initialJTextArea();

        JButton goBackBtn = new JButton("走了");
        JButton explainBtn = new JButton("怎么填嘛");
        JButton addMsgBtn = new JButton("添加信息");
        JButton resetBtn = new JButton("又错了");

        superBtnPanel = new JPanel();
        superBtnPanel.add(addMsgBtn);
        superBtnPanel.add(explainBtn);
        superBtnPanel.add(resetBtn);
        superBtnPanel.add(goBackBtn);
        superBtnPanel.setBorder(BorderFactory.createEtchedBorder());

        JPanel topPanel = new JPanel();
        topPanel.add(exceptionLabel);
        exceptionTypeList = new JList(ToolUtil.exceptionType);
        exceptionTypeList.setSelectedIndex(0);
        JScrollPane scrollPane = new JScrollPane(exceptionTypeList);

        JPanel msgPanel = new JPanel();
        msgPanel.setLayout(new BorderLayout());

        JScrollPane jScrollPane = new JScrollPane(exceptionMsg);
        JScrollPane answerScrollPane = new JScrollPane(exceptionAnswerMsg);
        JScrollPane causeScrollPane = new JScrollPane(exceptionCause);

        exceptionLanguageList = new JComboBox(ToolUtil.exceptionLanguage);
        exceptionLanguageList.setSelectedIndex(0);
        keyFiled = InitialComponent.setJTExtField(null, null, true, null);
        keyFiled.addKeyListener(new LimitKeyIn());

        Box msgBox = new Box(BoxLayout.X_AXIS);
        msgBox.add(jScrollPane);
        msgBox.add(answerScrollPane);

        msgPanel.add(exceptionLanguageList, "North");
        msgPanel.add(msgBox, "Center");
        msgPanel.add(keyFiled, "South");

        this.add(topPanel, "North");
        //西方,East东方
        this.add(scrollPane, "West");
        this.add(msgPanel, "Center");
        this.add(superBtnPanel, "South");
        this.add(causeScrollPane, "East");

        goBackBtn.addActionListener(goBackBtnActionListener());

        resetBtn.addActionListener(resetBtnActionListener());

        explainBtn.addActionListener(explainActionListener());

        addMsgBtn.addActionListener(addBtnActionListener());

        exceptionMsg.addMouseListener(exceptionMsgClickListener(exceptionMsg));
        exceptionAnswerMsg.addMouseListener(exceptionMsgClickListener(exceptionAnswerMsg));
    }

    /**
     * 初始化文本框
     */
    private void initialJTextArea() {
        exceptionMsg = InitialComponent.setJTextArea(15, Color.RED, null, true, "宋体", false, null, true);
        exceptionAnswerMsg = InitialComponent.setJTextArea(15, Color.BLUE, null, true, "宋体", false, null, true);

        exceptionCause = InitialComponent.setJTextArea(null, null, null, true, null, true, null, true);
        exceptionCause.addKeyListener(new LimitKeyIn());

        exceptionMsg.getDocument().addUndoableEditListener(undoExceptMsgManager);
        exceptionAnswerMsg.getDocument().addUndoableEditListener(undoExceptAnswerMsgManager);
        exceptionCause.getDocument().addUndoableEditListener(undoExceptCauseMsgManager);
    }

    /**
     * 快捷键
     */
    private void setShortsKeys() {
        logger.info("初始化快捷键  ....... ");
        //异常信息
        InputMap inputMap = exceptionMsg.getInputMap();
        //异常信息答案
        InputMap answerMap = exceptionAnswerMsg.getInputMap();
        //异常信息根本原因
        InputMap causeMap = exceptionCause.getInputMap();

        InitialComponent.initialCtrlZAndB(inputMap, undoExceptMsgManager);
        InitialComponent.initialCtrlZAndB(answerMap, undoExceptAnswerMsgManager);
        InitialComponent.initialCtrlZAndB(causeMap, undoExceptCauseMsgManager);
    }

    private MouseAdapter containerClickEvent(AddExceptionFrame addExceptionFrame, JFrame noteFrame) {
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
                        addExceptionFrame.setBounds(0, 30, screen.width, screen.height - 26);
                        windowSizeClick++;
                    } else {
                        addExceptionFrame.setBounds(noteFrame.getX(), noteFrame.getY(), noteFrame.getWidth(), noteFrame.getHeight());
                        windowSizeClick = 0;
                    }
                }
            }
        };
    }

    /**
     * 异常信息点击换行事件
     *
     * @return MouseAdapter
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

    /**
     * 添加异常信息
     *
     * @return ActionListener
     */
    private ActionListener addBtnActionListener() {
        return e -> {
            logger.info("添加异常信息 ");
            String type = (String) exceptionTypeList.getSelectedValue();
            String language = (String) exceptionLanguageList.getSelectedItem();
            String msg = exceptionMsg.getText().trim();
            String answerMsg = exceptionAnswerMsg.getText().trim();
            String keyWords = keyFiled.getText().trim();
            String cause = exceptionCause.getText().trim();
            if (StringUtils.isEmpty(msg)) {
                exceptionMsg.requestFocus();
                new JRootParentDiag(AddExceptionFrame.this,"异常信息不能为空",Constants.NOTICE_IMAGE);
            } else if (StringUtils.isEmpty(answerMsg)) {
                exceptionAnswerMsg.requestFocus();
                new JRootParentDiag(AddExceptionFrame.this,"异常信息答案不能为空",Constants.NOTICE_IMAGE);
            } else if (StringUtils.isEmpty(keyWords)) {
                keyFiled.requestFocus();
                JOptionPane.showMessageDialog(null, "关键字不能为空");
            } else {
                List<ExceptionWay> exceptionWayList = exceptionService.getByExceptionName(msg.trim());
                if (exceptionWayList == null || exceptionWayList.isEmpty()) {
                    if (exceptionService.addExceptionMsg(msg, language, type, keyWords.replaceAll("，", ","), answerMsg, cause)) {
                        JOptionPane.showMessageDialog(null, "添加成功");
                        resetWay();
                    } else {
                        JOptionPane.showMessageDialog(null, "添加失败");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "异常信息已存在");
                }
            }
        };
    }

    /**
     * 获取焦点事件
     *
     * @return WindowFocusListener
     */
    private WindowFocusListener windowsFocusListener() {
        return new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                if (iniNum == 0) {
                    exceptionMsg.requestFocus();
                    iniNum++;
                }
            }

            @Override
            public void windowLostFocus(WindowEvent e) {

            }
        };
    }

    /**
     * 说明
     *
     * @return ActionListener
     */
    private ActionListener explainActionListener() {
        return e -> JOptionPane.showMessageDialog(AddExceptionFrame.this, ToolUtil.getHowToFillAddExceptionMsg);
    }

    /**
     * 重置
     *
     * @return ActionListener
     */
    private ActionListener resetBtnActionListener() {
        return e -> resetWay();
    }

    /**
     * 重置
     */
    private void resetWay() {
        logger.info("重置异常信息 ");
        keyFiled.setText(Constants.EMPTY_STR);
        exceptionLanguageList.setSelectedIndex(0);
        exceptionMsg.setText(Constants.EMPTY_STR);
        exceptionCause.setText(Constants.EMPTY_STR);
        exceptionTypeList.setSelectedIndex(0);
        exceptionAnswerMsg.setText(Constants.EMPTY_STR);
    }

    private ActionListener goBackBtnActionListener() {
        return e -> {
            logger.info("返回上级菜单");
            this.dispose();
        };
    }

}
