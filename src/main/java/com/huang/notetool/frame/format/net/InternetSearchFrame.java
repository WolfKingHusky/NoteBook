package com.huang.notetool.frame.format.net;

import com.huang.notetool.util.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.net.URLEncoder;

/**
 * 网络搜索
 *
 * @author 黄
 */
public class InternetSearchFrame {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(InternetSearchFrame.class);

    public InternetSearchFrame(JFrame jFrame) {
        logger.info("进入网络搜索... ");
        String searchMsg = JOptionPane.showInputDialog(jFrame, "搜索内容：", "网络搜索", JOptionPane.PLAIN_MESSAGE);
        if (null != searchMsg && StringUtils.isEmpty(searchMsg)) {
            JOptionPane.showMessageDialog(null, "没有输入");
        } else if (StringUtils.isNotEmpty(searchMsg)) {
            if (java.awt.Desktop.isDesktopSupported()) {
                try {
                    //如果代码采用java 来访问请求的话，可以采用 URLEncoder.encodeBufferImag(param,"utf-8") 将参数值编码，对应的解码方法为URLEncoder.decode()
                    //创建一个URI实例,注意不是URL
                    java.net.URI uri = java.net.URI.create("http://www.baidu.com/s?wd=" + URLEncoder.encode(searchMsg, "utf-8"));
                    //获取当前系统桌面扩展
                    java.awt.Desktop dp = java.awt.Desktop.getDesktop();
                    //判断系统桌面是否支持要执行的功能
                    if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                        //获取系统默认浏览器打开链接
                        dp.browse(uri);
                    }
                } catch (NullPointerException e) {
                    logger.warn(e);
                    //此为uri为空时抛出异常
                } catch (java.io.IOException e) {
                    logger.warn(e);
                    //此为无法获取系统默认浏览器
                    JOptionPane.showMessageDialog(jFrame, "无法获取系统默认浏览器");
                }
            }
        }
    }
}
