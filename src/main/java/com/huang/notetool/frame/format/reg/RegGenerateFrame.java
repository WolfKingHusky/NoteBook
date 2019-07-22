package com.huang.notetool.frame.format.reg;

import com.huang.notetool.frame.common.ToolParentFrame;
import com.huang.notetool.tool.FormatData;
import com.huang.notetool.util.GridBagInitialTool;
import com.huang.notetool.util.InitialComponent;
import com.huang.notetool.util.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 简单正则生成显示面板
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-05-25 21:40:44
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-25   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class RegGenerateFrame extends ToolParentFrame {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(RegGenerateFrame.class);

    public RegGenerateFrame(JFrame noteFrame) {
        super(noteFrame);
        logger.info("打开简单正则生成显示面板... ");
        //需要匹配的数据
        JTextField regJFiled = InitialComponent.setJTExtField(5, null, true, null);
        //固定不变的数据
        JTextField regNoChangeJField = InitialComponent.setJTExtField(5, null, true, null);
        ;
        String[] withLineChanges = {"匹配数据存在换行", "匹配数据不存在换行"};
        String[] regAlls = {"贪婪模式", "非贪婪模式"};
        JComboBox jComboBox = new JComboBox(withLineChanges);
        jComboBox.setSelectedIndex(1);
        JComboBox regAllJComboBox = new JComboBox(regAlls);
        regAllJComboBox.setSelectedIndex(0);
        Box box = new Box(BoxLayout.X_AXIS);

        box.add(jComboBox);
        box.add(regAllJComboBox);

        JScrollPane jScrollPane = new JScrollPane(inputMsg);

        JPanel inputPanel = new JPanel();
        //设置面板
        setInputJPanel(regJFiled, jScrollPane, inputPanel, regNoChangeJField, box);

        JTextField regOutJFiled = InitialComponent.setJTExtField(null, null, false, null);
        JTextField regSuccessJFile = InitialComponent.setJTExtField(null, null, false, null);
        JScrollPane outJScrollPane = new JScrollPane(outputMsg);

        JPanel outputPanel = new JPanel();
        setOutPanel(regOutJFiled, regSuccessJFile, outJScrollPane, outputPanel);

        msgPanel.removeAll();
        msgPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        msgPanel.setLayout(new GridLayout(1, 2));

        msgPanel.add(inputPanel);
        msgPanel.add(outputPanel);

        JButton regTestBtn = new JButton("生成正则");

        buttonPanel.add(regTestBtn);

        this.validate();
        this.repaint();

        regTestBtn.addActionListener(regSetBtnAction(regJFiled, regOutJFiled, regSuccessJFile, regNoChangeJField, jComboBox, regAllJComboBox));
    }

    private void setOutPanel(JTextField regOutJFile, JTextField regSuccessJFiled, JScrollPane outJScrollPane, JPanel outputPanel) {
        outputPanel.setLayout(new GridBagLayout());
        outputPanel.setBorder(BorderFactory.createEtchedBorder());
        outputPanel.add(new JLabel("匹配结果"), GridBagInitialTool.setGridBagConstraints(GridBagConstraints.NORTH,
                10.0, 10.0,
                0, 0,
                1, 1));
        outputPanel.add(regSuccessJFiled, GridBagInitialTool.setGridBagConstraints(GridBagConstraints.HORIZONTAL,
                90.0, 10.0,
                1, 0,
                1, 9));

        outputPanel.add(new JLabel("正则表达式"), GridBagInitialTool.setGridBagConstraints(GridBagConstraints.NORTH,
                10.0, 10.0,
                0, 1,
                1, 1));
        outputPanel.add(regOutJFile, GridBagInitialTool.setGridBagConstraints(GridBagConstraints.HORIZONTAL,
                90.0, 10.0,
                1, 1,
                1, 9));

        outputPanel.add(new JLabel("匹配到的内容"), GridBagInitialTool.setGridBagConstraints(GridBagConstraints.FIRST_LINE_START,
                10.0, 10.0,
                0, 2,
                1, 2));
        outputPanel.add(outJScrollPane, GridBagInitialTool.setGridBagConstraints(GridBagConstraints.BOTH,
                90.0, 80.0,
                1, 2,
                5, 9));

        outputMsg.setEditable(false);
    }

    /**
     * 设置左边输入数据栏
     *
     * @param regJFile
     * @param jScrollPane
     * @param inputPanel
     * @param noChangeData
     * @param box
     */
    private void setInputJPanel(JTextField regJFile, JScrollPane jScrollPane, JPanel inputPanel, JTextField noChangeData, Box box) {
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEtchedBorder());

        inputPanel.add(new JLabel("需要匹配的内容"), GridBagInitialTool.setGridBagConstraints(GridBagConstraints.NORTH,
                10.0, 10.0,
                0, 0,
                1, 1));
        inputPanel.add(regJFile, GridBagInitialTool.setGridBagConstraints(GridBagConstraints.HORIZONTAL,
                90.0, 10.0,
                1, 0,
                1, 9));

        //一行里面weightX相加为100.0
        inputPanel.add(new JLabel("固定不变的内容"), GridBagInitialTool.setGridBagConstraints(GridBagConstraints.NORTH,
                10.0, 10.0,
                0, 1,
                1, 1));
        inputPanel.add(noChangeData, GridBagInitialTool.setGridBagConstraints(GridBagConstraints.HORIZONTAL,
                90.0, 10.0,
                1, 1,
                1, 4));

        //一行里面weightX相加为100.0
        inputPanel.add(new JLabel("其他需要的选项"), GridBagInitialTool.setGridBagConstraints(GridBagConstraints.NORTH,
                10.0, 10.0,
                0, 2,
                1, 1));
        inputPanel.add(box, GridBagInitialTool.setGridBagConstraints(GridBagConstraints.HORIZONTAL,
                90.0, 10.0,
                1, 2,
                1, 4));

        //gridx 第几列，，gridY第几行
        inputPanel.add(new JLabel("匹配内容"), GridBagInitialTool.setGridBagConstraints(GridBagConstraints.FIRST_LINE_START,
                10.0, 10.0,
                0, 3,
                1, 1));
        inputPanel.add(jScrollPane, GridBagInitialTool.setGridBagConstraints(GridBagConstraints.BOTH,
                90.0, 90.0,
                1, 3,
                9, 9));
    }

    /**
     * 生成正则表达式
     *
     * @param regJFiled
     * @param regOutJFiled
     * @param regSuccessJFile
     * @param regNoChangeJField
     * @param jComboBox
     * @param regAllJComboBox
     * @return
     */
    private ActionListener regSetBtnAction(JTextField regJFiled, JTextField regOutJFiled, JTextField regSuccessJFile, JTextField regNoChangeJField, JComboBox jComboBox, JComboBox regAllJComboBox) {
        return e -> {
            //匹配内容
            String origStr = inputMsg.getText().trim();
            //需要匹配的数据
            String regContent = regJFiled.getText().trim();
            //固定不变的内容
            String regNoChangeContent = regNoChangeJField.getText().trim();
            if (StringUtils.isEmpty(regContent)) {
                JOptionPane.showMessageDialog(RegGenerateFrame.this, "需要匹配的内容为空", "正则生成提示框", JOptionPane.WARNING_MESSAGE);
                regJFiled.requestFocus();
                return;
            }
            if (StringUtils.isEmpty(origStr)) {
                JOptionPane.showMessageDialog(RegGenerateFrame.this, "匹配字符串为空", "正则生成提示框", JOptionPane.WARNING_MESSAGE);
                inputMsg.requestFocus();
                return;
            }
            if (!regContent.contains(regNoChangeContent)) {
                JOptionPane.showMessageDialog(RegGenerateFrame.this, "固定不变的内容不存在需要匹配的内容里面", "正则生成提示框", JOptionPane.WARNING_MESSAGE);
                inputMsg.requestFocus();
                return;
            }
            try {
                String[] returnValue = FormatData.regSet(regContent, regNoChangeContent, jComboBox.getSelectedItem(), regAllJComboBox.getSelectedItem(), origStr);
                regOutJFiled.setText(returnValue[0]);
                outputMsg.setText(returnValue[1]);
            } catch (Exception e1) {
                logger.warn("正则测试异常" + e1);
                JOptionPane.showMessageDialog(RegGenerateFrame.this, e1.getMessage(), "正则测试提示框", JOptionPane.WARNING_MESSAGE);
            }
        };
    }
}
