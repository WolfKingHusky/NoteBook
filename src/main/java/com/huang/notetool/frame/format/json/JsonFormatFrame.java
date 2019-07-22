package com.huang.notetool.frame.format.json;

import com.huang.notetool.frame.common.ShowMsgSinglePanel;
import com.huang.notetool.frame.common.ToolParentFrame;
import com.huang.notetool.tool.FormatData;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * 格式化JSON显示面板
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-05-25 21:40:44
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-25   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class JsonFormatFrame extends ToolParentFrame {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(ToolParentFrame.class);

    public JsonFormatFrame(JFrame jFrame) {
        super(jFrame);
        setTitle("JSON 格式化，" + super.getTitle());
        logger.info("进入格式化JSON显示面板... ");
        JButton transferBtn = new JButton("格式化");

        buttonPanel.add(transferBtn);

        this.validate();
        this.repaint();

        transferBtn.addActionListener(transferBtnAction(jFrame));
    }

    /**
     * 转换方法
     *
     * @param jFrame 面板
     * @return ActionListener
     */
    private ActionListener transferBtnAction(JFrame jFrame) {
        return e -> {
            String origStr = inputMsg.getText().trim();
            try {
                String desStr = FormatData.jsonFormat(origStr);
                outputMsg.setText(desStr);
            } catch (Exception e1) {
                logger.warn("JSON转换异常" + e1);
                if (e1.getMessage().length() < 100) {
                    JOptionPane.showMessageDialog(JsonFormatFrame.this, e1.getMessage(), "JSON转换提示框", JOptionPane.WARNING_MESSAGE);
                } else {
                    new ShowMsgSinglePanel(e1.getMessage(), jFrame);
                }
            }
        };
    }
}
