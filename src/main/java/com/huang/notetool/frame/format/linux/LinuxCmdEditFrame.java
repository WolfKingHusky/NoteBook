package com.huang.notetool.frame.format.linux;

import com.huang.notetool.frame.common.LinuxParentFrame;
import com.huang.notetool.po.LinuxCmd;
import com.huang.notetool.po.LinuxCmdExam;
import com.huang.notetool.service.LinuxCmdService;
import com.huang.notetool.util.StringUtils;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 修改Linux命令菜单
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-06-21 15:10:26
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-21   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class LinuxCmdEditFrame extends LinuxParentFrame {
    /**
     * 注入Service
     */
    private LinuxCmdService linuxCmdService;
    /**
     * 存放初始信息
     */
    private LinuxCmd linuxCmdStatic;

    /**
     * 构造方法
     *
     * @param jFrame 面板
     */
    public LinuxCmdEditFrame(JFrame jFrame) {
        super(jFrame);
        linuxCmdService = new LinuxCmdService();
        JButton resetBtn = new JButton("重置");
        JButton updateBtn = new JButton("修改");
        buttonPanel.add(resetBtn);
        buttonPanel.add(updateBtn);

        resetBtn.addActionListener(resetBtnAction());
        updateBtn.addActionListener(updateBtnAction());
    }

    /**
     * 更新按钮事件
     *
     * @return ActionListener
     */
    private ActionListener updateBtnAction() {
        return e -> {
            logger.info("更新Linux命令数据事件");
            updateMsg();
        };
    }

    /**
     * 更新事件
     */
    private void updateMsg() {
        //初始数据
        LinuxCmd localLinuxCmd = linuxCmdStatic;
        LinuxCmd linuxCmd = new LinuxCmd();
        //主键
        linuxCmd.setId(Integer.parseInt(idJTextField.getText().trim()));

        //Linux命令
        String linuxCmdName = linuxCmdJTextField.getText().trim();
        if (StringUtils.isEmpty(linuxCmdName)) {
            JOptionPane.showMessageDialog(LinuxCmdEditFrame.this, "Linux命令名称不能为空");
            linuxCmdJTextField.requestFocus();
            return;
        }
        linuxCmd.setLinuxCmd(linuxCmdName);

        //作用
        String usages = usagesJTextField.getText().trim();
        if (StringUtils.isEmpty(usages)) {
            JOptionPane.showMessageDialog(LinuxCmdEditFrame.this, "Linux命令作用不能为空");
            usagesJTextField.requestFocus();
            return;
        }
        linuxCmd.setUsage(usages);

        //用法
        String example = exampleJTextField.getText().trim();
        if (StringUtils.isEmpty(example)) {
            JOptionPane.showMessageDialog(LinuxCmdEditFrame.this, "Linux命令用法不能为空");
            exampleJTextField.requestFocus();
            return;
        }
        linuxCmd.setExample(example);

        //使用权限
        String permission = (String) permissionJComboBox.getSelectedItem();
        if (StringUtils.isEmpty(permission)) {
            JOptionPane.showMessageDialog(LinuxCmdEditFrame.this, "Linux命令使用权限不能为空");
            permissionJComboBox.requestFocus();
            return;
        }
        linuxCmd.setPermission(permission);

        // 添加方法时间
        linuxCmd.setInsertDate(localLinuxCmd.getInsertDate());

        // 更新方法时间
        linuxCmd.setUpdateDate(new Date());

        //更新次数(dao层会加一)
        linuxCmd.setUpdateTimes(localLinuxCmd.getUpdateTimes());
        String para = parameterNoteJTextArea.getText().trim();
        String examples = examplesJTextArea.getText().trim();
        String linuxCmdExamId = linuxCmdExamIdJTextField.getText();
        LinuxCmdExam linuxCmdExam = new LinuxCmdExam();
        //换行
        String line = System.getProperty("line.separator");
        //关联表主键
        if (StringUtils.isNotEmpty(linuxCmdExamId)) {
            linuxCmdExam.setId(Integer.parseInt(linuxCmdExamId));
            if (StringUtils.isEmpty(para)) {
                JOptionPane.showMessageDialog(LinuxCmdEditFrame.this, "Linux命令参数shuom不能为空");
                parameterNoteJTextArea.requestFocus();
                return;
            }
            linuxCmdExam.setParameterNote(para.replaceAll(line, "；"));
            if (StringUtils.isEmpty(examples)) {
                JOptionPane.showMessageDialog(LinuxCmdEditFrame.this, "Linux命令参数实例不能为空");
                examplesJTextArea.requestFocus();
                return;
            }
            linuxCmdExam.setExamples(examples.replaceAll(line, "；"));
            linuxCmd.setLinuxCmdExam(linuxCmdExam);
        } else {
            if (StringUtils.isEmpty(examples) || StringUtils.isEmpty(para)) {
                if (StringUtils.isEmpty(examples)) {
                    linuxCmdExam.setExamples(examples.replaceAll(line, "；"));
                }
                if (StringUtils.isEmpty(para)) {
                    linuxCmdExam.setParameterNote(para.replaceAll(line, "；"));
                }
                linuxCmd.setLinuxCmdExam(linuxCmdExam);
            }
        }
        if (check(linuxCmd, localLinuxCmd)) {
            if (!linuxCmdService.update(linuxCmd)) {
                logger.warn("更新Linux命令失败 [[" + linuxCmd.toString());
                JOptionPane.showMessageDialog(LinuxCmdEditFrame.this, "更新Linux命令失败");
                return;
            }
            logger.warn("更新Linux命令成功 [[" + linuxCmd.toString());
            JOptionPane.showMessageDialog(LinuxCmdEditFrame.this, "更新Linux命令成功");
            dispose();
        } else {
            JOptionPane.showMessageDialog(LinuxCmdEditFrame.this, "信息没有改变，不需要修改");
        }

    }

    /**
     * 如果信息有改变，才返回true，否则返回false
     *
     * @param linuxCmd      改变后的Linux信息
     * @param localLinuxCmd 改变前的Linux信息
     * @return true or false
     */
    private boolean check(LinuxCmd linuxCmd, LinuxCmd localLinuxCmd) {
        //比较：Linux命令，作用，用法，使用权限，参数说明,多个实例(使用中文；作为换行);
        boolean result = localLinuxCmd.getLinuxCmd().equals(linuxCmd.getLinuxCmd())
                && localLinuxCmd.getUsage().equals(linuxCmd.getUsage())
                && localLinuxCmd.getExample().equals(linuxCmd.getUsage())
                && localLinuxCmd.getPermission().equals(linuxCmd.getPermission())
                && (
                (null != localLinuxCmd.getLinuxCmdExam()
                        && localLinuxCmd.getLinuxCmdExam().getParameterNote().equalsIgnoreCase(linuxCmd.getLinuxCmdExam().getParameterNote()))
                        && localLinuxCmd.getLinuxCmdExam().getExamples().equalsIgnoreCase(localLinuxCmd.getLinuxCmdExam().getExamples())
                        || (null == localLinuxCmd.getLinuxCmdExam()
                        && null != (linuxCmd.getLinuxCmdExam()))
        );

        return !result;
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
            initial(String.valueOf(linuxCmdStatic.getId()));
        };
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
            linuxCmdStatic = linuxCmd;
            refreshBtn.addActionListener(refreshBtnAction(id));
            //主键
            idJTextField.setText(linuxCmd.getId().toString());

            //Linux命令
            linuxCmdJTextField.setText(linuxCmd.getLinuxCmd());

            //作用
            usagesJTextField.setText(StringUtils.isEmpty(linuxCmd.getUsage()) ? "" : linuxCmd.getUsage().replaceAll("'", "’"));

            //用法
            exampleJTextField.setText(StringUtils.isEmpty(linuxCmd.getExample()) ? "" : linuxCmd.getExample().replaceAll("'", "’"));

            //使用权限
            permissionJComboBox.setSelectedItem(StringUtils.isEmpty(linuxCmd.getPermission()) ? "所有者" : linuxCmd.getPermission());

            // 添加方法时间
            if (null != linuxCmd.getInsertDate()) {
                insertDateJTextField.setText(simpleDateFormat.format(linuxCmd.getInsertDate()));
            }

            // 更新方法时间
            if (null != linuxCmd.getInsertDate()) {
                updateDateJTextField.setText(simpleDateFormat.format(linuxCmd.getUpdateDate()));
            }

            //更新次数
            updateTimesJTextField.setText(String.valueOf(linuxCmd.getUpdateTimes()));

            if (null != linuxCmd.getLinuxCmdExam()) {
                //关联表主键
                linuxCmdExamIdJTextField.setText(linuxCmd.getLinuxCmdExam().getId().toString());
                //换行
                String line = System.getProperty("line.separator");
                //参数
                if (null != linuxCmd.getLinuxCmdExam().getParameterNote()) {
                    parameterNoteJTextArea.setText(linuxCmd.getLinuxCmdExam().getParameterNote().replaceAll("；", line).replaceAll("'", "’"));
                }
                //实例
                if (null != linuxCmd.getLinuxCmdExam().getExamples()) {
                    examplesJTextArea.setText(linuxCmd.getLinuxCmdExam().getExamples().replaceAll("；", line).replaceAll("'", "’"));
                }
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
