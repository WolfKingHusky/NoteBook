package com.huang.notetool.po;

import java.util.Date;

/**
 * 异常信息
 *
 * @author huang
 */
public class ExceptionWay {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 使用次数
     */
    private int useTimes;
    /**
     * 异常名字
     * 具体的异常信息
     */
    private String name;
    /**
     * 异常类型
     */
    private String type;
    /**
     * 异常采用的语言
     */
    private String language;
    /**
     * 异常解决方法
     */
    private String answer;
    /**
     * 异常根本原因
     */
    private String cause;
    /**
     * 关键字
     */
    private String keyWord;
    /**
     * 异常描述
     */
    private String description;
    /**
     * 添加异常时间
     */
    private Date updateDate;
    /**
     * 异常是否被相应语言更新所解决
     * 0:没有
     * 1：解决了
     */
    private Integer isSolution;

    /**
     * 备注
     */
    private String remark;

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(Integer useTimes) {
        this.useTimes = useTimes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getIsSolution() {
        return isSolution;
    }

    public void setIsSolution(Integer isSolution) {
        this.isSolution = isSolution;
    }


    @Override
    public String toString() {
        return "ExceptionWay{" +
                "id=" + id +
                ", useTimes=" + useTimes +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", language='" + language + '\'' +
                ", answer='" + answer + '\'' +
                ", keyWord='" + keyWord + '\'' +
                ", description='" + description + '\'' +
                ", updateDate=" + updateDate +
                ", isSolution=" + isSolution +
                '}';
    }

    public ExceptionWay() {
        super();
    }
}
