package com.huang.notetool.util;

import com.huang.notetool.po.*;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 组装Sql
 *
 * @author 黄先生
 * @date 2018-12-12
 */
public class UtilSql {
    /**
     * 初始化日志
     */
    private static Logger logger = Logger.getLogger(UtilSql.class);
    /**
     * SQL error or missing database (9 values for 10 columns)
     * 问号和前面的属性个数不匹配
     */
    public static String saveMethodSql = "INSERT INTO method(id,name ,useTimes ,type ,language ," +
            "keyWord ,answer ,description ,updateDate ,isOutOfDate) VALUES(?, ?, ?, ?, ?, ?, ?, " +
            "?, ?, ?)";
    /**
     * 查询所有的方法信息
     */
    public static String selectAll = "SELECT * FROM method";
    /**
     * 查询所有的异常信息
     */
    public static String selectExceptionAll = "SELECT * FROM exception";
    /**
     * 日期统一格式化
     */
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 删除方法组装Sql
     *
     * @param entity 方法
     * @return Sql
     */
    public static String delByEntitySql(Method entity) {
        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM method WHERE 1=1 ");
        if (StringUtils.isNotEmpty(entity.getName())) {
            sqlBuilder.append(" and ").append("name = '").append(entity.getName()).append("' ");
        }
        if (StringUtils.isNotEmpty(entity.getType())) {
            sqlBuilder.append(" and ").append("type = '").append(entity.getType()).append("' ");
        }
        if (StringUtils.isNotEmpty(entity.getLanguage())) {
            sqlBuilder.append(" and ").append("language = '").append(entity.getLanguage()).append("' ");
        }
        if (StringUtils.isNotEmpty(entity.getKeyWord())) {
            sqlBuilder.append(" and ").append("keyWord = '").append(entity.getKeyWord()).append(
                    "' ");
        }
        if (StringUtils.isNotEmpty(entity.getAnswer())) {
            sqlBuilder.append(" and ").append("answer = '").append(entity.getAnswer()).append("' ");
        }
        //移除多余的
        logger.info("通过Entity删除 method 的Sql>> [" + sqlBuilder + "]");
        return sqlBuilder.toString();
    }

    /**
     * 根据ID删除方法Sql
     *
     * @param id 方法表ID
     * @return Sql
     */
    public static String delMethodSql(Integer id) {
        logger.info("根据ID删除方法Sql: DELETE FROM method WHERE id = " + id);
        return "DELETE FROM method WHERE id = " + id;
    }

    /**
     * 更新方法Sql
     *
     * @param entity 方法
     * @return Sql
     */
    public static String updateByEntitySql(Method entity) {
        StringBuilder sql = new StringBuilder("UPDATE method SET ");
        if (StringUtils.isNotEmpty(entity.getName())) {
            sql.append("name = '");
            sql.append(entity.getName());
            sql.append("' , ");
        }
        if (StringUtils.isNotEmpty(entity.getType())) {
            sql.append("type = '");
            sql.append(entity.getType());
            sql.append("' , ");
        }
        if (StringUtils.isNotEmpty(entity.getLanguage())) {
            sql.append("language = '");
            sql.append(entity.getLanguage());
            sql.append("' , ");
        }
        if (StringUtils.isNotEmpty(entity.getKeyWord())) {
            sql.append("keyWord = '");
            sql.append(entity.getKeyWord());
            sql.append("' , ");
        }
        if (StringUtils.isNotEmpty(entity.getAnswer())) {
            sql.append("answer = '");
            sql.append(entity.getAnswer());
            sql.append("' , ");
        }
        if (StringUtils.isNotEmpty(entity.getDescription())) {
            sql.append("description = '");
            sql.append(entity.getDescription());
            sql.append("' , ");
        }
        sql.append("updateDate='");
        sql.append(simpleDateFormat.format(entity.getUpdateDate()));
        sql.append(",'isOutOfDate = '");
        sql.append(entity.getIsOutOfDate());
        sql.append("WHERE id = ");
        sql.append(entity.getId());
        logger.info("更新method的Sql语句 [" + sql + "] ");
        return sql.toString();
    }

    /**
     * 通过ID查询数据的Sql
     *
     * @param id id
     * @return Sql
     */
    public static String selectById(Integer id) {
        logger.info("通过ID查询method的Sql语句 [SELECT * FROM method WHERE id = " + id + "] ");
        return "SELECT * FROM method WHERE id = " + id;
    }

    /**
     * 通过关键字查询Sql
     *
     * @param keyNameList 关键字列表
     * @return Sql
     */
    public static String findByKeyName(List<String> keyNameList, int keyNameNum) {
        StringBuilder sql = new StringBuilder("SELECT * FROM method WHERE ");
        for (String keyWord : keyNameList) {
            sql.append("keyWord LIKE  '%");
            sql.append(keyWord);
            sql.append("%' OR ");
        }
        //移除多余的Sql
        sql.delete(sql.length() - 4, sql.length());
        logger.info("通过关键字查询method的Sql语句 [" + sql + "] ");
        return sql.toString();
    }

    /**
     * 通过方法属性查询Sql
     *
     * @param entity 方法
     * @return Sql
     */
    public static String findByEntity(Method entity) {
        StringBuilder sql = new StringBuilder("SELECT * FROM method WHERE  1=1 ");
        if (StringUtils.isNotEmpty(entity.getName())) {
            sql.append(" and name LIKE '%").append(entity.getName()).append("%' ");
        }
        if (StringUtils.isNotEmpty(entity.getType())) {
            sql.append(" and type = '").append(entity.getType()).append("' ");
        }
        if (StringUtils.isNotEmpty(entity.getLanguage())) {
            sql.append(" and language = '").append(entity.getLanguage()).append("' ");
        }
        if (StringUtils.isNotEmpty(entity.getAnswer())) {
            sql.append(" and answer = '").append(entity.getAnswer()).append("' ");
        }
        if (null != (entity.getIsOutOfDate())) {
            sql.append(" and isOutOfDate = '").append(entity.getIsOutOfDate()).append("' ");
        }
        if (StringUtils.isNotEmpty(entity.getKeyWord())) {
            String[] keywords = entity.getKeyWord().split(",");
            int num = 0;
            for (String keyword : keywords) {
                if (num == 0) {
                    sql.append(" and keyWord LIKE  '%");
                    num++;
                } else {
                    sql.append(" or keyWord LIKE  '%");
                }
                sql.append(keyword);
                sql.append("%' ");
            }
        }
        logger.info("通过方法属性查询method的Sql [" + sql + "] ");
        return sql.toString();
    }

