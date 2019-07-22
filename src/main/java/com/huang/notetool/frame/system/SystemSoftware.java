package com.huang.notetool.frame.system;

import com.huang.notetool.frame.common.JRootParentFrame;
import com.huang.notetool.frame.common.MsgTableFrame;
import com.huang.notetool.po.SystemInstallMsg;
import com.huang.notetool.service.SystemInstallMsgService;
import com.huang.notetool.tool.ThreadPool;
import com.huang.notetool.util.SystemInstallMsgUtil;
import com.huang.notetool.util.ToolUtil;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Java获取系统软件安装列表, 代码核心来自网上, 主要通过Runtime实现,
 *
 * @author huang
 * 用JNI也行,解决乱码问题
 */
public class SystemSoftware extends JRootParentFrame implements Runnable {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(SystemSoftware.class);
    /**
     * 线程停止
     */
    private boolean exit;
    /**
     * Service
     */
    private SystemInstallMsgService systemInstallMsgService;
//    process = runtime
//    .exec("cmd /c reg query HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\");
//                .exec("cmd /c reg query HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\");
//                .exec("cmd /c reg query HKEY_CURRENT_USER\\SOFTWARE\\");
//                //查询多张注册表
//                .exec("cmd /c reg query HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\ && reg query HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\");
//                .exec("cmd /c reg query HKEY_LOCAL_MACHINE\\SOFTWARE\\ && reg query HKEY_CURRENT_USER\\SOFTWARE\\");


    private JTextPane textPane = new JTextPane();
    /**
     * 获取面板控件
     */
    private MsgTableFrame myTable = new MsgTableFrame();

    public SystemSoftware() {
        systemInstallMsgService = new SystemInstallMsgService();
    }

    public SystemSoftware(String ignored) {
        systemInstallMsgService = new SystemInstallMsgService();
        this.setTitle("本系统已经安装的软件列表");
        logger.info("查看本系统已经安装的软件列表");
        this.setLocation(300, 200);
        this.setSize(800, 500);
        Object[] headerArr = {"软件名称", "版本号", "出版商", "安装路径", "安装时间", "卸载路径"};
        myTable.initial(headerArr, new Object[50][6]);
        JScrollPane jScrollPane = new JScrollPane(myTable.initialTable(null));
        this.add(jScrollPane);
        this.setVisible(true);
        //Windows界面监听器
        //可以监听关闭事件
        this.addWindowListener(windowsListener());
        try {
            check();
        } catch (Exception e) {
            logger.warn(e);
        }
    }


    private void check() throws Exception {
        textPane.setText("您已经安装的软件：");
        //64位
        String cmd64Str = ToolUtil.CMD_QUERY_REG_64_STR;
        //32位
        String cmd32Str = ToolUtil.CMD_QUERY_REG_32_STR;
        //64位
        getEXEMsg(cmd64Str);
        //32位
        getEXEMsg(cmd32Str);

    }

    private void getEXEMsg(String cmdStr) {
        BufferedReader in = null;
        Process process = null;
        InputStreamReader inputStreamReader = null;
        List<SystemInstallMsg> systemInstallMsgList = new ArrayList<>();
        try {
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec(cmdStr);

            //HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\Microsoft\Windows\CurrentVersion\Uninstall
            inputStreamReader = new InputStreamReader(process.getInputStream(), "GBK");
            in = new BufferedReader(inputStreamReader);
            String valueString;

            while ((valueString = in.readLine()) != null) {
                process = runtime.exec("cmd /c reg query "
                        + valueString
                        + " /v DisplayName");
                String[] message = SystemInstallMsgUtil.queryValue(valueString);
                if (message != null) {
                    myTable.addRow(message, 6);
                    SystemInstallMsgUtil.generateMsg(message, systemInstallMsgList);
                }
                this.repaint();
            }
            if (!systemInstallMsgList.isEmpty()) {
                systemInstallMsgService.updateException(systemInstallMsgList);
            }
        } catch (IOException e) {
            logger.warn(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (IOException e) {
                logger.warn(e);
            }
        }
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        new SystemSoftware(null);
    }

    /**
     * 监听Windows事件
     *
     * @return WindowListener
     */
    private WindowListener windowsListener() {
        return new WindowListener() {

            /**
             * Invoked the first time a window is made visible.
             *
             * @param e WindowEvent
             */
            @Override
            public void windowOpened(WindowEvent e) {
            }

            /**
             * Invoked when the user attempts to close the window
             * from the window's system menu.
             *
             * @param e WindowEvent
             */
            @Override
            public void windowClosing(WindowEvent e) {
                ThreadPool.shutDownThread(Thread.currentThread());
                dispose();
            }

            /**
             * Invoked when a window has been closed as the result
             * of calling dispose on the window.
             *
             * @param e WindowEvent
             */
            @Override
            public void windowClosed(WindowEvent e) {
//                exit = true;
//                ThreadPool.shutDownThread(Thread.currentThread());
//                dispose();
            }

            /**
             * Invoked when a window is changed from a normal to a
             * minimized state. For many platforms, a minimized window
             * is displayed as the icon specified in the window's
             * iconImage property.
             *
             * @param e
             * @see Frame#setIconImage
             */
            @Override
            public void windowIconified(WindowEvent e) {

            }

            /**
             * Invoked when a window is changed from a minimized
             * to a normal state.
             *
             * @param e
             */
            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            /**
             * Invoked when the Window is set to be the active Window. Only a Frame or
             * a Dialog can be the active Window. The native windowing system may
             * denote the active Window or its children with special decorations, such
             * as a highlighted title bar. The active Window is always either the
             * focused Window, or the first Frame or Dialog that is an owner of the
             * focused Window.
             *
             * @param e
             */
            @Override
            public void windowActivated(WindowEvent e) {

            }

            /**
             * Invoked when a Window is no longer the active Window. Only a Frame or a
             * Dialog can be the active Window. The native windowing system may denote
             * the active Window or its children with special decorations, such as a
             * highlighted title bar. The active Window is always either the focused
             * Window, or the first Frame or Dialog that is an owner of the focused
             * Window.
             *
             * @param e
             */
            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        };
    }
}