package com.huang.notetool.util;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * 读取Icon图片
 *
 * @author 黄先生
 * @version 1.0
 * @date 2019-07-10 13:37:51
 * Content 此处说明类的解释
 * History   序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-07-10   创建项目           完成
 * LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class IconRead {
    /**
     * 日志信息
     */
    private static Logger logger = Logger.getLogger(IconRead.class);

    /**
     * 根据图片名称读取图片
     *
     * @return java.awt.Image
     * @author 黄先生
     * @date 2019/7/10 13:55
     */
    public static Image getImage(String imageName) {
        //得到图像
        Image image;
        try {
            URL imageURL = IconRead.class.getResource(imageName);
            if (null != imageURL) {
                image = ImageIO.read(imageURL);
                return image;
            } else {
                logger.warn("没有找到图片");
            }
        } catch (IOException e) {
            logger.warn("读取图片失败:" + e);
        }
        return null;
    }

    /**
     * 此处说明作用
     *
     * @param imageName 图片名称
     * @param height    高度
     * @param width     宽度
     * @return javax.swing.Icon
     * @author 黄先生
     * @date 2019/7/10 14:31
     */
    public static Icon getIconFromImage(String imageName, int width, int height) {
        Image image = getImage(imageName);
        if (null != image) {
            image = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            return new ImageIcon(image);
        }
        return null;
    }
}