    /**
     * 通过异常方法属性查询
     *
     * @param entity 异常信息
     * @return Sql
     */
    public static String findByExceptionEntity(ExceptionWay entity) {
        StringBuilder sql = new StringBuilder("SELECT * FROM exception WHERE 1=1");
        //{"'%"+keyword+"%'"}     这个数组比上面的正确的多了一堆单引号"'"，
        // 导致的结果是单引号被加入匹配串，实际匹配串成了" like ''%keyword%''"，
        // 意思为必须包含两边是单引号和中间任意关键字。问题出在多了一对单引号
        ////最后一个冒号前面的异常信息
        //        normalExceptionMsg = (WordFilter.replaceMarkTONumber(new StringBuffer
        //        (exceptionMsgArr[exceptionMsgArr.length - 1]).reverse().toString()));
        //        exceptionWay.setName(normalExceptionMsg);
        //        for (int exceptionNum = 1; exceptionNum < exceptionMsgArr.length;
        //        exceptionNum++) {
        //            exceptionMsgList.add(WordFilter.replaceMarkTONumber
        //            (exceptionMsgArr[exceptionNum]));
        //        }
        if (StringUtils.isNotEmpty(entity.getName())) {
            String[] exceptionMsgArr = entity.getName().split(";");
            for (int num = 0; num < exceptionMsgArr.length; num++) {
                if (num == 0) {
                    sql.append(" and name LIKE '%");
                } else {
                    sql.append(" or name LIKE '%");
                }
                sql.append(entity.getName());
                sql.append("%' ");
            }
        }
        if (StringUtils.isNotEmpty(entity.getType())) {
            sql.append(" and type = '").append(entity.getType()).append("' ");
        }
        if (StringUtils.isNotEmpty(entity.getLanguage())) {
            sql.append(" and language = '").append(entity.getLanguage()).append("' ");
        }
        if (StringUtils.isNotEmpty(entity.getAnswer())) {
            sql.append("and answer = '").append(entity.getAnswer()).append("' ");
        }
        if (null != (entity.getUseTimes())) {
            sql.append(" and useTimes = ").append(entity.getUseTimes());
        }
        if (null != (entity.getIsSolution())) {
            sql.append(" and isSolution = ").append(entity.getIsSolution());
        }
        if (StringUtils.isNotEmpty(entity.getKeyWord())) {
            String[] keywords = entity.getKeyWord().split(",");
            int num = 0;
            for (String keyword : keywords) {
                if (num == 0) {
                    sql.append(" and keyWord LIKE  '%");
                    num++;
                } else {
                    sql.append(" or keyWord LIKE  '%");
                }
                sql.append(keyword);
                sql.append("%' ");
            }
        }
        logger.info("查通过异常方法属性查询Exception的Sql语句 [" + sql + "] ");
        return sql.toString();
    }

    /**
     * 增加异常信息Sql
     *
     * @param entity 异常信息
     * @return Sql
     */
    public static String addByExceptionEntity(ExceptionWay entity) {
        StringBuilder sql = new StringBuilder("INSERT INTO exception");
        sql.append("(id,name");
        if (StringUtils.isNotEmpty(entity.getLanguage())) {
            sql.append(",language");
        }
        if (StringUtils.isNotEmpty(entity.getType())) {
            sql.append(",type");
        }
        if (StringUtils.isNotEmpty(entity.getKeyWord())) {
            sql.append(",keyWord");
        }
        sql.append(",answer,useTimes");
        if (StringUtils.isNotEmpty(entity.getDescription())) {
            sql.append(",description");
        }
        sql.append(",updateDate,isSolution");
        if (StringUtils.isNotEmpty(entity.getCause())) {
            sql.append(",cause");
        }
        sql.append(") VALUES");
        sql.append("('");
        sql.append(entity.getId());
        sql.append("','").append(entity.getName());
        if (StringUtils.isNotEmpty(entity.getLanguage())) {
            sql.append("','").append(entity.getLanguage());
        }
        if (StringUtils.isNotEmpty(entity.getType())) {
            sql.append("','").append(entity.getType());
        }
        if (StringUtils.isNotEmpty(entity.getKeyWord())) {
            sql.append("','").append(entity.getKeyWord());
        }
        sql.append("','").append(entity.getAnswer());
        sql.append("','").append(entity.getUseTimes());
        if (StringUtils.isNotEmpty(entity.getDescription())) {
            sql.append("','").append(entity.getDescription());
        }
        sql.append("','").append(simpleDateFormat.format(entity.getUpdateDate()));
        sql.append("','").append(entity.getIsSolution());
        if (StringUtils.isNotEmpty(entity.getCause())) {
            sql.append("','").append(entity.getCause());
        }
        sql.append("')");
        logger.info("查询Exception的Sql语句 [" + sql + "] ");
        return sql.toString();
    }

    /**
     * 根据ID删除异常信息
     *
     * @param id id
     * @return Sql
     */
    public static String delExceptionSql(Integer id) {
        return "DELETE FROM exception WHERE id = " + id;
    }

