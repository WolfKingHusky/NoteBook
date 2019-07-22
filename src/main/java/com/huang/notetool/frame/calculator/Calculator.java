package com.huang.notetool.frame.calculator;

import javax.swing.*;
import java.awt.*;

/**
 * 此处说明类的作用
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-06-04 13:56:10
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-04   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class Calculator {
    final String[] KEYS = {"CE", "C", "←", "÷", "7", "8", "9", "×", "4", "5", "6", "-", "1", "2", "3", "+", "0", ".", "="};
    JButton[] keys = new JButton[KEYS.length];
    JTextField resultText = new JTextField("0");

    public void addComponentsToPane(Container pane) {
        GridBagLayout layout = new GridBagLayout();
        pane.setLayout(layout);
        resultText.setFont(new Font("Century Schoolbook", Font.PLAIN, 14));
        resultText.setEditable(false);
        resultText.setHorizontalAlignment(SwingConstants.RIGHT);
        pane.add(resultText, new GBC(0, 0, 4, 1).setIpad(400, 50).setWeight(0.5, 0.5).setFill(GridBagConstraints.BOTH));
        for (int i = 0; i < keys.length; i++) {
            keys[i] = new JButton(KEYS[i]);
            if (i == keys.length - 3) {
                pane.add(keys[i], new GBC(i % 4, i / 4 + 1, 2, 1).setIpad(0, 12).setInsets(1).setFill(GridBagConstraints.BOTH).setWeight(0.5, 0.5));
            } else if (i == keys.length - 2 || i == keys.length - 1) {
                pane.add(keys[i], new GBC(i % 4 + 1, i / 4 + 1).setIpad(0, 12).setInsets(1).setFill(GridBagConstraints.BOTH).setWeight(0.5, 0.5));
            } else {
                pane.add(keys[i], new GBC(i % 4, i / 4 + 1).setIpad(0, 12).setInsets(1).setFill(GridBagConstraints.BOTH).setWeight(0.5, 0.5));
            }
        }
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Calculator().createAndShowGUI();
            }
        });
    }
}
