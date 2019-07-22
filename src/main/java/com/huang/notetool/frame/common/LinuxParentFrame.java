package com.huang.notetool.frame.common;

import com.huang.notetool.util.InitialComponent;
import com.huang.notetool.util.ToolUtil;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Linux命令相关的母版
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-05-25 19:08:24
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-25   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class LinuxParentFrame extends JDialog {
    /**
     * 日志
     */
    protected Logger logger = Logger.getLogger(LinuxParentFrame.class);
    /**
     * 总的面板
     */
    protected JPanel containerPanel;
    /**
     * 存放按钮的面板
     */
    protected JPanel buttonPanel;

    /**
     * 主键
     */
    protected JTextField idJTextField;

    /**
     * 关联表主键
     */
    protected JTextField linuxCmdExamIdJTextField;

    /**
     * Linux命令
     */
    protected JTextField linuxCmdJTextField;

    /**
     * 作用
     */
    protected JTextField usagesJTextField;

    /**
     * 用法
     */
    protected JTextField exampleJTextField;

    /**
     * 使用权限
     */
    protected JComboBox permissionJComboBox;
    /**
     * 添加方法时间
     */
    protected JTextField insertDateJTextField;
    /**
     * 更新方法时间
     */
    protected JTextField updateDateJTextField;

    /**
     * 更新次数
     */
    protected JTextField updateTimesJTextField;
    /**
     * 参数说明
     */
    protected JTextArea parameterNoteJTextArea;
    /**
     * 多个实例，使用中文；作为换行
     */
    protected JTextArea examplesJTextArea;
    /**
     * 刷新按钮
     */
    protected JButton refreshBtn;

    /**
     * 计数点击次数
     */
    private int parameterNoteJTextAreaClickCount;
    private int examplesJTextAreaClickCount;
    private int windowSizeClick;
    /**
     * 支持撤销操作
     */
    private UndoManager undoParameterNoteManager = new UndoManager();
    private UndoManager undoExamplesManager = new UndoManager();

    public LinuxParentFrame() {

    }

    public LinuxParentFrame(JFrame noteFrame) {
        logger.info("打开Linux相关页面 ... ");
        this.setTitle("双击可以切换是否换行,按钮面板点击三次实现缩放,");
        //容器
        Container container = this.getContentPane();
        this.setBounds(noteFrame.getX(), noteFrame.getY(), noteFrame.getWidth(), noteFrame.getHeight());
        container.setLayout(new BorderLayout());
        this.setModal(false);
        //此弹窗必须依赖于子弹窗
        this.setModal(true);
        initial(noteFrame);
        shiftClickListener();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initial(JFrame jFrame) {
        if (!InitialComponent.isWindowsLookAndFeel()) {
            this.setOpacity(jFrame.getOpacity());
        }
        this.setCursor(jFrame.getCursor());
        //初始化中间的面板
        JPanel msgPanel = initialCenterPanel();

        // 按钮面板
        buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        refreshBtn = new JButton("刷新");
        JButton exitBtn = new JButton("退出");

        buttonPanel.add(refreshBtn);
        buttonPanel.add(exitBtn);

        //总的面板
        containerPanel = new JPanel();
        containerPanel.setBorder(BorderFactory.createEtchedBorder(Color.black, Color.GRAY));
        containerPanel.setLayout(new BorderLayout());

        containerPanel.add(msgPanel, BorderLayout.CENTER);
        containerPanel.add(buttonPanel, BorderLayout.SOUTH);
        this.add(containerPanel);

        parameterNoteJTextArea.addMouseListener(inputMsgClickAction(parameterNoteJTextArea));
        parameterNoteJTextArea.getDocument().addUndoableEditListener(undoParameterNoteManager);
        examplesJTextArea.getDocument().addUndoableEditListener(undoExamplesManager);
        examplesJTextArea.addMouseListener(outputMsgClickAction(examplesJTextArea));
        //刷新事件
        refreshBtn.addActionListener(refreshBtnAction(jFrame));
        //退出事件
        exitBtn.addActionListener(exitAction());
        this.addMouseListener(containerClickEvent(this, jFrame));
    }

    /**
     * 初始化中间的面板
     *
     * @return 中间面板
     */
    private JPanel initialCenterPanel() {
        initialJTextField();
        //左边面板
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(9, 2));
        initialLeftPanel(leftPanel);

        //右边面板
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(2, 2));
        initialRightPanel(rightPanel);
        //输入输出框面板
        JPanel msgPanel = new JPanel();
        msgPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        msgPanel.setLayout(new GridLayout(1, 2));

        msgPanel.add(leftPanel);
        msgPanel.add(rightPanel);
        return msgPanel;
    }

    /**
     * 初始化右边显示面板
     *
     * @param rightPanel 右边总面板
     */
    private void initialRightPanel(JPanel rightPanel) {
        //参数说明输入框
        parameterNoteJTextArea = InitialComponent.setJTextArea(15, Color.black, null, true, "宋体", false, null, true);
        //多个实例输出框
        examplesJTextArea = InitialComponent.setJTextArea(15, Color.black, null, true, "宋体", false, null, true);
        JScrollPane inputScrollPane = new JScrollPane(parameterNoteJTextArea);
        JScrollPane outputScrollPane = new JScrollPane(examplesJTextArea);

        inputScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.black, Color.GRAY));
        outputScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.black, Color.GRAY));
        Box paramBox = new Box(BoxLayout.X_AXIS);
        paramBox.add(new JLabel("参数说明："));
        paramBox.add(inputScrollPane);

        Box examplesBox = new Box(BoxLayout.X_AXIS);
        examplesBox.add(new JLabel("多个实例："));
        examplesBox.add(outputScrollPane);

        rightPanel.add(paramBox);
        rightPanel.add(examplesBox);
    }

    /**
     * 初始化全局的JTextField
     */
    private void initialJTextField() {
        //主键
        idJTextField = InitialComponent.setJTExtField(null, null, false, null);
        //关联表主键
        linuxCmdExamIdJTextField = InitialComponent.setJTExtField(null, null, false, null);
        //Linux命令
        linuxCmdJTextField = InitialComponent.setJTExtField(null, null, true, null);
        //作用
        usagesJTextField = InitialComponent.setJTExtField(null, null, true, null);
        //用法
        exampleJTextField = InitialComponent.setJTExtField(null, null, true, null);
        //使用权限
        permissionJComboBox = new JComboBox(ToolUtil.LINUX_PERMISSION);
        //添加方法时间
        insertDateJTextField = InitialComponent.setJTExtField(null, null, false, null);
        //更新方法时间
        updateDateJTextField = InitialComponent.setJTExtField(null, null, false, null);
        //更新次数
        updateTimesJTextField = InitialComponent.setJTExtField(null, null, false, null);
    }

    /**
     * 初始化左边显示面板
     *
     * @param leftPanel 左边总面板
     */
    private void initialLeftPanel(JPanel leftPanel) {
        Box idBox = new Box(BoxLayout.X_AXIS);
        idBox.add(new JLabel("命令编号："));
        //主键
        idBox.add(idJTextField);

        Box examIdBox = new Box(BoxLayout.X_AXIS);
        examIdBox.add(new JLabel("关联编号："));
        //关联表主键
        examIdBox.add(linuxCmdExamIdJTextField);

        Box linuxCmdBox = new Box(BoxLayout.X_AXIS);
        linuxCmdBox.add(new JLabel("命令名称："));
        //Linux命令
        linuxCmdBox.add(linuxCmdJTextField);

        Box usagesBox = new Box(BoxLayout.X_AXIS);
        usagesBox.add(new JLabel("命令作用："));
        //作用
        usagesBox.add(usagesJTextField);

        Box exampleBox = new Box(BoxLayout.X_AXIS);
        exampleBox.add(new JLabel("命令用法："));
        //用法
        exampleBox.add(exampleJTextField);

        Box permissionBox = new Box(BoxLayout.X_AXIS);
        permissionBox.add(new JLabel("使用权限："));
        //使用权限
        permissionBox.add(permissionJComboBox);

        Box insertDateBox = new Box(BoxLayout.X_AXIS);
        insertDateBox.add(new JLabel("添加时间："));
        // 添加方法时间
        insertDateBox.add(insertDateJTextField);

        Box updateDateBox = new Box(BoxLayout.X_AXIS);
        updateDateBox.add(new JLabel("更新时间："));
        // 更新方法时间
        updateDateBox.add(updateDateJTextField);

        Box updateTimesBox = new Box(BoxLayout.X_AXIS);
        updateTimesBox.add(new JLabel("更新次数："));
        //更新次数
        updateTimesBox.add(updateTimesJTextField);

        leftPanel.add(idBox);
        leftPanel.add(examIdBox);
        leftPanel.add(linuxCmdBox);
        leftPanel.add(usagesBox);
        leftPanel.add(exampleBox);
        leftPanel.add(permissionBox);
        leftPanel.add(insertDateBox);
        leftPanel.add(updateDateBox);
        leftPanel.add(updateTimesBox);
    }

    /**
     * 设置快捷键支持撤销操作
     */
    private void shiftClickListener() {
        InputMap inputMap = parameterNoteJTextArea.getInputMap();
        InputMap outputMap = examplesJTextArea.getInputMap();
        InitialComponent.initialCtrlZAndB(inputMap, undoParameterNoteManager);
        InitialComponent.initialCtrlZAndB(outputMap, undoExamplesManager);
    }

    private MouseAdapter containerClickEvent(LinuxParentFrame toolParentFrame, JFrame noteFrame) {
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

    /**
     * 刷新事件
     *
     * @param jFrame 面板
     * @return ActionListener
     */
    private ActionListener refreshBtnAction(JFrame jFrame) {
        return e -> {
            logger.info("刷新Linux面板事件");
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
                    if (examplesJTextAreaClickCount == 0) {
                        examplesJTextAreaClickCount++;
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
                    } else {
                        textArea.setLineWrap(false);
                        textArea.setWrapStyleWord(true);
                        examplesJTextAreaClickCount = 0;
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
                    if (parameterNoteJTextAreaClickCount == 0) {
                        jTextArea.setLineWrap(true);
                        jTextArea.setWrapStyleWord(true);
                        parameterNoteJTextAreaClickCount++;
                    } else {
                        jTextArea.setLineWrap(false);
                        jTextArea.setWrapStyleWord(true);
                        parameterNoteJTextAreaClickCount = 0;
                    }
                }
            }
        };
    }
}
