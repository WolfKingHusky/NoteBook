package com.huang.notetool.frame.format.linux;

import com.huang.notetool.frame.common.LinuxParentFrame;
import com.huang.notetool.po.LinuxCmd;
import com.huang.notetool.service.LinuxCmdService;
import com.huang.notetool.util.StringUtils;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

/**
 * 查看Linux命令菜单
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-06-21 15:10:26
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-21   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class LinuxCmdViewFrame extends LinuxParentFrame {
    /**
     * 注入Service
     */
    private LinuxCmdService linuxCmdService;

    /**
     * 构造方法
     *
     * @param jFrame 面板
     */
    public LinuxCmdViewFrame(JFrame jFrame) {
        super(jFrame);
        linuxCmdService = new LinuxCmdService();
    }

    /**
     * 初始化数据
     *
     * @param id 数据库ID
     */
    public void initial(String id) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LinuxCmd linuxCmd = linuxCmdService.findMsgById(id);
        if (null == linuxCmd) {
            JOptionPane.showMessageDialog(this, "打开失败");
            dispose();

        } else {
            refreshBtn.addActionListener(refreshBtnAction(id));
            //主键
            idJTextField.setText(linuxCmd.getId().toString());
            idJTextField.setEditable(false);
            //Linux命令
            linuxCmdJTextField.setText(linuxCmd.getLinuxCmd());
            linuxCmdJTextField.setEditable(false);
            //作用
            usagesJTextField.setText(StringUtils.isEmpty(linuxCmd.getUsage()) ? "" : linuxCmd.getUsage());
            usagesJTextField.setEditable(false);
            //用法
            exampleJTextField.setText(StringUtils.isEmpty(linuxCmd.getExample()) ? "" : linuxCmd.getExample().replaceAll("’", "'"));
            exampleJTextField.setEditable(false);
            //使用权限
            permissionJComboBox.setSelectedItem(StringUtils.isEmpty(linuxCmd.getPermission()) ? "所有者" : linuxCmd.getPermission());
            permissionJComboBox.setEditable(false);
            // 添加方法时间
            if (null != linuxCmd.getInsertDate()) {
                insertDateJTextField.setText(simpleDateFormat.format(linuxCmd.getInsertDate()));
            }
            insertDateJTextField.setEditable(false);

            // 更新方法时间
            if (null != linuxCmd.getInsertDate()) {
                updateDateJTextField.setText(simpleDateFormat.format(linuxCmd.getUpdateDate()));
            }
            updateDateJTextField.setEditable(false);
            //更新次数
            updateTimesJTextField.setText(String.valueOf(linuxCmd.getUpdateTimes()));
            updateTimesJTextField.setEditable(false);
            if (null != linuxCmd.getLinuxCmdExam()) {
                //关联表主键
                linuxCmdExamIdJTextField.setText(linuxCmd.getLinuxCmdExam().getId().toString());
                linuxCmdExamIdJTextField.setEditable(false);
                //换行
                String line = System.getProperty("line.separator");
                //参数
                if (null != linuxCmd.getLinuxCmdExam().getParameterNote()) {
                    parameterNoteJTextArea.setText(linuxCmd.getLinuxCmdExam().getParameterNote().replaceAll("；", line).replaceAll("’", "'"));
                }
                parameterNoteJTextArea.setEditable(false);
                //实例
                if (null != linuxCmd.getLinuxCmdExam().getExamples()) {
                    examplesJTextArea.setText(linuxCmd.getLinuxCmdExam().getExamples().replaceAll("；", line).replaceAll("’", "'"));
                }
                examplesJTextArea.setEditable(false);
            }
        }
    }

    /**
     * 刷新事件
     *
     * @param id id
     * @return ActionListener
     */
    private ActionListener refreshBtnAction(String id) {
        return e -> {
            logger.info("刷新Linux命令数据");
            initial(id);
        };
    }
}