    /**
     * 更新异常信息
     *
     * @param entity 异常信息
     * @return Sql
     */
    public static String updateByExceptionEntitySql(ExceptionWay entity) {
        StringBuffer sql = new StringBuffer("UPDATE exception SET   ");
        if (StringUtils.isNotEmpty(entity.getName())) {
            sql.append("name = '");
            sql.append(entity.getName());
            sql.append("' , ");
        }
        if (StringUtils.isNotEmpty(entity.getType())) {
            sql.append("type = '");
            sql.append(entity.getType());
            sql.append("' , ");
        }
        if (StringUtils.isNotEmpty(entity.getLanguage())) {
            sql.append("language = '");
            sql.append(entity.getLanguage());
            sql.append("' , ");
        }
        if (StringUtils.isNotEmpty(entity.getKeyWord())) {
            sql.append("keyWord = '");
            sql.append(entity.getKeyWord());
            sql.append("' , ");
        }
        if (StringUtils.isNotEmpty(entity.getAnswer())) {
            sql.append("answer = '");
            sql.append(entity.getAnswer());
            sql.append("' , ");
        }
        if (StringUtils.isNotEmpty(entity.getDescription())) {
            sql.append("description = '");
            sql.append(entity.getDescription());
            sql.append("' , ");
        }
        if (null != entity.getIsSolution()) {
            sql.append("isSolution = '");
            sql.append(entity.getIsSolution());
            sql.append("' , ");
        }
        sql.delete(sql.length() - 3, sql.length());
        sql.append("WHERE id = ");
        sql.append(entity.getId());
        logger.info("更新Exception的Sql语句 [" + sql + "] ");
        return sql.toString();
    }

    /**
     * 通过关键字查询的Sql
     *
     * @param keyNameList 关键字列表
     * @return Sql
     */
    public static String findByExceptionKey(List<String> keyNameList) {
        StringBuilder sql = new StringBuilder("SELECT * FROM exception WHERE ");
        for (String keyWord : keyNameList) {
            sql.append("keyWord LIKE  '%");
            sql.append(keyWord);
            sql.append("%' OR ");
        }
        //移除多余的Sql
        sql.delete(sql.length() - 4, sql.length());
        logger.info("通过关键字查询Exception的Sql语句 [" + sql + "] ");
        return sql.toString();
    }

    /**
     * 通过异常信息名字查询Sql
     *
     * @param name 异常信息名字
     * @return Sql
     */
    public static String findByExceptionName(String name) {
        logger.info("通过异常信息名字查询exception的Sql语句 [SELECT * FROM exception WHERE name ='" + name +
                "'] ");
        return "SELECT * FROM exception WHERE name ='" + name + "'";
    }

    /**
     * 清除所有SQl
     *
     * @return Sql
     */
    public static String delAllException() {
        logger.info("清除所有exception的Sql>> DELETE  FROM exception");
        return "DELETE  FROM exception";
    }

    /**
     * 清除所有SQl
     *
     * @return Sql
     */
    public static String delAllMethod() {
        logger.info("清除所有method的Sql>> DELETE  FROM method");
        return "DELETE  FROM method";
    }

    /**
     * 删除Exception表
     *
     * @return Sql
     */
    public static String getDropExceptionTableSql() {
        logger.info("删除Exception表的Sql>> drop Table if exists exception");
        return "drop Table if exists exception";
    }

    /**
     * 删除Method表
     *
     * @return Sql
     */
    public static String getDropMethodTableSql() {
        logger.info("删除method表的Sql>> drop Table if exists method");
        return "drop Table if exists method";
    }

    /**
     * 删除systemInstallMsg表
     *
     * @return Sql
     */
    public static String getDropSystemInstallMsgTableSql() {
        logger.info("删除systemInstallMsg表的Sql>> drop Table if exists systemInstallMsg");
        return "drop Table if exists systemInstallMsg";
    }

    /**
     * 删除Linux命令信息表
     *
     * @return Sql
     */
    public static String getDropLinuxCmdMsgTableSql() {
        logger.info("删除linuxCmdMsg表的Sql>> drop Table if exists linuxCmdMsg");
        return "drop Table if exists linuxCmdMsg";
    }

    /**
     * 删除Linux命令样例信息表
     *
     * @return Sql
     */
    public static String getDropLinuxCmdExampleMsgTableSql() {
        logger.info("删除linuxCmdExamMsg表的Sql>> drop Table if exists linuxCmdExamMsg");
        return "drop Table if exists linuxCmdExamMsg";
    }

    /**
     * 默认创建数据表的Sql
     *
     * @return Sql
     */
    public static String getCreateLinuxCmdExampleMsgTableSql() {
        logger.info("默认创建数据表 linuxCmdExamMsg 的Sql ...");
        return "CREATE TABLE linuxCmdExamMsg\n" +
                "(\n" +
                "  id            BIGINT(500)  NOT NULL,         -- 主键\n" +
                "  parameterNote VARCHAR(500) NULL DEFAULT null,-- 参数说明\n" +
                "  examples      VARCHAR(500) NULL DEFAULT null,-- 多个实例，使用中文；作为换行\n" +
                "  insertDate    date         NULL DEFAULT null,-- 添加方法时间\n" +
                "  updateTimes   INTEGER(5)   NULL DEFAULT null,-- 更新次数\n" +
                "  updateDate    date         NULL DEFAULT NULL,-- 更新方法时间\n" +
                "  enable        INTEGER(2)   NULL DEFAULT NULL\n" +
                ");";
    }

