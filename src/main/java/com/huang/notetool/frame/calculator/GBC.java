package com.huang.notetool.frame.calculator;

import java.awt.*;

/**
 * 此处说明类的作用
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-06-04 13:57:26
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-04   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class GBC extends GridBagConstraints {
    /**
     * 构造函数，用来设置组件所占单元格的坐标
     *
     * @param gridx 横向坐标
     * @param gridy 纵向坐标
     */
    public GBC(int gridx, int gridy) {
        this.gridx = gridx;
        this.gridy = gridy;
    }

    /**
     * 构造函数，用来设置组件所占单元格的坐标，同时指定宽度和高度
     *
     * @param gridx      横向坐标
     * @param gridy      纵向坐标
     * @param gridwidth  组件宽度
     * @param gridheight 组件高度
     */
    public GBC(int gridx, int gridy, int gridwidth, int gridheight) {
        this.gridx = gridx;
        this.gridy = gridy;
        this.gridwidth = gridwidth;
        this.gridheight = gridheight;
    }

    /**
     * 当组件没有空间大时，设置组件的位置
     *
     * @param anchor 组建位置
     * @return 当前对象
     */
    public GBC setAnchor(int anchor) {
        this.anchor = anchor;
        return this;
    }

    /**
     * 设置填充
     *
     * @param fill 设置填充方式
     * @return 当前对象
     */
    public GBC setFill(int fill) {
        this.fill = fill;
        return this;
    }

    /**
     * 设置组件随窗口延伸的幅度
     *
     * @param weightx 横向延伸大小
     * @param weighty 纵向延伸大小
     * @return 当前对象
     */
    public GBC setWeight(double weightx, double weighty) {
        this.weightx = weightx;
        this.weighty = weighty;
        return this;
    }

    /**
     * 设置组件之间的间距
     *
     * @param distance 上下左右间距一样
     * @return 当前对象
     */
    public GBC setInsets(int distance) {
        this.insets = new Insets(distance, distance, distance, distance);
        return this;
    }

    /**
     * 设置组件之间的间距
     *
     * @param top    上间距
     * @param left   左间距
     * @param bottom 底间距
     * @param right  右间距
     * @return 当前对象
     */
    public GBC setInsets(int top, int left, int bottom, int right) {
        this.insets = new Insets(top, left, bottom, right);
        return this;
    }

    /**
     * 设置组件内部填充空间，即给组件的最小高度或宽度添加多大的空间
     *
     * @param ipadx 横向填充
     * @param ipady 纵向填充
     * @return 当前对象
     */
    public GBC setIpad(int ipadx, int ipady) {
        this.ipadx = ipadx;
        this.ipady = ipady;
        return this;
    }
}
