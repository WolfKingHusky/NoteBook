package com.huang.notetool.dao.impl;

import com.huang.notetool.dao.NoteMsgDao;
import com.huang.notetool.po.NoteMsg;
import com.huang.notetool.util.Base64Tool;
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
 * @Content 此处说明类的解释
 * @date 2019-06-28 13:50:55
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-28   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class NoteMsgDaoImpl implements NoteMsgDao {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(NoteMsgDaoImpl.class);

    /**
     * 新增
     *
     * @param entity 实体对象
     * @return 保存成功返回true 否则返回false
     */
    @Override
    public boolean save(NoteMsg entity) {
        entity.setId(GenerateRandom.randomInt());
        entity.setInsertDate(new Date());
        entity.setUpdateDate(new Date());
        entity.setEnable(true);
        entity.setUpdateTimes(0);
        entity.setNote(Base64Tool.encode(entity.getNote()));
        return DBUtil.connection(UtilSql.saveNoteMsgSql(entity));
    }

    /**
     * 删除
     *
     * @param id 标识字段
     * @return 删除成功返回true 否则返回false
     */
    @Override
    public boolean deleteById(Integer id) {
        return DBUtil.connection(UtilSql.delNoteMsgById(id));
    }

    /**
     * 删除
     *
     * @param entity 标识字段
     * @return 删除成功返回true 否则返回false
     */
    @Override
    public boolean deleteByEntity(NoteMsg entity) {
        return false;
    }

    /**
     * 修改
     *
     * @param entity 实体对象
     */
    @Override
    public boolean update(NoteMsg entity) {
        entity.setNote(Base64Tool.encode(entity.getNote()));
        return DBUtil.connection(UtilSql.updateNoteMsgSql(entity));
    }

    /**
     * 根据ID查询
     *
     * @param id ID字段
     * @return 实体对象或null
     */
    @Override
    public NoteMsg findById(Integer id) {
        List<Object> objectList = DBUtil.connectionNoteMsg(UtilSql.findNoteMsgById(id), NoteMsg.class.getName(), new NoteMsg());
        List<NoteMsg> noteMsgList = transferToEntity(objectList);
        return (noteMsgList == null || noteMsgList.isEmpty()) ? null : noteMsgList.get(0);
    }

    /**
     * 查询所有
     *
     * @return 返回所有实体对象
     */
    @Override
    public List<NoteMsg> findAll() {
        return null;
    }

    /**
     * 关键字查询
     *
     * @param keyNames
     * @param keyNum
     * @return 保存所有实体对象的列表容器
     */
    @Override
    public List<NoteMsg> findByKeyName(List<String> keyNames, int keyNum) {
        return null;
    }

    /**
     * 对象字查询
     *
     * @param entity
     * @return 所有实体对象的列表容器
     */
    @Override
    public List<NoteMsg> findByEntity(NoteMsg entity) {
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
    public List<NoteMsg> findByKey(List<String> keyList) {
        return null;
    }

    /**
     * 通过名字查询
     *
     * @param name 名字（主要的，不能重复的信息）
     * @return 列表
     */
    @Override
    public List<NoteMsg> findByName(String name) {
        List<Object> objectList = DBUtil.connectionNoteMsg(UtilSql.findNoteMsgByName(name), NoteMsg.class.getName(), new NoteMsg());
        return transferToEntity(objectList);
    }

    /**
     * 对象转换
     *
     * @param objectList 返回信息列表
     * @return 转换后的列表
     */
    private List<NoteMsg> transferToEntity(List<Object> objectList) {
        if (null != objectList && !objectList.isEmpty()) {
            List<NoteMsg> noteMsgList = new ArrayList<>();
            for (Object object : objectList) {
                NoteMsg noteMsg = new NoteMsg();
                //赋值
                try {
                    BeanUtils.copyProperties(object, noteMsg);
                    noteMsgList.add(noteMsg);
                } catch (Exception e) {
                    logger.warn(e);
                }
            }
            return noteMsgList;
        }
        return null;
    }

    /**
     * 清空数据
     */
    @Override
    public void deleteAll() {
        DBUtil.connection(UtilSql.delAllNoteMsg());
    }

    /**
     * 计数数据库数据量
     *
     * @return 数据库数据量
     */
    @Override
    public int countNum() {
        return DBUtil.connectCountTableNum(UtilSql.countNoteMsgNum());
    }

    /**
     * 查询所有的笔记名称
     *
     * @return 笔记名称数组
     */
    @Override
    public List<String> findAllName() {
        return DBUtil.connectionSingleField(UtilSql.findAllNoteMsgName());
    }

    /**
     * 通过笔记名称查询所有的笔记名称
     *
     * @param noteType 笔记名称
     * @return 笔记名称数组
     */
    @Override
    public List<String> findAllNameByType(String noteType) {
        return DBUtil.connectionSingleField(UtilSql.findAllNoteMsgNameByType(noteType));
    }
}
