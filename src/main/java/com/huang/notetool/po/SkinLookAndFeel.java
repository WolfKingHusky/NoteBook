package com.huang.notetool.po;

import java.util.Date;

/**
 * 皮肤信息
 *
 * @author 黄先生
 * @version 1.0
 * @date 2019-07-22 11:34:41
 * Content 此处说明类的解释
 * History   序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-07-22   创建项目           完成
 * LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class SkinLookAndFeel {
    private Integer id;
    private String themName;
    private String borderName;
    private String waterMarkName;
    private String buttonShaperName;
    private String titlePainterName;
    private String fontPolicyName;
    private String skinName;
    private Date updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getThemName() {
        return themName;
    }

    public void setThemName(String themName) {
        this.themName = themName;
    }

    public String getBorderName() {
        return borderName;
    }

    public void setBorderName(String borderName) {
        this.borderName = borderName;
    }

    public String getWaterMarkName() {
        return waterMarkName;
    }

    public void setWaterMarkName(String waterMarkName) {
        this.waterMarkName = waterMarkName;
    }

    public String getButtonShaperName() {
        return buttonShaperName;
    }

    public void setButtonShaperName(String buttonShaperName) {
        this.buttonShaperName = buttonShaperName;
    }

    public String getTitlePainterName() {
        return titlePainterName;
    }

    public void setTitlePainterName(String titlePainterName) {
        this.titlePainterName = titlePainterName;
    }

    public String getFontPolicyName() {
        return fontPolicyName;
    }

    public void setFontPolicyName(String fontPolicyName) {
        this.fontPolicyName = fontPolicyName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getSkinName() {
        return skinName;
    }

    public void setSkinName(String skinName) {
        this.skinName = skinName;
    }
}
