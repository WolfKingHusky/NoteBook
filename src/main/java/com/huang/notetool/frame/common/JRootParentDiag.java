package com.huang.notetool.frame.common;

import com.huang.notetool.util.InitialComponent;
import com.huang.notetool.util.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * 此处说明类的作用
 *
 * @author 黄招荣
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-07-04 17:41:59
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-07-04   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class JRootParentDiag extends JDialog {

    /**
     * 构造方法，初始化弹窗
     *
     * @param jFrame 面板
     * @param text   文本内容
     * @param icon   图标
     * @return JRootParentDiag
     * @author 黄先生
     * @date 2019/7/10 11:12
     */
    public JRootParentDiag(JFrame jFrame, String text, Icon icon) {

        int x = 0;
        int y = 0;
        int jFrameX = 0;
        int jFrameY = 0;
        int width;
        if (null != jFrame) {
            x = jFrame.getX();
            y = jFrame.getY();
            jFrameX = jFrame.getWidth();
            jFrameY = jFrame.getHeight();
        } else {
            //得到屏幕尺寸
            Dimension screen = getToolkit().getScreenSize();
            jFrameX = screen.width;
            jFrameY = screen.height;
        }
        if (StringUtils.isNotEmpty(text)) {
            width = text.length() + 300;
        } else {
            width = 300;
        }
        int height = 130;
        this.setLayout(new GridLayout(2, 1));
        //居中：上层面板的x坐标+（（上层面板的长-弹窗的长）/2）（除2是为了长度前后各取部分，好居中）
        this.setBounds(x + ((jFrameX - width) / 2),
                y + ((jFrameY - height) / 2), width, height);
        //默认窗口也要显示信息
        if (!InitialComponent.isWindowsLookAndFeel()) {
            //隐藏Windows默认窗体
            //修改Windows窗体都需要这个属性
            this.setUndecorated(true);
            this.getRootPane().setWindowDecorationStyle(JRootPane.WHEN_IN_FOCUSED_WINDOW);
            if (jFrame != null) {
                this.setOpacity(jFrame.getOpacity());
            }
        }
        this.setTitle("信息提示框");
        this.setModal(true);
        if (jFrame != null) {
            this.setLocationRelativeTo(jFrame);
        }
        initial(text, icon);
        this.setVisible(true);
    }

    /**
     * 此处说明作用
     *
     * @param text 提示信息
     * @param icon 图标
     * @author 黄先生
     * @date 2019/7/10 14:13
     */
    private void initial(String text, Icon icon) {
        JButton okBtn = new JButton("确定");
        JPanel btnPanel = new JPanel();
        JPanel msgPanel = new JPanel();
        btnPanel.add(okBtn);
        JLabel msgLabel = new JLabel(text);
        if (null != icon) {
            msgLabel.setIcon(icon);
        }
        msgLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
        msgPanel.setLayout(new BorderLayout());
        msgPanel.add(msgLabel, BorderLayout.CENTER);
        okBtn.addActionListener(okBtnAction());
        this.add(msgPanel, BorderLayout.CENTER);
        this.add(btnPanel);
    }

    /**
     * 事件监听
     *
     * @return java.awt.event.ActionListener
     * @author 黄先生
     * @date 2019/7/10 11:31
     */
    private ActionListener okBtnAction() {
        return e -> this.dispose();
    }

}