    /**
     * 默认创建数据表的Sql
     *
     * @return Sql
     */
    public static String getCreateLinuxCmdMsgTableSql() {
        logger.info("默认创建数据表 linuxCmdMsg 的Sql ...");
        return "CREATE TABLE linuxCmdMsg\n" +
                "(\n" +
                "  id             BIGINT(500)  NOT NULL,         -- 主键\n" +
                "  linuxCmdExamId BIGINT(500)  NULL DEFAULT null,-- 关联表主键\n" +
                "  linuxCmd       VARCHAR(50)  NULL DEFAULT null,-- Linux命令\n" +
                "  usages         VARCHAR(100) NULL DEFAULT null,-- 作用\n" +
                "  example        VARCHAR(200) NULL DEFAULT null,-- 用法\n" +
                "  permission     VARCHAR(50)  NULL DEFAULT null,-- 使用权限\n" +
                "  insertDate     date         NULL DEFAULT null,-- 添加方法时间\n" +
                "  updateTimes    INTEGER(5)   NULL DEFAULT null,-- 更新次数\n" +
                "  updateDate     date         NULL DEFAULT NULL,-- 更新方法时间\n" +
                "  enable         INTEGER(2)   NULL DEFAULT NULL\n" +
                ");";
    }

    /**
     * 默认创建数据表的Sql
     *
     * @return Sql
     */
    public static String getCreateSystemInstallMsgTableSql() {
        logger.info("默认创建数据表systemInstallMsg的Sql ...");
        return "CREATE TABLE systemInstallMsg\n" +
                "(\n" +
                "  id            INTEGER(500) NOT NULL,\n" +
                "  name          VARCHAR(300) NULL DEFAULT null,\n" +
                "  version       VARCHAR(100) NULL DEFAULT null,\n" +
                "  publisher     VARCHAR(200) NULL DEFAULT null,\n" +
                "  installAddr   VARCHAR(500) NULL DEFAULT null,\n" +
                "  installDate   VARCHAR(10)  NULL DEFAULT null,\n" +
                "  unInstallAddr VARCHAR(500) NULL DEFAULT null,\n" +
                "  updateDate    date         NULL DEFAULT NULL,\n" +
                "  enable        INTEGER(2)   NULL DEFAULT NULL\n" +
                ")";
    }

    /**
     * 默认创建数据表的Sql
     *
     * @return Sql
     */
    public static String getCreateExceptionTableSql() {
        logger.info("默认创建数据表exception的Sql ...");
        return "CREATE TABLE exception\n" +
                "(\n" +
                "  id          INTEGER(500)  NOT NULL,\n" +
                "  useTimes    INTEGER(500)  NULL DEFAULT null,\n" +
                "  isSolution  INTEGER(4)    NULL DEFAULT null,\n" +
                "  name        VARCHAR(800)  NULL DEFAULT null,\n" +
                "  type        VARCHAR(100)  NULL DEFAULT null,\n" +
                "  language    VARCHAR(100)  NULL DEFAULT null,\n" +
                "  answer      VARCHAR(800)  NULL DEFAULT null,\n" +
                "  cause       VARCHAR(800)  NULL DEFAULT null,\n" +
                "  keyWord     VARCHAR(300)  NULL DEFAULT null,\n" +
                "  description VARCHAR(1000) NULL DEFAULT null,\n" +
                "  updateDate  date          NULL DEFAULT NULL,\n" +
                "  remark      VARCHAR(500)  NULL DEFAULT null\n" +
                ")";
    }

    /**
     * 默认创建数据表的Sql
     *
     * @return Sql
     */
    public static String getCreateMethodTableSql() {
        logger.info("默认创建数据表method ...");
        return "CREATE TABLE method\n" +
                "(\n" +
                "  id           INTEGER(500) NOT NULL,\n" +
                "  useTimes     INTEGER(50)  NOT NULL,\n" +
                "  isOutOfDate  INTEGER(4)   NOT NULL,\n" +
                "  name         VARCHAR(200) NULL DEFAULT null,\n" +
                "  type         VARCHAR(100) NULL DEFAULT null,\n" +
                "  language     VARCHAR(100) NULL DEFAULT null,\n" +
                "  keyWord      VARCHAR(300) NULL DEFAULT null,\n" +
                "  answer       VARCHAR(200) NULL DEFAULT null,\n" +
                "  description  VARCHAR(500) NULL DEFAULT null,\n" +
                "  remark       VARCHAR(500) NULL DEFAULT null,\n" +
                "  updateDate   date         NULL DEFAULT NULL\n" +
                ")";
    }

    /**
     * 增加安装软件信息Sql
     *
     * @param entity 安装软件信息
     * @return Sql
     */
    public static String addBySystemInstallMsgEntitySql(SystemInstallMsg entity) {
        StringBuilder sql = new StringBuilder("INSERT INTO systemInstallMsg");
        sql.append("(id,name");
        if (StringUtils.isNotEmpty(entity.getVersion())) {
            sql.append(",version");
        }
        if (StringUtils.isNotEmpty(entity.getPublisher())) {
            sql.append(",publisher");
        }
        if (StringUtils.isNotEmpty(entity.getInstallAddr())) {
            sql.append(",installAddr");
        }
        if (StringUtils.isNotEmpty(entity.getInstallDate())) {
            sql.append(",installDate");
        }
        sql.append(",updateDate");
        if (StringUtils.isNotEmpty(entity.getUnInstallAddr())) {
            sql.append(",unInstallAddr");
        }
        sql.append(") VALUES('");
        sql.append(entity.getId());
        sql.append("','").append(entity.getName());
        if (StringUtils.isNotEmpty(entity.getVersion())) {
            sql.append("','").append(entity.getVersion());
        }
        if (StringUtils.isNotEmpty(entity.getPublisher())) {
            sql.append("','").append(entity.getPublisher());
        }
        if (StringUtils.isNotEmpty(entity.getInstallAddr())) {
            sql.append("','").append(entity.getInstallAddr());
        }
        if (StringUtils.isNotEmpty(entity.getInstallDate())) {
            sql.append("','").append(entity.getInstallDate());
        }
        sql.append("','").append(simpleDateFormat.format(entity.getUpdateDate()));
        if (StringUtils.isNotEmpty(entity.getUnInstallAddr())) {
            sql.append("','").append(entity.getUnInstallAddr());
        }
        sql.append("')");
        logger.info("查询systemInstallMsg的Sql>> [" + sql + "] ");
        return sql.toString();
    }

