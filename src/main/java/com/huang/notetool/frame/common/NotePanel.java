package com.huang.notetool.frame.common;

import com.huang.notetool.util.InitialComponent;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;

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
public class NotePanel extends JRootParentFrame {
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
     * 存放信息
     */
    protected JTextArea jTextArea;
    /**
     * 支持撤销操作
     */
    private UndoManager undoManager;

    /**
     * 构造方法，显示信息
     *
     * @param jFrame 基于面板
     */
    public NotePanel(JFrame jFrame) {
        undoManager = new UndoManager();
        //设置之后就不能弹出多个窗口： setModal(true);
        int x = jFrame.getX();
        int y = jFrame.getY();
        int width = jFrame.getWidth() * 3 / 4;
        int height = jFrame.getHeight() * 3 / 4;
        //居中：上层面板的x坐标+（（上层面板的长-弹窗的长）/2）（除2是为了长度前后各取部分，好居中）
        this.setBounds(x + ((jFrame.getWidth() - width) / 2), y + ((jFrame.getHeight() - height) / 2), width, height);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //非Windows主题才能设置透明度
        if (!InitialComponent.isWindowsLookAndFeel()) {
            this.setOpacity(jFrame.getOpacity());
        }
        this.setCursor(jFrame.getCursor());
        JScrollPane jScrollPane = initialComponent();
        this.add(btnPanel, BorderLayout.SOUTH);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.setResizable(true);
        this.setTitle("未命名");
    }

    /**
     * 初始化组件
     *
     * @return JScrollPane
     */
    private JScrollPane initialComponent() {
        jTextArea = InitialComponent.setJTextArea(null, null
                , null, true, null
                , false, null, true);
        jTextArea.addMouseListener(jTextAreaClickListener(jTextArea));
        //撤销操作
        jTextArea.getDocument().addUndoableEditListener(undoManager);
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        initialButton();
        //撤销操作快捷键
        InitialComponent.initialCtrlZAndB(jTextArea.getInputMap(), undoManager);
        return jScrollPane;
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
        String[] strSize = {"8", "12", "14", "18", "22", "30", "40", "52", "86", "100"};
        // 字号
        JComboBox fontSize = new JComboBox(strSize);
        //存放按钮面板
        btnPanel = new JPanel();
        btnPanel.add(new JLabel("字号："));
        btnPanel.add(fontSize);
        btnPanel.add(addBtn);
        btnPanel.add(saveBtn);
        btnPanel.add(delBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(exitBtn);

        exitBtn.addActionListener(exitBtnActionListener());
        addBtn.addActionListener(addBtnActionListener());
        fontSize.addItemListener(fontSizeSelectAction(fontSize));
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
                int textSize = Integer.parseInt(fontSize.getSelectedItem() == null ? "14" : (String) fontSize.getSelectedItem());
                jTextArea.setFont(new Font("宋体", Font.BOLD, textSize));
            }
        };
    }

    /**
     * 点击新增按钮监听事件
     *
     * @return ActionListener
     */
    protected ActionListener addBtnActionListener() {
        return e -> jTextArea.setText("");
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
        this.jTextArea.setText(text);
    }

    /**
     * 输入框点击换行事件
     *
     * @return MouseAdapter
     */
    private MouseAdapter jTextAreaClickListener(JTextArea jTextArea) {
        return new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param mouseEvent
             */
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    if (clickNum == 0) {
                        //换行不断字
                        jTextArea.setWrapStyleWord(true);
                        //换行
                        jTextArea.setLineWrap(true);
                        clickNum++;
                    } else {
                        jTextArea.setLineWrap(false);
                        jTextArea.setWrapStyleWord(false);
                        clickNum = 0;
                    }
                }
            }
        };
    }

}
