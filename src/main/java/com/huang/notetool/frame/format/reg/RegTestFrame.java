package com.huang.notetool.frame.format.reg;

import com.huang.notetool.frame.common.ToolParentFrame;
import com.huang.notetool.tool.FormatData;
import com.huang.notetool.util.InitialComponent;
import com.huang.notetool.util.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 正则测试显示面板
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-05-25 21:40:44
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-25   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class RegTestFrame extends ToolParentFrame {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(RegTestFrame.class);

    public RegTestFrame(JFrame noteFrame) {
        super(noteFrame);
        logger.info("正则测试显示面板... ");
        JTextField regJFiled = InitialComponent.setJTExtField(5, null, true, null);
        JTextField contentFiled = new JTextField();
        JScrollPane jScrollPane = new JScrollPane(inputMsg);

        JPanel inputPanel = new JPanel();
        //设置面板
        setInputJPanel(regJFiled, jScrollPane, inputPanel);

        JTextField regOutJFiled = new JTextField();
        JTextField regSuccessJFile = new JTextField();
        JScrollPane outJScrollPane = new JScrollPane(outputMsg);

        JPanel outputPanel = new JPanel();
        setOutPanel(regOutJFiled, regSuccessJFile, outJScrollPane, outputPanel);

        msgPanel.removeAll();
        msgPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        msgPanel.setLayout(new GridLayout(1, 2));

        msgPanel.add(inputPanel);
        msgPanel.add(outputPanel);

        JButton regTestBtn = new JButton("测试");

        buttonPanel.add(regTestBtn);

        this.validate();
        this.repaint();

        regTestBtn.addActionListener(regTestBtnAction(regJFiled, regOutJFiled, regSuccessJFile));
    }

    private void setOutPanel(JTextField regOutJFile, JTextField regSuccessJFiled, JScrollPane outJScrollPane, JPanel outputPanel) {
        outputPanel.setLayout(new GridBagLayout());
        outputPanel.setBorder(BorderFactory.createEtchedBorder());

        outputPanel.add(new JLabel("匹配结果"), setGridBagConstraints(GridBagConstraints.NORTH,
                10.0, 10.0,
                0, 0,
                1, 1));
        outputPanel.add(regSuccessJFiled, setGridBagConstraints(GridBagConstraints.HORIZONTAL,
                90.0, 10.0,
                1, 0,
                1, 9));

        outputPanel.add(new JLabel("正则表达式"), setGridBagConstraints(GridBagConstraints.NORTH,
                10.0, 10.0,
                0, 1,
                1, 1));
        outputPanel.add(regOutJFile, setGridBagConstraints(GridBagConstraints.HORIZONTAL,
                90.0, 10.0,
                1, 1,
                1, 9));

        outputPanel.add(new JLabel("匹配到的内容"), setGridBagConstraints(GridBagConstraints.FIRST_LINE_START,
                10.0, 10.0,
                0, 2,
                1, 2));
        outputPanel.add(outJScrollPane, setGridBagConstraints(GridBagConstraints.BOTH,
                90.0, 80.0,
                1, 2,
                5, 9));

        regSuccessJFiled.setEditable(false);

        regOutJFile.setEditable(false);

        outputMsg.setEditable(false);
    }

    private void setInputJPanel(JTextField regJFile, JScrollPane jScrollPane, JPanel inputPanel) {
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEtchedBorder());

        inputPanel.add(new JLabel("正则表达式"), setGridBagConstraints(GridBagConstraints.NORTH,
                10.0, 10.0,
                0, 0,
                1, 1));
        inputPanel.add(regJFile, setGridBagConstraints(GridBagConstraints.HORIZONTAL,
                90.0, 10.0,
                1, 0,
                1, 9));
        inputPanel.add(new JLabel("匹配内容"), setGridBagConstraints(GridBagConstraints.FIRST_LINE_START,
                10.0, 10.0,
                0, 1,
                1, 1));
        inputPanel.add(jScrollPane, setGridBagConstraints(GridBagConstraints.BOTH,
                90.0, 90.0,
                1, 1,
                9, 9));
    }

    /**
     * @param fill       设置填充方式
     * @param weightX    列 90.0=90%
     * @param weightY    行
     * @param gridX      第几列
     * @param gridY      第几行
     * @param gridHeight 占用多少行
     * @param gridWidth  占用多少列
     * @return
     */
    private GridBagConstraints setGridBagConstraints(int fill, double weightX, double weightY, int gridX, int gridY, int gridHeight, int gridWidth) {
        // 创建网格组布局约束条件
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        // 设置控件的空白
        gridBagConstraints.insets = new Insets(0, 0, 5, 5);
        // 设置填充方式  GridBagConstraints.HORIZONTAL
        gridBagConstraints.fill = fill;
        // 第一行的分布方式为10.0=10%
        gridBagConstraints.weightx = weightX;
        // 第一列的分布方式为10.0=10%
        gridBagConstraints.weighty = weightY;
        // 起始点为0=第1行
        gridBagConstraints.gridx = gridX;
        // 占用4=4行
        gridBagConstraints.gridheight = gridHeight;
        // 占用4=4列
        gridBagConstraints.gridwidth = gridWidth;
        // 起始点为第0=1行
        gridBagConstraints.gridy = gridY;
        return gridBagConstraints;
    }

    private ActionListener regTestBtnAction(JTextField regJFiled, JTextField regOutJFiled, JTextField regSuccessJFile) {
        return e -> {
            String origStr = inputMsg.getText().trim();
            String reg = regJFiled.getText().trim();
            if (StringUtils.isEmpty(reg)) {
                JOptionPane.showMessageDialog(RegTestFrame.this, "正则表达式为空", "正则测试提示框", JOptionPane.WARNING_MESSAGE);
                regJFiled.requestFocus();
                return;
            }
            if (StringUtils.isEmpty(origStr)) {
                JOptionPane.showMessageDialog(RegTestFrame.this, "匹配字符串为空", "正则测试提示框", JOptionPane.WARNING_MESSAGE);
                inputMsg.requestFocus();
                return;
            }
            try {
                String[] returnValue = FormatData.regTest(reg, origStr);
                regSuccessJFile.setText(returnValue[0]);
                if (returnValue[0].contains("匹配成功")) {
                    regOutJFiled.setText(returnValue[1]);
                    outputMsg.setText(returnValue[2]);
                } else {
                    regOutJFiled.setText("");
                    outputMsg.setText("");
                }
            } catch (Exception e1) {
                logger.warn("正则测试异常" + e1);
                JOptionPane.showMessageDialog(RegTestFrame.this, e1.getMessage(), "正则测试提示框", JOptionPane.WARNING_MESSAGE);
            }
        };
    }
}
