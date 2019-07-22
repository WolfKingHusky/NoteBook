package com.huang.notetool.frame.common;

import com.huang.notetool.tool.ReadFile;
import com.huang.notetool.util.InitialComponent;
import com.huang.notetool.util.NormalFileFilter;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.huang.notetool.util.Constants.EMPTY_STR;
import static java.awt.event.InputEvent.CTRL_MASK;

/**
 * 显示记录信息
 * 记录是通过Base64编码
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-06-27 09:36:52
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-27   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class NoteImagPanel extends JRootParentFrame {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(NoteImagPanel.class);
    /**
     * 监听异常信息输入框换行计数
     */
    private int clickNum;
    /**
     * 保存按钮
     */
    protected JButton saveBtn;
    /**
     * 删除按钮
     */
    protected JButton delBtn;
    /**
     * 刷新按钮
     */
    protected JButton refreshBtn;
    /**
     * 新增按钮
     */
    protected JButton addBtn;
    /**
     * 存放按钮面板
     */
    protected JPanel btnPanel;
    /**
     * 支持剪切操作
     */
    private Clipboard clipboard;
    /**
     * 可存放图片面板
     */
    protected JTextPane jTextPane;
    private StyledDocument doc;
    /**
     * 支持撤销操作
     */
    private UndoManager undoManager;

    /**
     * 构造方法，显示信息
     *
     * @param jFrame 基于面板
     */
    public NoteImagPanel(JFrame jFrame) {
        undoManager = new UndoManager();
        if (!InitialComponent.isWindowsLookAndFeel()) {
            this.setOpacity(jFrame.getOpacity());
        }
        this.setCursor(jFrame.getCursor());
        int x = jFrame.getX();
        int y = jFrame.getY();
        int width = jFrame.getWidth() * 3 / 4;
        int height = jFrame.getHeight() * 3 / 4;
        //居中：上层面板的x坐标+（（上层面板的长-弹窗的长）/2）（除2是为了长度前后各取部分，好居中）
        this.setBounds(x + ((jFrame.getWidth() - width) / 2), y + ((jFrame.getHeight() - height) / 2), width, height);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JScrollPane jTextPaneJSP = initialJTextPanel();
        initialButton();
        initialShiftClick();
        this.add(btnPanel, BorderLayout.SOUTH);
        this.add(jTextPaneJSP, BorderLayout.CENTER);
        this.setResizable(true);
        this.setTitle("未命名");
    }

    /**
     * 初始化数据
     *
     * @return JScrollPane
     */
    private JScrollPane initialJTextPanel() {
        jTextPane = new JTextPane();
        setJTextPanel(jTextPane);
        //关键部分，没有为cb申请内存，下面对cb操作会出错
        //获取系统剪贴板
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        return new JScrollPane(jTextPane);
    }

    private void setJTextPanel(JTextPane jTextPane) {
        doc = jTextPane.getStyledDocument();
        jTextPane.setEditable(true);
        jTextPane.setBorder(BorderFactory.createLoweredBevelBorder());
        //初始光标样式
        jTextPane.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        //撤销操作
        jTextPane.getDocument().addUndoableEditListener(undoManager);
    }

    /**
     * 快捷键
     */
    private void initialShiftClick() {
        InputMap inputMap = jTextPane.getInputMap();
        InitialComponent.initialCtrlZAndB(inputMap, undoManager);
        //CTRL+V
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_V, CTRL_MASK);
        inputMap.put(key, paseActionListener());
        //CTRL+C
        key = KeyStroke.getKeyStroke(KeyEvent.VK_C, CTRL_MASK);
        inputMap.put(key, copyActionListener());
        //CTRL+X
        key = KeyStroke.getKeyStroke(KeyEvent.VK_X, CTRL_MASK);
        inputMap.put(key, cutActionListener());
    }


    /**
     * 初始化按钮
     */
    private void initialButton() {
        //保存按钮
        saveBtn = new JButton("保存");
        //删除按钮
        delBtn = new JButton("删除");
        //刷新按钮
        refreshBtn = new JButton("刷新");
        //新增
        addBtn = new JButton("新增");
        //退出
        JButton exitBtn = new JButton("退出");

        //初始化面板
        setJPanel(exitBtn);

        exitBtn.addActionListener(exitBtnActionListener());
        addBtn.addActionListener(addBtnActionListener());

    }

    /**
     * 初始化面板
     *
     * @param exitBtn 按钮
     */
    private void setJPanel(JButton exitBtn) {
        String[] strSize = {"8", "12", "14", "18", "22", "30", "40", "52", "86", "100"};
        // 字号
        JComboBox fontSizeJComBox = new JComboBox(strSize);

        //存放按钮面板
        btnPanel = new JPanel();
        btnPanel.add(new JLabel("字号："));
        btnPanel.add(fontSizeJComBox);
        btnPanel.add(addBtn);
        btnPanel.add(saveBtn);
        btnPanel.add(delBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(exitBtn);

        fontSizeJComBox.addItemListener(fontSizeSelectAction(fontSizeJComBox));
    }

    /**
     * 字体选择事件
     *
     * @param fontSize 字体大小
     * @return ItemListener
     */
    private ItemListener fontSizeSelectAction(JComboBox fontSize) {
        return e -> {
            //如果选中了一个
            if (e.getStateChange() == ItemEvent.SELECTED) {
                //这里写你的任务 ，比如取到现在的值
                int textSize = Integer.parseInt(fontSize.getSelectedItem() == null ? "12" : (String) fontSize.getSelectedItem());
                jTextPane.setFont(new Font("宋体", Font.BOLD, textSize));
            }
        };
    }

    /**
     * 点击新增按钮监听事件
     *
     * @return ActionListener
     */
    protected ActionListener addBtnActionListener() {
        return e -> jTextPane.setText(EMPTY_STR);
    }

    /**
     * 点击退出按钮监听事件
     *
     * @return ActionListener
     */
    private ActionListener exitBtnActionListener() {
        return e -> this.dispose();
    }


    /**
     * 设置文本数据
     */
    public void setText(String text) {
        this.jTextPane.setText(text);
    }


    /**
     * 粘贴
     *
     * @return AbstractAction
     */
    private AbstractAction paseActionListener() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String copyStr = EMPTY_STR;
                Transferable transferable = clipboard.getContents(null);
                try {
                    if (transferable != null) {
                        if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                            // 因为原系的剪贴板里有多种信息, 如文字, 图片, 文件等
                            // 先判断开始取得的可传输的数据是不是文字, 如果是, 取得这些文字
                            copyStr = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                            // 同样, 因为Transferable中的DataFlavor是多种类型的,
                            // 所以传入DataFlavor这个参数, 指定要取得哪种类型的Data.
                        } else if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                            BufferedImage bufferedImage = (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
                            //插入图片
                            insertPic(bufferedImage);
                        } else if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                            paseFileWay(transferable);
                        }
                    }
                } catch (UnsupportedFlavorException | IOException ex) {
                    logger.warn("复制异常" + ex);
                }
                try {
                    doc.insertString(jTextPane.getCaretPosition(), copyStr, jTextPane.getCharacterAttributes());
                } catch (BadLocationException e1) {
                    logger.warn(e);
                }
            }
        };
    }

    /**
     * 粘贴文件
     *
     * @param transferable 面板
     * @throws UnsupportedFlavorException 不支持的类型
     * @throws IOException                文件读写异常
     */
    private void paseFileWay(Transferable transferable) throws UnsupportedFlavorException, IOException {
        List<File> fileList = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
        for (File file : fileList) {
            if (file.isFile()) {
                String name = file.getName();
                String type = name.substring(name.lastIndexOf(".")).toLowerCase();
                if (NormalFileFilter.isaPicture(type)) {
                    jTextPane.insertIcon(new ImageIcon(file.getAbsolutePath()));
                } else {
                    NormalFileFilter normalFileFilter = new NormalFileFilter();
                    if (normalFileFilter.accept(file)) {
                        String fileContent = ReadFile.readNormalFile(file.getAbsolutePath());
                        insert(fileContent, jTextPane.getFont());
                    }
                }
            }
        }
    }


    /**
     * 插入图片
     *
     * @param bufferedImage 图片Buffered
     */
    protected void insertPic(BufferedImage bufferedImage) {
        if (null != bufferedImage) {
            ImageIcon imageIcon = new ImageIcon(bufferedImage);
            jTextPane.insertIcon(imageIcon);
        }
    }

    /**
     * 复制
     *
     * @return AbstractAction
     */
    private AbstractAction copyActionListener() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = jTextPane.getSelectedText();
