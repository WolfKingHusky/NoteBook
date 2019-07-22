package com.huang.notetool.po;

import java.util.Date;

/**
 * 笔记信息实体类
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-06-19 10:26:43
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-19   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class NoteMsg {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 更新次数
     */
    private int updateTimes;
    /**
     * 笔记
     */
    private String note;
    /**
     * 笔记说明
     */
    private String explains;
    /**
     * 笔记类型
     */
    private String noteType;
    /**
     * 笔记名称
     */
    private String name;
    /**
     * 添加方法时间
     */
    private Date insertDate;
    /**
     * 更新方法时间
     */
    private Date updateDate;

    /**
     * 是否启用
     */
    private boolean enable;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

}
