package com.huang.notetool.frame.common;

import com.huang.notetool.util.InitialComponent;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 此处说明类的作用
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-05-25 19:08:24
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-25   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class ToolParentFrame extends JFrame {
    /**
     * 日志
     */
    protected Logger logger = Logger.getLogger(ToolParentFrame.class);

    /**
     * 输入框
     */
    protected JTextArea inputMsg;
    /**
     * 输出框
     */
    protected JTextArea outputMsg;
    /**
     * 输入输出框面板
     */
    protected JPanel msgPanel;
    /**
     * 按钮面板
     */
    protected JPanel buttonPanel;
    /**
     * 总的面板
     */
    protected JPanel containerPanel;

    /**
     * 计数点击次数
     */
    private int inputClick;
    private int outputClick;
    private int windowSizeClick;
    /**
     * 支持撤销操作
     */
    private UndoManager undoInputMsgManager = new UndoManager();
    private UndoManager undoOutputMsgManager = new UndoManager();

    public ToolParentFrame(JFrame jFrame) {
        //不是Windows窗体才设置可变信息
        if (!InitialComponent.isWindowsLookAndFeel()) {
            this.setUndecorated(true);
            this.getRootPane().setWindowDecorationStyle(JRootPane.WHEN_IN_FOCUSED_WINDOW);
            this.setOpacity(jFrame.getOpacity());
        }
        this.setCursor(jFrame.getCursor());
        logger.info("打开工具页面 ... ");
        this.setTitle("双击可以切换是否换行,按钮面板点击三次实现缩放,");
        //容器
        JPanel containerJPanel = new JPanel();
        this.setBounds(jFrame.getX(), jFrame.getY(), jFrame.getWidth(), jFrame.getHeight());
        containerJPanel.setLayout(new BorderLayout());
        this.setLayout(new BorderLayout());
        initial(jFrame, containerJPanel);
        shiftClickListener();
        this.setContentPane(containerJPanel);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initial(JFrame noteFrame, JPanel container) {
        //输入框
        inputMsg = InitialComponent.setJTextArea(15, Color.black, null, true, "宋体", false, null, true);
        //输出框
        outputMsg = InitialComponent.setJTextArea(15, Color.black, null, true, "宋体", false, null, true);
        JScrollPane inputScrollPane = new JScrollPane(inputMsg);
        JScrollPane outputScrollPane = new JScrollPane(outputMsg);

        inputScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.black, Color.GRAY));
        outputScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.black, Color.GRAY));
        //输入输出框面板
        msgPanel = new JPanel();
        msgPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        msgPanel.setLayout(new GridLayout(1, 2));

        msgPanel.add(inputScrollPane);
        msgPanel.add(outputScrollPane);

        // 按钮面板
        buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        JButton resetBtn = new JButton("重置");
        JButton exitBtn = new JButton("退出");

        buttonPanel.add(exitBtn);
        buttonPanel.add(resetBtn);

        //总的面板
        containerPanel = new JPanel();
        containerPanel.setBorder(BorderFactory.createEtchedBorder(Color.black, Color.GRAY));
        containerPanel.setLayout(new BorderLayout());

        containerPanel.add(msgPanel, BorderLayout.CENTER);
        containerPanel.add(buttonPanel, BorderLayout.SOUTH);
        container.add(containerPanel, BorderLayout.CENTER);

        inputMsg.addMouseListener(inputMsgClickAction(inputMsg));
        inputMsg.getDocument().addUndoableEditListener(undoInputMsgManager);
        outputMsg.getDocument().addUndoableEditListener(undoOutputMsgManager);
        outputMsg.addMouseListener(outputMsgClickAction(outputMsg));
        resetBtn.addActionListener(resetAction());
        exitBtn.addActionListener(exitAction());
        this.addMouseListener(containerClickEvent(this, noteFrame));
    }

    private void shiftClickListener() {
        InputMap inputMap = inputMsg.getInputMap();
        InputMap outputMap = outputMsg.getInputMap();
        InitialComponent.initialCtrlZAndB(inputMap, undoInputMsgManager);
        InitialComponent.initialCtrlZAndB(outputMap, undoOutputMsgManager);

    }

    private MouseAdapter containerClickEvent(ToolParentFrame toolParentFrame, JFrame noteFrame) {
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
                        toolParentFrame.setBounds(0, 30, screen.width, screen.height - 26);
                        windowSizeClick++;
                    } else {
                        toolParentFrame.setBounds(noteFrame.getX(), noteFrame.getY(), noteFrame.getWidth(), noteFrame.getHeight());
                        windowSizeClick = 0;
                    }
                }
            }
        };
    }

    private ActionListener exitAction() {
        return e -> this.dispose();
    }

    private ActionListener resetAction() {
        return e -> {
            inputMsg.setText("");
            outputMsg.setText("");
            inputClick = 0;
            outputClick = 0;
            inputMsg.requestFocus();
        };
    }

    private MouseAdapter outputMsgClickAction(JTextArea textArea) {
        return new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (outputClick == 0) {
                        outputClick++;
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
                    } else {
                        textArea.setLineWrap(false);
                        textArea.setWrapStyleWord(true);
                        outputClick = 0;
                    }
                }
            }
        };
    }

    private MouseAdapter inputMsgClickAction(JTextArea jTextArea) {
        return new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (inputClick == 0) {
                        jTextArea.setLineWrap(true);
                        jTextArea.setWrapStyleWord(true);
                        inputClick++;
                    } else {
                        jTextArea.setLineWrap(false);
                        jTextArea.setWrapStyleWord(true);
                        inputClick = 0;
                    }
                }
            }
        };
    }
}
