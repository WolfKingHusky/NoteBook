package com.huang.notetool.util;

import java.awt.*;

/**
 * GridBag布局设置
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-06-25 09:48:12
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-25   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class GridBagInitialTool {
    /**
     * @param fill       设置填充方式
     * @param weightX    列 90.0=90%
     * @param weightY    行
     * @param gridX      第几列
     * @param gridY      第几行
     * @param gridHeight 占用多少行
     * @param gridWidth  占用多少列
     * @return GridBagConstraints
     */
    public static GridBagConstraints setGridBagConstraints(int fill, double weightX, double weightY, int gridX, int gridY, int gridHeight, int gridWidth) {
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
}
