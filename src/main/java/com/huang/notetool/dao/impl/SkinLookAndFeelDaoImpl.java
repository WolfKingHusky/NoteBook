package com.huang.notetool.dao.impl;

import com.huang.notetool.po.SkinLookAndFeel;
import com.huang.notetool.util.DBUtil;
import com.huang.notetool.util.GenerateRandom;
import com.huang.notetool.util.UtilSql;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 此处说明类的作用
 *
 * @author 黄先生
 * @version 1.0
 * @date 2019-07-22 11:47:09
 * Content 此处说明类的解释
 * History   序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-07-22   创建项目           完成
 * LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class SkinLookAndFeelDaoImpl implements com.huang.notetool.dao.SkinLookAndFeelDao {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(SkinLookAndFeelDaoImpl.class);

    /**
     * 新增
     *
     * @param entity 实体对象
     * @return 保存成功返回true 否则返回false
     */
    @Override
    public boolean save(SkinLookAndFeel entity) {
        entity.setId(GenerateRandom.randomInt());
        entity.setUpdateDate(new Date());
        return DBUtil.connection(UtilSql.addBySkinLookAndFeelEntity(entity));
    }

    /**
     * 删除
     *
     * @param id 标识字段
     * @return 删除成功返回true 否则返回false
     */
    @Override
    public boolean deleteById(Integer id) {
        return false;
    }

    /**
     * 删除
     *
     * @param entity 标识字段
     * @return 删除成功返回true 否则返回false
     */
    @Override
    public boolean deleteByEntity(SkinLookAndFeel entity) {
        return false;
    }

    /**
     * 修改
     *
     * @param entity 实体对象
     */
    @Override
    public boolean update(SkinLookAndFeel entity) {
        return false;
    }

    /**
     * 根据ID查询
     *
     * @param id ID字段
     * @return 实体对象或null
     */
    @Override
    public SkinLookAndFeel findById(Integer id) {
        return null;
    }

    /**
     * 查询所有
     *
     * @return 返回所有实体对象
     */
    @Override
    public List<SkinLookAndFeel> findAll() {
        List<Object> objectList = DBUtil.connectionNoteMsg(UtilSql.findAllSkin(),
                SkinLookAndFeel.class.getName(), new SkinLookAndFeel());
        List<SkinLookAndFeel> skinLookAndFeelList = transferToEntity(objectList);
        return (skinLookAndFeelList == null || skinLookAndFeelList.isEmpty()) ? null :
                skinLookAndFeelList;
    }

    private List<SkinLookAndFeel> transferToEntity(List<Object> objectList) {
        if (null != objectList && !objectList.isEmpty()) {
            List<SkinLookAndFeel> feelArrayList = new ArrayList<>();
            for (Object object : objectList) {
                SkinLookAndFeel skinLookAndFeel = new SkinLookAndFeel();
                //赋值
                try {
                    BeanUtils.copyProperties(object, skinLookAndFeel);
                    feelArrayList.add(skinLookAndFeel);
                } catch (Exception e) {
                    logger.warn(e);
                }
            }
            return feelArrayList;
        }
        return null;
    }

    /**
     * 关键字查询
     *
     * @param keyNames 关键字名称列表
     * @param keyNum   关键字数量
     * @return 保存所有实体对象的列表容器
     */
    @Override
    public List<SkinLookAndFeel> findByKeyName(List<String> keyNames, int keyNum) {
        return null;
    }

    /**
     * 对象字查询
     *
     * @param entity 实体类
     * @return 所有实体对象的列表容器
     */
    @Override
    public List<SkinLookAndFeel> findByEntity(SkinLookAndFeel entity) {
        return null;
    }

    /**
     * MySQL状态查询
     *
     * @return mysql连接状态
     */
    @Override
    public boolean returnMysqlState() {
        return false;
    }

    /**
     * 通过关键字组合查询
     *
     * @param keyList 键字组合
     * @return E
     */
    @Override
    public List<SkinLookAndFeel> findByKey(List<String> keyList) {
        return null;
    }

    /**
     * 通过名字查询
     *
     * @param name 名字（主要的，不能重复的信息）
     * @return 列表
     */
    @Override
    public List<SkinLookAndFeel> findByName(String name) {
        return null;
    }

    /**
     * 清空数据
     */
    @Override
    public void deleteAll() {
        DBUtil.connection(UtilSql.delAllSkinMsg());
    }
}
