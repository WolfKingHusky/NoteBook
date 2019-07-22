package com.huang.notetool.po;

import java.util.Date;

/**
 * 系统安装软件信息
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-05-30 12:24:40
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-30   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class SystemInstallMsg {
    /**
     * 主键ID
     */
    private int id;
    /**
     * 软件名字
     */
    private String name;
    /**
     * 软件版本
     */
    private String version;
    /**
     * 软件出版商
     */
    private String publisher;
    /**
     * 软件安装路径
     */
    private String installAddr;
    /**
     * 软件安装时间
     */
    private String installDate;
    /**
     * 软件卸载路径
     */
    private String unInstallAddr;

    /**
     * 更新日期
     */
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getInstallAddr() {
        return installAddr;
    }

    public void setInstallAddr(String installAddr) {
        this.installAddr = installAddr;
    }

    public String getInstallDate() {
        return installDate;
    }

    public void setInstallDate(String installDate) {
        this.installDate = installDate;
    }

    public String getUnInstallAddr() {
        return unInstallAddr;
    }

    public void setUnInstallAddr(String unInstallAddr) {
        this.unInstallAddr = unInstallAddr;
    }
}
