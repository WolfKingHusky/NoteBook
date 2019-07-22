package com.huang.notetool.frame.format.crypt;

import com.huang.notetool.frame.common.ToolParentFrame;
import com.huang.notetool.tool.FormatData;
import com.huang.notetool.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 加密解密显示面板
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-05-25 21:40:44
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-25   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class DecryptAndEncryptFrame extends ToolParentFrame {

    public static final String URL = "URL";
    /**
     * 显示信息
     */
    private JLabel passJLabel;

    public DecryptAndEncryptFrame(JFrame noteFrame) {
        super(noteFrame);
        setTitle(super.getTitle() + ", 左边是明文，右边是密文，密文的输入和输出都在右边，");
        logger.info("进入加密解密显示面板... ");
        // 创建单选按钮
        //第一个默认选中
        JRadioButton base64EncryptBtn = new JRadioButton("Base64加密", true);
        JRadioButton aesEncryptBtn = new JRadioButton("Aes加密");
        JRadioButton md5EncryptBtn = new JRadioButton("MD5加密");
        JRadioButton aesDecryptBtn = new JRadioButton("Aes解密");
        JRadioButton base64DecryptBtn = new JRadioButton("Base64解密");
        JRadioButton urlDecryptBtn = new JRadioButton("URL解密");
        JRadioButton urlEncryptBtn = new JRadioButton("URL加密");

        // 创建按钮组，把单选按钮添加到该组
        ButtonGroup btnGroup = new ButtonGroup();

        addGroupBtn(base64EncryptBtn, aesEncryptBtn, md5EncryptBtn, aesDecryptBtn, base64DecryptBtn, btnGroup, urlEncryptBtn, urlDecryptBtn);

        JPanel radioPanel = new JPanel();
        radioPanel.add(base64EncryptBtn);
        radioPanel.add(base64DecryptBtn);
        radioPanel.add(aesEncryptBtn);
        radioPanel.add(aesDecryptBtn);
        radioPanel.add(md5EncryptBtn);
        radioPanel.add(urlEncryptBtn);
        radioPanel.add(urlDecryptBtn);

        radioPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        JButton cryptBtn = new JButton("Base64加密");
        buttonPanel.add(cryptBtn);
        JTextField passJField = InitialComponent.setJTExtField(null, null, true, BorderFactory.createLoweredBevelBorder());

        Box passBox = new Box(BoxLayout.X_AXIS);

        passJLabel = new JLabel("密码：");

        passBox.add(passJLabel);
        passBox.add(passJField);
        passBox.setBorder(BorderFactory.createLoweredBevelBorder());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(radioPanel, BorderLayout.CENTER);

        this.add(inputPanel, BorderLayout.NORTH);

        this.validate();
        this.repaint();

        base64EncryptBtn.addActionListener(noPassAction(inputPanel, cryptBtn, passBox, "Base64加密"));
        base64DecryptBtn.addActionListener(noPassAction(inputPanel, cryptBtn, passBox, "Base64解密"));
        md5EncryptBtn.addActionListener(noPassAction(inputPanel, cryptBtn, passBox, "MD5加密"));
        aesEncryptBtn.addActionListener(withPassAction(inputPanel, cryptBtn, passBox, "Aes加密"));
        aesDecryptBtn.addActionListener(withPassAction(inputPanel, cryptBtn, passBox, "Aes解密"));
        urlDecryptBtn.addActionListener(withPassAction(inputPanel, cryptBtn, passBox, "URL解密"));
        urlEncryptBtn.addActionListener(withPassAction(inputPanel, cryptBtn, passBox, "URL加密"));

        cryptBtn.addActionListener(cryptBtnActionListener(base64DecryptBtn, base64EncryptBtn, aesDecryptBtn, aesEncryptBtn, md5EncryptBtn, passJField, urlDecryptBtn, urlEncryptBtn));
    }

    /**
     * 将按钮添加到group
     *
     * @param base64EncryptBtn
     * @param aesEncryptBtn
     * @param md5EncryptBtn
     * @param aesDecryptBtn
     * @param base64DecryptBtn
     * @param btnGroup
     * @param urlEncryptBtn
     * @param urlDecryptBtn
     */
    private void addGroupBtn(JRadioButton base64EncryptBtn, JRadioButton aesEncryptBtn, JRadioButton md5EncryptBtn, JRadioButton aesDecryptBtn, JRadioButton base64DecryptBtn, ButtonGroup btnGroup, JRadioButton urlEncryptBtn, JRadioButton urlDecryptBtn) {
        btnGroup.add(base64EncryptBtn);
        btnGroup.add(base64DecryptBtn);
        btnGroup.add(aesEncryptBtn);
        btnGroup.add(aesDecryptBtn);
        btnGroup.add(md5EncryptBtn);
        btnGroup.add(urlEncryptBtn);
        btnGroup.add(urlDecryptBtn);

        base64EncryptBtn.addActionListener(encryptBtnAction());
        base64DecryptBtn.addActionListener(decryptBtnAction());
        aesDecryptBtn.addActionListener(decryptBtnAction());
        urlDecryptBtn.addActionListener(decryptBtnAction());
        aesEncryptBtn.addActionListener(encryptBtnAction());
        md5EncryptBtn.addActionListener(encryptBtnAction());
        urlEncryptBtn.addActionListener(encryptBtnAction());
    }

    /**
     * 点击事件
     * 解密
     * 左边是明文，右边是密文
     *
     * @return
     */
    private ActionListener decryptBtnAction() {
        return e -> inputMsg.setText("");
    }

    /**
     * 点击事件
     * 加密
     * 左边是明文，右边是密文
     *
     * @return
     */
    private ActionListener encryptBtnAction() {
        return e -> outputMsg.setText("");
    }

    /**
     * 点击事件
     *
     * @param base64DecryptBtn
     * @param base64EncryptBtn
     * @param aesDecryptBtn
     * @param aesEncryptBtn
     * @param md5EncryptBtn
     * @param passJField       密码输入框
     * @param urlDecryptBtn    URL解密按钮
     * @param urlEncryptBtn    URL加密按钮
     * @return ActionListener
     */
    private ActionListener cryptBtnActionListener(JRadioButton base64DecryptBtn, JRadioButton base64EncryptBtn, JRadioButton aesDecryptBtn, JRadioButton aesEncryptBtn, JRadioButton md5EncryptBtn, JTextField passJField, JRadioButton urlDecryptBtn, JRadioButton urlEncryptBtn) {
        return e -> {
            //base64解密
            if (base64DecryptBtn.isSelected()) {
                base64Decrypt(outputMsg.getText().trim());
                logger.info("base64解密");
            }
            //base64加密
            if (base64EncryptBtn.isSelected()) {
                base64Encrypt(inputMsg.getText().trim());
                logger.info("base64加密");
            }
            //Aes加密
            if (aesEncryptBtn.isSelected()) {
                aesEncrypt(inputMsg.getText().trim(), passJField.getText().trim());
                logger.info("Aes加密");
            }
            //Aes解密
            if (aesDecryptBtn.isSelected()) {
                aesDecrypt(outputMsg.getText().trim(), passJField.getText().trim());
                logger.info("Aes解密");
            }
            //Md5加密
            if (md5EncryptBtn.isSelected()) {
                md5Encrypt(inputMsg.getText().trim());
                logger.info("Md5加密");
            }
            //URL加密
            if (urlEncryptBtn.isSelected()) {
                urlEncrypt(inputMsg.getText().trim(), passJField.getText().trim());
                logger.info("URL加密");
            }   //URL解密
            if (urlDecryptBtn.isSelected()) {
                urlDecrypt(outputMsg.getText().trim(), passJField.getText().trim());
                logger.info("URL解密");
            }
        };
    }

    /**
     * URL解密
     *
     * @param msg      信息
     * @param encoding 编码
     */
    private void urlDecrypt(String msg, String encoding) {
        if (StringUtils.isEmpty(msg)) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, "右边密文为空");
            return;
        }
        if (StringUtils.isEmpty(encoding)) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, "编码为空");
            return;
        }
        try {
            String value = UrlUtils.urlDecode(msg, encoding);
            if (StringUtils.isNotEmpty(value)) {
                outputMsg.setText(value);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, "不支持的编码：" + e.getMessage());
        }
    }

    /**
     * URL加密
     *
     * @param msg      原文
     * @param encoding 编码
     */
    private void urlEncrypt(String msg, String encoding) {
        if (StringUtils.isEmpty(msg)) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, "左边明文为空");
            return;
        }
        if (StringUtils.isEmpty(encoding)) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, "编码为空");
            return;
        }
        try {
            String value = UrlUtils.urlEncode(msg, encoding);
            if (StringUtils.isNotEmpty(value)) {
                outputMsg.setText(value);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, "不支持的编码：" + e.getMessage());
        }
    }

    /**
     * MD5加密
     *
     * @param encryptMsg 原文
     */
    private void md5Encrypt(String encryptMsg) {
        if (StringUtils.isEmpty(encryptMsg)) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, "左边明文为空");
            return;
        }
        try {
            String value = AesUtils.md5Encrypt(encryptMsg);
            if (StringUtils.isNotEmpty(value)) {
                outputMsg.setText(value);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, e.getMessage());
        }
    }

    /**
     * 解密
     *
     * @param decryptMsg
     * @param passKey
     */
    private void aesDecrypt(String decryptMsg, String passKey) {
        if (StringUtils.isEmpty(decryptMsg)) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, "右边密文为空");
            return;
        }
        if (StringUtils.isEmpty(passKey)) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, "密码为空");
            return;
        }
        if (Constants.NUMBER_SIXTY != passKey.length()) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, "密码没有16位");
            return;
        }
        try {
            String value = AesUtils.decryptByAes(passKey, decryptMsg);
            if (StringUtils.isNotEmpty(value)) {
                inputMsg.setText(value);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, e.getMessage());
        }
    }

    /**
     * aes加密
     *
     * @param encryptMsg 明文
     * @param passKey    密码
     */

    private void aesEncrypt(String encryptMsg, String passKey) {
        if (StringUtils.isEmpty(encryptMsg)) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, "左边明文为空");
            return;
        }
        if (StringUtils.isEmpty(passKey)) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, "密码为空");
            return;
        }
        if (Constants.NUMBER_SIXTY != passKey.length()) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, "密码没有16位");
            return;
        }
        try {
            String value = AesUtils.encryptByAes(passKey, encryptMsg);
            if (StringUtils.isNotEmpty(value)) {
                outputMsg.setText(value);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, e.getMessage());
        }
    }

    private void base64Encrypt(String decryptMsg) {
        if (StringUtils.isEmpty(decryptMsg)) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, "左边明文为空");
        }
        try {
            String value = Base64Tool.encode(decryptMsg);
            if (StringUtils.isNotEmpty(value)) {
                outputMsg.setText(value);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, e.getMessage());
        }
    }

    private void base64Decrypt(String encryptMsg) {
        if (StringUtils.isEmpty(encryptMsg)) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, "右边密文为空");
        }
        try {
            String value = Base64Tool.decode(encryptMsg);
            if (StringUtils.isNotEmpty(value)) {
                inputMsg.setText(value);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, e.getMessage());
        }
    }

    /**
     * 需要密码框
     *
     * @param inputPanel
     * @param cryptBtn
     * @param passBox
     * @param text
     * @return
     */
    private ActionListener withPassAction(JPanel inputPanel, JButton cryptBtn, Box passBox, String text) {
        return e -> {
            inputPanel.add(passBox, BorderLayout.SOUTH);
            cryptBtn.setText(text);
            if (text.contains(URL)) {
                passJLabel.setText("编码：");
            } else {
                passJLabel.setText("AES密码：");
            }
            //对panel1面板中的组件重新布局并绘制
            inputPanel.revalidate();
            //对panel1本身进行重绘
            inputPanel.repaint();
        };
    }

    /**
     * Base64加密
     *
     * @param inputPanel
     * @param cryptBtn
     * @param passBox
     * @param text
     * @return
     */
    private ActionListener noPassAction(JPanel inputPanel, JButton cryptBtn, Box passBox, String text) {
        return e -> {
            inputPanel.remove(passBox);
            cryptBtn.setText(text);
            //对panel1面板中的组件重新布局并绘制
            inputPanel.revalidate();
            //对panel1本身进行重绘
            inputPanel.repaint();
        };
    }

    private ActionListener formatBtnAction() {
        return e -> {
            String origStr = inputMsg.getText().trim();
            try {
                String desStr = FormatData.jsonFormat(origStr);
                outputMsg.setText(desStr);
            } catch (Exception e1) {
                logger.warn("JSON转换异常" + e1);
                JOptionPane.showMessageDialog(DecryptAndEncryptFrame.this, e1.getMessage(), "JSON转换提示框", JOptionPane.WARNING_MESSAGE);
            }
        };
    }
}
