package com.huang.notetool.frame.common;

import com.huang.notetool.util.InitialComponent;

import javax.swing.*;

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
public class JRootParentFrame extends JFrame {
    /**
     * 隐藏Windows默认框
     */
    public JRootParentFrame() {
        if (!InitialComponent.isWindowsLookAndFeel()) {
            //隐藏Windows默认窗体
            //修改Windows窗体都需要这个属性
            this.setUndecorated(true);
            this.getRootPane().setWindowDecorationStyle(JRootPane.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        }
    }
}
