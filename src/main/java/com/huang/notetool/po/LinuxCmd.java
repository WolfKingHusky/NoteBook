package com.huang.notetool.po;

import java.util.Date;

/**
 * Linux命令大全
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-06-19 10:26:43
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-19   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class LinuxCmd {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 更新次数
     */
    private int updateTimes;
    /**
     * Linux命令
     */
    private String linuxCmd;
    /**
     * 作用
     */
    private String usage;
    /**
     * 用法
     */
    private String example;
    /**
     * 使用权限
     */
    private String permission;
    /**
     * 实例和参数说明
     */
    private LinuxCmdExam linuxCmdExam;
    /**
     * 添加方法时间
     */
    private Date insertDate;
    /**
     * 更新方法时间
     */
    private Date updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUpdateTimes() {
        return updateTimes;
    }

    public void setUpdateTimes(int updateTimes) {
        this.updateTimes = updateTimes;
    }

    public String getLinuxCmd() {
        return linuxCmd;
    }

    public void setLinuxCmd(String linuxCmd) {
        this.linuxCmd = linuxCmd;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public LinuxCmdExam getLinuxCmdExam() {
        return linuxCmdExam;
    }

    public void setLinuxCmdExam(LinuxCmdExam linuxCmdExam) {
        this.linuxCmdExam = linuxCmdExam;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
