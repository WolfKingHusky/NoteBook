package com.huang.notetool.dao.impl;

import com.huang.notetool.dao.ExceptionWayDao;
import com.huang.notetool.po.ExceptionWay;
import com.huang.notetool.util.DBUtil;
import com.huang.notetool.util.UtilSql;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Dao层实现类
 *
 * @author huang
 */
public class ExceptionWayDaoImpl implements ExceptionWayDao {
    /**
     * 日志
     */
    private static Logger logger = Logger.getLogger(ExceptionWayDaoImpl.class);

    @Override
    public boolean save(ExceptionWay entity) {
        return DBUtil.connection(UtilSql.addByExceptionEntity(entity));
    }

    @Override
    public boolean deleteById(Integer id) {
        return DBUtil.connection(UtilSql.delExceptionSql(id));
    }

    @Override
    public boolean deleteByEntity(ExceptionWay entity) {
        return false;
    }

    /**
     * 修改
     *
     * @param entity 实体对象
     */
    @Override
    public boolean update(ExceptionWay entity) {
        return DBUtil.connection(UtilSql.updateByExceptionEntitySql(entity));
    }

    @Override
    public ExceptionWay findById(Integer id) {
        return null;
    }

    @Override
    public List<ExceptionWay> findAll() {
        logger.info("查找所有 ExceptionWay");
        return DBUtil.connectionExceptionWay(UtilSql.selectExceptionAll);
    }

    @Override
    public List<ExceptionWay> findByKeyName(List<String> keyNames, int keyNum) {
        return null;
    }

    @Override
    public List<ExceptionWay> findByEntity(ExceptionWay entity) {
        return DBUtil.connectionExceptionWay(UtilSql.findByExceptionEntity(entity));
    }

    @Override
    public boolean returnMysqlState() {
        return false;
    }

    @Override
    public List<ExceptionWay> findByKey(List<String> keyList) {
        return DBUtil.connectionExceptionWay(UtilSql.findByExceptionKey(keyList));
    }

    /**
     * 通过异常信息查询
     *
     * @param name 异常信息名称
     * @return 异常信息
     */
    @Override
    public List<ExceptionWay> findByName(String name) {
        return DBUtil.connectionExceptionWay(UtilSql.findByExceptionName(name));
    }

    /**
     * 清空数据
     */
    @Override
    public void deleteAll() {
        DBUtil.connection(UtilSql.delAllException());
    }
}
