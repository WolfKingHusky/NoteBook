package com.huang.notetool.frame.progress;

import com.huang.notetool.frame.common.JRootParentFrame;
import com.huang.notetool.frame.start.Note;
import com.huang.notetool.tool.ThreadPool;
import com.huang.notetool.util.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 显示系统进程信息
 *
 * @author huang
 * @date 2019-05-08
 */
public class ShowProcess extends JRootParentFrame implements Runnable {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(ShowProcess.class);

    /**
     * 是否退出
     */
    private boolean isExit;

    public ShowProcess(Note jFrame) {
    }

    /**
     * 初始化面板
     */
    private ShowProcess() {
        super();
        logger.info("查看进程信息");
        setLayout(new BorderLayout());
        //得到屏幕尺寸
        Dimension screen = getToolkit().getScreenSize();

        setSize(500, 400);
        //设置窗口位置
        setLocation((screen.width - getSize().width) / 2, (screen.height - getSize().height) / 2);
        //Windows界面监听器
        this.addWindowListener(windowsListener());
        //此处有循环，需要放到初始换的最后面
        initial();
    }

    /**
     * 初始化其他组件
     */
    private void initial() {
        //进程信息
        String[] progressTaskListArray = getSystemProgressMsg();

        JList progressJList = new JList(progressTaskListArray);

        JScrollPane jsp = new JScrollPane(progressJList);
        //设置边界
        jsp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(jsp, "Center");
        //设置窗口可视
        setVisible(true);
        JOptionPane.showMessageDialog(null, "双击可以关闭进程，注意不要关闭电脑核心进程");
        //监听事件
        progressJList.addMouseListener(progressClickListener(progressJList));

        isExit = true;

        refreshJList(progressJList);

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
                isExit = false;
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
                isExit = false;
                dispose();
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

    private String[] getSystemProgressMsg() {
        //进程列表字符串，通过\n分隔
        StringBuilder progressListStr = new StringBuilder();
        //获取任务列表
        getTaskListProgress(progressListStr);

        return progressListStr.toString().split("\n");
    }

    /**
     * 刷新界面
     */
    private void refreshJList(JList progressJList) {
        while (isExit) {
            try {
                Thread.sleep(2000);
                logger.info("刷新进程");
                getNewProgressMsg(progressJList);
            } catch (InterruptedException e) {
                logger.warn(e);
            }
        }
    }

    /**
     * 获取进程信息并且刷新到界面
     *
     * @param progressJList 进程列表
     */
    private void getNewProgressMsg(JList progressJList) {
        progressJList.removeAll();
        //进程信息
        String[] progressTaskListArray = getSystemProgressMsg();

        progressJList.setListData(progressTaskListArray);
        progressJList.validate();
        progressJList.repaint();
    }

    /**
     * 点击事件
     *
     * @param progressJList 进程列表
     * @return MouseAdapter
     */
    private MouseAdapter progressClickListener(JList progressJList) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (progressJList.getSelectedIndex() != -1) {
                    if (mouseEvent.getClickCount() == 2) {
                        twoClick(mouseEvent, progressJList);
                    }
                }
            }

            void twoClick(MouseEvent mouseEvent, JList progressJList) {
                JList theList = (JList) mouseEvent.getSource();
                int index = theList.locationToIndex(mouseEvent.getPoint());
                if (index >= 0) {
                    String progressMsg = (String) theList.getModel().getElementAt(index);
                    if (StringUtils.isNotEmpty(progressMsg)) {
                        String progressName = progressMsg.replaceAll("\n", "").substring(0, progressMsg.lastIndexOf(".exe") + 4);
                        String progressId = progressMsg.replaceAll("\n", "").substring(progressMsg.lastIndexOf(",") + 1).trim();
                        int isSureCloseP = JOptionPane.showConfirmDialog(ShowProcess.this, "您正在关闭" + progressName, "提示", 0);
                        if (isSureCloseP == 0) {
                            isSureCloseP = JOptionPane.showConfirmDialog(ShowProcess.this, "是否关闭所有 " + progressName + " 进程", "提示", 0);
                            try {
                                if (isSureCloseP == 0) {
                                    //通过名称关闭
                                    Runtime.getRuntime().exec("taskkill /F /IM " + progressName);
                                } else if (isSureCloseP == 1) {
                                    //通过PID关闭
                                    Runtime.getRuntime().exec("taskkill /F /PID  " + progressId);
                                }
                                getNewProgressMsg(progressJList);
                            } catch (IOException e1) {
                                logger.warn(e1);
                                //显示消息
                                JOptionPane.showMessageDialog(ShowProcess.this, "关闭程序失败", "提示信息", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
                }
            }
        };
    }

    /**
     * 取得进程列表
     *
     * @param progressListStr 进程列表字符串，通过\n分隔
     */
    private void getTaskListProgress(StringBuilder progressListStr) {
        BufferedReader input = null;
        InputStreamReader inputStreamReader = null;
        try {
            //命令行获取进程
            Process process = Runtime.getRuntime().exec("cmd.exe   /c   tasklist");
            inputStreamReader = new InputStreamReader(process.getInputStream());
            input = new BufferedReader(inputStreamReader);
            String line;
            while ((line = input.readLine()) != null) {
                if (line.contains(".exe")) {
                    //trim()能够去掉一个字符串的前后空格
                    String pidStr = line.substring(line.indexOf(".exe") + 4).trim();
                    if (StringUtils.isNotEmpty(pidStr)) {
                        progressListStr.append(line, 0, line.indexOf(".exe") + 4)
                                .append(",").append(pidStr.split(" ")[0])
                                .append("\n");
                    }

                }
            }
        } catch (IOException e) {
            logger.warn(e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
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
        new ShowProcess();
    }

    /**
     * 设置透明度
     * TODO
     *
     * @param opacity 透明度
     */
    @Override
    public void setOpacity(float opacity) {
        super.setOpacity(opacity);
    }
}
