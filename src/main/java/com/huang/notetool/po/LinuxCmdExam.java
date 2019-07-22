package com.huang.notetool.po;

import java.util.Date;

/**
 * Linux命令实例大全
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-06-19 10:26:43
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-19   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class LinuxCmdExam {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 更新次数
     */
    private int updateTimes;
    /**
     * 参数说明
     */
    private String parameterNote;
    /**
     * 多个实例，使用中文；作为换行
     */
    private String examples;
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

    public String getParameterNote() {
        return parameterNote;
    }

    public void setParameterNote(String parameterNote) {
        this.parameterNote = parameterNote;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
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
