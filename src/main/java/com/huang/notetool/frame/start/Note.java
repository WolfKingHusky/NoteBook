package com.huang.notetool.frame.start;

import com.huang.notetool.frame.common.JRootParentDiag;
import com.huang.notetool.frame.common.ShowMsgSinglePanel;
import com.huang.notetool.frame.exception.AddExceptionFrame;
import com.huang.notetool.frame.exception.FindExceptionMsgFrame;
import com.huang.notetool.frame.exception.ManageExceptionFrame;
import com.huang.notetool.frame.format.crypt.DecryptAndEncryptFrame;
import com.huang.notetool.frame.format.json.JsonFormatFrame;
import com.huang.notetool.frame.format.linux.LinuxCmdFrame;
import com.huang.notetool.frame.format.net.InternetSearchFrame;
import com.huang.notetool.frame.format.reg.RegGenerateFrame;
import com.huang.notetool.frame.format.reg.RegTestFrame;
import com.huang.notetool.frame.format.str.StrEncodingChangeFrame;
import com.huang.notetool.frame.format.unicode.UnicodeTransferFrame;
import com.huang.notetool.frame.format.xml.XmlFormatFrame;
import com.huang.notetool.frame.method.AddMethodFrame;
import com.huang.notetool.frame.method.ManageMethodFrame;
import com.huang.notetool.frame.method.SearchMethodFrame;
import com.huang.notetool.frame.note.NoteFrame;
import com.huang.notetool.frame.note.NoteImagFrame;
import com.huang.notetool.frame.progress.ShowProcess;
import com.huang.notetool.frame.system.SystemLogFrame;
import com.huang.notetool.frame.system.SystemSoftware;
import com.huang.notetool.po.NoteMsg;
import com.huang.notetool.po.SkinLookAndFeel;
import com.huang.notetool.quote.BlueScreen;
import com.huang.notetool.service.ExceptionService;
import com.huang.notetool.service.MethodService;
import com.huang.notetool.service.NoteMsgService;
import com.huang.notetool.service.SkinLookAndFeelService;
import com.huang.notetool.tool.*;
import com.huang.notetool.util.*;
import org.apache.log4j.Logger;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.fonts.FontPolicy;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import static java.awt.event.InputEvent.*;
import static javax.swing.text.DefaultEditorKit.pageUpAction;

/**
 * 页面主窗口
 * 实现DropTargetListener 支持整个面板拖入
 * notetool.jar中没有主清单属性
 * 将META-INF文件夹选择在sources文件夹下面
 *
 * @author 黄
 */
public class Note extends JFrame implements ActionListener {
    private static final String EXIT_STR = "exit";
    private static final String EXIT_STR_CN = "我要退出";
    private static final String PASSWORD = "1234";
    private static final String EMPTY_STR = "";
    private static final String POINT = ".";
    private static final String IMAGE_NOTE = "imageNote";
    /**
     * 方法Service
     */
    private MethodService methodService;
    /**
     * 方法Service
     */
    private NoteMsgService noteMsgService;
    /**
     * 异常Service
     */
    private ExceptionService exceptionService;
    /**
     * 皮肤Service
     */
    private SkinLookAndFeelService skinLookAndFeelService;
    /**
     * 计数换行与否
     */
    private int lineSprite;
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(NoteStartBar.class);
    /**
     * 搜索
     */
    private JMenuItem searchJMenuItem;
    /**
     * 加速
     */
    private JMenuItem speedup;
    /**
     * 显示文本框
     */
    private JTextArea inputTextArea;
    /**
     * 定义界面内容
     */
    private JTextField fontSizeField, searchInTextAreaField, replaceOrigField, replaceDesField;
    /**
     * 支持剪切操作
     */
    private Clipboard clipboard;
    /**
     * 文件名字
     */
    private String fileName = EMPTY_STR;
    /**
     * 支持撤销操作
     */
    private UndoManager undoManager = new UndoManager();


