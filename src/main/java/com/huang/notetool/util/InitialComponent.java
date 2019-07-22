package com.huang.notetool.util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static java.awt.event.InputEvent.CTRL_MASK;

/**
 * 初始化组件
 *
 * @author 黄招荣
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-07-03 17:52:54
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-07-03   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class InitialComponent {
    /**
     * 设置子菜单
     *
     * @param name   菜单名字
     * @param enable 是否可用
     * @return 菜单
     */
    public static JMenuItem setJMenuItem(String name, Color color, boolean enable) {
        JMenuItem jMenuItem = new JMenuItem(name);
        //设置字体颜色
        jMenuItem.setForeground(color);
        jMenuItem.setEnabled(enable);
        return jMenuItem;
    }

    /**
     * 初始化JTextField
     *
     * @param length         输入框长度
     * @param defaultContent 默认显示内容
     * @param editable       是否可编辑
     * @param border         边框
     * @return 初始化JTextField
     */
    public static JTextField setJTExtField(Integer length, String defaultContent, boolean editable, Border border) {
        JTextField jTextField = commonjTextField(length, defaultContent, editable);
        if (null != border) {
            jTextField.setBorder(border);
        }
        return jTextField;
    }

    /**
     * 初始化JTextField
     *
     * @param length         输入框长度
     * @param defaultContent 默认显示内容
     * @param editable       是否可编辑
     * @return 初始化JTextField
     */
    public static JTextField setJTExtField(Integer length, String defaultContent, boolean editable) {
        JTextField jTextField = commonjTextField(length, defaultContent, editable);
        return jTextField;
    }

    /**
     * 设置公共属性
     *
     * @param length         长度
     * @param defaultContent 内容
     * @param editable       是否可编辑
     * @return JTextField
     */
    private static JTextField commonjTextField(Integer length, String defaultContent, boolean editable) {
        JTextField jTextField;
        if (null != length) {
            jTextField = new JTextField(length);
        } else {
            jTextField = new JTextField();
        }
        if (StringUtils.isNotEmpty(defaultContent)) {
            jTextField.setText(defaultContent);
        }
        //是否被编辑 false：不能
        jTextField.setEditable(editable);
        return jTextField;
    }

    /**
     * 设置菜单
     *
     * @param name     菜单名字
     * @param keyEvent 按键
     * @return 菜单
     */
    public static JMenu setJMenu(String name, Integer keyEvent) {
        JMenu jMenu = new JMenu(name);
        jMenu.setName(name);
        if (null != keyEvent) {
            //快捷键
            jMenu.setMnemonic(keyEvent);
        }
        return jMenu;
    }

    /**
     * 设置文本框初始化
     *
     * @param fontSize  字体大小
     * @param fontColor 字体颜色
     * @param backColor 背景颜色
     * @param enable    是否可选中
     * @param font      字体：宋体等
     * @param lineWrap  是否换行
     * @param text      文本内容
     * @param editable  是否可编辑
     * @return 输入框
     */
    public static JTextArea setJTextArea(Integer fontSize, Color fontColor
            , Color backColor, boolean enable
            , String font, boolean lineWrap, String text, boolean editable) {
        JTextArea jTextArea = new JTextArea();
        setJTextAreaFontSizeAndColor(fontSize, fontColor, font, jTextArea, editable);
        if (null != backColor) {
            jTextArea.setBackground(backColor);
        }
        if (StringUtils.isNotEmpty(text)) {
            jTextArea.setText(text);
        }
        jTextArea.setEnabled(enable);
        jTextArea.setLineWrap(lineWrap);
        jTextArea.setWrapStyleWord(lineWrap);
        //初始光标样式
        jTextArea.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        return jTextArea;
    }

    /**
     * 设置文本框初始化
     *
     * @param fontSize  字体大小
     * @param fontColor 字体颜色
     * @param editable  是否可编辑
     * @param lineWrap  是否换行
     * @param font      字体：宋体等
     * @param text      文本内容
     * @return 输入框
     */
    public static JTextArea setJTextArea(Integer fontSize, Color fontColor
            , boolean editable, String font, String text, boolean lineWrap) {
        JTextArea jTextArea = new JTextArea();
        setJTextAreaFontSizeAndColor(fontSize, fontColor, font, jTextArea, editable);
        if (StringUtils.isNotEmpty(text)) {
            jTextArea.setText(text);
        }
        jTextArea.setLineWrap(lineWrap);
        jTextArea.setWrapStyleWord(lineWrap);
        //初始光标样式
        jTextArea.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        return jTextArea;
    }

    /**
     * 设置文本框初始化
     *
     * @param fontSize 字体大小
     * @param font     字体：宋体等
     * @return 输入框
     */
    public static JTextArea setJTextArea(Integer fontSize, String font) {
        JTextArea jTextArea = new JTextArea();
        if (null != font && null != fontSize) {
            jTextArea.setFont(new Font(font, Font.BOLD, fontSize));
        }
        //初始光标样式
        jTextArea.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        return jTextArea;
    }

    /**
     * 设置文本框初始化
     *
     * @param fontSize  字体大小
     * @param fontColor 字体颜色
     * @param editable  是否可编辑
     * @param font      字体：宋体等
     * @param text      文本内容
     * @return 输入框
     */
    public static JTextArea setJTextArea(Integer fontSize, Color fontColor
            , boolean editable, String font, String text) {
        JTextArea jTextArea = new JTextArea();
        setJTextAreaFontSizeAndColor(fontSize, fontColor, font, jTextArea, editable);
        if (StringUtils.isNotEmpty(text)) {
            jTextArea.setText(text);
        }
        //初始光标样式
        jTextArea.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        return jTextArea;
    }

    /**
     * 设置字体
     *
     * @param fontSize  字体大小
     * @param fontColor 字体颜色
     * @param font      字体名称
     * @param jTextArea 文本框
     * @param editable  是否可编辑
     */
    private static void setJTextAreaFontSizeAndColor(Integer fontSize, Color fontColor
            , String font, JTextArea jTextArea, boolean editable) {
        if (null != font && null != fontSize) {
            jTextArea.setFont(new Font(font, Font.BOLD, fontSize));
        }
        if (null != fontColor) {
            jTextArea.setForeground(fontColor);
        }
        jTextArea.setEditable(editable);
    }


    /**
     * 快捷键
     * 撤销和还原
     *
     * @param inputMap    文本框输入监听
     * @param undoManager 支持撤销操作
     */
    public static void initialCtrlZAndB(InputMap inputMap, UndoManager undoManager) {
        //ctrl +z
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_Z, CTRL_MASK);
        inputMap.put(key, unDoAction(undoManager));
        //ctrl +b 撤销还原
        key = KeyStroke.getKeyStroke(KeyEvent.VK_B, CTRL_MASK);
        inputMap.put(key, reDoAction(undoManager));
        // 只读ctrl+r
        key = KeyStroke.getKeyStroke(KeyEvent.VK_R, CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.readOnlyAction);
        //可写trl+w
        key = KeyStroke.getKeyStroke(KeyEvent.VK_W, CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.writableAction);
    }

    /**
     * 撤销
     *
     * @param undoManager 支持撤销操作
     * @return AbstractAction
     */
    private static AbstractAction unDoAction(UndoManager undoManager) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
            }
        };
    }

    /**
     * 撤销还原
     *
     * @param undoManager 支持撤销操作
     * @return AbstractAction
     */
    private static AbstractAction reDoAction(UndoManager undoManager) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                }
            }
        };
    }

    /**
     * 判断是否为Windows窗口
     *
     * @return boolean
     */
    public static boolean isWindowsLookAndFeel() {
        LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
        String sysLookAndFeelName = UIManager.getSystemLookAndFeelClassName();
        //默认窗口也要显示信息
        return lookAndFeel.getClass().getName().equalsIgnoreCase(sysLookAndFeelName);
    }
}