    /**
     * 更新 SystemInstallMsg
     *
     * @param entity SystemInstallMsg
     * @return SQl
     */
    public static String updateBySystemInstallMsgSql(SystemInstallMsg entity) {
        StringBuilder sql = new StringBuilder("UPDATE systemInstallMsg SET ");
        if (null != entity.getName() && !Constants.EMPTY_STR.equals(entity.getName())) {
            sql.append("name = '");
            sql.append(entity.getName()).append("' ");
        }
        if (null != entity.getVersion() && !Constants.EMPTY_STR.equals(entity.getVersion())) {
            sql.append(" ,version = '");
            sql.append(entity.getVersion()).append("' ");
        }
        if (null != entity.getPublisher() && !Constants.EMPTY_STR.equals(entity.getPublisher())) {
            sql.append(",publisher = '");
            sql.append(entity.getPublisher()).append("' ");
        }
        if (null != entity.getInstallAddr() && !Constants.EMPTY_STR.equals(entity.getInstallAddr())) {
            sql.append(", installAddr = '");
            sql.append(entity.getInstallAddr()).append("' ");
        }
        if (null != entity.getInstallDate() && !Constants.EMPTY_STR.equals(entity.getInstallDate())) {
            sql.append(",installDate = '");
            sql.append(entity.getInstallDate()).append("' ");
        }
        if (null != entity.getUnInstallAddr() && !Constants.EMPTY_STR.equals(entity.getUnInstallAddr())) {
            sql.append(",unInstallAddr = '");
            sql.append(entity.getUnInstallAddr()).append("' ");
        }
        sql.append(",updateDate='");
        sql.append(simpleDateFormat.format(entity.getUpdateDate())).append("' ");
        sql.append("WHERE id = ").append(entity.getId());
        logger.info("更新systemInstallMsg的Sql>> [" + sql + "] ");
        return sql.toString();
    }

    /**
     * 模糊查询
     *
     * @param keyList 关键字列表
     * @return Sql
     */
    public static String findBySystemInstallMsgKey(List<String> keyList) {
        StringBuilder sql = new StringBuilder("SELECT * FROM systemInstallMsg WHERE ");
        for (String name : keyList) {
            sql.append("name LIKE  '%");
            sql.append(name);
            sql.append("%' OR ");
        }
        //移除多余的Sql
        sql.delete(sql.length() - 4, sql.length());
        logger.info("模糊查询systemInstallMsg的Sql>> [" + sql + "] ");
        return sql.toString();
    }

    /**
     * 模糊查询systemInstallMsg name的Sql语句
     *
     * @param name 异常信息名称
     * @return Sql
     */
    public static String findBySystemInstallMsgName(String name) {
        logger.info("模糊查询systemInstallMsg name的Sql>> SELECT * FROM systemInstallMsg WHERE name = " +
                "'" + name + "' ");
        return "SELECT * FROM systemInstallMsg WHERE name = '" + name + "'";
    }

    /**
     * 查询所有的systemInstallMsg 的Sql语句
     *
     * @return Sql
     */
    public static String findAllSystemInstallMsg() {
        logger.info("查询所有的systemInstallMsg 的Sql>> SELECT * FROM systemInstallMsg");
        return "SELECT * FROM systemInstallMsg ";
    }

    /**
     * 计数数据库条目的Sql
     *
     * @return Sql
     */
    public static String countLinuxCmdNum() {
        logger.info("计数数据库表linuxCmdMsg条目的Sql>> SELECT count(*) as count FROM linuxCmdMsg");
        return "SELECT count(*) as count FROM linuxCmdMsg";
    }

    /**
     * 查询所有的 LinuxCmd Sql
     *
     * @return Sql
     */
    public static String findAllLinuxCmdMsg() {
        return "SELECT *  FROM linuxCmdMsg ORDER BY linuxCmd";
    }

    /**
     * 根据ID查询LinuxCmdExam信息 Sql
     *
     * @param id id
     * @return Sql
     */
    public static String getLinuxCmdExamById(Integer id) {
        logger.info("根据ID查询LinuxCmdExam信息的SQL>>  SELECT *  FROM linuxCmdExamMsg WHERE id=" + id);
        return "SELECT *  FROM linuxCmdExamMsg WHERE id=" + id;
    }

    /**
     * 根据ID查询LinuxCmd信息 Sql
     *
     * @param id id
     * @return Sql
     */
    public static String findLinuxCmdMsgByID(Integer id) {
        logger.info("查根据ID查询LinuxCmd信息的SQL>>  SELECT *  FROM linuxCmdMsg WHERE id=" + id);
        return "SELECT *  FROM linuxCmdMsg WHERE id=" + id;
    }

    /**
     * 根据ID删除LinuxCmd信息 Sql
     *
     * @param id id
     * @return Sql
     */
    public static String delLinuxCmdMsgById(long id) {
        logger.info("根据ID删除LinuxCmd信息的SQL>>  DELETE FROM linuxCmdMsg WHERE id=" + id);
        return "DELETE FROM linuxCmdMsg WHERE id=" + id;
    }

    /**
     * 根据ID删除LinuxCmdExamMsg信息 Sql
     *
     * @param linuxCmdExamId id
     * @return Sql
     */
    public static String delLinuxCmdExamMsgById(Integer linuxCmdExamId) {
        logger.info("根据ID删除LinuxCmdExamMsg信息的SQL>>  DELETE FROM linuxCmdExamMsg WHERE id=" + linuxCmdExamId);
        return "DELETE FROM linuxCmdExamMsg WHERE id=" + linuxCmdExamId;
    }