    /**
     * 初始化窗口
     *
     * @param content         显示内容
     * @param skinLookAndFeel 皮肤
     */
    public Note(String content, SkinLookAndFeel skinLookAndFeel) {
        logger.info("初始化应用程序 .... ");
        //隐藏Windows默认窗体
        //修改Windows窗体都需要这个属性
        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        try {
            /*将LookAndFeel设置成Windows样式
             * 更改皮肤，点击substance.jar -> 找到org.jvnet.substance.skin这个包 ->
             * 将下面的SubstanceDustCoffeeLookAndFeel 替换成
             * 刚刚打开的包下的任意一个“Substance....LookAndFeel”即可
            //海洋场景
            // MetalLookAndFeel.setCurrentTheme(new OceanTheme());
            UIManager.setLookAndFeel(new org.jvnet.substance.skin.SubstanceCremeCoffeeLookAndFeel
            ());
            UIManager.setLookAndFeel(new com.nilo.plaf.nimrod.NimRODLookAndFeel());
            */
            //设置默认为Windows样式
            if (null == skinLookAndFeel) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } else {
                //路径(包含包路径如:org.test.GetClass)。利用反射机制
                Class onwClass = Class.forName(skinLookAndFeel.getSkinName());
                //有了一个Class类的实例了，下面就是获取一个Object类的实例对象
                //有了一个Object对象，下一步就可以对这个Object进行强制转换了
                SubstanceLookAndFeel substanceCremeCoffeeLookAndFeel =
                        (SubstanceLookAndFeel) onwClass.newInstance();
                UIManager.setLookAndFeel(substanceCremeCoffeeLookAndFeel);

                setSkin(skinLookAndFeel);
                //javax.swing.plaf.metal.MetalRootPaneUI cannot be cast to org.jvnet.substance
                // .SubstanceRootPaneUI
                //没有这个会跑抛出异常
                SwingUtilities.updateComponentTreeUI(this);
            }
        } catch (Exception ex) {
            logger.warn("加载系统样式失败" + ex);
        }
        this.setTitle("NoteBook");
        //得到屏幕尺寸
        Dimension screen = getToolkit().getScreenSize();
        //窗口风格
        setLayout(new BorderLayout());
        //窗口大小
        setSize((screen.width) * 2 / 3, (screen.height) * 2 / 3);
        //窗口位置
        setLocation((screen.width - getSize().width) / 2, (screen.height - getSize().height) / 2);
        //初始化控件
        initial(content);
        //快捷键
        shiftClickListener();
        setVisible(true);
    }

    private void setSkin(SkinLookAndFeel skinLookAndFeel) {
        if (StringUtils.isNotEmpty(skinLookAndFeel.getThemName())) {
            SubstanceLookAndFeel.setCurrentTheme(skinLookAndFeel.getThemName());
        }
        if (StringUtils.isNotEmpty(skinLookAndFeel.getBorderName())) {
            SubstanceLookAndFeel.setCurrentBorderPainter(skinLookAndFeel.getBorderName());
        }
        if (StringUtils.isNotEmpty(skinLookAndFeel.getButtonShaperName())) {
            SubstanceLookAndFeel.setCurrentButtonShaper(skinLookAndFeel
                    .getButtonShaperName());
        }
        if (StringUtils.isNotEmpty(skinLookAndFeel.getWaterMarkName())) {
            SubstanceLookAndFeel.setCurrentWatermark(skinLookAndFeel.getWaterMarkName());
        }
    }

    /**
     * 窗口样式设置为windows
     */
    private void setWindowsStyle() {
        try {
            LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
            String sysLookAndFeelName = UIManager.getSystemLookAndFeelClassName();
            //默认窗口也要显示信息
            if (!lookAndFeel.getClass().getName().equalsIgnoreCase(sysLookAndFeelName)) {
                ThreadPool.shutDownThreadPool();
                NoteStartBar noteStartBar = new NoteStartBar(inputTextArea
                        .getText());
                ThreadPool.startThread(noteStartBar, null);
                this.dispose();
            } else {
                logger.info("已经是Windows样式窗口，不用反复替换");
            }
        } catch (Exception ex) {
            new JRootParentDiag(Note.this, "系统不支持Windows样式", Constants.NOTICE_IMAGE);
            logger.warn("加载系统样式失败" + ex);
        }
    }

    /**
     * 按键监听
     */
    private void shiftClickListener() {
        InputMap inputMap = inputTextArea.getInputMap();
        //撤销操作快捷键
        InitialComponent.initialCtrlZAndB(inputMap, undoManager);
        //ctrl+c
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_C, CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.copyAction);
        //ctrl +x
        key = KeyStroke.getKeyStroke(KeyEvent.VK_X, CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.cutAction);
        //ctrl + v
        key = KeyStroke.getKeyStroke(KeyEvent.VK_V, CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.pasteAction);
        //ctrl + 左箭头
        key = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.backwardAction);
        //ctrl + 右箭头
        key = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.forwardAction);
        //
        key = KeyStroke.getKeyStroke(KeyEvent.VK_UP, CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.upAction);
        key = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.downAction);

        //垂直向下翻页的操作名称。
        key = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0);
        inputMap.put(key, pageUpAction);
        //           垂直向上翻页的操作名称。
        key = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0);
        inputMap.put(key, DefaultEditorKit.pageDownAction);
    }

    /**
     * 初始化控件
     *
     * @param content 显示内容
     */
    private void initial(String content) {

        methodService = new MethodService();
        noteMsgService = new NoteMsgService();
        exceptionService = new ExceptionService();
        skinLookAndFeelService = new SkinLookAndFeelService();
        //关键部分，没有为cb申请内存，下面对cb操作会出错
        clipboard = new Clipboard("Clipboard");
        //初始化容器
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        //设置显示文本框
        setInputTextArea(content);
        JScrollPane jsp = new JScrollPane(inputTextArea);
        jsp.setBorder(BorderFactory.createEtchedBorder());

        //第一个菜单，点击出现子菜单列表
        JMenuBar firstJMenuBar = new JMenuBar();
        //第一个菜单的子菜单面板
        JPanel firstJPanel = new JPanel();
        firstJPanel.setLayout(new GridLayout(1, 1));
        //开始菜单
        //ALT+S
        JMenu startJMenu = InitialComponent.setJMenu("开始(S)", KeyEvent.VK_S);
        //开始菜单和子菜单设置
        setStartJMenuItem(startJMenu);
        //快捷键ALT+C
        JMenu insertJMenu = InitialComponent.setJMenu("插入(C)", KeyEvent.VK_C);
        //设置插入菜单以及子菜单
        setInsertJMenuItem(insertJMenu);
        //ALT+B
        JMenu pageLayoutJMenu = InitialComponent.setJMenu("页面布局(B)", KeyEvent.VK_B);
        //设置页面布局菜单以及子菜单
        setPageLayoutJMenu(pageLayoutJMenu);

        //快捷键 ALT+L
        JMenu checkJMenu = InitialComponent.setJMenu("审阅(L)", KeyEvent.VK_L);
        //设置审阅菜单以及子菜单
        setCheckJMenuJMenu(checkJMenu);

        //快捷键ALT+Y
        JMenu quoteJMenu = InitialComponent.setJMenu("引用(Y)", KeyEvent.VK_Y);
        //设置引用菜单以及子菜单
        setQuoteJMenuJMenu(quoteJMenu);

        //ALT+V
        JMenu viewJMenu = InitialComponent.setJMenu("视图(V)", KeyEvent.VK_V);
        //设置视图菜单以及子菜单
        setViewJMenu(viewJMenu);

        //快捷键ALT+T
        JMenu toolJMenu = InitialComponent.setJMenu("工具(T)", KeyEvent.VK_T);
        //设置工具菜单以及子菜单
        setToolJMenuJMenu(toolJMenu);

        //ALT+U
        JMenu showMsgJMenu = InitialComponent.setJMenu("关于我们(U)", KeyEvent.VK_U);
        //设置关于我们菜单以及子菜单
        setShowMsgJMenu(showMsgJMenu);

        //ALT+J
        JMenu noteMsgJMenu = InitialComponent.setJMenu("笔记(J)", KeyEvent.VK_J);
        //设置笔记菜单以及子菜单
        setNoteMsgJMenu(noteMsgJMenu);

        firstJMenuBar.add(startJMenu);
        firstJMenuBar.add(insertJMenu);
        firstJMenuBar.add(pageLayoutJMenu);
        firstJMenuBar.add(checkJMenu);
        firstJMenuBar.add(quoteJMenu);
        firstJMenuBar.add(toolJMenu);
        firstJMenuBar.add(viewJMenu);
        firstJMenuBar.add(noteMsgJMenu);
        firstJMenuBar.add(showMsgJMenu);

        firstJPanel.add(firstJMenuBar);

        contentPane.add(firstJPanel, BorderLayout.NORTH);
        contentPane.add(jsp, BorderLayout.CENTER);
        //关闭窗口时退出程序
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(windowsDefaultListener());
        //JFrame是由这么几部分组成：最底下一层JRootPane，
        // 上面是glassPane(一个JPanel)和layeredPane(一个JLayeredPane)，
        // 而layeredPane又由contentPane(一个JPanel)和menuBar构成。
        // 我们一般在JFrame上添加组件往往都是加在contentPane上面
        //替代Windows默认窗体
        this.setContentPane(contentPane);
    }

    private WindowListener windowsDefaultListener() {
        return new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                doExit();
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        };
    }

    /**
     * 设置笔记菜单以及子菜单
     *
     * @param noteMsgJMenu 笔记菜单
     */
    private void setNoteMsgJMenu(JMenu noteMsgJMenu) {
        JMenu noteJMenu = new JMenu("工作笔记");
        JMenu studyJMenu = new JMenu("学习笔记");
        JMenu imageJMenu = new JMenu("图片笔记");

        String noteType = "workNote";
        noteJMenu.addMouseListener(workNoteMenuClickAction(noteJMenu, noteType));
        noteType = "studyNote";
        studyJMenu.addMouseListener(workNoteMenuClickAction(studyJMenu, noteType));
        noteType = "imageNote";
        imageJMenu.addMouseListener(workNoteMenuClickAction(imageJMenu, noteType));

        noteMsgJMenu.add(noteJMenu);
        noteMsgJMenu.add(studyJMenu);
        noteMsgJMenu.add(imageJMenu);
    }

    /**
     * 菜单鼠标点击事件
     *
     * @param noteJMenu 笔记菜单
     * @param noteType  笔记类型
     * @return MouseAdapter
     */
    private MouseAdapter workNoteMenuClickAction(JMenu noteJMenu, String noteType) {
        return new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e MouseEvent
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                noteJMenu.removeAll();
                setNoteJMenu(noteJMenu, noteType);
                validate();
                repaint();
            }
        };
    }

    /**
     * 设置工作笔记菜单以及子菜单
     *
     * @param noteJMenu 工作笔记菜单
     * @param noteType  笔记类型
     */
    private void setNoteJMenu(JMenu noteJMenu, String noteType) {
        List<String> nameList = noteMsgService.findAllNameListByType(noteType);
        if (null != nameList && !nameList.isEmpty()) {
            String[] msgArr = nameList.toArray(new String[0]);
            JList jList = new JList(msgArr);
            jList.setBackground(noteJMenu.getBackground());
            JScrollPane jScrollPane = new JScrollPane(jList);
            Point point = ToolUtil.getNoteJListPoint();
            jScrollPane.setPreferredSize(new Dimension(point.x, point.y));
            noteJMenu.add(jScrollPane);

            //工作笔记点击事件
            jList.addMouseListener(noteWorkMouseListener(jList, noteType));
        } else {
            JMenuItem addItem = new JMenuItem("添加");
            noteJMenu.add(addItem);
            addItem.addActionListener(addItemActionListener(noteType));
            setVisible(true);
        }
    }

    /**
     * 笔记点击事件
     *
     * @param jList    列表
     * @param noteType 笔记类型
     * @return MouseListener
     */
    private MouseListener noteWorkMouseListener(JList jList, String noteType) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                if (jList.getSelectedIndex() != -1) {
                    if (mouseEvent.getClickCount() == 1) {
                        if (!IMAGE_NOTE.equalsIgnoreCase(noteType)) {
                            String noteName = (String) theList.getSelectedValue();
                            if (StringUtils.isNotEmpty(noteName)) {
                                showNote(noteName, noteType);
                            }
                        } else {
                            String noteName = (String) theList.getSelectedValue();
                            if (StringUtils.isNotEmpty(noteName)) {
                                showImageNote(noteName, noteType);
                            }
                        }
                    }
                }
            }
        };
    }

    /**
     * 显示笔记信息
     *
     * @param noteName 笔记名称
     * @param noteType 笔记类型
     */
    private void showImageNote(String noteName, String noteType) {
        NoteImagFrame noteImageFrame = new NoteImagFrame(Note.this);
        logger.info("打开笔记 " + noteName);
        NoteMsg noteMsg = noteMsgService.findMsgByName(noteName);
        if (null != noteMsg) {
            noteImageFrame.setText(noteMsg.getNote(), noteMsg.getId());
            noteImageFrame.setTitle(noteMsg.getName());
            noteImageFrame.setType(noteType);
        }
        noteImageFrame.setVisible(true);
    }

    /**
     * 新增事件
     *
     * @param noteType 笔记类型
     * @return ActionListener
     */
    private ActionListener addItemActionListener(String noteType) {
        return e -> {
            logger.info("新增笔记 ");
            if (!IMAGE_NOTE.equalsIgnoreCase(noteType)) {
                NoteFrame noteFrame = new NoteFrame(Note.this);
                noteFrame.setTitle("未命名");
                noteFrame.setType(noteType);
                noteFrame.setVisible(true);
            } else {
                NoteImagFrame noteImageFrame = new NoteImagFrame(Note.this);
                noteImageFrame.setTitle("未命名");
                noteImageFrame.setType(noteType);
                noteImageFrame.setVisible(true);
            }
        };
    }


    /**
     * 显示笔记信息
     *
     * @param name     笔记名称
     * @param noteType 笔记类型
     */
    private void showNote(String name, String noteType) {
        NoteFrame noteFrame = new NoteFrame(Note.this);
        logger.info("打开笔记 " + name);
        NoteMsg noteMsg = noteMsgService.findMsgByName(name);
        if (null != noteMsg) {
            noteFrame.setText(noteMsg.getNote(), noteMsg.getId());
            noteFrame.setTitle(noteMsg.getName());
            noteFrame.setType(noteType);
        }
        noteFrame.setVisible(true);
    }

    /**
     * 设置显示文本框
     *
     * @param content 内容
     */
    private void setInputTextArea(String content) {
        inputTextArea = InitialComponent.setJTextArea(20, Color.BLACK, null, true, "宋体", false,
                null, true);
        inputTextArea.setText(content == null ? EMPTY_STR : content);
        //接收输入退出
        inputTextArea.addKeyListener(inputTextAreaExitWay());
        inputTextArea.getDocument().addUndoableEditListener(undoManager);
        // 在 textArea 上注册拖拽目标监听器
        DropTarget dropTarget = new DropTarget(inputTextArea, DnDConstants.ACTION_COPY_OR_MOVE,
                inputJTextAreaDropAction(), true);
        inputTextArea.setDropTarget(dropTarget);
    }

    /**
     * 置关于我们菜单以及子菜单
     *
     * @param showMsgJMenu
     */
    private void setShowMsgJMenu(JMenu showMsgJMenu) {
        JMenuItem aboutAsJMenuItem = new JMenuItem("2.0.1更新日志");
        JMenuItem aboutAsJMenuItem2 = new JMenuItem("2.0.2更新日志");
        JMenuItem aboutAsJMenuItem3 = new JMenuItem("2.0.3更新日志");
        JMenuItem ideaCodeJMenuItem = new JMenuItem("IDEA注册码");

        showMsgJMenu.add(aboutAsJMenuItem);
        showMsgJMenu.add(aboutAsJMenuItem2);
        showMsgJMenu.add(aboutAsJMenuItem3);
        showMsgJMenu.add(ideaCodeJMenuItem);

        aboutAsJMenuItem.addActionListener(showMsgAction());
        aboutAsJMenuItem2.addActionListener(showMsgAction2());
        aboutAsJMenuItem3.addActionListener(showMsgAction3());
        ideaCodeJMenuItem.addActionListener(ideaCodeAction());
    }

    /**
     * 关于我们2.0.3
     *
     * @return ActionListener
     */
    private ActionListener showMsgAction3() {
        return e -> new ShowMsgSinglePanel(ToolUtil.aboutAsMsg3, Note.this);
    }

    /**
     * 显示IDEA注册码
     *
     * @return ActionListener
     */
    private ActionListener ideaCodeAction() {
        return e -> {
            //到2019.11
            String code = "YZVR7WDLV8" +
                    "-eyJsaWNlbnNlSWQiOiJZWlZSN1dETFY4IiwibGljZW5zZWVOYW1lIjoiamV0YnJhaW5zIGpzIiwiYXNzaWduZWVOYW1lIjoiIiwiYXNzaWduZWVFbWFpbCI6IiIsImxpY2Vuc2VSZXN0cmljdGlvbiI6IkZvciBlZHVjYXRpb25hbCB1c2Ugb25seSIsImNoZWNrQ29uY3VycmVudFVzZSI6ZmFsc2UsInByb2R1Y3RzIjpbeyJjb2RlIjoiSUkiLCJwYWlkVXBUbyI6IjIwMTktMTEtMjYifSx7ImNvZGUiOiJBQyIsInBhaWRVcFRvIjoiMjAxOS0xMS0yNiJ9LHsiY29kZSI6IkRQTiIsInBhaWRVcFRvIjoiMjAxOS0xMS0yNiJ9LHsiY29kZSI6IlBTIiwicGFpZFVwVG8iOiIyMDE5LTExLTI2In0seyJjb2RlIjoiR08iLCJwYWlkVXBUbyI6IjIwMTktMTEtMjYifSx7ImNvZGUiOiJETSIsInBhaWRVcFRvIjoiMjAxOS0xMS0yNiJ9LHsiY29kZSI6IkNMIiwicGFpZFVwVG8iOiIyMDE5LTExLTI2In0seyJjb2RlIjoiUlMwIiwicGFpZFVwVG8iOiIyMDE5LTExLTI2In0seyJjb2RlIjoiUkMiLCJwYWlkVXBUbyI6IjIwMTktMTEtMjYifSx7ImNvZGUiOiJSRCIsInBhaWRVcFRvIjoiMjAxOS0xMS0yNiJ9LHsiY29kZSI6IlBDIiwicGFpZFVwVG8iOiIyMDE5LTExLTI2In0seyJjb2RlIjoiUk0iLCJwYWlkVXBUbyI6IjIwMTktMTEtMjYifSx7ImNvZGUiOiJXUyIsInBhaWRVcFRvIjoiMjAxOS0xMS0yNiJ9LHsiY29kZSI6IkRCIiwicGFpZFVwVG8iOiIyMDE5LTExLTI2In0seyJjb2RlIjoiREMiLCJwYWlkVXBUbyI6IjIwMTktMTEtMjYifSx7ImNvZGUiOiJSU1UiLCJwYWlkVXBUbyI6IjIwMTktMTEtMjYifV0sImhhc2giOiIxMTA1NzI3NC8wIiwiZ3JhY2VQZXJpb2REYXlzIjowLCJhdXRvUHJvbG9uZ2F0ZWQiOmZhbHNlLCJpc0F1dG9Qcm9sb25nYXRlZCI6ZmFsc2V9-rsJR5mlJcjibqRu1gQAMUCngMe8i+AOWIi+JZkNFYPET2G1ONcLPcIzoATTRi6ofkDm5l+3Y4HXjBPjVU6bHDdMBAzCnUqpXKsCknwSYyPSU0Y5pzuLvw6O9aPlQ46UBoTEC2BL5W6f11S7NlAq7tTbDuvFUynqSGAmTEfuZtKmzRmp20ejTPuMlSO7UqSkZvkg6YvSTrax1d2K+P9SAmVGZ9iC7AzBs4AwTf84QB9qHvE/Nh0oELSHWGG9hsZZ7sVghI/39/jPQFTp8GLFsl36ZPybPhGDam721zxS9H++/eJk23Jz3nxaRluE4dWmpHrDg1qBHp8qVpSFejg2QYw==-MIIElTCCAn2gAwIBAgIBCTANBgkqhkiG9w0BAQsFADAYMRYwFAYDVQQDDA1KZXRQcm9maWxlIENBMB4XDTE4MTEwMTEyMjk0NloXDTIwMTEwMjEyMjk0NlowaDELMAkGA1UEBhMCQ1oxDjAMBgNVBAgMBU51c2xlMQ8wDQYDVQQHDAZQcmFndWUxGTAXBgNVBAoMEEpldEJyYWlucyBzLnIuby4xHTAbBgNVBAMMFHByb2QzeS1mcm9tLTIwMTgxMTAxMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxcQkq+zdxlR2mmRYBPzGbUNdMN6OaXiXzxIWtMEkrJMO/5oUfQJbLLuMSMK0QHFmaI37WShyxZcfRCidwXjot4zmNBKnlyHodDij/78TmVqFl8nOeD5+07B8VEaIu7c3E1N+e1doC6wht4I4+IEmtsPAdoaj5WCQVQbrI8KeT8M9VcBIWX7fD0fhexfg3ZRt0xqwMcXGNp3DdJHiO0rCdU+Itv7EmtnSVq9jBG1usMSFvMowR25mju2JcPFp1+I4ZI+FqgR8gyG8oiNDyNEoAbsR3lOpI7grUYSvkB/xVy/VoklPCK2h0f0GJxFjnye8NT1PAywoyl7RmiAVRE/EKwIDAQABo4GZMIGWMAkGA1UdEwQCMAAwHQYDVR0OBBYEFGEpG9oZGcfLMGNBkY7SgHiMGgTcMEgGA1UdIwRBMD+AFKOetkhnQhI2Qb1t4Lm0oFKLl/GzoRykGjAYMRYwFAYDVQQDDA1KZXRQcm9maWxlIENBggkA0myxg7KDeeEwEwYDVR0lBAwwCgYIKwYBBQUHAwEwCwYDVR0PBAQDAgWgMA0GCSqGSIb3DQEBCwUAA4ICAQAF8uc+YJOHHwOFcPzmbjcxNDuGoOUIP+2h1R75Lecswb7ru2LWWSUMtXVKQzChLNPn/72W0k+oI056tgiwuG7M49LXp4zQVlQnFmWU1wwGvVhq5R63Rpjx1zjGUhcXgayu7+9zMUW596Lbomsg8qVve6euqsrFicYkIIuUu4zYPndJwfe0YkS5nY72SHnNdbPhEnN8wcB2Kz+OIG0lih3yz5EqFhld03bGp222ZQCIghCTVL6QBNadGsiN/lWLl4JdR3lJkZzlpFdiHijoVRdWeSWqM4y0t23c92HXKrgppoSV18XMxrWVdoSM3nuMHwxGhFyde05OdDtLpCv+jlWf5REAHHA201pAU6bJSZINyHDUTB+Beo28rRXSwSh3OUIvYwKNVeoBY+KwOJ7WnuTCUq1meE6GkKc4D/cXmgpOyW/1SmBz3XjVIi/zprZ0zf3qH5mkphtg6ksjKgKjmx1cXfZAAX6wcDBNaCL+Ortep1Dh8xDUbqbBVNBL4jbiL3i3xsfNiyJgaZ5sX7i8tmStEpLbPwvHcByuf59qJhV/bZOl8KqJBETCDJcY6O2aqhTUy+9x93ThKs1GKrRPePrWPluud7ttlgtRveit/pcBrnQcXOl1rHq7ByB8CFAxNotRUYL9IF5n3wJOgkPojMy6jetQA5Ogc8Sm7RG6vg1yow==";
            new ShowMsgSinglePanel(code, Note.this);
        };
    }

    /**
     * 关于我们2.0.2
     *
     * @return ActionListener
     */
    private ActionListener showMsgAction2() {
        return e -> new ShowMsgSinglePanel(ToolUtil.aboutAsMsg2, Note.this);
    }

    /**
     * 关于我们2.0.1
     *
     * @return ActionListener
     */
    private ActionListener showMsgAction() {
        return e -> new ShowMsgSinglePanel(ToolUtil.aboutAsMsg, Note.this);
    }

    /**
     * 设置工具菜单和子菜单
     *
     * @param toolJMenu 工具菜单
     */
    private void setToolJMenuJMenu(JMenu toolJMenu) {
        JMenuItem jsonFormatJMenuItem = InitialComponent.setJMenuItem("JSON格式化", Color.black, true);
        JMenuItem jsonToEntityJMenuItem = InitialComponent.setJMenuItem("JSON实体化", Color.gray,
                false);
        JMenuItem xmlFormatJMenuItem = InitialComponent.setJMenuItem("XML格式化", Color.black, true);
        JMenuItem xmlToEntityJMenuItem = InitialComponent.setJMenuItem("XML实体化", Color.gray, false);
        JMenuItem unicodeTransferJMenuItem = InitialComponent.setJMenuItem("Unicode转换",
                Color.black, true);
        JMenuItem regTestJMenuItem = InitialComponent.setJMenuItem("正则测试", Color.black, true);
        JMenuItem regSetJMenuItem = InitialComponent.setJMenuItem("简单正则生成", Color.black, true);
        JMenuItem binaryConversionJMenuItem = InitialComponent.setJMenuItem("进制转换", Color.gray,
                false);
        JMenuItem calculatorJMenuItem = InitialComponent.setJMenuItem("计算器", Color.gray, false);
        JMenuItem sqlJMenuItem = InitialComponent.setJMenuItem("Sql美化", Color.gray, false);
        JMenuItem deAndEncryptJMenuItem = InitialComponent.setJMenuItem("加密解密", Color.black, true);
        JMenuItem linuxCmdJMenuItem = InitialComponent.setJMenuItem("Linux命令大全", Color.black, true);
        JMenuItem searchInNetJMenuItem = InitialComponent.setJMenuItem("网络搜索", Color.blue, true);
        JMenuItem strEncodingJMenuItem = InitialComponent.setJMenuItem("字符串转码", Color.black, true);


        toolJMenu.add(jsonFormatJMenuItem);
        toolJMenu.add(jsonToEntityJMenuItem);
        toolJMenu.add(xmlFormatJMenuItem);
        toolJMenu.add(xmlToEntityJMenuItem);
        toolJMenu.add(unicodeTransferJMenuItem);
        toolJMenu.add(regTestJMenuItem);
        toolJMenu.add(regSetJMenuItem);
        toolJMenu.add(binaryConversionJMenuItem);
        toolJMenu.add(calculatorJMenuItem);
        toolJMenu.add(sqlJMenuItem);
        toolJMenu.add(deAndEncryptJMenuItem);
        toolJMenu.add(linuxCmdJMenuItem);
        toolJMenu.add(searchInNetJMenuItem);
        toolJMenu.add(strEncodingJMenuItem);

        jsonFormatJMenuItem.addActionListener(jsonFormatAction());
        //快捷键CTRL+J
        jsonFormatJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, CTRL_MASK));
        xmlFormatJMenuItem.addActionListener(xmlFormatAction());
        //快捷键CTRL+M
        xmlFormatJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, CTRL_MASK));
        unicodeTransferJMenuItem.addActionListener(unicodeTransferAction());
        //快捷键CTRL+U
        unicodeTransferJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, CTRL_MASK));
        regTestJMenuItem.addActionListener(regTestAction());
        //快捷键CTRL+H
        regTestJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, CTRL_MASK));
        regSetJMenuItem.addActionListener(regSetAction());
        //快捷键CTRL+K
        regSetJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, CTRL_MASK));
        searchInNetJMenuItem.addActionListener(searchInNetAction());
        //快捷键CTRL+I
        searchInNetJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, CTRL_MASK));
        deAndEncryptJMenuItem.addActionListener(deAndEncryptAction());
        //快捷键CTRL+Y
        deAndEncryptJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, CTRL_MASK));
        strEncodingJMenuItem.addActionListener(strEncodingAction());
        //快捷键CTRL+E
        strEncodingJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, CTRL_MASK));
        linuxCmdJMenuItem.addActionListener(linuxCmdJMenuItemAction());
        //快捷键CTRL+L
        linuxCmdJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, CTRL_MASK));

    }

    /**
     * Linux 命令大全
     *
     * @return
     */
    private ActionListener linuxCmdJMenuItemAction() {
        return e -> {
            logger.info("启动线程 Linux 命令大全显示界面");
            LinuxCmdFrame linuxCmdFrame = new LinuxCmdFrame();
            try {
                ThreadPool.startThread(linuxCmdFrame, null);
            } catch (InterruptedException ex) {
                logger.warn("开启线程失败" + ex);
                new JRootParentDiag(Note.this, "请重启本程序", Constants.NOTICE_IMAGE);
            }
        };
    }

    private ActionListener strEncodingAction() {
        return e -> {
            StrEncodingChangeFrame strEncodingChangeFrame = new StrEncodingChangeFrame(Note.this);
            strEncodingChangeFrame.setVisible(true);
        };
    }

    /**
     * 加密解密事件
     *
     * @return ActionListener
     */
    private ActionListener deAndEncryptAction() {
        return e -> {
            DecryptAndEncryptFrame decryptAndEncryptFrame = new DecryptAndEncryptFrame(Note.this);
            decryptAndEncryptFrame.setVisible(true);
        };
    }

    /**
     * 网络搜索
     *
     * @return ActionListener
     */
    private ActionListener searchInNetAction() {
        return e -> new InternetSearchFrame(Note.this);
    }

    /**
     * 正则测试
     *
     * @return ActionListener
     */
    private ActionListener regTestAction() {
        return e -> {
            RegTestFrame regTestFrame = new RegTestFrame(Note.this);
            regTestFrame.setVisible(true);
        };
    }

    /**
     * 正则简单生成
     *
     * @return ActionListener
     */
    private ActionListener regSetAction() {
        return e -> {
            RegGenerateFrame regGenerateFrame = new RegGenerateFrame(Note.this);
            regGenerateFrame.setVisible(true);
        };
    }

    /**
     * unicode
     *
     * @return ActionListener
     */
    private ActionListener unicodeTransferAction() {
        return e -> showUnicodeFrame();
    }

    private void showUnicodeFrame() {
        UnicodeTransferFrame unicodeTransferFrame = new UnicodeTransferFrame(Note.this);
        unicodeTransferFrame.setVisible(true);
    }

    /**
     * XML格式化面板
     *
     * @return ActionListener
     */
    private ActionListener xmlFormatAction() {
        return e -> {
            XmlFormatFrame xmlFormatFrame = new XmlFormatFrame(Note.this);
            xmlFormatFrame.setVisible(true);
        };
    }

    /**
     * JSON格式化面板
     *
     * @return ActionListener
     */
    private ActionListener jsonFormatAction() {
        return e -> {
            JsonFormatFrame jsonFormatFrame = new JsonFormatFrame(Note.this);
            jsonFormatFrame.setVisible(true);
        };
    }

    /**
     * 拖拽目标监听器
     *
     * @return DropTargetListener
     */
    private DropTargetListener inputJTextAreaDropAction() {
        return new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {

            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {

            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {

            }

            @Override
            public void dragExit(DropTargetEvent dte) {

            }

            @Override
            public void drop(DropTargetDropEvent dropTargetDropEvent) {
                try {
                    if (dropTargetDropEvent.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        List<?> list =
                                (List<?>) (dropTargetDropEvent.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                        String beforeFileName = fileName;
                        for (Object object : list) {
                            File file = (File) object;
                            //html,text,txt,bat,exc,log,xml
                            if (StringUtils.isNotEmpty(file.getAbsolutePath()) && checkIsSave(beforeFileName, inputTextArea.getText().trim())) {
                                NormalFileFilter normalFileFilter = new NormalFileFilter();
                                if (normalFileFilter.accept(file)) {
                                    fileName = file.getAbsolutePath();
                                    inputTextArea.setText(EMPTY_STR);
                                    String content = ReadFile.readNormalFile(fileName);
                                    title(file.getName());
                                    inputTextArea.setText(content == null ? "" : content);
                                } else {
                                    new JRootParentDiag(Note.this, "不支持的文件类型：" + file.getName(),
                                            Constants.NOTICE_IMAGE);
                                }
                            }
                        }
                        dropTargetDropEvent.dropComplete(true);
                    } else {
                        dropTargetDropEvent.rejectDrop();
                    }
                } catch (IOException | UnsupportedFlavorException ioe) {
                    logger.warn("拖入文件操作失败" + ioe);
                }
            }
        };
    }

    /**
     * 设置视图菜单以及子菜单
     *
     * @param viewJMenu 视图菜单
     */
    private void setViewJMenu(JMenu viewJMenu) {
        JMenuItem lineSprite = InitialComponent.setJMenuItem("自动换行", Color.black, true);
        JMenu windowsStyleJMenu = InitialComponent.setJMenu("窗体风格", null);
        //设置窗体风格
        setWindowsStyleJMenu(windowsStyleJMenu);
        viewJMenu.add(lineSprite);
        viewJMenu.add(windowsStyleJMenu);
        lineSprite.addActionListener(lineSpriteAction());
        //快捷键CTRL+G
        lineSprite.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, CTRL_MASK));
    }

    /**
     * 设置窗体风格菜单以及子菜单
     *
     * @param windowsStyleJMenu 设置窗体风格
     */
    private void setWindowsStyleJMenu(JMenu windowsStyleJMenu) {
        JMenuItem style1jMenuItem = InitialComponent.setJMenuItem("Windows风格窗体(需要重启)",
                Color.black, true);
        JMenuItem substanceStyleJMenuItem = InitialComponent.setJMenuItem("Substance风格窗体",
                Color.black, true);
        //WebLookAndFeel
        JMenuItem webStyleJMenuItem = InitialComponent.setJMenuItem("Web风格窗体",
                Color.black, true);

        windowsStyleJMenu.add(style1jMenuItem);
        windowsStyleJMenu.add(substanceStyleJMenuItem);
        windowsStyleJMenu.add(webStyleJMenuItem);

        style1jMenuItem.addActionListener(style1jMenuItemActionListener());
        substanceStyleJMenuItem.addActionListener(substanceStyleJMenuItemActionListener());
        webStyleJMenuItem.addActionListener(webStyleJMenuItemActionListener());

    }

    /**
     * Web风格
     *
     * @return ActionListener
     */
    private ActionListener webStyleJMenuItemActionListener() {
        LookAndFeel substanceCremeCoffeeLookAndFeel =
                new com.alee.laf.WebLookAndFeel();
        return e -> setWindowsLookAndFeelStyle(substanceCremeCoffeeLookAndFeel);
    }

    /**
     * SubstanceCremeCoffee风格
     *
     * @return ActionListener
     */
    private ActionListener substanceStyleJMenuItemActionListener() {
        LookAndFeel substanceCremeCoffeeLookAndFeel =
                new org.jvnet.substance.skin.SubstanceCremeCoffeeLookAndFeel();
        return e -> setWindowsLookAndFeelStyle(substanceCremeCoffeeLookAndFeel);
    }

    /**
     * 设置窗体风格
     *
     * @param lookAndFeel 风格
     */
    private void setWindowsLookAndFeelStyle(LookAndFeel lookAndFeel) {
        try {
            UIManager.setLookAndFeel(lookAndFeel);
            logger.info("加载" + lookAndFeel.getClass().getName() + "风格");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            new JRootParentDiag(Note.this, "系统不支持  " + lookAndFeel.getName(),
                    Constants.NOTICE_IMAGE);
            logger.warn("加载" + lookAndFeel.getName() + "样式失败" + ex);
        }
    }

    /**
     * 设置Windows样式
     *
     * @return ActionListener
     */
    private ActionListener style1jMenuItemActionListener() {
        return e -> setWindowsStyle();
    }

    /**
     * 自动换行
     *
     * @return
     */
    private ActionListener lineSpriteAction() {
        return e -> {
            if (lineSprite % 2 == 0) {
                lineSprite = 0;
            }
            spriteLine(lineSprite);
            lineSprite++;
        };
    }

    /**
     * 设置引用菜单以及子菜单
     *
     * @param quoteJMenu 引用菜单
     */
    private void setQuoteJMenuJMenu(JMenu quoteJMenu) {

        JMenuItem findExceptionAnswer = InitialComponent.setJMenuItem("查找异常解决", Color.RED, true);
        JMenuItem addExceptionAnswer = InitialComponent.setJMenuItem("增加异常信息", Color.BLUE, true);
        JMenuItem manageExceptionAnswer = InitialComponent.setJMenuItem("管理异常信息", Color.GRAY, true);
        JMenuItem importExceptionAnswer = InitialComponent.setJMenuItem("导入异常信息", Color.GRAY, true);
        JMenuItem findWayAnswer = InitialComponent.setJMenuItem("查找编程方法", Color.BLUE, true);
        JMenuItem addWayAnswer = InitialComponent.setJMenuItem("增加编程方法", Color.BLUE, true);
        JMenuItem manageWayAnswer = InitialComponent.setJMenuItem("管理编程方法", Color.GRAY, true);
        JMenuItem importWayAnswer = InitialComponent.setJMenuItem("导入编程方法", Color.GRAY, true);
        JMenuItem noticeJMenuItem = InitialComponent.setJMenuItem("点击四下", Color.GREEN, true);

        quoteJMenu.add(findExceptionAnswer).setEnabled(true);
        quoteJMenu.add(addExceptionAnswer).setEnabled(true);
        quoteJMenu.add(manageExceptionAnswer).setEnabled(false);
        quoteJMenu.add(importExceptionAnswer).setEnabled(false);
        quoteJMenu.add(findWayAnswer).setEnabled(true);
        quoteJMenu.add(addWayAnswer).setEnabled(true);
        quoteJMenu.add(manageWayAnswer).setEnabled(false);
        quoteJMenu.add(importWayAnswer).setEnabled(false);
        quoteJMenu.add(noticeJMenuItem).setEnabled(false);

        //事件
        quoteJMenu.addMouseListener(quoteClickAction(manageWayAnswer, importWayAnswer,
                manageExceptionAnswer, importExceptionAnswer));
        findWayAnswer.addActionListener(findAction());
        //快捷键CTRL+5
        findWayAnswer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, CTRL_MASK));
        addWayAnswer.addActionListener(addAnswerAction());
        //快捷键CTRL+6
        addWayAnswer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, CTRL_MASK));
        manageWayAnswer.addMouseListener(manageWayAnswerClick(manageWayAnswer));
        manageWayAnswer.addActionListener(manageWayAnswerAction());
        importWayAnswer.addActionListener(importWayAnswerAction());
        importWayAnswer.addMouseListener(manageWayAnswerClick(importWayAnswer));

        addExceptionAnswer.addActionListener(addExceptionMsgAction());
        //快捷键CTRL+2
        addExceptionAnswer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, CTRL_MASK));
        //快捷键CTRL+1
        findExceptionAnswer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, CTRL_MASK));
        findExceptionAnswer.addActionListener(findExceptionMsgAction());
        manageExceptionAnswer.addMouseListener(manageWayAnswerClick(manageExceptionAnswer));
        manageExceptionAnswer.addActionListener(manageExceptionAction());
        importExceptionAnswer.addMouseListener(manageWayAnswerClick(importExceptionAnswer));
        importExceptionAnswer.addActionListener(importExceptionAction());
    }

    /**
     * 管理异常信息
     *
     * @return
     */
    private ActionListener manageExceptionAction() {
        return e -> {
            String[] allExceptionArr = exceptionService.getAllExceptions();
            ManageExceptionFrame manageExceptionFrame = new ManageExceptionFrame(Note.this,
                    allExceptionArr);
            manageExceptionFrame.setVisible(true);
        };
    }

    /**
     * 查找异常解决方法
     *
     * @return 监听事件
     */
    private ActionListener findExceptionMsgAction() {
        return e -> {
            FindExceptionMsgFrame findExceptionMsgFrame = new FindExceptionMsgFrame(Note.this);
            findExceptionMsgFrame.setVisible(true);
            String returnMsg = findExceptionMsgFrame.getExceptionAnswer();
            if (StringUtils.isNotEmpty(returnMsg)) {
                if (checkIsSave(fileName, inputTextArea.getText().trim())) {
                    inputTextArea.setText(EMPTY_STR);
                    inputTextArea.setText(returnMsg);
                    inputTextArea.setBackground(Color.WHITE);
                    inputTextArea.setForeground(Color.BLUE);
                    inputTextArea.setFont(new Font("宋体", Font.BOLD, 15));
                }
            }
        };
    }

    /**
     * 增加异常信息
     *
     * @return
     */
    private ActionListener addExceptionMsgAction() {
        return e -> {
            AddExceptionFrame addExceptionFrame = new AddExceptionFrame(Note.this);
            addExceptionFrame.setVisible(true);
        };
    }

    /**
     * 导入方法
     *
     * @return
     */
    private ActionListener importWayAnswerAction() {
        return e -> {
            TxtFileFilter txtFileFilter = new TxtFileFilter();
            String filePath = ChooseWayFrame.chooseFile(Note.this, txtFileFilter,
                    JFileChooser.FILES_ONLY, true, Note.this);
            if (StringUtils.isNotEmpty(filePath)) {
                logger.info("准备导入方法数据... ");
                String returnMsg = methodService.importMethod(filePath);
                new ShowMsgSinglePanel(returnMsg, Note.this);
            }
        };
    }

    /**
     * 导入异常信息
     *
     * @return ActionListener
     */
    private ActionListener importExceptionAction() {
        return e -> {
            TxtFileFilter txtFileFilter = new TxtFileFilter();
            String filePath = ChooseWayFrame.chooseFile(Note.this, txtFileFilter,
                    JFileChooser.FILES_ONLY, true, Note.this);
            if (StringUtils.isNotEmpty(filePath)) {
                logger.info("准备导入数据... ");
                String returnMsg = exceptionService.importMethod(filePath);
                new JRootParentDiag(Note.this, returnMsg, Constants.NOTICE_IMAGE);
            }
        };
    }

    /**
     * 点击引用事件
     *
     * @param manageWayAnswer
     * @param insertWayAnswer
     * @param manageExceptionAnswer
     * @param insertExceptionAnswer
     * @return
     */
    private MouseAdapter quoteClickAction(JMenuItem manageWayAnswer, JMenuItem insertWayAnswer,
                                          JMenuItem manageExceptionAnswer,
                                          JMenuItem insertExceptionAnswer) {
        return new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    manageWayAnswer.setForeground(Color.GRAY);
                    manageWayAnswer.setEnabled(false);
                    insertWayAnswer.setForeground(Color.GRAY);
                    insertWayAnswer.setEnabled(false);
                    manageExceptionAnswer.setForeground(Color.GRAY);
                    manageExceptionAnswer.setEnabled(false);
                    insertExceptionAnswer.setForeground(Color.GRAY);
                    insertExceptionAnswer.setEnabled(false);
                }
            }
        };
    }

    /**
     * 管理编程方法
     *
     * @return ActionListener
     */
    private ActionListener manageWayAnswerAction() {
        return e -> {
            ManageMethodFrame manageMethodFrame = new ManageMethodFrame(this);
            manageMethodFrame.setVisible(true);
        };
    }

    /**
     * 接收点击事件
     *
     * @param jMenuItem 菜单
     * @return MouseAdapter
     */
    private MouseAdapter manageWayAnswerClick(JMenuItem jMenuItem) {
        return new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 4) {
                    jMenuItem.setEnabled(true);
                    jMenuItem.setForeground(Color.BLUE);
                }
            }
        };
    }

    /**
     * 搜索方法事件
     *
     * @return ActionListener
     */
    private ActionListener findAction() {
        return e -> {
            if (checkIsSave(fileName, inputTextArea.getText().trim())) {
                logger.info("进入方法查找");
                inputTextArea.setText(EMPTY_STR);
                SearchMethodFrame searchMethodFrame = new SearchMethodFrame(this);
                searchMethodFrame.setVisible(true);
                String searchMsg = searchMethodFrame.getSearchMsg();
                if (StringUtils.isNotEmpty(searchMsg)) {
                    inputTextArea.setText(searchMsg);
                    inputTextArea.setForeground(Color.BLUE);
                    inputTextArea.setBackground(Color.WHITE);
                }
            }
        };
    }

    /**
     * 添加方法
     *
     * @return
     */
    private ActionListener addAnswerAction() {
        return e -> {
            String inputTextAreaStr = inputTextArea.getText().trim();
            if (StringUtils.isNotEmpty(inputTextAreaStr)) {
                if (!checkIsSave(fileName, inputTextAreaStr)) {
                    return;
                }
            }
            inputTextArea.setText(EMPTY_STR);
            logger.info("开始启动添加方法页面 --> Note.class");
            AddMethodFrame addMethodFrame = new AddMethodFrame(this);
            addMethodFrame.setVisible(true);
        };
    }

    /**
     * 显示进程事件
     *
     * @return ActionListener
     */
    private ActionListener showProgressListener() {
        return e -> {
            ShowProcess showProcess = new ShowProcess(Note.this);
            try {
                ThreadPool.startThread(showProcess, null);
            } catch (InterruptedException ex) {
                logger.warn("开启线程失败" + ex);
            }
        };
    }

    /**
     * 设置审阅菜单以及子菜单
     *
     * @param checkJMenu 审阅菜单
     */
    private void setCheckJMenuJMenu(JMenu checkJMenu) {
        JMenuItem countJMenu = InitialComponent.setJMenuItem("字数统计", Color.black, true);
        JMenuItem showProgressJMenuItem = InitialComponent.setJMenuItem("查看进程", Color.blue, true);
        JMenuItem showSystemLogJMenuItem = InitialComponent.setJMenuItem("查看系统日志", Color.blue,
                true);

        checkJMenu.add(countJMenu);
        checkJMenu.add(showProgressJMenuItem).setEnabled(true);
        checkJMenu.add(showSystemLogJMenuItem).setEnabled(true);
        //菜单点击事件
        countJMenu.addActionListener(getCountFontActionListener());
        //快捷键CTRL+T
        countJMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, CTRL_MASK));
        showProgressJMenuItem.addActionListener(showProgressListener());
        //快捷键CTRL+SHIFT+C
        showProgressJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
                CTRL_MASK | SHIFT_MASK, true));
        showSystemLogJMenuItem.addActionListener(showSystemLogListener());
        //快捷键CTRL+SHIFT+L
        showSystemLogJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
                CTRL_MASK | SHIFT_MASK, true));
    }

    /**
     * 查看系统日志
     *
     * @return java.awt.event.ActionListener
     * @author 黄先生
     * @date 2019/7/10 16:10
     */
    private ActionListener showSystemLogListener() {
        return e -> {
            SystemLogFrame systemLogFrame = new SystemLogFrame(Note.this);
            systemLogFrame.setVisible(true);
        };
    }

    /**
     * 字数统计
     *
     * @return ActionListener
     */
    private ActionListener getCountFontActionListener() {
        return e -> countStrLength();
    }

    /**
     * 统计字符串个数
     */
    private void countStrLength() {
        //中文字符
        int chCharacter = 0;
        //英文字符
        int enCharacter = 0;
        //数字
        int numberCharacter = 0;
        //空格
        int spaceCharacter = 0;
        //其他字符
        int otherCharacter = 0;
        //总字数
        int totalCharacter = 0;
        String selectStr = inputTextArea.getSelectedText();
        String fileContent = inputTextArea.getText();
        StringSelection select = new StringSelection(selectStr);
        clipboard.setContents(select, null);
        int selectLength = inputTextArea.getSelectionEnd() - inputTextArea.getSelectionStart(),
                lineNum = 0;
        if (selectLength <= 0) {
            selectStr = fileContent;
        }

        if (StringUtils.isNotEmpty(selectStr)) {
            lineNum = 1;
            for (int i = 0; i < selectStr.length(); i++) {
                char tmp = selectStr.charAt(i);
                if ((tmp >= 'A' && tmp <= 'Z') || (tmp >= 'a' && tmp <= 'z')) {
                    enCharacter++;
                } else if (tmp == ' ') {
                    spaceCharacter++;
                } else if ((tmp >= '0') && (tmp <= '9')) {
                    numberCharacter++;
                } else if (FormatData.isChinese(tmp)) {
                    chCharacter++;
                } else if (tmp == '\n') {
                    lineNum++;
                } else {
                    otherCharacter++;
                }
            }
        }
        totalCharacter =
                otherCharacter + spaceCharacter + numberCharacter + enCharacter + chCharacter;
        String msg = "选中的字数为：" + totalCharacter
                + Constants.LINE_SEPARATOR + "不计空格数为：" + (totalCharacter - spaceCharacter)
                + Constants.LINE_SEPARATOR + "中文的个数为：" + (chCharacter)
                + Constants.LINE_SEPARATOR + "英文字母数为：" + (enCharacter)
                + Constants.LINE_SEPARATOR + "空格的个数为：" + (spaceCharacter)
                + Constants.LINE_SEPARATOR + "数字的个数为：" + (numberCharacter)
                + Constants.LINE_SEPARATOR + "其他字符数为：" + (otherCharacter)
                + Constants.LINE_SEPARATOR + "总行数为   ：" + (lineNum);
        //显示消息
        new ShowMsgSinglePanel(msg, Note.this);
    }

    /**
     * 审阅菜单设置
     *
     * @param pageLayoutJMenu 审阅菜单
     */
    private void setPageLayoutJMenu(JMenu pageLayoutJMenu) {

        JMenu backColorJMenu = InitialComponent.setJMenu("背景颜色", null);
        JMenu cursorJMenu = InitialComponent.setJMenu("光标选择", null);
        JMenu opacyJMenu = InitialComponent.setJMenu("透明度", null);

        //预定义光标数组
        String[] cursorArr = {"箭头", "手势", "四方", "上下", "左右", "加载", "文本", "十字", "自定义"};
        String[] backColorArr = {"红色", "橘色", "黄色", "绿色", "白色", "蓝色", "紫色", "灰色", "黑色", "更多"};
        String[] opacyArr = {"100", "80", "60", "50", "40", "20", "10", "5", "3", "0"};

        JList backColorList = new JList(backColorArr);
        JList cursorJLIst = new JList(cursorArr);
        JList opacyJLIst = new JList(opacyArr);

        backColorList.setBackground(backColorJMenu.getBackground());
        cursorJLIst.setBackground(cursorJMenu.getBackground());
        opacyJLIst.setBackground(opacyJMenu.getBackground());

        backColorJMenu.add(backColorList);
        cursorJMenu.add(cursorJLIst);
        opacyJMenu.add(opacyJLIst);

        pageLayoutJMenu.add(backColorJMenu);
        pageLayoutJMenu.add(cursorJMenu);
        pageLayoutJMenu.add(opacyJMenu);

        //背景颜色
        backColorList.addMouseListener(backColorMouseListener(backColorList));
        //光标
        cursorJLIst.addMouseListener(curseMouseListener(cursorJLIst));
        //透明度
        opacyJLIst.addMouseListener(opacyJLIstMouseListener(opacyJLIst));
    }

    /**
     * 透明度
     *
     * @param opacyJList 透明度列表
     * @return MouseAdapter
     */
    private MouseListener opacyJLIstMouseListener(JList opacyJList) {
        return new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e MouseEvent
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 1) {
                    oneClick(e);
                }
            }

            private void oneClick(MouseEvent e) {
                if (InitialComponent.isWindowsLookAndFeel()) {
                    new JRootParentDiag(Note.this, "Windows窗体不支持设置透明度", Constants.NOTICE_IMAGE);
                    return;
                }
                //鼠标事件列表
                JList mouseJList = (JList) e.getSource();
                float course = Float.parseFloat((String) mouseJList.getSelectedValue()) / 100;
                Note.this.setOpacity(course);
                setVisible(true);
            }
        };
    }

    /**
     * 光标
     *
     * @param cursorJLIst 光标列表
     * @return MouseAdapter
     */
    private MouseAdapter curseMouseListener(JList cursorJLIst) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (cursorJLIst.getSelectedIndex() != -1) {
                    if (mouseEvent.getClickCount() == 1) {
                        oneClick(mouseEvent);
                    }
                }
            }

            /**
             * 单次点击
             * @param mouseEvent 鼠标事件
             */
            void oneClick(MouseEvent mouseEvent) {
                //鼠标事件列表
                JList mouseJList = (JList) mouseEvent.getSource();
                //鼠标点击
                int index = mouseJList.locationToIndex(mouseEvent.getPoint());
                if (index >= 0) {
                    String course = (String) mouseJList.getModel().getElementAt(index);
                    //预定义光标数组
                    int[] cursorArr = {Cursor.DEFAULT_CURSOR, Cursor.HAND_CURSOR,
                            Cursor.MOVE_CURSOR, Cursor.N_RESIZE_CURSOR, Cursor.W_RESIZE_CURSOR
                            , Cursor.WAIT_CURSOR, Cursor.TEXT_CURSOR, Cursor.CROSSHAIR_CURSOR};
                    if (StringUtils.isNotEmpty(course)) {
                        switch (course) {
                            //设置文本框的光标
                            case "箭头":
                                inputTextArea.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                                break;
                            case "手势":
                                inputTextArea.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                                break;
                            case "四方":
                                inputTextArea.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                                break;
                            case "上下":
                                inputTextArea.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                                break;
                            case "左右":
                                inputTextArea.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                                break;
                            case "加载":
                                inputTextArea.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                break;
                            case "文本":
                                inputTextArea.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                                break;
                            case "十字":
                                inputTextArea.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                                break;
                            default:
                                //得到默认的ToolKit对象
                                Toolkit toolkit = Toolkit.getDefaultToolkit();
                                //得到图像
                                Image image = IconRead.getImage("/cursor.gif");
                                if (null != image) {
                                    //实例化自定义光标对象
                                    Cursor customCursor = toolkit.createCustomCursor(image,
                                            new Point(6, 6), "MyCursor");
                                    //设置文本框光标
                                    inputTextArea.setCursor(customCursor);
                                } else {
                                    logger.warn("自定义光标失败，没有找到对于的图片！！");
                                }
                                break;
                        }
                        setVisible(true);
                    }
                }
            }
        };
    }

    /**
     * 背景颜色
     *
     * @param backColorList b背景颜色列表
     * @return MouseAdapter
     */
    private MouseAdapter backColorMouseListener(JList backColorList) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (backColorList.getSelectedIndex() != -1) {
                    if (mouseEvent.getClickCount() == 1) {
                        oneClick(mouseEvent);
                    }
                }
            }

            /**
             * 单击事件
             * @param mouseEvent 鼠标事件
             */
            public void oneClick(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                int index = theList.locationToIndex(mouseEvent.getPoint());
                if (index >= 0) {
                    String color = (String) theList.getModel().getElementAt(index);
                    if (StringUtils.isNotEmpty(color)) {
                        switch (color) {
                            case "红色":
                                inputTextArea.setBackground(Color.red);
                                break;
                            case "橘色":
                                inputTextArea.setBackground(Color.orange);
                                break;
                            case "黄色":
                                inputTextArea.setBackground(Color.yellow);
                                break;
                            case "绿色":
                                inputTextArea.setBackground(Color.green);
                                break;
                            case "紫色":
                                inputTextArea.setBackground(Color.magenta);
                                break;
                            case "蓝色":
                                inputTextArea.setBackground(Color.blue);
                                break;
                            case "灰色":
                                inputTextArea.setBackground(Color.gray);
                                break;
                            case "黑色":
                                inputTextArea.setBackground(Color.black);
                                break;
                            case "白色":
                                inputTextArea.setBackground(Color.white);
                                break;
                            default:
                                //改变面板的背景色
                                inputTextArea.setBackground(otherDetailColor());
                                break;
                        }
                        setVisible(true);
                    }
                }
            }
        };
    }

    /**
     * 设置插入菜单下的二级菜单累了咋办
     *
     * @param moreJMenu 累了咋办菜单
     */
    private void setMoreJMenu(JMenu moreJMenu) {
        JMenuItem music = InitialComponent.setJMenuItem("听听酷狗音乐", Color.black, true),
                game = InitialComponent.setJMenuItem("玩玩LOL游戏", Color.gray, true),
                movieJMenuItem = InitialComponent.setJMenuItem("看看电影", Color.black, true);
        moreJMenu.add(music);
        moreJMenu.add(game);
        moreJMenu.add(movieJMenuItem);

        music.addActionListener(getMusicActionListener());
        //快捷键CTRL+ALT+M
        music.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, CTRL_MASK | ALT_MASK));

        game.addActionListener(getGAmeActionListener());
        //快捷键CTRL+ALT+G
        game.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, CTRL_MASK | ALT_MASK));

        movieJMenuItem.addActionListener(getMovieActionListener());
        //快捷键CTRL+ALT+V
        movieJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, CTRL_MASK | ALT_MASK));
    }

    private ActionListener systemRegisterJMenuItemAction() {
        return e -> {
            try {
                SystemSoftware systemSoftware = new SystemSoftware();
                ThreadPool.startThread(systemSoftware, null);
            } catch (InterruptedException e1) {
                logger.warn("开启线程失败" + e1);
            }
        };
    }

    /**
     * 设置插入菜单
     *
     * @param insertJMenu 插入菜单
     */
    private void setInsertJMenuItem(JMenu insertJMenu) {
        JMenu moreJMenu = InitialComponent.setJMenu("累了咋办", null);
        //设置插入菜单下的二级菜单累了咋办
        setMoreJMenu(moreJMenu);

        JMenuItem pictureJMenu = InitialComponent.setJMenuItem("蓝屏释放测试", Color.gray, true);
        JMenuItem numberJMenuItem = InitialComponent.setJMenu("数字", null);
        JMenuItem dateJMenuItem = InitialComponent.setJMenuItem("日期", Color.black, true);
        JMenuItem systemRegisterJMenuItem = InitialComponent.setJMenuItem("查看安装软件", Color.black,
                true);

        JButton numberInsertBtn = new JButton("插入");

        JTextField numberField = InitialComponent.setJTExtField(5, EMPTY_STR, true, null);
        numberJMenuItem.add(numberField);
        numberJMenuItem.add(numberInsertBtn);

        insertJMenu.add(systemRegisterJMenuItem);
        insertJMenu.add(pictureJMenu);
        insertJMenu.add(moreJMenu);
        insertJMenu.add(numberJMenuItem);
        insertJMenu.add(dateJMenuItem);

        systemRegisterJMenuItem.addActionListener(systemRegisterJMenuItemAction());
        //快捷键CTRL+ALT+C
        systemRegisterJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                CTRL_MASK | ALT_MASK));
        //相关事件
        numberField.addKeyListener(new KeyHandler());
        //蓝屏病毒测试
        pictureJMenu.addActionListener(getPictureActionListener(pictureJMenu));
        //插入日期
        dateJMenuItem.addActionListener(getInsertDateActionListener());
        //快捷键CTRL+ALT+R
        dateJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
                InputEvent.CTRL_MASK | ALT_MASK));
        //插入数字
        numberInsertBtn.addActionListener(getInsertNumberBtnActionListener(numberField));
    }

    /**
     * 插入日期
     *
     * @return ActionListener
     */
    private ActionListener getInsertDateActionListener() {
        return e -> {
            String selectStr = inputTextArea.getSelectedText();
            StringSelection select = new StringSelection(selectStr);
            clipboard.setContents(select, null);
            inputTextArea.replaceRange(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()),
                    inputTextArea.getCaretPosition(), inputTextArea.getCaretPosition());
        };
    }

    /**
     * 插入数字
     *
     * @param numberField 数字输入框
     * @return ActionListener
     */
    private ActionListener getInsertNumberBtnActionListener(JTextField numberField) {
        return e -> {
            String s = inputTextArea.getSelectedText();
            StringSelection select = new StringSelection(s);
            clipboard.setContents(select, null);
            inputTextArea.replaceRange(numberField.getText(), inputTextArea.getCaretPosition(),
                    inputTextArea.getCaretPosition());
        };
    }

    /**
     * 打开电影
     *
     * @return
     */
    private ActionListener getMovieActionListener() {
        return e -> {
            logger.info("\n\n\t打开中.....,\n\t\t请不要移动鼠标和其他操作");
            SpecialSearch specialSearch = new SpecialSearch("movie", true);
            try {
                ThreadPool.startThread(specialSearch, null);
                if (specialSearch.isSuccess()) {
                    this.dispose();
                }
            } catch (InterruptedException ex) {
                logger.warn("开启线程失败" + ex);
            }
        };
    }

    /**
     * 游戏
     *
     * @return
     */
    private ActionListener getGAmeActionListener() {
        return e -> {
            inputTextArea.setText("\n\n\t打开中.....,\n\t\t请不要移动鼠标和其他操作");
            //  SpecialSearch returns = new SpecialSearch("game");
            //  if (returns.returns.length() != 0) {
            inputTextArea.setText(EMPTY_STR);
            this.dispose();
            //  }
        };
    }

    /**
     * 打开音乐
     *
     * @return ActionListener
     */
    private ActionListener getMusicActionListener() {
        return e -> {
            logger.info("\n\n\t打开中.....,\n\t\t请不要移动鼠标和其他操作");
            SpecialSearch specialSearch = new SpecialSearch("music", true);
            try {
                ThreadPool.startThread(specialSearch, null);
                if (specialSearch.isSuccess()) {
                    this.dispose();
                }
            } catch (InterruptedException ex) {
                logger.warn("开启线程失败" + ex);
            }
        };
    }

    /**
     * 蓝屏病毒测试
     *
     * @param pictureJMenu 蓝屏病毒测试菜单
     * @return ActionListener
     */
    private ActionListener getPictureActionListener(JMenuItem pictureJMenu) {
        return e -> {
            String password = JOptionPane.showInputDialog(Note.this, "请输入密码", "密码输入框",
                    JOptionPane.PLAIN_MESSAGE);
            if (StringUtils.isNotEmpty(password) && PASSWORD.equalsIgnoreCase(password)) {
                pictureJMenu.setForeground(Color.blue);
                inputTextArea.setText("\n\n\t释放中.....,\n\t\t请随便操作");
                new BlueScreen();
            } else if (null != password) {
                new JRootParentDiag(Note.this, "密码错误", Constants.NOTICE_IMAGE);
                pictureJMenu.setForeground(Color.gray);
            }
        };
    }

    /**
     * 输入框退出事件
     *
     * @return KeyListener
     */
    private KeyListener inputTextAreaExitWay() {
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                String content = inputTextArea.getText();
                if (EXIT_STR.equals(content) || EXIT_STR_CN.equals(content)) {
                    doExit();
                }
            }
        };
    }

    /**
     * 设置开始菜单下的二级菜单
     *
     * @param findAndReplaceJMenu 查找和替换菜单
     */
    private void setFindAndReplaceJMenu(JMenu findAndReplaceJMenu) {
        JMenuItem findJMenuItem = InitialComponent.setJMenuItem("查找", Color.black, true),
                locationJMenuItem = InitialComponent.setJMenuItem("定位", Color.black, true);
        JMenu replaceJMenu = InitialComponent.setJMenu("替换", KeyEvent.VK_R);
        JLabel replaceOrigLabel = new JLabel("原句:"), replaceDesLabel = new JLabel("替换为:");
        JButton replaceButton = new JButton("替换");
        //替换框
        replaceOrigField = InitialComponent.setJTExtField(20, null, true, null);
        replaceDesField = InitialComponent.setJTExtField(20, null, true, null);
        //替换菜单
        replaceJMenu.add(replaceOrigLabel);
        replaceJMenu.add(replaceOrigField);
        replaceJMenu.add(replaceDesLabel);
        replaceJMenu.add(replaceDesField);
        replaceJMenu.add(replaceButton);
        //查找和替换菜单
        findAndReplaceJMenu.add(findJMenuItem);
        findAndReplaceJMenu.add(replaceJMenu);
        findAndReplaceJMenu.add(locationJMenuItem);

        //相关事件
        //查找
        findJMenuItem.addActionListener(findStrActionListener());
        //快捷键CTRL+F
        findJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, CTRL_MASK));
        //替换
        replaceButton.addActionListener(replaceActionListener());
        //快捷键CTRL+SHIFT+R
        //Exception in thread "xxx-pool-0" java.lang.Error: setAccelerator() is not defined for
        // JMenu.  Use setMnemonic() instead.
        //定位
        locationJMenuItem.addActionListener(locationActionListener());
        //快捷键CTRL+D
        locationJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, CTRL_MASK));
    }

    /**
     * 设置开始菜单下的二级菜单文字工具
     *
     * @param wordToolJMenu 文字工具菜单
     */
    private void setWordToolJMenuJMenu(JMenu wordToolJMenu) {
        JMenuItem cutJMenuItem = InitialComponent.setJMenuItem("剪贴", Color.black, true);
        JMenuItem copyJMenuItem = InitialComponent.setJMenuItem("复制", Color.black, true);
        JMenuItem parseJMenuItem = InitialComponent.setJMenuItem("粘贴", Color.black, true);
        JMenuItem delSpaceJMenuItem = InitialComponent.setJMenuItem("删除空格", Color.black, true);

        wordToolJMenu.add(cutJMenuItem);
        wordToolJMenu.add(copyJMenuItem);
        wordToolJMenu.add(parseJMenuItem);
        wordToolJMenu.add(delSpaceJMenuItem);
        //相关事件
        //剪切
        cutJMenuItem.addActionListener(cutActionListener());
        //复制
        copyJMenuItem.addActionListener(copyActionListener());
        //粘贴
        parseJMenuItem.addActionListener(paseActionListener());
        //删除空格
        delSpaceJMenuItem.addActionListener(delSpaceActionListener());
    }

    /**
     * 设置开始菜单下的二级菜单文件工具
     *
     * @param fileToolJMenu 文件工具
     */
    private void setFileToolJMenuJMenu(JMenu fileToolJMenu) {
        JMenuItem delFileJMenu = InitialComponent.setJMenuItem("删除文件", Color.black, true);
        JMenuItem renameFileJMenuItem = InitialComponent.setJMenuItem("重命名", Color.black, true),
                zipFileJMenuItem = InitialComponent.setJMenuItem("加压", Color.black, true),
                timeLimitJMenu = InitialComponent.setJMenuItem("定时器", Color.black, true);
        searchJMenuItem = InitialComponent.setJMenuItem("搜索", Color.blue, true);

        fileToolJMenu.add(searchJMenuItem);
        fileToolJMenu.add(renameFileJMenuItem);
        fileToolJMenu.add(delFileJMenu);
        fileToolJMenu.add(zipFileJMenuItem);
        fileToolJMenu.add(timeLimitJMenu);

        //定时器
        timeLimitJMenu.addActionListener(getTimeLimitActionListener());
        //快捷键CTRL+SHIFT+D
        timeLimitJMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                CTRL_MASK | SHIFT_MASK));
        //压缩按钮事件处理
        zipFileJMenuItem.addActionListener(getZipFileActionListener());
        //快捷键CTRL+SHIFT+Z
        zipFileJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                CTRL_MASK | SHIFT_MASK));
        //删除文件
        delFileJMenu.addActionListener(delFileActionListener());
        //快捷键CTRL+SHIFT+Y
        delFileJMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, CTRL_MASK | SHIFT_MASK));
        //重命名文件
        renameFileJMenuItem.addActionListener(getRenameFileActionListener());
        //快捷键CTRL+SHIFT+R
        renameFileJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
                CTRL_MASK | SHIFT_MASK));
        //文件搜索
        searchJMenuItem.addActionListener(getFileSearchActionListener());
        //快捷键CTRL+SHIFT+F
        searchJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
                CTRL_MASK | SHIFT_MASK));
    }

    /**
     * 设置开始菜单下的二级菜单打开保存
     *
     * @param openAndSaveJMenu 打开保存
     */
    private void setOpenAndSaveJMenuJMenu(JMenu openAndSaveJMenu) {
        JMenuItem openFileJMenuItem = InitialComponent.setJMenuItem("打开", Color.black, true);
        JMenuItem saveJMenuItem = InitialComponent.setJMenuItem("保存", Color.black, true);

        //菜单添加项
        openAndSaveJMenu.add(openFileJMenuItem);
        openAndSaveJMenu.add(saveJMenuItem);

        //打开文件
        openFileJMenuItem.addActionListener(getOpenFileActionListener());
        //快捷键CTRL+O
        openFileJMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O,
                CTRL_MASK));
        //保存文件
        saveJMenuItem.addActionListener(getSaveFileActionListener());
        //快捷键CTRL+S
        saveJMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
                CTRL_MASK));
        //助记符
        saveJMenuItem.setMnemonic('S');
    }

    /**
     * 设置开始菜单的子菜单
     *
     * @param startJMenu 开始菜单
     */
    private void setStartJMenuItem(JMenu startJMenu) {
        JMenu findAndReplaceJMenu = InitialComponent.setJMenu("查找替换", null);
        //设置开始菜单下的二级菜单查找替换
        setFindAndReplaceJMenu(findAndReplaceJMenu);
        JMenu wordToolJMenu = InitialComponent.setJMenu("文字工具", null);
        //设置开始菜单下的二级菜单文字工具
        setWordToolJMenuJMenu(wordToolJMenu);
        JMenu fileToolJMenu = InitialComponent.setJMenu("文件工具", null);
        //设置开始菜单下的二级菜单文件工具
        setFileToolJMenuJMenu(fileToolJMenu);
        JMenu openAndSaveJMenu = InitialComponent.setJMenu("打开保存", null);
        //设置开始菜单下的二级菜单打开保存
        setOpenAndSaveJMenuJMenu(openAndSaveJMenu);
        JMenuItem addNewJMenu = InitialComponent.setJMenuItem("新建", Color.black, true);
        speedup = InitialComponent.setJMenuItem("运行加速", Color.blue, true);
        JMenuItem exitJMenuItem = InitialComponent.setJMenuItem("退出", Color.black, true);

        //开始菜单下的字体大小
        JMenu fontSizeJMenu = InitialComponent.setJMenu("字体大小", null),
                fontColorJMenu = InitialComponent.setJMenu("字体颜色", null);
        //inputTextArea的背景颜色显示框
        JTextField inputTextAreaBackColorField = InitialComponent.setJTExtField(15, "     黑色",
                false, null);
        //字体大小显示框
        fontSizeField = InitialComponent.setJTExtField(5, "15", true, null);
        //输入字监听事件
        fontSizeField.addKeyListener(new KeyHandler());
        //查询信息
        searchInTextAreaField = InitialComponent.setJTExtField(20, null, true, null);

        fontSizeJMenu.add(fontSizeField);
        fontColorJMenu.add(inputTextAreaBackColorField);
        String[] sizeArr = {"100", "50", "45", "40", "35", "30", "26", "24", "20", "18", "16",
                "12", "10", "8", "6", "4", "1"};
        String[] colorArr = {"红色", "橘色", "黄色", "绿色", "紫色", "蓝色", "粉色", "灰色", "黑色", "更多"};

        JList fontSizeJList = new JList(sizeArr);
        JList fontColorJList = new JList(colorArr);

        fontSizeJList.setBackground(fontSizeJMenu.getBackground());
        fontColorJList.setBackground(fontSizeJMenu.getBackground());

        fontSizeJMenu.add(fontSizeJList);
        fontColorJMenu.add(fontColorJList);

        startJMenu.add(addNewJMenu);
        startJMenu.add(fontSizeJMenu);
        startJMenu.add(fontColorJMenu);
        startJMenu.add(speedup);
        startJMenu.add(wordToolJMenu);
        startJMenu.add(fileToolJMenu);
        startJMenu.add(findAndReplaceJMenu);
        startJMenu.add(openAndSaveJMenu);
        startJMenu.add(exitJMenuItem);

        //新建
        addNewJMenu.addActionListener(addNewActionListener());
        //快捷键CTRL+N
        addNewJMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, CTRL_MASK));
        //加速事件
        speedup.addActionListener(speedupActionListener());
        //输入限制为数字
        fontSizeField.addKeyListener(fontSizeKeyListener());
        //颜色点击事件
        fontColorJList.addMouseListener(fontColorMouseListener(fontColorJList,
                inputTextAreaBackColorField));
        //字体大小点击事件
        fontSizeJList.addMouseListener(fontSizeMouseListener(fontSizeJList));
        //监听执行事件
        exitJMenuItem.addActionListener(exitActionListener(fileName,
                inputTextArea.getText().trim()));
        //快捷键CTRL+caps lock
        exitJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_CAPS_LOCK, CTRL_MASK));
    }

    /**
     * 保存文件
     *
     * @return ActionListener
     */
    private ActionListener getSaveFileActionListener() {
        return e -> {
            String name = fileName;
            String fileContent = inputTextArea.getText().trim();
            saveFile(name, fileContent);
        };
    }

    /**
     * 定时器
     *
     * @return ActionListener
     */
    private ActionListener getTimeLimitActionListener() {
        return event -> {
            //new dingshiqi();
        };
    }

    /**
     * 压缩按钮事件处理
     *
     * @return ActionListener
     */
    private ActionListener getZipFileActionListener() {
        return event -> {
            //加压其他文件
            if (StringUtils.isEmpty(fileName) || fileName.length() < 3) {
                FileDialog openFileDialog = setOpenFD();
                String localFileName = openFileDialog.getDirectory() + openFileDialog.getFile();
                //弹出文件选择器,并判断是否点击了打开按钮
                if (StringUtils.isNotEmpty(openFileDialog.getFile())) {
                    //调用压缩文件方法
                    zipFile(localFileName, localFileName + ".zip");
                } else {
                    new JRootParentDiag(Note.this, "压缩失败", Constants.NOTICE_IMAGE);
                }
            } else {
                //调用压缩文件方法
                zipFile(this.fileName, this.fileName + ".zip");
            }
        };
    }

    /**
     * 打开文件
     *
     * @return FileDialog
     */
    private FileDialog setOpenFD() {
        FileDialog dialog = new FileDialog(new Frame(), "打开", FileDialog.LOAD);
        dialog.setVisible(true);
        return dialog;
    }

    /**
     * 删除文件
     *
     * @return 监听事件
     */
    private ActionListener delFileActionListener() {
        return e -> {
            String delFileName = fileName;
            if (StringUtils.isEmpty(delFileName) && delFileName.length() < 3) {
                FileDialog openFD = setOpenFD();
                //弹出文件选择器,并判断是否点击了打开按钮
                if (openFD.getFile() != null) {
                    delFileName = openFD.getDirectory() + openFD.getFile();
                }
            }
            //源文件
            File delFile = new File(delFileName);
            try {
                //删除文件
                if (delFile.delete()) {
                    logger.info("删除文件" + delFileName + "成功");
                    //显示信息
                    new JRootParentDiag(Note.this, delFileName + " 删除成功.", Constants.NOTICE_IMAGE);
                    fileName = EMPTY_STR;
                    inputTextArea.setText(EMPTY_STR);
                    title(EMPTY_STR);
                }
            } catch (Exception ex) {
                logger.warn("删除文件失败" + ex);
                new JRootParentDiag(Note.this, delFileName + " 删除失败.", Constants.NOTICE_IMAGE);
            }
        };
    }

    /**
     * 打开文件
     *
     * @return ActionListener
     */
    private ActionListener getOpenFileActionListener() {
        return e -> {
            String fileContent = inputTextArea.getText().trim();
            boolean saveFile = true;
            if (StringUtils.isNotEmpty(fileContent)) {
                saveFile = checkIsSave(fileName, fileContent);
            }
            if (saveFile) {
                inputTextArea.setText(EMPTY_STR);

                //JFileChooser控件
                JFileChooser jFileChooser = new JFileChooser();
                //设置默认访问路径
                jFileChooser.setCurrentDirectory(null);
                NormalFileFilter normalFileFilter = new NormalFileFilter();
                jFileChooser.addChoosableFileFilter(normalFileFilter);
                jFileChooser.setFileFilter(normalFileFilter);
                int index = jFileChooser.showOpenDialog(this.getContentPane());
                if (index == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jFileChooser.getSelectedFile();
                    String content = ReadFile.readNormalFile(selectedFile.getAbsolutePath());
                    if (StringUtils.isNotEmpty(content)) {
                        inputTextArea.setText(content);
                        this.fileName = selectedFile.getAbsolutePath();
                        title(selectedFile.getName());
                        logger.info("打开文件绝对路径 [" + selectedFile.getAbsolutePath() + "]  ");
                    } else {
                        new JRootParentDiag(Note.this, " 打开失败", Constants.NOTICE_IMAGE);
                    }
                }
            }
        };
    }

    /**
     * 重命名文件
     *
     * @return ActionListener
     */
    private ActionListener getRenameFileActionListener() {
        return e -> {
            String fileAddr = fileName;
            if (StringUtils.isEmpty(fileAddr) || fileAddr.length() < 3) {
                FileDialog openFD = setOpenFD();
                fileName = openFD.getDirectory() + openFD.getFile();
                //弹出文件选择器,并判断是否点击了打开按钮
                if (StringUtils.isNotEmpty(openFD.getFile())) {
                    fileAddr = fileName;
                } else {
                    new JRootParentDiag(Note.this, " 重命名失败.", Constants.NOTICE_IMAGE);
                    return;
                }
            }
            //输入新文件名对话框
            String newName = JOptionPane.showInputDialog("输入新文件名");
            if (StringUtils.isNotEmpty(newName)) {
                //源文件
                File origFile = new File(fileAddr);
                String origFileName = origFile.getName();
                String origDir = origFile.getAbsolutePath().substring(0,
                        origFile.getAbsolutePath().indexOf(origFileName));
                //新文件
                File newFileTest = new File(origDir + newName);
                if (StringUtils.isNotEmpty(newFileTest.getName())) {
                    if (!newFileTest.getName().contains(POINT)) {
                        newName = newName + ".txt";
                    }
                    //新文件
                    File newFile = new File(origDir + newName);
                    //重命名
                    if (origFile.renameTo(newFile)) {
                        //显示信息
                        new JRootParentDiag(Note.this, origFile.getName() + " 重命名成功.",
                                Constants.NOTICE_IMAGE);
                        fileName = newFile.getAbsolutePath();
                        //不为空说明是另存为的重命名
                        if (StringUtils.isNotEmpty(inputTextArea.getText().trim())) {
                            title(newName);
                        }
                    } else {
                        new JRootParentDiag(Note.this, " 重命名失败.", Constants.NOTICE_IMAGE);
                    }
                } else {
                    new JRootParentDiag(Note.this, " 重命名失败.", Constants.NOTICE_IMAGE);
                }

            } else {
                new JRootParentDiag(Note.this, " 重命名失败.", Constants.NOTICE_IMAGE);
            }
        };
    }

    /**
     * 文件搜索
     *
     * @return ActionListener
     */
    private ActionListener getFileSearchActionListener() {
        return event -> {
            //密码对话框
            String passWord = JOptionPane.showInputDialog(Note.this, "请输入密码", "密码输入框",
                    JOptionPane.PLAIN_MESSAGE);
            if (StringUtils.isNotEmpty(passWord) && PASSWORD.equalsIgnoreCase(passWord)) {
                setVisible(true);
                logger.info("\n\n\t搜索中.....,\n\t\t请不要移动鼠标和其他操作");
                NormalSearch normalSearch = new NormalSearch();
                try {
                    ThreadPool.startThread(normalSearch, null);
                    if (StringUtils.isNotEmpty(normalSearch.returns)) {
                    }
                } catch (InterruptedException e) {
                    logger.warn("开启线程失败" + e);
                }
                searchJMenuItem.setForeground(Color.blue);
            } else {
                logger.warn("密码错误");
                searchJMenuItem.setForeground(Color.gray);
                new JRootParentDiag(Note.this, " 密码错误.", Constants.NOTICE_IMAGE);
            }
        };
    }

    /**
     * 剪切
     *
     * @return ActionListener
     */
    private ActionListener cutActionListener() {
        return e -> {
            //类text中没有cut方法，不能使用text.cut
            String s = inputTextArea.getSelectedText();
            StringSelection select = new StringSelection(s);
            clipboard.setContents(select, null);
            inputTextArea.replaceRange(EMPTY_STR, inputTextArea.getSelectionStart(),
                    inputTextArea.getSelectionEnd());
        };
    }

    /**
     * 复制
     *
     * @return ActionListener
     */
    private ActionListener copyActionListener() {
        return e -> {
            //同上，没有copy这个方法
            String s = inputTextArea.getSelectedText();
            StringSelection select = new StringSelection(s);
            clipboard.setContents(select, null);
        };
    }

    /**
     * 粘贴
     *
     * @return ActionListener
     */
    private ActionListener paseActionListener() {
        return e -> {
            String copyStr = EMPTY_STR;
            Transferable t = clipboard.getContents(null);
            try {
                if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    // 因为原系的剪贴板里有多种信息, 如文字, 图片, 文件等
                    // 先判断开始取得的可传输的数据是不是文字, 如果是, 取得这些文字
                    copyStr = (String) t.getTransferData(DataFlavor.stringFlavor);
                    // 同样, 因为Transferable中的DataFlavor是多种类型的,
                    // 所以传入DataFlavor这个参数, 指定要取得哪种类型的Data.
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                logger.warn("复制异常" + ex);
            }
            inputTextArea.insert(copyStr, inputTextArea.getCaretPosition());
        };
    }

    /**
     * 删除空格
     *
     * @return ActionListener
     */
    private ActionListener delSpaceActionListener() {
        return e -> {
            //类text中没有cut方法，不能使用text.cut
            String origStr = inputTextArea.getSelectedText();
            StringSelection select = new StringSelection(origStr);
            String afterReplaceStr = origStr.replace(" ", EMPTY_STR);
            clipboard.setContents(select, null);
            inputTextArea.replaceRange(afterReplaceStr, inputTextArea.getSelectionStart(),
                    inputTextArea.getSelectionEnd());
        };
    }

    /**
     * 颜色点击事件
     *
     * @param fontColorJList              颜色列表
     * @param inputTextAreaBackColorField 显示框的字体颜色名字
     * @return MouseAdapter
     */
    private MouseAdapter fontColorMouseListener(JList fontColorJList,
                                                JTextField inputTextAreaBackColorField) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                if (fontColorJList.getSelectedIndex() != -1) {
                    if (mouseEvent.getClickCount() == 1) {
                        oneClick(theList.getSelectedValue());
                    }
                    if (mouseEvent.getClickCount() == 1) {
                        int index = theList.locationToIndex(mouseEvent.getPoint());
                        if (index >= 0) {
                            Object o = theList.getModel().getElementAt(index);
                            String colorStr = (String) o;
                            if (colorStr != null) {
                                if (colorStr.equalsIgnoreCase("红色")) {
                                    inputTextArea.setForeground(Color.red);
                                }
                                if (colorStr.equalsIgnoreCase("橘色")) {
                                    inputTextArea.setForeground(Color.orange);
                                }
                                if (colorStr.equalsIgnoreCase("黄色")) {
                                    inputTextArea.setForeground(Color.yellow);
                                }
                                if (colorStr.equalsIgnoreCase("绿色")) {
                                    inputTextArea.setForeground(Color.green);
                                }
                                if (colorStr.equalsIgnoreCase("紫色")) {
                                    inputTextArea.setForeground(Color.magenta);
                                }
                                if (colorStr.equalsIgnoreCase("蓝色")) {
                                    inputTextArea.setForeground(Color.blue);
                                }
                                if (colorStr.equalsIgnoreCase("灰色")) {
                                    inputTextArea.setForeground(Color.gray);
                                }
                                if (colorStr.equalsIgnoreCase("黑色")) {
                                    inputTextArea.setForeground(Color.black);
                                }
                                if (colorStr.equalsIgnoreCase("粉色")) {
                                    inputTextArea.setForeground(Color.pink);
                                }
                                if (colorStr.equalsIgnoreCase("更多")) {
                                    inputTextArea.setForeground(otherDetailColor());
                                }
                                inputTextAreaBackColorField.setText("     " + colorStr);
                            }

                        }
                    }
                    twoClick(theList.getSelectedValue());
                }
            }

            public void oneClick(Object value) {
            }

            public void twoClick(Object value) {

            }
        };
    }

    /**
     * 字体大小点击事件
     *
     * @return
     */
    private MouseAdapter fontSizeMouseListener(JList fontSizeJList) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                if (fontSizeJList.getSelectedIndex() != -1) {
                    if (mouseEvent.getClickCount() == 1) {
                        oneClick(theList.getSelectedValue());
                    }
                    if (mouseEvent.getClickCount() == 1) {
                        int index = theList.locationToIndex(mouseEvent.getPoint());
                        if (index >= 0) {
                            String fontSizeStr = (String) theList.getModel().getElementAt(index);
                            int fontSize = Integer.parseInt(fontSizeStr);
                            inputTextArea.setFont(new Font("宋体", Font.BOLD, fontSize));
                            setVisible(true);
                            fontSizeField.setText(fontSizeStr);
                        }
                    }
                    twoClick(theList.getSelectedValue());
                }
            }

            void oneClick(Object value) {
            }

            void twoClick(Object value) {

            }
        };
    }

    /**
     * 退出事件
     *
     * @param fileName    文件名
     * @param fileContent 文件内容
     * @return ActionListener
     */
    private ActionListener exitActionListener(String fileName, String fileContent) {
        return e -> exitProject(fileName, fileContent);
    }

    private void exitProject(String fileName, String fileContent) {
        if (checkIsSave(fileName, fileContent)) {
            doExit();
        }
    }

    /**
     * 退出
     */
    private void doExit() {
        saveSkin();
        System.exit(0);
    }

    /**
     * 定位
     *
     * @return
     */
    private ActionListener locationActionListener() {
        return e -> {
            //输入新文件名对话框
            String lor01 = JOptionPane.showInputDialog(Note.this, "定位内容", "定位",
                    JOptionPane.PLAIN_MESSAGE);
            String lor02 = inputTextArea.getText(), xch = EMPTY_STR;
            int x0, x05 = 0;
            if (lor02.length() != 0 && lor01.length() != 0) {
                int x01 = 0;
                x0 = lor02.indexOf(lor01) + 1;
                for (int i = 0; i < x0; i++) {
                    if (lor02.charAt(i) == '\n') {
                        xch = lor02.substring(x05, i);
                        ++x01;
                        x05++;
                    }
                }
                if (x0 != 0) {
                    int m = xch.length();
                    new JRootParentDiag(Note.this, (x05 + 1) + "行 " + (x0 - x01 - m) + "列",
                            Constants.NOTICE_IMAGE);
                } else {
                    new JRootParentDiag(Note.this, "文档没有", Constants.NOTICE_IMAGE);
                }
            } else {
                new JRootParentDiag(Note.this, "文档没有", Constants.NOTICE_IMAGE);
            }
        };
    }

    /**
     * 替换
     *
     * @return
     */
    private ActionListener replaceActionListener() {
        return e -> {
            String lor11 = replaceOrigField.getText(), lor12 = replaceDesField.getText(), lor13 =
                    inputTextArea.getText();
            int x0 = -1, x02 = 0, x03 = 0, x04 = 0, ss[] = new int[3000];

            if (lor13.length() != 0 && lor11.length() != 0) {
                for (int j = 0; j < lor13.length(); j++) {
                    int x01 = 0;
                    x0 = lor13.indexOf(lor11, x02) + 1;
                    if (x0 != 0) {
                        ss[x03++] = x0 - 2 * x01;
                        x02 = x0;
                    }
                }
                int kl = 0;
                for (int k = 0; k < ss.length; k++) {
                    if (ss[k] == 0) {
                        kl = k;
                        break;
                    }
                }
                for (int j = 0; j < kl; j++) {
                    int x01 = 0;
                    String lor131 = inputTextArea.getText();
                    x0 = lor131.indexOf(lor11) + 1;
                    //  for(int i=0;i<x0;i++)
                    //    if(lor131.charAt(i)=='\n')
                    //    x01++;
                    if (x0 != -1) {
                        x04++;
                        inputTextArea.replaceRange(lor12, x0 - x01 - 1,
                                x0 - x01 + lor11.length() - 1);
                    }
                }
                if (x04 != 0) {
                    new JRootParentDiag(Note.this, "替换成功", Constants.NOTICE_IMAGE);
                } else {
                    new JRootParentDiag(Note.this, "替换失败", Constants.NOTICE_IMAGE);
                }
            } else {
                new JRootParentDiag(Note.this, "替换失败", Constants.NOTICE_IMAGE);
            }
            replaceOrigField.setText(EMPTY_STR);
            replaceDesField.setText(EMPTY_STR);
        };
    }

    /**
     * 文本内查找
     *
     * @return 事件监听
     */
    private ActionListener findStrActionListener() {
        return e -> {
            int findIndex;
            String findDes = JOptionPane.showInputDialog(Note.this, "查找内容", "查找",
                    JOptionPane.PLAIN_MESSAGE);
            String findOrig = inputTextArea.getText();
            String selectedStr = inputTextArea.getSelectedText();
            int replaceStrLength = 0;
            if (StringUtils.isNotEmpty(findDes)) {
                if (StringUtils.isEmpty(findOrig)) {
                    new JRootParentDiag(Note.this, "文档没有", Constants.NOTICE_IMAGE);
                    return;
                }
                String findStr = findOrig;
                if (StringUtils.isNotEmpty(selectedStr)) {
                    findStr = findOrig.substring(inputTextArea.getSelectionEnd());
                    replaceStrLength = inputTextArea.getSelectionEnd();
                }
                findIndex = findStr.indexOf(findDes);
                //找完了从头开始
                if (findIndex == -1 && findStr.length() < findOrig.length()) {
                    findIndex = findOrig.indexOf(findDes);
                    replaceStrLength = 0;
                }
                findIndex = findIndex + replaceStrLength;
                if (findIndex != -1) {
                    inputTextArea.requestFocus();
                    inputTextArea.select(findIndex, findIndex + findDes.length());
                } else {
                    //显示消息
                    new JRootParentDiag(Note.this, "文档没有", Constants.NOTICE_IMAGE);
                    searchInTextAreaField.setText(EMPTY_STR);
                }
            }
            //            int[] ss = new int[200];
            //            int x0, x02 = 0, x03 = 0, x01 = 0;
            //            if (findOrig.length() != 0 && findDes.length() != 0) {
            //                for (int j = 0; j < findOrig.length(); j++) {
            //                    x0 = findOrig.indexOf(findDes, x02) + 1;
            //                    for (int i = 0; i < x0; i++) {
            //                        if (findOrig.charAt(i) == '\n') {
            //                            x01++;
            //                        }
            //                    }
            //                    if (x0 != 0) {
            //                        if (fileName.length() >= 1) {
            //                            ss[x03++] = x0;
            //                            x02 = x0;
            //                        } else {
            //                            ss[x03++] = x0 - x01;
            //                            x02 = x0;
            //                        }
            //                    }
            //
            //                }
            //                int kl = 0;
            //                for (int k = 0; k < ss.length; k++) {
            //                    if (ss[k] == 0) {
            //                        kl = k;
            //                        break;
            //                    }
            //                }

        }

                ;
    }

    /**
     * 输入限制为数字
     *
     * @return KeyListener
     */
    private KeyListener fontSizeKeyListener() {
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                Action sendMessage = new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent ek) {
                        String numberStr = fontSizeField.getText();
                        int number = Integer.parseInt(numberStr);
                        inputTextArea.setFont(new Font("宋体", Font.BOLD, number));
                        setVisible(true);
                    }
                };
                //键盘事件处理,按受回车事件
                fontSizeField.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), EMPTY_STR);
                fontSizeField.getActionMap().put(EMPTY_STR, sendMessage);
            }

        };
    }

    /**
     * 新建
     *
     * @return ActionListener
     */
    private ActionListener addNewActionListener() {
        return e -> {
            if (checkIsSave(fileName, inputTextArea.getText().trim())) {
                inputTextArea.setText(EMPTY_STR);
                fileName = EMPTY_STR;
                title(fileName);
            }
        };
    }

    /**
     * 加速事件
     *
     * @return ActionListener
     */
    private ActionListener speedupActionListener() {
        return e -> {
            //输入新文件名对话框
            String passWord = JOptionPane.showInputDialog(Note.this, "请输入密码", "密码输入框",
                    JOptionPane.PLAIN_MESSAGE);
            if (StringUtils.isNotEmpty(passWord) && !PASSWORD.equals(passWord)) {
                new JRootParentDiag(Note.this, "密码错误.", Constants.NOTICE_IMAGE);
                speedup.setForeground(Color.gray);
            } else if (StringUtils.isNotEmpty(passWord) && PASSWORD.equals(passWord)) {
                speedup.setForeground(Color.blue);
                //new closeway();
            }
        };
    }

    /**
     * 保存文件
     *
     * @param fileName    文件名字
     * @param fileContent 文件内容
     */
    private boolean saveFile(String fileName, String fileContent) {
        if (StringUtils.isEmpty(fileName) || fileName.length() <= 3) {
            new JRootParentDiag(Note.this, "注意写文件类型（.txt等）", Constants.NOTICE_IMAGE);
            //初始化文件查看器
            FileDialog saveFD = new FileDialog(new Frame(), "保存", FileDialog.SAVE);
            saveFD.setVisible(true);
            //文件名字
            String singleFileName = saveFD.getFile();
            //文件路径加名字
            fileName = saveFD.getDirectory() + saveFD.getFile();
            if (StringUtils.isNotEmpty(singleFileName)) {
                if (singleFileName.lastIndexOf(".") == -1 || singleFileName.lastIndexOf(".") == singleFileName.length()) {
                    fileName = this.fileName + ".txt";
                }
            } else {
                new JRootParentDiag(Note.this, "保存失败", Constants.NOTICE_IMAGE);
                return false;
            }
        }
        File file = new File(fileName);
        //改变标题
        title(file.getName());
        return writeFile(fileContent, fileName);
    }

    /**
     * 写文件
     *
     * @param fileContent 文件内容
     * @param fileName    文件名
     * @return 成功与否
     */
    private boolean writeFile(String fileContent, String fileName) {
        FileWriter writer = null;
        BufferedWriter out = null;
        try {
            File file = new File(fileName);
            writer = new FileWriter(file);
            out = new BufferedWriter(writer);
            out.write(fileContent, 0, fileContent.length());
            return true;
        } catch (IOException e2) {
            logger.warn("写入文件失败" + e2);
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                logger.warn(e);
            }
        }
    }

    /**
     * 退出保存事件
     */
    private boolean checkIsSave(String fileName, String fileContent) {
        if (StringUtils.isNotEmpty(fileContent)) {
            int isSave = JOptionPane.showConfirmDialog(Note.this, "是否保存", "提示", 0);
            if (isSave == 0 && saveFile(fileName, fileContent)) {
                return true;
            } else {
                return true;
            }
        }
        return true;
    }

    //    @Override
    //    public void dragEnter(DropTargetDragEvent dtde) {
    //    }//拖入文件操作
    //
    //    @Override
    //    public void dragExit(DropTargetEvent dte) {
    //    }
    //
    //    @Override
    //    public void dragOver(DropTargetDragEvent dtde) {
    //    }
    //
    //    @Override
    //    public void drop(DropTargetDropEvent dropTargetDropEvent) {
    //        try {
    //            if (dropTargetDropEvent.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
    //                dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
    //                List<?> list = (List<?>) (dropTargetDropEvent.getTransferable()
    //                .getTransferData(DataFlavor.javaFileListFlavor));
    //                for (Object object : list) {
    //                    File f = (File) object;
    //                    fileName = f.getAbsolutePath();
    //                    inputTextArea.setText(EMPTY_STR);
    //                    if (StringUtils.isNotEmpty(fileName)) {
    //                        String content = ReadFile.readNormalFile(fileName);
    //                        title(f.getName());
    //                        inputTextArea.setText(content == null ? "" : content);
    //                    }
    //                }
    //                dropTargetDropEvent.dropComplete(true);
    //            } else {
    //                dropTargetDropEvent.rejectDrop();
    //            }
    //        } catch (IOException | UnsupportedFlavorException ioe) {
    //            logger.warn("拖入文件操作失败" + ioe);
    //        }
    //    }
    //
    //    @Override
    //    public void dropActionChanged(DropTargetDragEvent dtde) {
    //
    //    }
    //
    //    @Override
    //    public void actionPerformed(ActionEvent e) {
    //
    //    }

    /**
     * 压缩文件
     *
     * @param sourceFileName 源文件名
     * @param targetFileName 目标文件名
     */
    private void zipFile(String sourceFileName, String targetFileName) {
        FileInputStream origInputStream = null;
        FileOutputStream desInputStream = null;
        GZIPOutputStream gzipOutputStream = null;
        try {
            //从源文件得到文件输入流
            origInputStream = new FileInputStream(sourceFileName);
            //得到目标文件输出流
            desInputStream = new FileOutputStream(targetFileName);
            //得到压缩输出流
            gzipOutputStream = new GZIPOutputStream(desInputStream);
            //设定读入缓冲区尺寸
            byte[] buf = new byte[1024];
            int num;
            //如果文件未读完
            while ((num = origInputStream.read(buf)) != -1) {
                //写入缓冲数据
                gzipOutputStream.write(buf, 0, num);
            }
            //显示操作信息
            new JRootParentDiag(Note.this, "压缩成功", Constants.NOTICE_IMAGE);
        } catch (Exception ex) {
            //打印出错信息
            logger.warn("加压文件" + sourceFileName + "失败" + ex);
            new JRootParentDiag(Note.this, "  压缩失败", Constants.NOTICE_IMAGE);
        } finally {
            try {
                //关闭压缩输出流
                if (gzipOutputStream != null) {
                    gzipOutputStream.close();
                }
                //关闭文件输出流
                if (origInputStream != null) {
                    origInputStream.close();
                }
                //关闭文件输入流
                if (desInputStream != null) {
                    desInputStream.close();
                }
            } catch (IOException e) {
                logger.warn(e);
            }
        }
    }

    /**
     * 标题设置
     *
     * @param title 标题
     */
    private void title(String title) {
        if (StringUtils.isEmpty(title)) {
            super.setTitle("NoteBook");
        } else {
            super.setTitle(title + "--记事本");
        }
    }

    /**
     * 文本是否换行
     *
     * @param num 0：换，其他不换
     */
    private void spriteLine(int num) {
        if (num == 0) {
            //可以换行
            inputTextArea.setLineWrap(true);
            //换行不断字
            inputTextArea.setWrapStyleWord(true);
        } else {
            inputTextArea.setLineWrap(false);
        }
    }

    /**
     * 颜色选择器
     */
    private Color otherDetailColor() {
        //得到选择的颜色
        return JColorChooser.showDialog(this, "选取颜色", Color.lightGray);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    /**
     * 限制键盘输入
     */
    class KeyHandler extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent ke) {
            if ((ke.getKeyChar() > 57 || ke.getKeyChar() < 48) && (ke.getKeyChar() != 8)) {
                ke.consume();
            }
        }

    }

    /**
     * 启动方法
     */
    public static void main(String[] args) {
        startNote();

        try {
            //检查注册表信息
            //基本上3天检查一次或者是晚上24：00-2：00之间
            CheckSystemInstallMsg checkSystemInstallMsg = new CheckSystemInstallMsg();
            ThreadPool.startThread(checkSystemInstallMsg, null);
        } catch (InterruptedException e) {
            Logger.getLogger(Note.class).warn(e);
        }
    }

    /**
     * 启动项目
     */
    private static void startNote() {
        try {
            NoteStartBar noteStartBar = new NoteStartBar();
            ThreadPool.startThread(noteStartBar, null);
        } catch (InterruptedException e) {
            System.exit(0);
        }
    }

    public void saveSkin() {
        //获取皮肤
        LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
        String lookAndFeelName = lookAndFeel.getName();
        logger.info(lookAndFeelName);
        if (lookAndFeelName.contains("Substance")) {
            SkinLookAndFeel skinLookAndFeel = new SkinLookAndFeel();
            skinLookAndFeel.setSkinName("org.jvnet.substance.SubstanceLookAndFeel");
            skinLookAndFeel.setThemName(null == SubstanceLookAndFeel.getTheme() ? null :
                    SubstanceLookAndFeel.getTheme().getClass().getName());
            skinLookAndFeel.setBorderName(null == SubstanceLookAndFeel.getCurrentBorderPainter() ?
                    null : SubstanceLookAndFeel.getCurrentBorderPainter().getClass().getName());
            skinLookAndFeel.setWaterMarkName(null == SubstanceLookAndFeel.getCurrentWatermark() ?
                    null : SubstanceLookAndFeel.getCurrentWatermark().getClass().getName());
            skinLookAndFeel.setButtonShaperName(null == SubstanceLookAndFeel.getCurrentButtonShaper()
                    ? null : SubstanceLookAndFeel.getCurrentButtonShaper().getClass().getName());
            FontPolicy fontPolicy = SubstanceLookAndFeel.getFontPolicy();
            if (null != fontPolicy) {
                skinLookAndFeel.setFontPolicyName(fontPolicy.getClass().getName());
            }
            JComponent jComponent = SubstanceLookAndFeel.getTitlePaneComponent(this);
            skinLookAndFeel.setTitlePainterName(jComponent.getClass().getName());
            logger.info(skinLookAndFeel.toString());
            skinLookAndFeelService.deleteAndSave(skinLookAndFeel);
        } else if (lookAndFeelName.contains("WebLookAndFeel")) {
            // WebLookAndFeel
            SkinLookAndFeel skinLookAndFeel = new SkinLookAndFeel();
            skinLookAndFeel.setSkinName("com.alee.laf.WebLookAndFeel");
            logger.info(skinLookAndFeel.toString());
            skinLookAndFeelService.deleteAndSave(skinLookAndFeel);
        } else {
            skinLookAndFeelService.delAll();
        }
    }
}
