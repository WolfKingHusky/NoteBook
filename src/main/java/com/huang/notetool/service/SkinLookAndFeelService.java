package com.huang.notetool.service;

import com.huang.notetool.dao.SkinLookAndFeelDao;
import com.huang.notetool.dao.impl.SkinLookAndFeelDaoImpl;
import com.huang.notetool.po.SkinLookAndFeel;

import javax.swing.*;
import java.util.List;

/**
 * 此处说明类的作用
 *
 * @author 黄先生
 * @version 1.0
 * @date 2019-07-22 11:43:37
 * Content 此处说明类的解释
 * History   序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-07-22   创建项目           完成
 * LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class SkinLookAndFeelService {
    /**
     * 注入Dao层
     */
    private SkinLookAndFeelDao skinLookAndFeelDao;

    public SkinLookAndFeelService() {
        skinLookAndFeelDao = new SkinLookAndFeelDaoImpl();
    }

    /**
     * 更新皮肤
     *
     * @param lookAndFeel 皮肤
     */
    public void deleteAndSave(SkinLookAndFeel lookAndFeel) {
        skinLookAndFeelDao.deleteAll();
        skinLookAndFeelDao.save(lookAndFeel);
    }

    public List<SkinLookAndFeel> findAll() {
        return skinLookAndFeelDao.findAll();
    }

    public void delAll() {
        skinLookAndFeelDao.deleteAll();
    }
}
