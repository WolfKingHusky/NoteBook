package com.huang.notetool.frame.format.str;

import com.huang.notetool.frame.common.ToolParentFrame;
import com.huang.notetool.tool.ChangeStrEncoding;
import com.huang.notetool.util.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;

/**
 * 此处说明类的作用
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-05-28 09:34:52
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-28   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class StrEncodingChangeFrame extends ToolParentFrame {
    /**
     * 选中的编码
     */
    private String encoding;
    /**
     * 顶部面板
     */
    private JPanel inputPanel;

    /**
     * 构造方法
     *
     * @param noteFrame 依赖的面板
     */
    public StrEncodingChangeFrame(JFrame noteFrame) {
        super(noteFrame);
        logger.info("初始化字符串编码页面... ");
        setTitle(super.getTitle() + ", 左边是输入，右边是输出");

        outputMsg.setEditable(false);
        // 创建单选按钮
        //第一个默认选中
        JRadioButton utf_8 = new JRadioButton("UTF-8", true);
        JRadioButton utf_16 = new JRadioButton("UTF-16");
        JRadioButton gbk = new JRadioButton("GBK");
        JRadioButton gb2312 = new JRadioButton("GB2312");
        JRadioButton utf_16le = new JRadioButton("UTF-16LE");
        JRadioButton utf_16be = new JRadioButton("UTF-16BE");
        JRadioButton iso_8859_1 = new JRadioButton("ISO-8859-1");
        JRadioButton us_ASCII = new JRadioButton("US-ASCII");
        JRadioButton other = new JRadioButton("其他");

        JTextField passJField = new JTextField();
        passJField.setBorder(BorderFactory.createLoweredBevelBorder());

        Box encodingBox = new Box(BoxLayout.X_AXIS);
        encodingBox.add(new JLabel("其他编码(可为空)："));
        encodingBox.add(passJField);
        encodingBox.setBorder(BorderFactory.createLoweredBevelBorder());

        JPanel radioPanel = new JPanel();
        setRadioPanel(utf_8, utf_16, gbk, gb2312, utf_16le, utf_16be, iso_8859_1, us_ASCII, other, radioPanel);

        inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(radioPanel, BorderLayout.CENTER);

        // 创建按钮组，把单选按钮添加到该组
        ButtonGroup btnGroup = new ButtonGroup();
        setGroupBtn(utf_8, utf_16, gbk, gb2312, utf_16le, utf_16be, iso_8859_1, us_ASCII, other, btnGroup, inputPanel, encodingBox);

        utf_8.addActionListener(selectRadioBtnAction(inputPanel, encodingBox, "UTF-8"));
        gb2312.addActionListener(selectRadioBtnAction(inputPanel, encodingBox, "GB2312"));
        gbk.addActionListener(selectRadioBtnAction(inputPanel, encodingBox, "GBK"));
        iso_8859_1.addActionListener(selectRadioBtnAction(inputPanel, encodingBox, "ISO-8859-1"));
        utf_16.addActionListener(selectRadioBtnAction(inputPanel, encodingBox, "UTF-16"));
        utf_16le.addActionListener(selectRadioBtnAction(inputPanel, encodingBox, "UTF-16LE"));
        utf_16be.addActionListener(selectRadioBtnAction(inputPanel, encodingBox, "UTF-16BE"));
        us_ASCII.addActionListener(selectRadioBtnAction(inputPanel, encodingBox, "US_ASCII"));
        other.addActionListener(otherRadioBtnAction(encodingBox));

        this.add(inputPanel, BorderLayout.NORTH);
        this.validate();
        this.repaint();

        //设置按钮和事件
        setButtonAndAction(passJField);
    }

    private void setGroupBtn(JRadioButton utf_8, JRadioButton utf_16, JRadioButton gbk, JRadioButton gb2312, JRadioButton utf_16le, JRadioButton utf_16be, JRadioButton iso_8859_1, JRadioButton us_ASCII, JRadioButton other, ButtonGroup btnGroup, JPanel inputPanel, Box encodingBox) {
        btnGroup.add(utf_8);
        btnGroup.add(gb2312);
        btnGroup.add(gbk);
        btnGroup.add(iso_8859_1);
        btnGroup.add(utf_16);
        btnGroup.add(utf_16le);
        btnGroup.add(utf_16be);
        btnGroup.add(us_ASCII);
        btnGroup.add(other);
    }

    private ActionListener selectRadioBtnAction(JPanel inputPanel, Box encodingBox, String msgEncoding) {
        return e -> {
            inputPanel.remove(encodingBox);
            encoding = msgEncoding;
            //对panel1面板中的组件重新布局并绘制
            inputPanel.revalidate();
            //对panel1本身进行重绘
            inputPanel.repaint();
        };
    }

    private ActionListener otherRadioBtnAction(Box encodingBox) {
        return e -> {
            inputPanel.add(encodingBox, BorderLayout.SOUTH);
            encoding = "other";
            //对panel1面板中的组件重新布局并绘制
            inputPanel.revalidate();
            //对panel1本身进行重绘
            inputPanel.repaint();
        };
    }

    private void setButtonAndAction(JTextField passJField) {
        JButton transferToUtf8Btn = new JButton("转UTF-8");
        JButton transferToUtf16Btn = new JButton("转UTF-16");
        JButton transferToGBKBtn = new JButton("转GBK");
        JButton transferToGB2312Btn = new JButton("转GB2312");
        JButton transferToUtf16LEBtn = new JButton("转UTF-16LE");
        JButton transferToUtf16BEBtn = new JButton("转UTF-16BE");
        JButton transferToISO88591Btn = new JButton("转ISO-8859-1");
        JButton transferToUsASCIIBtn = new JButton("转US-ASCII");

        buttonPanel.add(transferToUtf8Btn);
        buttonPanel.add(transferToUtf16Btn);
        buttonPanel.add(transferToGBKBtn);
        buttonPanel.add(transferToGB2312Btn);
        buttonPanel.add(transferToUtf16LEBtn);
        buttonPanel.add(transferToUtf16BEBtn);
        buttonPanel.add(transferToISO88591Btn);
        buttonPanel.add(transferToUsASCIIBtn);

        transferToUtf8Btn.addActionListener(transferToUtf8BtnAction(passJField));
        transferToUtf16Btn.addActionListener(transferToUtf16BtnAction(passJField));
        transferToGBKBtn.addActionListener(transferToGBKBtnAction(passJField));
        transferToGB2312Btn.addActionListener(transferToGB2312BtnAction(passJField));
        transferToUtf16LEBtn.addActionListener(transferToUtf16LEBtnAction(passJField));
        transferToUtf16BEBtn.addActionListener(transferToUtf16BEBtnBtnAction(passJField));
        transferToISO88591Btn.addActionListener(transferToISO88591BtnBtnBtnAction(passJField));
        transferToUsASCIIBtn.addActionListener(transferToUsASCIIBtnBtnAction(passJField));
    }

    private void setRadioPanel(JRadioButton utf_8, JRadioButton utf_16, JRadioButton gbk, JRadioButton gb2312, JRadioButton utf_16le, JRadioButton utf_16be, JRadioButton iso_8859_1, JRadioButton us_ASCII, JRadioButton other, JPanel radioPanel) {
        radioPanel.add(new JLabel("原字符串编码："));
        radioPanel.add(utf_8);
        radioPanel.add(gb2312);
        radioPanel.add(gbk);
        radioPanel.add(iso_8859_1);
        radioPanel.add(utf_16);
        radioPanel.add(utf_16le);
        radioPanel.add(utf_16be);
        radioPanel.add(us_ASCII);
        radioPanel.add(other);
        radioPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }

    /**
     * 转为ASCII编码的监听事件
     *
     * @param passJField
     * @return
     */
    private ActionListener transferToUsASCIIBtnBtnAction(JTextField passJField) {
        return e -> {
            String msg = inputMsg.getText();
            if (StringUtils.isEmpty(msg)) {
                JOptionPane.showMessageDialog(StrEncodingChangeFrame.this, "没有输入");
                return;
            }
            if (StringUtils.isEmpty(encoding)) {
                encodingToAsCIIWithoutOrigEncoding(msg);
            } else if (encoding.equals("other")) {
                String inputEncoding = passJField.getText().trim();
                if (StringUtils.isEmpty(inputEncoding)) {
                    encodingToAsCIIWithoutOrigEncoding(msg);
                } else {
                    encodingToAsCIIWithOrigEncoding(msg, inputEncoding);
                }
            } else {
                encodingToAsCIIWithOrigEncoding(msg, encoding);
            }
        };
    }

    /**
     * 指定源字符串编码
     * 转为ASCII编码
     *
     * @param msg
     */
    private void encodingToAsCIIWithOrigEncoding(String msg, String inputEncoding) {
        try {
            String returnValue = ChangeStrEncoding.toASCIIWithOrigEncoding(msg, inputEncoding);
            if (StringUtils.isNotEmpty(returnValue)) {
                outputMsg.setText(returnValue);
            }
        } catch (UnsupportedEncodingException ex) {
            logger.warn(ex);
            JOptionPane.showMessageDialog(StrEncodingChangeFrame.this, "不支持的编码:" + ex.getMessage());
        }
    }

    /**
     * 没有指定源字符串编码
     * 转为ASCII编码
     *
     * @param msg
     */
    private void encodingToAsCIIWithoutOrigEncoding(String msg) {
        try {
            String returnValue = ChangeStrEncoding.toASCII(msg);
            if (StringUtils.isNotEmpty(returnValue)) {
                outputMsg.setText(returnValue);
            }
        } catch (UnsupportedEncodingException ex) {
            logger.warn(ex);
            JOptionPane.showMessageDialog(StrEncodingChangeFrame.this, "不支持的编码:" + ex.getMessage());
        }
    }

    private ActionListener transferToISO88591BtnBtnBtnAction(JTextField passJField) {
        return e -> {
            String msg = inputMsg.getText();
            if (StringUtils.isEmpty(msg)) {
                JOptionPane.showMessageDialog(StrEncodingChangeFrame.this, "没有输入");
                return;
            }
            try {
                if (StringUtils.isEmpty(encoding)) {
                    encodingToISO88591WithoutOrigEncoding(msg);
                } else if (encoding.equals("other")) {
                    String inputEncoding = passJField.getText().trim();
                    if (StringUtils.isEmpty(inputEncoding)) {
                        encodingToISO88591WithoutOrigEncoding(msg);
                    } else {
                        encodingToISO88591WithOrigEncoding(msg, inputEncoding);
                    }
                } else {
                    encodingToISO88591WithOrigEncoding(msg, encoding);
                }
            } catch (UnsupportedEncodingException ex) {
                logger.warn(ex);
                JOptionPane.showMessageDialog(StrEncodingChangeFrame.this, "不支持的编码:" + ex.getMessage());
            }
        };
    }

    /**
     * 指定源字符串编码
     * 转为IS-O8859-1编码
     *
     * @param msg
     */
    private void encodingToISO88591WithOrigEncoding(String msg, String inputEncoding) throws UnsupportedEncodingException {
        String returnValue = ChangeStrEncoding.toISO_8859_1WithOrigEncoding(msg, inputEncoding);
        if (StringUtils.isNotEmpty(returnValue)) {
            outputMsg.setText(returnValue);
        }
    }

    /**
     * 没有指定源字符串编码
     * 转为IS-O8859-1编码
     *
     * @param msg
     */
    private void encodingToISO88591WithoutOrigEncoding(String msg) throws UnsupportedEncodingException {
        String returnValue = ChangeStrEncoding.toISO_8859_1(msg);
        if (StringUtils.isNotEmpty(returnValue)) {
            outputMsg.setText(returnValue);
        }
    }

    private ActionListener transferToUtf16BEBtnBtnAction(JTextField passJField) {
        return e -> {
            String msg = inputMsg.getText();
            if (StringUtils.isEmpty(msg)) {
                JOptionPane.showMessageDialog(StrEncodingChangeFrame.this, "没有输入");
                return;
            }
            try {
                if (StringUtils.isEmpty(encoding)) {
                    encodingToUtf16BEWithoutOrigEncoding(msg);
                } else if (encoding.equals("other")) {
                    String inputEncoding = passJField.getText().trim();
                    if (StringUtils.isEmpty(inputEncoding)) {
                        encodingToUtf16BEWithoutOrigEncoding(msg);
                    } else {
                        encodingToUtf16BEWithOrigEncoding(msg, inputEncoding);
                    }
                } else {
                    encodingToUtf16BEWithOrigEncoding(msg, encoding);
                }
            } catch (UnsupportedEncodingException ex) {
                logger.warn(ex);
                JOptionPane.showMessageDialog(StrEncodingChangeFrame.this, "不支持的编码:" + ex.getMessage());
            }
        };
    }

    /**
     * 指定源字符串编码
     * 转为Utf16BE编码
     *
     * @param msg
     */
    private void encodingToUtf16BEWithOrigEncoding(String msg, String inputEncoding) throws UnsupportedEncodingException {
        String returnValue = ChangeStrEncoding.toUTF_16BE(msg, inputEncoding);
        if (StringUtils.isNotEmpty(returnValue)) {
            outputMsg.setText(returnValue);
        }
    }

    /**
     * 没有指定源字符串编码
     * 转为Utf16BE编码
     *
     * @param msg
     */
    private void encodingToUtf16BEWithoutOrigEncoding(String msg) throws UnsupportedEncodingException {
        String returnValue = ChangeStrEncoding.toUTF_16BE(msg);
        if (StringUtils.isNotEmpty(returnValue)) {
            outputMsg.setText(returnValue);
        }
    }

    private ActionListener transferToUtf16LEBtnAction(JTextField passJField) {
        return e -> {
            String msg = inputMsg.getText();
            if (StringUtils.isEmpty(msg)) {
                JOptionPane.showMessageDialog(StrEncodingChangeFrame.this, "没有输入");
                return;
            }
            try {
                if (StringUtils.isEmpty(encoding)) {
                    encodingToUtf16LEWithoutOrigEncoding(msg);
                } else if (encoding.equals("other")) {
                    String inputEncoding = passJField.getText().trim();
                    if (StringUtils.isEmpty(inputEncoding)) {
                        encodingToUtf16LEWithoutOrigEncoding(msg);
                    } else {
                        encodingToUtf16LEWithOrigEncoding(msg, inputEncoding);
                    }
                } else {
                    encodingToUtf16LEWithOrigEncoding(msg, encoding);
                }
            } catch (UnsupportedEncodingException ex) {
                logger.warn(ex);
                JOptionPane.showMessageDialog(StrEncodingChangeFrame.this, "不支持的编码:" + ex.getMessage());
            }
        };
    }

    private void encodingToUtf16LEWithOrigEncoding(String msg, String inputEncoding) throws UnsupportedEncodingException {
        String returnValue = ChangeStrEncoding.toUTF_16LE(msg, inputEncoding);
        if (StringUtils.isNotEmpty(returnValue)) {
            outputMsg.setText(returnValue);
        }
    }

    private void encodingToUtf16LEWithoutOrigEncoding(String msg) throws UnsupportedEncodingException {
        String returnValue = ChangeStrEncoding.toUTF_16LE(msg);
        if (StringUtils.isNotEmpty(returnValue)) {
            outputMsg.setText(returnValue);
        }
    }

    private ActionListener transferToGB2312BtnAction(JTextField passJField) {
        return e -> {
            String msg = inputMsg.getText();
            if (StringUtils.isEmpty(msg)) {
                JOptionPane.showMessageDialog(StrEncodingChangeFrame.this, "没有输入");
                return;
            }
            try {
                if (StringUtils.isEmpty(encoding)) {
                    encodingToGB2312WithoutOrigEncoding(msg);
                } else if (encoding.equals("other")) {
                    String inputEncoding = passJField.getText().trim();
                    if (StringUtils.isEmpty(inputEncoding)) {
                        encodingToGB2312WithoutOrigEncoding(msg);
                    } else {
                        encodingToGB2312WithOrigEncoding(msg, inputEncoding);
                    }
                } else {
                    encodingToGB2312WithOrigEncoding(msg, encoding);
                }
            } catch (UnsupportedEncodingException ex) {
                logger.warn(ex);
                JOptionPane.showMessageDialog(StrEncodingChangeFrame.this, "不支持的编码:" + ex.getMessage());
            }
        };
    }

    private void encodingToGB2312WithOrigEncoding(String msg, String inputEncoding) throws UnsupportedEncodingException {
        String returnValue = ChangeStrEncoding.toGB2312(msg, inputEncoding);
        if (StringUtils.isNotEmpty(returnValue)) {
            outputMsg.setText(returnValue);
        }
    }

    private void encodingToGB2312WithoutOrigEncoding(String msg) throws UnsupportedEncodingException {
        String returnValue = ChangeStrEncoding.toGB2312(msg);
        if (StringUtils.isNotEmpty(returnValue)) {
            outputMsg.setText(returnValue);
        }
    }

    private ActionListener transferToGBKBtnAction(JTextField passJField) {
        return e -> {
            String msg = inputMsg.getText();
            if (StringUtils.isEmpty(msg)) {
                JOptionPane.showMessageDialog(StrEncodingChangeFrame.this, "没有输入");
                return;
            }
            try {
                if (StringUtils.isEmpty(encoding)) {
                    encodingToGBKWithoutOrigEncoding(msg);
                } else if (encoding.equals("other")) {
                    String inputEncoding = passJField.getText().trim();
                    if (StringUtils.isEmpty(inputEncoding)) {
                        encodingToGBKWithoutOrigEncoding(msg);
                    } else {
                        encodingToGBKWithOrigEncoding(msg, inputEncoding);
                    }
                } else {
                    encodingToGBKWithOrigEncoding(msg, encoding);
                }
            } catch (UnsupportedEncodingException ex) {
                logger.warn(ex);
                JOptionPane.showMessageDialog(StrEncodingChangeFrame.this, "不支持的编码:" + ex.getMessage());
            }
        };
    }

    private void encodingToGBKWithOrigEncoding(String msg, String inputEncoding) throws UnsupportedEncodingException {
        String returnValue = ChangeStrEncoding.toGBK(msg, inputEncoding);
        if (StringUtils.isNotEmpty(returnValue)) {
            outputMsg.setText(returnValue);
        }
    }

    private void encodingToGBKWithoutOrigEncoding(String msg) throws UnsupportedEncodingException {
        String returnValue = ChangeStrEncoding.toGBK(msg);
        if (StringUtils.isNotEmpty(returnValue)) {
            outputMsg.setText(returnValue);
        }
    }

    private ActionListener transferToUtf16BtnAction(JTextField passJField) {
        return e -> {
            String msg = inputMsg.getText();
            if (StringUtils.isEmpty(msg)) {
                JOptionPane.showMessageDialog(StrEncodingChangeFrame.this, "没有输入");
                return;
            }
            try {
                if (StringUtils.isEmpty(encoding)) {
                    encodingToUtf16WithoutOrigEncoding(msg);
                } else if (encoding.equals("other")) {
                    String inputEncoding = passJField.getText().trim();
                    if (StringUtils.isEmpty(inputEncoding)) {
                        encodingToUtf16WithoutOrigEncoding(msg);
                    } else {
                        encodingToUtf16WithOrigEncoding(msg, inputEncoding);
                    }
                } else {
                    encodingToUtf16WithOrigEncoding(msg, encoding);
                }
            } catch (UnsupportedEncodingException ex) {
                logger.warn(ex);
                JOptionPane.showMessageDialog(StrEncodingChangeFrame.this, "不支持的编码:" + ex.getMessage());
            }
        };
    }

    private void encodingToUtf16WithOrigEncoding(String msg, String inputEncoding) throws UnsupportedEncodingException {
        String returnValue = ChangeStrEncoding.toUTF_16(msg, inputEncoding);
        if (StringUtils.isNotEmpty(returnValue)) {
            outputMsg.setText(returnValue);
        }
    }

    private void encodingToUtf16WithoutOrigEncoding(String msg) throws UnsupportedEncodingException {
        String returnValue = ChangeStrEncoding.toUTF_16(msg);
        if (StringUtils.isNotEmpty(returnValue)) {
            outputMsg.setText(returnValue);
        }
    }

    private ActionListener transferToUtf8BtnAction(JTextField passJField) {
        return e -> {
            String msg = inputMsg.getText();
            if (StringUtils.isEmpty(msg)) {
                JOptionPane.showMessageDialog(StrEncodingChangeFrame.this, "没有输入");
                return;
            }
            try {
                if (StringUtils.isEmpty(encoding)) {
                    encodingToUtf8WithoutOrigEncoding(msg);
                } else if (encoding.equals("other")) {
                    String inputEncoding = passJField.getText().trim();
                    if (StringUtils.isEmpty(inputEncoding)) {
                        encodingToUtf8WithoutOrigEncoding(msg);
                    } else {
                        encodingToUtf8WithOrigEncoding(msg, inputEncoding);
                    }
                } else {
                    encodingToUtf8WithOrigEncoding(msg, encoding);
                }
            } catch (UnsupportedEncodingException ex) {
                logger.warn(ex);
                JOptionPane.showMessageDialog(StrEncodingChangeFrame.this, "不支持的编码:" + ex.getMessage());
            }
        };
    }

    private void encodingToUtf8WithOrigEncoding(String msg, String inputEncoding) throws UnsupportedEncodingException {
        String returnValue = ChangeStrEncoding.toUTF_8(msg, inputEncoding);
        if (StringUtils.isNotEmpty(returnValue)) {
            outputMsg.setText(returnValue);
        }
    }

    private void encodingToUtf8WithoutOrigEncoding(String msg) throws UnsupportedEncodingException {
        String returnValue = ChangeStrEncoding.toUTF_8(msg);
        if (StringUtils.isNotEmpty(returnValue)) {
            outputMsg.setText(returnValue);
        }
    }
}
