package com.huang.notetool.frame.system;

import com.huang.notetool.frame.common.JRootParentDiag;
import com.huang.notetool.frame.common.JRootParentFrame;
import com.huang.notetool.frame.start.Note;
import com.huang.notetool.tool.ReadFile;
import com.huang.notetool.util.Constants;
import com.huang.notetool.util.InitialComponent;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 显示系统日志信息
 *
 * @author huang
 * @date 2019-07-08
 */
public class SystemLogFrame extends JRootParentFrame {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(SystemLogFrame.class);

    /**
     * 构造
     *
     * @param jFrame 面板
     * @return SystemLogFrame
     * @author 黄先生
     * @date 2019/7/10 16:05
     */
    public SystemLogFrame(Note jFrame) {
        super();
        logger.info("查看进程信息");
        int x = jFrame.getX();
        int y = jFrame.getY();
        int width = jFrame.getWidth() * 3 / 5;
        int height = jFrame.getHeight() * 3 / 5;
        //居中：上层面板的x坐标+（（上层面板的长-弹窗的长）/2）（除2是为了长度前后各取部分，好居中）
        this.setBounds(x + ((jFrame.getWidth() - width) / 2),
                y + ((jFrame.getHeight() - height) / 2), width, height);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //非Windows主题才能设置透明度
        if (!InitialComponent.isWindowsLookAndFeel()) {
            this.setOpacity(jFrame.getOpacity());
        }
        this.setResizable(true);
        this.setTitle("系统日志信息，双击可以打开");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //此处有循环，需要放到初始换的最后面
        initial();
    }

    /**
     * 初始化其他组件
     */
    private void initial() {
        //系统日志信息
        File[] systemLogArr = ReadFile.listSystemLogs(Constants.SYSTEM_LOG_DIR);

        JList systemLogJList = new JList(systemLogArr);

        JScrollPane jsp = new JScrollPane(systemLogJList);
        //设置边界
        jsp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(jsp, "Center");
        //监听事件
        systemLogJList.addMouseListener(progressClickListener(systemLogJList));
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
                File logFile = (File) theList.getSelectedValue();
                if (null != logFile) {
                    try {
                        Desktop.getDesktop().open(logFile);
                    } catch (IOException e) {
                        //显示消息
                        new JRootParentDiag(SystemLogFrame.this, e.getMessage(),
                                Constants.NOTICE_IMAGE);
                        logger.warn(e);
                    }
                }
            }
        };
    }
}
