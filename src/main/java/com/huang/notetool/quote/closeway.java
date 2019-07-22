package com.huang.notetool.quote;

import com.huang.notetool.frame.progress.ShowProcess;

import javax.swing.*;
import java.io.IOException;

public class closeway {
    public closeway() {
        JOptionPane.showMessageDialog(null, "部分未收录，只需要输入路径下的.exe文件名字即可");
        String close1 = JOptionPane.showInputDialog(null, "关闭程序名字(不含.exe):", "关闭", JOptionPane.PLAIN_MESSAGE);
        if (close1 == null) {
            close1 = "";
        }
        if (close1.length() != 0) {
            if (close1.equalsIgnoreCase("酷狗") || close1.equalsIgnoreCase("酷狗音乐") || close1.equalsIgnoreCase("KuGou")) {
                close1 = "KuGou";
            } else if (close1.equalsIgnoreCase("lol") || close1.equalsIgnoreCase("英雄联盟") || close1.equalsIgnoreCase("Client")) {
                close1 = "Client";
            } else if (close1.equalsIgnoreCase("乐视") || close1.equalsIgnoreCase("乐视TV") || close1.equalsIgnoreCase("乐视视频") || close1.equalsIgnoreCase("LeTVLoader") || close1.equalsIgnoreCase("Letv")) {
                close1 = "LeTVLoader";
            } else if (close1.equalsIgnoreCase("vc++") || close1.equalsIgnoreCase("c/c++编译器") || close1.equalsIgnoreCase("c++编译器") || close1.equalsIgnoreCase("c编译器") || close1.equalsIgnoreCase("MSDEV") || close1.equalsIgnoreCase("Microsoft Visual C++")) {
                close1 = "MSDEV";
            } else if (close1.equalsIgnoreCase("java") || close1.equalsIgnoreCase("Java编译器") || close1.equalsIgnoreCase("MyEclipse 10") || close1.equalsIgnoreCase("MyEclipse") || close1.equalsIgnoreCase("myeclipse") || close1.equalsIgnoreCase("javaw")) {
                try {
                    Runtime.getRuntime().exec("taskkill /F /IM myeclipse.exe");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                close1 = "javaw";
            } else if (close1.equalsIgnoreCase("qq") || close1.equalsIgnoreCase("腾讯qq") || close1.equalsIgnoreCase("QQApp")) {
                close1 = "QQ";
            } else if (close1.equalsIgnoreCase("搜狗") || close1.equalsIgnoreCase("搜狗浏览器") || close1.equalsIgnoreCase("搜狗高速浏览器") || close1.equalsIgnoreCase("SogouExplorer")) {
                close1 = "SogouExplorer";
            } else if (close1.equalsIgnoreCase("wps") || close1.equalsIgnoreCase("wps文字")) {
                close1 = "wps";
            } else if (close1.equalsIgnoreCase("et") || close1.equalsIgnoreCase("wps表格") || close1.equalsIgnoreCase("w表格")) {
                close1 = "et";
            } else if (close1.equalsIgnoreCase("wps演示") || close1.equalsIgnoreCase("wpp") || close1.equalsIgnoreCase("ppt")) {
                close1 = "wpp";
            } else if (close1.equalsIgnoreCase("迅雷") || close1.equalsIgnoreCase("迅雷下载") || close1.equalsIgnoreCase("迅雷极速版") || close1.equalsIgnoreCase("Thunder")) {
                close1 = "Thunder";
            } else if (close1.equalsIgnoreCase("youku") || close1.equalsIgnoreCase("优酷") || close1.equalsIgnoreCase("YoukuDesktop.exe") || close1.equalsIgnoreCase("youkupage.exe")) {
                close1 = "YoukuDesktop";
            }

            try {
                Runtime.getRuntime().exec("taskkill /F /IM " + close1 + ".exe");
            } catch (IOException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "关闭程序失败", "error", JOptionPane.CANCEL_OPTION);  //显示消息
            }
        } else {
            JOptionPane.showMessageDialog(null, "关闭程序失败", "error", JOptionPane.CANCEL_OPTION);  //显示消息
        }
        if (close1.length() != 0) {
            int hpath = JOptionPane.showConfirmDialog(null, "是否查看当前进程？", "提示", 0);
            if (hpath == 0) {
                new ShowProcess(null);
            }
        }
    }
}