    /**
     * 通过名称查询Linux命令信息Sql
     *
     * @param name Linux命令名字
     * @return Sql
     */
    public static String findAllLinuxCmdMsgByName(String name) {
        logger.info("通过名称查询Linux命令信息Sql>> SELECT *  FROM linuxCmdMsg WHERE linuxCmd='" + name +
                "'");
        return "SELECT *  FROM linuxCmdMsg WHERE linuxCmd='" + name + "'";
    }

    /**
     * 保存Linux命令Sql
     *
     * @param linuxCmd Linux命令
     * @return Sql
     */
    public static String addByLinuxCmd(LinuxCmd linuxCmd) {
        StringBuilder sql = new StringBuilder("INSERT INTO linuxCmdMsg");
        //id,Linux命令,作用,用法
        sql.append("(id,linuxCmd,usages,example");
        //关联表主键
        if (null != linuxCmd.getLinuxCmdExam()) {
            sql.append(",linuxCmdExamId");
        }
        //使用权限
        if (StringUtils.isNotEmpty(linuxCmd.getPermission())) {
            sql.append(",permission");
        }
        sql.append(",updateTimes,insertDate,updateDate");

        sql.append(") VALUES(");
        sql.append(linuxCmd.getId());
        sql.append(",'").append(linuxCmd.getLinuxCmd()).append("'");
        sql.append(",'").append(linuxCmd.getUsage()).append("'");
        sql.append(",'").append(linuxCmd.getExample()).append("'");
        if (null != linuxCmd.getLinuxCmdExam()) {
            sql.append(",").append(linuxCmd.getLinuxCmdExam().getId());
        }
        //使用权限
        if (StringUtils.isNotEmpty(linuxCmd.getPermission())) {
            sql.append(",'").append(linuxCmd.getPermission()).append("'");
        }
        sql.append(",").append(linuxCmd.getUpdateTimes());
        //添加方法时间
        sql.append(",'").append(simpleDateFormat.format(linuxCmd.getInsertDate())).append("'");
        //更新方法时间
        sql.append(",'").append(simpleDateFormat.format(linuxCmd.getUpdateDate())).append("')");
        logger.info("保存Linux命令的Sql>> " + sql.toString());
        return sql.toString();
    }

    /**
     * 保存LinuxExam关联表信息Sql
     *
     * @param linuxCmdExam Linux命令关联信息
     * @return Sql
     */
    public static String addByLinuxCmdExam(LinuxCmdExam linuxCmdExam) {
        StringBuilder sql = new StringBuilder("INSERT INTO linuxCmdExamMsg");
        //主键,添加方法时间,更新方法时间,更新次数
        sql.append("(id,insertDate,updateDate,updateTimes");
        //使用中文；作为换行
        if (StringUtils.isNotEmpty(linuxCmdExam.getParameterNote())) {
            sql.append(",parameterNote");
        }
        //多个实例，使用中文；作为换行
        if (StringUtils.isNotEmpty(linuxCmdExam.getExamples())) {
            sql.append(",examples");
        }
        sql.append(") VALUES(");
        sql.append(linuxCmdExam.getId());
        //添加方法时间
        sql.append(",'").append(simpleDateFormat.format(linuxCmdExam.getInsertDate())).append("'");
        //更新方法时间
        sql.append(",'").append(simpleDateFormat.format(linuxCmdExam.getUpdateDate())).append("'");
        sql.append(",").append(linuxCmdExam.getUpdateTimes());
        String line = System.getProperty("line.separator");
        //使用中文；作为换行
        if (StringUtils.isNotEmpty(linuxCmdExam.getParameterNote())) {
            sql.append(",'").append(linuxCmdExam.getParameterNote().replaceAll(line, "；")).append("'");
        }
        //多个实例，使用中文；作为换行
        if (StringUtils.isNotEmpty(linuxCmdExam.getExamples())) {
            sql.append(",'").append(linuxCmdExam.getExamples().replaceAll(line, "；")).append("'");
        }
        sql.append(")");
        logger.info("保存Linux命令关联信息的Sql>> " + sql.toString());
        return sql.toString();
    }

    /**
     * 清空数据表LinuxCmdMsg Sql
     *
     * @return Sql
     */
    public static String delAllLinuxCmd() {
        logger.info("清空数据表linuxCmdMsg Sql>> DELETE FROM linuxCmdMsg");
        return "DELETE FROM linuxCmdMsg";
    }

    /**
     * 清空数据表LinuxCmdExamMsg Sql
     *
     * @return Sql
     */
    public static String delAllLinuxCmdExam() {
        logger.info("清空数据表linuxCmdExamMsg Sql>> DELETE FROM linuxCmdMsg");
        return "DELETE FROM linuxCmdExamMsg";
    }

    /**
     * 更新Linux命令Sql
     *
     * @param linuxCmd Linux命令
     * @return Sql
     */
    public static String updateLinuxCmdMsgSql(LinuxCmd linuxCmd) {
        StringBuilder sql = new StringBuilder("UPDATE linuxCmdMsg SET ");
        //Linux命令
        sql.append("linuxCmd = '").append(linuxCmd.getLinuxCmd()).append("' ");
        //作用
        if (StringUtils.isNotEmpty(linuxCmd.getUsage())) {
            sql.append(",").append("usages = '").append(linuxCmd.getUsage()).append("' ");
        }
        //用法
        if (StringUtils.isNotEmpty(linuxCmd.getExample())) {
            sql.append(",").append("example = '").append(linuxCmd.getExample()).append("' ");
        }
        //使用权限
        if (StringUtils.isNotEmpty(linuxCmd.getPermission())) {
            sql.append(",").append("permission = '").append(linuxCmd.getPermission()).append("' ");
        }
        //更新方法时间
        sql.append(",").append("updateDate = '").append(simpleDateFormat.format(new Date())).append("' ");
        //更新次数
        sql.append(",").append("updateTimes = '").append(linuxCmd.getUpdateTimes() + 1).append("'" +
                " ");
        //关联表主键
        if (null != linuxCmd.getLinuxCmdExam()) {
            sql.append(",").append("linuxCmdExamId = ").append(linuxCmd.getLinuxCmdExam().getId());
        }
        sql.append(" where id= ").append(linuxCmd.getId());
        logger.info("更新Linux命令的Sql>> " + sql.toString());
        return sql.toString();
    }