//                //获取读取的StyledDocument
//                StyledDocument styledDocument = jTextPane.getStyledDocument();
//                DataFlavor[] dataFlavors=new DataFlavor[styledDocument.getLength()];
//                //遍历读取的StyledDocument
//                for (int i = 0; i < styledDocument.getLength(); i++) {
//                    //如果发现是icon元素，那么：
//                    if (styledDocument.getCharacterElement(i).getName().equals("icon")) {
//                        Element ele = styledDocument.getCharacterElement(i);
//                        ImageIcon icon = (ImageIcon) StyleConstants.getIcon(ele.getAttributes());
//                        BufferedImage bufferedImage = ImageTool.imageToBufferedImage(icon.getImage());
//                        dataFlavors[i]=new DataFlavor();
//                    } else {//如果不是icon（可以判断是文字，因为目前只有图片元素插入）
//                        try {
//                            String content = styledDocument.getText(i, 1);
//                            //从光标处插入文字
//                            dataFlavors[i]=new DataFlavor().set;
//                        } catch (BadLocationException e1) {
//                            e1.printStackTrace();
//                        }
//                    }
//                }
//
//                clipboard.setContents(new Transferable() {
//                    @Override
//                    public DataFlavor[] getTransferDataFlavors() {
//                        return dataFlavors;
//                    }
//
//                    @Override
//                    public boolean isDataFlavorSupported(DataFlavor flavor) {
//                        return true;
//                    }
//
//                    @Override
//                    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
//                        return null;
//                    }
//                }, null);
                StringSelection select = new StringSelection(content);
                clipboard.setContents(select, null);
            }
        };
    }

    /**
     * 剪切
     *
     * @return ActionListener
     */
    private ActionListener cutActionListener() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //类text中没有cut方法，不能使用text.cut
                String content = jTextPane.getSelectedText();
                StringSelection select = new StringSelection(content);
                clipboard.setContents(select, null);
                jTextPane.replaceSelection(EMPTY_STR);
            }
        };
    }

    /**
     * 复制图片到剪贴板
     *
     * @param image 图片
     */
    public void setClipboardImage(final Image image) {
        Transferable trans = new Transferable() {
            @Override
            public Object getTransferData(DataFlavor flavor)
                    throws UnsupportedFlavorException, IOException {
                // TODO Auto-generated method stub
                if (isDataFlavorSupported(flavor)) {
                    return image;
                }
                throw new UnsupportedFlavorException(flavor);
            }

            @Override
            public DataFlavor[] getTransferDataFlavors() {
                // TODO Auto-generated method stub
                return new DataFlavor[]{DataFlavor.imageFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                // TODO Auto-generated method stub
                return DataFlavor.imageFlavor.equals(flavor);
            }
        };
        clipboard.setContents(trans, null);
    }

    /**
     * 插入字符串
     *
     * @param content 字符串
     * @param font    字体
     */
    protected void insert(String content, Font font) {
        try { // 插入文本
            doc.insertString(doc.getLength(), content + "\n", getAttributeSet(font));
        } catch (BadLocationException e) {
            logger.warn(e);
        }
    }

    /**
     * 字体设置
     *
     * @param font 字体
     * @return AttributeSet
     */
    private SimpleAttributeSet getAttributeSet(Font font) {
        SimpleAttributeSet attrSet = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attrSet, font.getFamily());

        StyleConstants.setBold(attrSet, false);
        StyleConstants.setItalic(attrSet, false);
        StyleConstants.setFontSize(attrSet, font.getSize());
        return attrSet;
    }
}
