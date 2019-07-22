package com.huang.notetool.dao.impl;

import com.huang.notetool.dao.SystemInstallMsgDao;
import com.huang.notetool.po.SystemInstallMsg;
import com.huang.notetool.util.DBUtil;
import com.huang.notetool.util.UtilSql;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 此处说明类的作用
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-05-30 13:17:20
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-30   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class SystemInstallMsgDaoImpl implements SystemInstallMsgDao {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(SystemInstallMsgDaoImpl.class);

    /**
     * 新增
     *
     * @param entity 实体对象
     * @return 保存成功返回true 否则返回false
     */
    @Override
    public boolean save(SystemInstallMsg entity) {
        logger.info("通过 Entity [" + entity.toString() + "] 进行添加 SystemInstallMsg ");
        return DBUtil.connection(UtilSql.addBySystemInstallMsgEntitySql(entity));
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
    public boolean deleteByEntity(SystemInstallMsg entity) {
        return false;
    }

    /**
     * 修改
     *
     * @param entity 实体对象
     */
    @Override
    public boolean update(SystemInstallMsg entity) {
        logger.info("通过 [ " + entity.toString() + " ] 进行更新 SystemInstallMsg ");
        return DBUtil.connection(UtilSql.updateBySystemInstallMsgSql(entity));
    }

    /**
     * 根据ID查询
     *
     * @param id ID字段
     * @return 实体对象或null
     */
    @Override
    public SystemInstallMsg findById(Integer id) {
        return null;
    }

    /**
     * 查询所有
     *
     * @return 返回所有实体对象
     */
    @Override
    public List<SystemInstallMsg> findAll() {
        return DBUtil.connectionSystemInstallMsg(UtilSql.findAllSystemInstallMsg());
    }

    /**
     * 关键字查询
     *
     * @param keyNames
     * @param keyNum
     * @return 保存所有实体对象的列表容器
     */
    @Override
    public List<SystemInstallMsg> findByKeyName(List<String> keyNames, int keyNum) {
        return null;
    }

    /**
     * 对象字查询
     *
     * @param entity
     * @return 所有实体对象的列表容器
     */
    @Override
    public List<SystemInstallMsg> findByEntity(SystemInstallMsg entity) {
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
     * 通过关键字列表获取系统安装软件信息
     *
     * @param keyList 关键字信息列表
     * @return java.util.List<com.huang.notetool.po.SystemInstallMsg>
     * @author 黄先生
     * @date 2019/7/7 10:32
     */
    @Override
    public List<SystemInstallMsg> findByKey(List<String> keyList) {
        return DBUtil.connectionSystemInstallMsg(UtilSql.findBySystemInstallMsgKey(keyList));
    }

    /**
     * 通过名字查询
     *
     * @param name 名字（主要的，不能重复的信息）
     * @return 列表
     */
    @Override
    public List<SystemInstallMsg> findByName(String name) {
        return DBUtil.connectionSystemInstallMsg(UtilSql.findBySystemInstallMsgName(name));
    }

    /**
     * 清空数据
     */
    @Override
    public void deleteAll() {
        logger.info("清空数据 SystemInstallMsg");
    }
}
