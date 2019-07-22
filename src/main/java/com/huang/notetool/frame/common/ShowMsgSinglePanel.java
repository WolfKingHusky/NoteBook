package com.huang.notetool.frame.common;

import com.huang.notetool.util.InitialComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 显示信息面板
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-06-27 09:36:52
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-27   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class ShowMsgSinglePanel extends JDialog {
    /**
     * 构造方法，显示信息
     *
     * @param msg    信息
     * @param jFrame 基于面板
     */
    public ShowMsgSinglePanel(String msg, JFrame jFrame) {
        //非Windows主题才能设置透明度
        if (!InitialComponent.isWindowsLookAndFeel()) {
            this.setUndecorated(true);
            this.getRootPane().setWindowDecorationStyle(JRootPane.WHEN_IN_FOCUSED_WINDOW);
            this.setOpacity(jFrame.getOpacity());
        }
        this.setCursor(jFrame.getCursor());
        this.setModal(true);
        int x = jFrame.getX();
        int y = jFrame.getY();
        int width = jFrame.getWidth() * 3 / 5;
        int height = jFrame.getHeight() * 3 / 5;
        //居中：上层面板的x坐标+（（上层面板的长-弹窗的长）/2）（除2是为了长度前后各取部分，好居中）
        this.setBounds(x + ((jFrame.getWidth() - width) / 2), y + ((jFrame.getHeight() - height) / 2), width, height);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JButton okBtn = new JButton("确定");
        okBtn.addActionListener(okBtnActionListener());
        JTextArea jTextArea = InitialComponent.setJTextArea(null, null
                , false, null, msg, true);
        JScrollPane jScrollPane = new JScrollPane(jTextArea);

        this.add(okBtn, BorderLayout.SOUTH);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.setResizable(false);
        this.setTitle(" 信息提示框");
        //设置光标在最前面
        jTextArea.setCaretPosition(0);
        this.setVisible(true);
    }

    /**
     * 点击按钮监听事件
     *
     * @return ActionListener
     */
    private ActionListener okBtnActionListener() {
        return e -> this.dispose();
    }

}