    /**
     * 更新Linux命令关联表Sql
     *
     * @param linuxCmdExam LinuxCmdExam
     * @return Sql
     */
    public static String updateLinuxCmdExamMsgSql(LinuxCmdExam linuxCmdExam) {
        StringBuilder sql = new StringBuilder("UPDATE linuxCmdExamMsg SET ");
        //更新方法时间
        sql.append("updateDate = '").append(simpleDateFormat.format(new Date())).append("' ");
        //更新次数
        sql.append(",").append("updateTimes = '").append(linuxCmdExam.getUpdateTimes() + 1).append("' ");
        //参数说明
        if (StringUtils.isNotEmpty(linuxCmdExam.getParameterNote())) {
            sql.append(",").append("parameterNote = '").append(linuxCmdExam.getParameterNote()).append("' ");
        }
        //多个实例，使用中文；作为换行
        if (StringUtils.isNotEmpty(linuxCmdExam.getExamples())) {
            sql.append(",").append("examples = '").append(linuxCmdExam.getExamples()).append("' ");
        }
        sql.append(" where id= ").append(linuxCmdExam.getId());
        logger.info("更新Linux命令linuxCmdExamMsg的Sql>> " + sql.toString());
        return sql.toString();
    }

    /**
     * 清空数据表 noteMsg Sql
     *
     * @return Sql
     */
    public static String getDropNoteMsgTableSql() {
        logger.info("清空数据表noteMsg Sql>> drop table if exists noteMsg;");
        return "drop table if exists noteMsg;";
    }

    /**
     * 创建数据表 noteMsg Sql
     *
     * @return Sql
     */
    public static String getCreateNoteMsgTableSql() {
        logger.info("创建数据表 noteMsg");
        return "CREATE TABLE noteMsg\n" +
                "(\n" +
                "  id          BIGINT(100)  NOT NULL,         -- 主键\n" +
                "  note        TEXT(2000)   NULL DEFAULT null,-- 笔记\n" +
                "  name        VARCHAR(100) NULL DEFAULT null,-- 笔记名称\n" +
                "  noteType    VARCHAR(100) NULL DEFAULT null,-- 笔记类型\n" +
                "  explains    VARCHAR(500) NULL DEFAULT null,-- 笔记说明\n" +
                "  insertDate  date         NULL DEFAULT null,-- 添加方法时间\n" +
                "  updateTimes INTEGER(5)   NULL DEFAULT null,-- 更新次数\n" +
                "  updateDate  date         NULL DEFAULT NULL,-- 更新方法时间\n" +
                "  enable      INTEGER(2)   NULL DEFAULT NULL -- 是否启用\n" +
                ");";
    }

    /**
     * 计数NoteMsg数据量Sql
     *
     * @return Sql
     */
    public static String countNoteMsgNum() {
        logger.info("计数数据库表noteMsg条目的Sql>> SELECT count(*) as count FROM noteMsg");
        return "SELECT count(*) as count FROM noteMsg";
    }

    /**
     * 获取所有的笔记信息名称Sql
     *
     * @return Sql
     */
    public static String findAllNoteMsgName() {
        logger.info("获取所有的笔记信息名称Sql>> SELECT name as single  FROM noteMsg");
        return "SELECT name  as single  FROM noteMsg";
    }

    /**
     * 清空数据表 noteMsg数据 Sql
     *
     * @return Sql
     */
    public static String delAllNoteMsg() {
        logger.info("清空数据表noteMsg Sql>> DELETE FROM noteMsg");
        return "DELETE FROM noteMsg";
    }

    /**
     * 通过笔记名称查询笔记信息Sql
     *
     * @param name 笔记名称
     * @return Sql
     */
    public static String findNoteMsgByName(String name) {
        logger.info("通过笔记名称查询笔记信息Sql>> SELECT * FROM noteMsg WHERE name='" + name + "'");
        return " SELECT * FROM noteMsg WHERE name='" + name + "'";
    }

    /**
     * 通过笔记id查询笔记信息Sql
     *
     * @param id 笔记id
     * @return Sql
     */
    public static String findNoteMsgById(Integer id) {
        logger.info("通过笔记id查询笔记信息Sql>> SELECT * FROM noteMsg WHERE id=" + id);
        return " SELECT * FROM noteMsg WHERE id=" + id;
    }

    /**
     * 通过id删除信息 Sql
     *
     * @param id id
     * @return Sql
     */
    public static String delNoteMsgById(Integer id) {
        logger.info("通过笔记id删除笔记信息Sql>> DELETE FROM noteMsg WHERE id=" + id);
        return " DELETE  FROM noteMsg WHERE id=" + id;
    }

    /**
     * 保存笔记信息Sql
     *
     * @param entity 笔记信息
     * @return Sql
     */
    public static String saveNoteMsgSql(NoteMsg entity) {
        StringBuilder sql = new StringBuilder("INSERT INTO noteMsg");
        sql.append("(id,name,note");
        if (StringUtils.isNotEmpty(entity.getNoteType())) {
            sql.append(",noteType");
        }
        if (StringUtils.isNotEmpty(entity.getExplains())) {
            sql.append(",explains");
        }
        sql.append(",updateDate");
        sql.append(",insertDate");
        sql.append(",updateTimes");
        sql.append(",enable");

        sql.append(") VALUES('");
        sql.append(entity.getId());
        sql.append("','").append(entity.getName());
        sql.append("','").append(entity.getNote());
        //笔记类型
        if (StringUtils.isNotEmpty(entity.getNoteType())) {
            sql.append("','").append(entity.getNoteType());
        }
        //笔记说明
        if (StringUtils.isNotEmpty(entity.getExplains())) {
            sql.append("','").append(entity.getExplains());
        }
        sql.append("','").append(simpleDateFormat.format(entity.getUpdateDate()));
        sql.append("','").append(simpleDateFormat.format(entity.getInsertDate()));
        sql.append("','").append(entity.getUpdateTimes());
        sql.append("','").append(true);
        sql.append("')");
        logger.info("新增noteMsg的Sql>> [" + sql + "] ");
        return sql.toString();
    }

