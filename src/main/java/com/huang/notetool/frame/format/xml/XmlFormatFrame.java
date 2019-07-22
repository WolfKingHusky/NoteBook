package com.huang.notetool.frame.format.xml;

import com.huang.notetool.frame.common.ShowMsgSinglePanel;
import com.huang.notetool.frame.common.ToolParentFrame;
import com.huang.notetool.tool.FormatData;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * 格式化XML显示面板
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-05-25 21:40:44
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-25   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class XmlFormatFrame extends ToolParentFrame {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(XmlFormatFrame.class);

    public XmlFormatFrame(JFrame jFrame) {
        super(jFrame);
        setTitle("XML 格式化，" + super.getTitle());
        logger.info("格式化XML显示面板");
        JButton formatBtn = new JButton("格式化");

        buttonPanel.add(formatBtn);

        formatBtn.addActionListener(formatBtnAction(jFrame));
    }

    private ActionListener formatBtnAction(JFrame jFrame) {
        return e -> {
            String origStr = inputMsg.getText().trim();
            try {
                String desStr = FormatData.formatXml(origStr);
                outputMsg.setText(desStr);
            } catch (Exception e1) {
                logger.warn("XML转换异常" + e1);
                if (e1.getMessage().length() < 100) {
                    JOptionPane.showMessageDialog(XmlFormatFrame.this, e1.getMessage(), "XML转换提示框", JOptionPane.WARNING_MESSAGE);
                } else {
                    new ShowMsgSinglePanel(e1.getMessage(), jFrame);
                }
            }
        };
    }
}
