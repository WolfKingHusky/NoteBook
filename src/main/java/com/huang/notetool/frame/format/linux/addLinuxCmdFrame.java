package com.huang.notetool.frame.format.linux;

import com.huang.notetool.frame.common.LinuxParentFrame;
import com.huang.notetool.po.LinuxCmd;
import com.huang.notetool.po.LinuxCmdExam;
import com.huang.notetool.service.LinuxCmdService;
import com.huang.notetool.util.StringUtils;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

/**
 * 新增Linux命令菜单
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-06-21 15:10:26
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-21   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class addLinuxCmdFrame extends LinuxParentFrame {
    /**
     * 注入Service
     */
    private LinuxCmdService linuxCmdService;

    public addLinuxCmdFrame() {
        super();
        linuxCmdService = new LinuxCmdService();
    }

    /**
     * 构造方法
     *
     * @param jFrame 面板
     */
    public addLinuxCmdFrame(JFrame jFrame) {
        super(jFrame);
        linuxCmdService = new LinuxCmdService();
        JButton resetBtn = new JButton("重置");
        JButton addBtn = new JButton("新增");
        buttonPanel.add(resetBtn);
        buttonPanel.add(addBtn);

        resetBtn.addActionListener(resetBtnAction());
        addBtn.addActionListener(addBtnAction());
    }

    /**
     * 新增按钮事件
     *
     * @return ActionListener
     */
    private ActionListener addBtnAction() {
        return e -> {
            logger.info("新增Linux命令数据事件");
            addMsg();
        };
    }

    /**
     * 新增事件
     */
    private void addMsg() {
        LinuxCmd linuxCmd = new LinuxCmd();

        //Linux命令
        String linuxCmdName = linuxCmdJTextField.getText().trim();
        if (StringUtils.isEmpty(linuxCmdName)) {
            JOptionPane.showMessageDialog(addLinuxCmdFrame.this, "Linux命令名称不能为空");
            linuxCmdJTextField.requestFocus();
            return;
        }
        linuxCmd.setLinuxCmd(linuxCmdName);

        //作用
        String usages = usagesJTextField.getText().trim();
        if (StringUtils.isEmpty(usages)) {
            JOptionPane.showMessageDialog(addLinuxCmdFrame.this, "Linux命令作用不能为空");
            usagesJTextField.requestFocus();
            return;
        }
        linuxCmd.setUsage(usages.replaceAll("，", ",").replaceAll("'", "‘"));

        //用法
        String example = exampleJTextField.getText().trim();
        if (StringUtils.isEmpty(example)) {
            JOptionPane.showMessageDialog(addLinuxCmdFrame.this, "Linux命令用法不能为空");
            exampleJTextField.requestFocus();
            return;
        }
        linuxCmd.setExample(example.replaceAll("，", ",").replaceAll("'", "‘"));

        //使用权限
        String permission = (String) permissionJComboBox.getSelectedItem();
        if (StringUtils.isEmpty(permission)) {
            JOptionPane.showMessageDialog(addLinuxCmdFrame.this, "Linux命令使用权限不能为空");
            permissionJComboBox.requestFocus();
            return;
        }
        linuxCmd.setPermission(permission);

        // 添加方法时间
        linuxCmd.setInsertDate(new Date());

        // 新增方法时间
        linuxCmd.setUpdateDate(new Date());

        //新增次数(dao层会加一)
        linuxCmd.setUpdateTimes(0);
        String para = parameterNoteJTextArea.getText().trim();
        String examples = examplesJTextArea.getText().trim();
        LinuxCmdExam linuxCmdExam = new LinuxCmdExam();
        //换行
        String line = System.getProperty("line.separator");
        //关联表信息
        if (StringUtils.isNotEmpty(para) || StringUtils.isNotEmpty(examples)) {
            linuxCmdExam.setInsertDate(linuxCmd.getInsertDate());
            linuxCmdExam.setUpdateDate(linuxCmd.getUpdateDate());
            linuxCmdExam.setUpdateTimes(linuxCmd.getUpdateTimes());
            if (StringUtils.isEmpty(para)) {
                JOptionPane.showMessageDialog(addLinuxCmdFrame.this, "Linux命令参数说明不能为空");
                parameterNoteJTextArea.requestFocus();
                return;
            }
            linuxCmdExam.setParameterNote(para.replaceAll("；", ";").replaceAll("，", ",")
                    .replaceAll(line, "；").replaceAll("'", "‘"));
            if (StringUtils.isEmpty(examples)) {
                JOptionPane.showMessageDialog(addLinuxCmdFrame.this, "Linux命令参数实例不能为空");
                examplesJTextArea.requestFocus();
                return;
            }
            linuxCmdExam.setExamples(examples.replaceAll("；", ";")
                    .replaceAll(line, "；")
                    .replaceAll("，", ",")
                    .replaceAll("'", "‘"));
            linuxCmd.setLinuxCmdExam(linuxCmdExam);
        }
        List<LinuxCmd> linuxCmdInDBList = linuxCmdService.findMsgByName(linuxCmd.getLinuxCmd());
        if (null != linuxCmdInDBList && !linuxCmdInDBList.isEmpty()) {
            logger.warn("新增Linux命令失败 [[" + linuxCmd.getLinuxCmd() + "]] 数据库已存在");
            JOptionPane.showMessageDialog(addLinuxCmdFrame.this, "新增Linux命令失败，数据库已存在");
            return;
        }
        if (!linuxCmdService.addLinuxCmd(linuxCmd)) {
            logger.warn("新增Linux命令失败 [[" + linuxCmd.getLinuxCmd());
            JOptionPane.showMessageDialog(addLinuxCmdFrame.this, "新增Linux命令失败");
            return;
        }
        logger.warn("新增Linux命令成功 [[" + linuxCmd.getLinuxCmd());
        JOptionPane.showMessageDialog(addLinuxCmdFrame.this, "新增Linux命令成功");
        resetMsg();
    }

    /**
     * 重置信息
     */
    private void resetMsg() {
        linuxCmdJTextField.setText("");
        usagesJTextField.setText("");
        exampleJTextField.setText("");
        examplesJTextArea.setText("");
        parameterNoteJTextArea.setText("");
        permissionJComboBox.setSelectedIndex(0);
    }


    /**
     * 重置按钮事件
     *
     * @return ActionListener
     */
    private ActionListener resetBtnAction() {
        return e -> {
            //重置按钮
            logger.info("重置修改Linux命令数据事件");
            resetMsg();
        };
    }

    /**
     * 初始化数据
     */
    public void initial() {
        //移除刷新按钮
        super.buttonPanel.remove(refreshBtn);
        validate();
        repaint();
    }
}
