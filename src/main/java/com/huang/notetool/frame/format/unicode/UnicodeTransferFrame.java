package com.huang.notetool.frame.format.unicode;

import com.huang.notetool.frame.common.ToolParentFrame;
import com.huang.notetool.tool.FormatData;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * 格式化Unicode显示面板
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-05-25 21:40:44
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-25   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class UnicodeTransferFrame extends ToolParentFrame {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(UnicodeTransferFrame.class);

    public UnicodeTransferFrame(JFrame noteFrame) {
        super(noteFrame);
        logger.info("Unicode转换显示面板...");
        this.setTitle(super.getTitle() + "，中文输出输入在右边");
        JButton unicodeToChinaBtn = new JButton("Unicode转中文");
        JButton chinaToUnicodeBtn = new JButton("中文转Unicode");
        JButton asciiToChinaBtn = new JButton("ASCII转中文");
        JButton chinaToAsciiBtn = new JButton("中文转ASCII");

        buttonPanel.add(unicodeToChinaBtn);
        buttonPanel.add(chinaToUnicodeBtn);
        buttonPanel.add(asciiToChinaBtn);
        buttonPanel.add(chinaToAsciiBtn);

        unicodeToChinaBtn.addActionListener(unicodeToChinaBtnAction());
        chinaToUnicodeBtn.addActionListener(chinaToUnicodeBtnAction());
    }

    /**
     * 中文转unicode
     *
     * @return
     */
    private ActionListener chinaToUnicodeBtnAction() {
        return e -> {
            String origStr = outputMsg.getText().trim();
            try {
                String desStr = FormatData.chinaToUnicode(origStr);
                inputMsg.setText(desStr);
            } catch (Exception e1) {
                logger.warn("Unicode转换异常" + e1);
                JOptionPane.showMessageDialog(UnicodeTransferFrame.this, e1.getMessage(), "Unicode转换提示框", JOptionPane.WARNING_MESSAGE);
            }
        };
    }

    /**
     * unicode转中文
     *
     * @return
     */
    private ActionListener unicodeToChinaBtnAction() {
        return e -> {
            String origStr = inputMsg.getText().trim();
            try {
                String desStr = FormatData.unicodeToChina(origStr);
                outputMsg.setText(desStr);
            } catch (Exception e1) {
                logger.warn("Unicode转换异常" + e1);
                JOptionPane.showMessageDialog(UnicodeTransferFrame.this, e1.getMessage(), "Unicode转换提示框", JOptionPane.WARNING_MESSAGE);
            }
        };
    }
}
