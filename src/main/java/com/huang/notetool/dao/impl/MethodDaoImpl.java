package com.huang.notetool.dao.impl;

import com.huang.notetool.dao.MethodDao;
import com.huang.notetool.po.Method;
import com.huang.notetool.util.DBUtil;
import com.huang.notetool.util.UtilSql;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 方法操作数据库
 *
 * @author 黄
 */
public class MethodDaoImpl implements MethodDao {
    /**
     * 日志
     */
    private static Logger logger = Logger.getLogger(MethodDaoImpl.class);

    @Override
    public boolean save(Method entity) {
        Connection con = DBUtil.getConnection();
        logger.info("正在保存 Method [" + entity.toString() + "] ... ");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(UtilSql.saveMethodSql);
            //id,name ,useTimes ,type ,language ,keyWord ,answer ,description ,updateDate ,isOutOfDate
            ps.setInt(1, entity.getId());
            ps.setString(2, entity.getName());
            //使用次数
            ps.setInt(3, 0);
            ps.setString(4, entity.getType());
            ps.setString(5, entity.getLanguage());
            ps.setString(6, entity.getKeyWord());
            ps.setString(7, entity.getAnswer());
            ps.setString(8, entity.getDescription());
            ps.setString(9, format.format(new Date()));
            ps.setInt(10, entity.getIsOutOfDate());
            ps.execute();
            logger.info("保存 [" + entity.toString() + "] 成功 ");
        } catch (SQLException e) {
            logger.info("保存 [" + entity.toString() + "] 出错 " + e);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                DBUtil.getClose(con);
            } catch (SQLException e) {
                logger.warn(e);
            }
        }
        return true;
    }

    @Override
    public boolean deleteById(Integer id) {
        return DBUtil.connection(UtilSql.delMethodSql(id));
    }

    @Override
    public boolean deleteByEntity(Method entity) {
        if (null != entity.getId()) {
            return deleteById(entity.getId());
        } else {
            return DBUtil.connection(UtilSql.delByEntitySql(entity));
        }
    }

    @Override
    public boolean update(Method entity) {
        return DBUtil.connection(UtilSql.updateByEntitySql(entity));
    }

    @Override
    public Method findById(Integer id) {
        List<Method> methodList = DBUtil.connections(UtilSql.selectById(id));
        if (null != methodList && !methodList.isEmpty()) {
            return methodList.get(0);
        }
        return null;
    }

    @Override
    public List<Method> findAll() {
        logger.info("查询所有的 Method ");
        return DBUtil.connections(UtilSql.selectAll);
    }

    @Override
    public List<Method> findByKeyName(List<String> keyNames, int keyNameNum) {
        return DBUtil.connections(UtilSql.findByKeyName(keyNames, keyNameNum));
    }

    @Override
    public List<Method> findByEntity(Method entity) {
        return DBUtil.connections(UtilSql.findByEntity(entity));
    }

    @Override
    public boolean returnMysqlState() {
        return true;
    }

    @Override
    public List<Method> findByKey(List<String> keyList) {
        return null;
    }

    /**
     * 通过名字查询信息
     *
     * @param name 名字
     * @return Method列表
     */
    @Override
    public List<Method> findByName(String name) {
        return null;
    }

    /**
     * 清空数据
     */
    @Override
    public void deleteAll() {
        DBUtil.connection(UtilSql.delAllMethod());
    }

}
