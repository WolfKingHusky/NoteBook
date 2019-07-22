package com.huang.notetool.tool;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图片转换类
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-07-02 12:39:20
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-07-02   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class ImageTool {
    public static BufferedImage imageToBufferedImage(Image image) {
        try {
            Frame frame = new Frame();
            frame.addNotify();
            // frame acts as an ImageObserver
            MediaTracker mt = new MediaTracker(frame);
            mt.addImage(image, 0);
            mt.waitForAll();
            int w = image.getWidth(frame);
            int h = image.getHeight(frame);
            BufferedImage buf = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics gc = buf.createGraphics();
            gc.drawImage(image, 0, 0, frame);
            return buf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