    /**
     * 更新笔记信息Sql
     *
     * @param entity 笔记信息
     * @return Sql
     */
    public static String updateNoteMsgSql(NoteMsg entity) {
        StringBuilder sql = new StringBuilder("UPDATE noteMsg SET ");
        sql.append("note ='").append(entity.getNote());
        if (StringUtils.isNotEmpty(entity.getNoteType())) {
            sql.append(",noteType='").append(entity.getNoteType()).append("' ");
        }
        sql.append("' where id= ").append(entity.getId());
        logger.info("更新noteMsg的Sql>> [" + sql + "] ");
        return sql.toString();
    }

    /**
     * 通过笔记名称查询所有的笔记名称Sql
     *
     * @param noteType 笔记名称
     * @return Sql
     */
    public static String findAllNoteMsgNameByType(String noteType) {

        logger.info("通过笔记名称查询所有的笔记名称Sql>> SELECT name as single  FROM noteMsg WHERE noteType='" + noteType + "' ORDER BY name");
        return "SELECT name  as single  FROM noteMsg WHERE noteType='" + noteType + "' ORDER BY " +
                "name";
    }

    /**
     * 清空皮肤信息Sql
     *
     * @return Sql
     */
    public static String delAllSkinMsg() {
        logger.info("清除所有skin的Sql>> DELETE  FROM method");
        return "DELETE  FROM skin";
    }

    /**
     * 删除skin表Sql
     *
     * @return Sql
     */
    public static String getDropSkinTableSql() {
        logger.info("清空数据表skin Sql>> drop table if exists skin;");
        return "drop table if exists skin;";
    }

    /**
     * 新建skin表Sql
     *
     * @return Sql
     */
    public static String getCreateSkinTableSql() {
        logger.info("新建数据表skin ");
        return "CREATE TABLE skin\n" +
                "(\n" +
                "  id               INTEGER(2) NOT NULL,\n" +
                "  themName         VARCHAR(100) NULL DEFAULT null,\n" +
                "  borderName       VARCHAR(100) NULL DEFAULT null,\n" +
                "  waterMarkName    VARCHAR(100) NULL DEFAULT null,\n" +
                "  buttonShaperName VARCHAR(100) NULL DEFAULT null,\n" +
                "  skinName         VARCHAR(100) NULL DEFAULT null,\n" +
                "  fontPolicyName   VARCHAR(300)  NULL DEFAULT null,\n" +
                "  titlePainterName VARCHAR(100) NULL DEFAULT null,\n" +
                "  updateDate       date         NULL DEFAULT NULL,\n" +
                "  enable           INTEGER(2)   NULL DEFAULT NULL\n" +
                ");";
    }

    /**
     * 查询所有的皮肤Sql
     *
     * @return Sql
     */
    public static String findAllSkin() {
        logger.info("查询所有的skin 的Sql>> SELECT * FROM skin");
        return "SELECT * FROM skin ";
    }

    /**
     * 增加皮肤信息Sql
     *
     * @param entity 皮肤实体类
     * @return Sql
     */
    public static String addBySkinLookAndFeelEntity(SkinLookAndFeel entity) {

        StringBuilder sql = new StringBuilder("INSERT INTO skin");
        sql.append("(id");
        if (StringUtils.isNotEmpty(entity.getThemName())) {
            sql.append(",themName");
        }
        if (StringUtils.isNotEmpty(entity.getSkinName())) {
            sql.append(",skinName");
        }
        if (StringUtils.isNotEmpty(entity.getBorderName())) {
            sql.append(",borderName");
        }
        if (StringUtils.isNotEmpty(entity.getWaterMarkName())) {
            sql.append(",waterMarkName");
        }
        if (StringUtils.isNotEmpty(entity.getButtonShaperName())) {
            sql.append(",buttonShaperName");
        }
        sql.append(",updateDate");
        if (StringUtils.isNotEmpty(entity.getTitlePainterName())) {
            sql.append(",titlePainterName");
        }
        if (StringUtils.isNotEmpty(entity.getFontPolicyName())) {
            sql.append(",fontPolicyName");
        }
        sql.append(") VALUES");
        sql.append("('");
        sql.append(entity.getId());
        if (StringUtils.isNotEmpty(entity.getThemName())) {
            sql.append("','").append(entity.getThemName());
        }
        if (StringUtils.isNotEmpty(entity.getSkinName())) {
            sql.append("','").append(entity.getSkinName());
        }
        if (StringUtils.isNotEmpty(entity.getBorderName())) {
            sql.append("','").append(entity.getBorderName());
        }
        if (StringUtils.isNotEmpty(entity.getWaterMarkName())) {
            sql.append("','").append(entity.getWaterMarkName());
        }
        if (StringUtils.isNotEmpty(entity.getButtonShaperName())) {
            sql.append("','").append(entity.getButtonShaperName());
        }
        sql.append("','").append(simpleDateFormat.format(entity.getUpdateDate()));
        if (StringUtils.isNotEmpty(entity.getTitlePainterName())) {
            sql.append("','").append(entity.getTitlePainterName());
        }
        if (StringUtils.isNotEmpty(entity.getFontPolicyName())) {
            sql.append("','").append(entity.getFontPolicyName());
        }
        sql.append("')");
        logger.info("保存skin的Sql语句 [" + sql + "] ");
        return sql.toString();
    }
}