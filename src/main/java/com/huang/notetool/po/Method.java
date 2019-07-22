package com.huang.notetool.po;

import java.util.Date;

/**
 * 方法
 *
 * @author huang
 */
public class Method {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 使用次数
     */
    private int useTimes;
    /**
     * 方法名字
     */
    private String name;
    /**
     * 方法类型
     */
    private String type;
    /**
     * 方法采用的语言
     */
    private String language;
    /**
     * 关键字
     */
    private String keyWord;
    /**
     * 具体方法
     */
    private String answer;
    /**
     * 方法描述
     */
    private String description;
    /**
     * 添加方法时间
     */
    private Date updateDate;
    /**
     * 方法是否被淘汰,0没有，1是
     */
    private int isOutOfDate;

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

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    public Integer getIsOutOfDate() {
        return isOutOfDate;
    }

    public void setIsOutOfDate(Integer isOutOfDate) {
        this.isOutOfDate = isOutOfDate;
    }

    @Override
    public String toString() {
        return "[id=" + id +
                ", useTimes=" + useTimes +
                ", name='" + name +
                ", type='" + type +
                ", language='" + language +
                ", keyWord='" + keyWord +
                ", answer='" + answer +
                ", description='" + description +
                ", updateDate=" + updateDate +
                ", isOutOfDate=" + isOutOfDate +
                "]";
    }

    public Method() {
        super();
    }
}