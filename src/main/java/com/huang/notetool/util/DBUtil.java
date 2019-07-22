package com.huang.notetool.util;

//import com.alibaba.druid.pool.DruidDataSource;

import com.huang.notetool.po.*;
import com.huang.notetool.tool.FindDir;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DBUtil {
    /**
     * ORACLE Query 验证
     */
    private static final String ORACLE_VALIDATION_QUERY = "select 1 from dual";
    /**
     * DB2 Query 验证
     */
    private static final String DB_2_VALIDATION_QUERY = "select 1 from sysibm.sysdummy1";
    /**
     * HSQLDB Query 验证
     */
    private static final String HSQLDB_VALIDATION_QUERY = "select 1 from INFORMATION_SCHEMA" +
            ".SYSTEM_USERS";
    /**
     * POSTGRESQL Query 验证
     */
    private static final String POST_GRE_SQL_VALIDATION_QUERY = "select version()";
    /**
     * INFORMIX Query 验证
     */
    private static final String INFORMIX_VALIDATION_QUERY = "select count(*) from systables";
    /**
     * 大多数 Query 验证（MySQL、SQL server 等等）
     */
    private static final String NORMAL_AND_MANY_DB_VALIDATION_QUERY = "select 1";
    /**
     * 如果存在，此数据不为false，不用每次都检查
     */
    private static boolean isDatabaseExits;
    /**
     * 数据库文件名字
     */
    private static String dbName = "notetool.db";
    private static DataSource ds;
    private static String driver = "org.sqlite.JDBC";
    private static String url = "jdbc:sqlite:" + FilePath.sqlitePath + dbName;
    //	private static String url="jdbc:mysql://127.0.0.1:3306/notebook?setCharacterEncoding
    //	('utf-8')";
    private static String username = "";
    private static String password = "";
    private static Logger logger = Logger.getLogger(DBUtil.class);


    static {
        //最先运行
        setDataSource();
        checkDataBaseIsExists();
    }

    private static void setDataSource() {
        BasicDataSource bds = new BasicDataSource();
        logger.info("初始化数据连接池 ");
        bds.setDriverClassName(driver);
        bds.setUrl(url);
        bds.setUsername(username);
        bds.setPassword(password);
        DBUtil.ds = bds;
        //        DruidDataSource dataSource = new DruidDataSource();
        //        logger.info("初始化数据连接池 ");
        //        dataSource.setDriverClassName(driver);
        //        dataSource.setUrl(url);
        //        dataSource.setUsername(username);
        //        dataSource.setPassword(password);
        //        //初始化连接数
        //        dataSource.setInitialSize(0);
        //        //最大允许的连接数
        //        dataSource.setMaxActive(8);
        //        //最小的空闲连接数
        //        dataSource.setMinIdle(0);
        //        //不同的数据源的Sql验证有差异
        //        if (driver.contains(Constants.ORACLE_EN)
        //                || driver.contains(Constants.ORACLE_CAPITAL_EN)) {
        //            dataSource.setPoolPreparedStatements(true);
        //            dataSource.setMaxOpenPreparedStatements(4);
        //            dataSource.setValidationQuery(ORACLE_VALIDATION_QUERY);
        //        } else if (driver.contains(Constants.DB_2_CAPITAL_EN)
        //                || driver.contains(Constants.DB_2_EN)) {
        //            dataSource.setValidationQuery(DB_2_VALIDATION_QUERY);
        //        } else if (driver.contains(Constants.HSQLDB_CAPITAL_EN)
        //                || driver.contains(Constants.HSQLDB_EN)) {
        //            dataSource.setValidationQuery(HSQLDB_VALIDATION_QUERY);
        //        } else if (driver.contains(Constants.POST_GRE_SQL_CAPITAL_EN)
        //                || driver.contains(Constants.POST_GRE_SQL_EN)) {
        //            dataSource.setValidationQuery(POST_GRE_SQL_VALIDATION_QUERY);
        //        } else if (driver.contains(Constants.INFORMIX_CAPITAL_EN)
        //                || driver.contains(Constants.INFORMIX_EN)) {
        //            dataSource.setValidationQuery(INFORMIX_VALIDATION_QUERY);
        //        } else {
        //            dataSource.setValidationQuery(NORMAL_AND_MANY_DB_VALIDATION_QUERY);
        //        }
        //        //获取连接等待的超时时间 5s
        //        dataSource.setMaxWait(5000);
        //        //检测需要关闭的空闲连接的间隔时间
        //        dataSource.setTimeBetweenEvictionRunsMillis(30000);
        //        //设置是否保持连接活动
        //        dataSource.setKeepAlive(false);
        //        //连接在池中的最小生存时间
        //        dataSource.setMinEvictableIdleTimeMillis(300000);
        //        //设置空闲时是否检测连接可用性
        //        dataSource.setTestWhileIdle(true);
        //        //设置获取连接时是否检测连接可用性(会影响性能)
        //        dataSource.setTestOnBorrow(false);
        //        //归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
        //        dataSource.setTestOnReturn(false);
        //        //校验连接是否可用的超时时间
        //        dataSource.setValidationQueryTimeout(10);
        //        //设置获取连接出错时的自动重连次数
        //        dataSource.setConnectionErrorRetryAttempts(2);
        //        //设置获取连接出错时是否马上返回错误，true为马上返回
        //        dataSource.setFailFast(true);
        //        //设置获取连接时的重试次数，-1为不重试
        //        dataSource.setNotFullTimeoutRetryCount(2);
        //        //true表示向数据库请求连接失败后,就算后端数据库恢复正常也不进行重连,客户端对pool的请求都拒绝掉.
        //        dataSource.setBreakAfterAcquireFailure(true);
        //        DBUtil.ds = dataSource;
    }

    /**
     * 检查数据库文件是否存在
     */
    private static void checkDataBaseIsExists() {
        //不存在，则创建数据库文件
        if (!isDatabaseExits) {
            String dir = FilePath.sqlitePath;
            String fileSep = System.getProperty("file.separator");
            File file = new File(FindDir.getProjectDir() + fileSep + "NoteBook" + fileSep);
            if (!file.exists()) {
                if (file.mkdir()) {
                    file = new File(dir + System.getProperty("file.separator"));
                    if (!file.exists()) {
                        if (file.mkdir()) {
                            logger.info("创建文件夹成功");
                        }
                    } else {
                        logger.info("创建文件夹" + file.getAbsolutePath() + "失败");
                        isDatabaseExits = false;
                        return;
                    }
                } else {
                    logger.info("创建文件夹" + file.getAbsolutePath() + "失败");
                    isDatabaseExits = false;
                    return;
                }
            }
            logger.info("数据库路径：[ " + file.getAbsolutePath() + "，数据库名字" + dbName);
            file = new File(dir + System.getProperty("file.separator") + dbName);
            if (file.exists() && file.isFile()) {
                isDatabaseExits = true;
                return;
            }
            isDatabaseExits = connection(UtilSql.getDropExceptionTableSql());
            if (isDatabaseExits) {
                isDatabaseExits = connection(UtilSql.getDropMethodTableSql());
            }
            if (isDatabaseExits) {
                isDatabaseExits = connection(UtilSql.getCreateExceptionTableSql());
            }
            if (isDatabaseExits) {
                isDatabaseExits = connection(UtilSql.getCreateMethodTableSql());
            }
            createSystemInstallMsgTable();
            createLinuxCmdTables();
            createNoteMsgTables();
            createSkinTables();
        }
    }

    /**
     * 创建skin数据表
     */
    private static void createSkinTables() {
        if (isDatabaseExits) {
            isDatabaseExits = connection(UtilSql.getDropSkinTableSql());
        }
        if (isDatabaseExits) {
            isDatabaseExits = connection(UtilSql.getCreateSkinTableSql());
        }
    }

    /**
     * 创建noteMsg数据表
     */
    private static void createNoteMsgTables() {
        if (isDatabaseExits) {
            isDatabaseExits = connection(UtilSql.getDropNoteMsgTableSql());
        }
        if (isDatabaseExits) {
            isDatabaseExits = connection(UtilSql.getCreateNoteMsgTableSql());
        }
    }

    /**
     * 创建Linux数据表
     */
    private static void createLinuxCmdTables() {
        if (isDatabaseExits) {
            isDatabaseExits = connection(UtilSql.getDropLinuxCmdMsgTableSql());
        }
        if (isDatabaseExits) {
            isDatabaseExits = connection(UtilSql.getCreateLinuxCmdMsgTableSql());
        }
        if (isDatabaseExits) {
            isDatabaseExits = connection(UtilSql.getDropLinuxCmdExampleMsgTableSql());
        }
        if (isDatabaseExits) {
            isDatabaseExits = connection(UtilSql.getCreateLinuxCmdExampleMsgTableSql());
        }
    }

    public static Connection getConnection() {//way 1
        Connection connection = null;
        try {
            DataSource dataSource = DBUtil.ds;
            if (null != dataSource) {
                setDataSource();
            }
            connection = DBUtil.ds.getConnection();
            logger.info("连接数据库成功 ");
        } catch (SQLException e) {
            logger.warn("数据库没有打开 " + e);
            checkStatic(e);
        }
        return connection;
    }

    public static boolean connection(String sql) {
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            logger.info("开始执行 Sql 语句 ");
            ps.execute();
        } catch (SQLException e) {
            logger.warn("执行 Sql[" + sql + "] 异常\n " + e);
            checkStatic(e);
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

    /**
     * 检查是否是表不存在问题
     * 不存在就创建
     *
     * @param e SQLException
     */
    private static void checkStatic(SQLException e) {
        String exMsg = e.toString();
        //Table linuxCmdExamMsg don't exist
        if (exMsg.contains("no such table: linuxCmdMsg")
                || exMsg.contains("Table linuxCmdMsg don't exist")) {
            createLinuxCmdTables();
        } else if (exMsg.contains("no such table: systemInstallMsg")) {
            createSystemInstallMsgTable();
        } else if (exMsg.contains("no such table: noteMsg")) {
            createNoteMsgTables();
        }else if (exMsg.contains("no such table: skin")){
            createSkinTables();
        }
    }

    /**
     * 创建系统安装软件表
     */
    private static void createSystemInstallMsgTable() {
        if (isDatabaseExits) {
            isDatabaseExits = connection(UtilSql.getDropSystemInstallMsgTableSql());
        }
        if (isDatabaseExits) {
            isDatabaseExits = connection(UtilSql.getCreateSystemInstallMsgTableSql());
        }
    }

    public static List<Method> connections(String sql) {
        List<Method> methods = new ArrayList<Method>();
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            logger.info("开始封装数据 ");
            while (rs.next()) {
                Method method = new Method();
                method.setId(rs.getInt("id"));
                method.setName(rs.getString("name"));
                method.setType(rs.getString("type"));
                method.setUseTimes(rs.getInt("useTimes"));
                method.setLanguage(rs.getString("language"));
                method.setAnswer(rs.getString("answer"));
                method.setKeyWord(rs.getString("keyWord"));
                method.setDescription(rs.getString("description"));
                try {
                    method.setUpdateDate(simpleDateFormat.parse(rs.getString("updateDate")));
                } catch (ParseException e) {
                    logger.warn(e);
                    method.setUpdateDate(new Date());
                }
                method.setIsOutOfDate(rs.getInt("isOutOfDate"));
                methods.add(method);
            }
        } catch (SQLException e) {
            logger.warn("执行 Sql[" + sql + "] 异常 ", e);
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                DBUtil.getClose(con);
            } catch (SQLException e) {
                logger.warn(e);
            }
        }
        return methods;
    }

    public static List<ExceptionWay> connectionExceptionWay(String sql) {
        List<ExceptionWay> exceptionWays = new ArrayList<>();
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            logger.info("开始封装数据 Exception ");
            while (rs.next()) {
                ExceptionWay exceptionWay = new ExceptionWay();
                exceptionWay.setId(rs.getInt("id"));
                exceptionWay.setName(rs.getString("name"));
                exceptionWay.setType(rs.getString("type"));
                exceptionWay.setUseTimes(rs.getInt("useTimes"));
                exceptionWay.setLanguage(rs.getString("language"));
                exceptionWay.setAnswer(rs.getString("answer"));
                exceptionWay.setCause(rs.getString("cause"));
                exceptionWay.setKeyWord(rs.getString("keyWord"));
                exceptionWay.setDescription(rs.getString("description"));
                //Caused by: java.text.ParseException: Unparseable date: "2019-05-23" does not
                // match (\p{Nd}++)\Q-\E(\p{Nd}++)\Q-\E(\p{Nd}++)\Q \E(\p{Nd}++)\Q:\E(\p{Nd}++)
                // \Q:\E(\p{Nd}++)\Q.\E(\p{Nd}++)
                try {
                    exceptionWay.setUpdateDate(simpleDateFormat.parse(rs.getString("updateDate")));
                } catch (ParseException e) {
                    logger.warn("日期转换 异常 " + e);
                    exceptionWay.setUpdateDate(new Date());
                }
                exceptionWay.setIsSolution(rs.getInt("isSolution"));
                exceptionWays.add(exceptionWay);
            }
            return exceptionWays;
        } catch (SQLException e) {
            logger.warn("执行 Sql[" + sql + "] 异常 " + e);
            return null;
        } finally {
            closeDBAnd(ps, rs, con);
        }
    }

    public static List<SystemInstallMsg> connectionSystemInstallMsg(String installMsgKeySql) {
        List<SystemInstallMsg> installMsgList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Connection con = DBUtil.getConnection();
        try {
            ps = con.prepareStatement(installMsgKeySql);
            rs = ps.executeQuery();
            logger.info("开始封装数据 SystemInstallMsg ");
            while (rs.next()) {
                SystemInstallMsg installMsg = new SystemInstallMsg();
                installMsg.setId(rs.getInt("id"));
                installMsg.setName(rs.getString("name"));
                installMsg.setVersion(rs.getString("version"));
                installMsg.setInstallDate(rs.getString("installDate"));
                installMsg.setInstallAddr(rs.getString("installAddr"));
                installMsg.setPublisher(rs.getString("publisher"));
                installMsg.setUnInstallAddr(rs.getString("unInstallAddr"));
                try {
                    installMsg.setUpdateDate(simpleDateFormat.parse(rs.getString("updateDate")));
                } catch (ParseException e) {
                    logger.warn("日期转换 异常 " + e);
                    installMsg.setUpdateDate(new Date());
                }
                installMsgList.add(installMsg);
            }
            if (!installMsgList.isEmpty()) {
                return installMsgList;
            }
        } catch (SQLException e) {
            logger.warn("执行 Sql[" + installMsgKeySql + "] 异常 " + e);
            checkStatic(e);
            return null;
        } finally {
            closeDBAnd(ps, rs, con);
        }
        return null;
    }

    /**
     * 计数
     *
     * @param sql sql
     * @return int
     */
    public static int connectCountTableNum(String sql) {
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            logger.info("开始封装数据 ");
            while (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            logger.warn("执行 Sql[" + sql + "] 异常 " + e);
            checkStatic(e);
            return 0;
        } finally {
            closeDBAnd(ps, rs, con);
        }
        return 0;
    }

    /**
     * 查询所有的Linux信息
     *
     * @param linuxCmdMsgSql 查询Sql
     * @return
     */
    public static List<LinuxCmd> connectionLinuxCmd(String linuxCmdMsgSql) {
        List<LinuxCmd> linuxCmdList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Connection con = DBUtil.getConnection();
        try {
            ps = con.prepareStatement(linuxCmdMsgSql);
            rs = ps.executeQuery();
            logger.info("开始封装数据 LinuxCmd ");
            while (rs.next()) {
                LinuxCmd linuxCmd = new LinuxCmd();
                linuxCmd.setId(Integer.parseInt(rs.getString("id")));
                linuxCmd.setLinuxCmd(rs.getString("linuxCmd"));
                linuxCmd.setExample(rs.getString("example"));
                linuxCmd.setPermission(rs.getString("permission"));
                linuxCmd.setUsage(rs.getString("usages"));
                try {
                    linuxCmd.setUpdateDate(simpleDateFormat.parse(rs.getString("updateDate")));
                    linuxCmd.setInsertDate(simpleDateFormat.parse(rs.getString("insertDate")));
                } catch (Exception e) {
                    logger.warn("日期转换 异常 " + e);
                    linuxCmd.setUpdateDate(new Date());
                }
                linuxCmd.setUpdateTimes(rs.getInt("updateTimes"));
                String id = rs.getString("linuxCmdExamId");
                if (StringUtils.isNotEmpty(id)) {
                    LinuxCmdExam linuxCmdExam = new LinuxCmdExam();
                    linuxCmdExam.setId(Integer.parseInt(id));
                    linuxCmd.setLinuxCmdExam(linuxCmdExam);
                }
                linuxCmd.setUpdateTimes(rs.getInt("updateTimes"));
                linuxCmdList.add(linuxCmd);
            }
            if (!linuxCmdList.isEmpty()) {
                return linuxCmdList;
            }
        } catch (SQLException e) {
            logger.warn("执行 Sql[" + linuxCmdMsgSql + "] 异常 " + e);
            checkStatic(e);
            return null;
        } finally {
            closeDBAnd(ps, rs, con);
        }
        return null;
    }

    /**
     * 统一关闭读取数据库的流
     *
     * @param ps
     * @param rs
     * @param con
     */
    private static void closeDBAnd(PreparedStatement ps, ResultSet rs, Connection con) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            DBUtil.getClose(con);
        } catch (SQLException e) {
            logger.warn(e);
        }
    }

    /**
     * 根据Sql查询Exam信息
     *
     * @param linuxCmdExamByIdSql Sql
     * @return Linux命令示例信息
     */
    public static LinuxCmdExam connectionLinuxCmdExam(String linuxCmdExamByIdSql) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Connection con = DBUtil.getConnection();
        try {
            ps = con.prepareStatement(linuxCmdExamByIdSql);
            rs = ps.executeQuery();
            logger.info("开始封装数据 LinuxCmdExam ");
            while (rs.next()) {
                LinuxCmdExam linuxCmdExam = new LinuxCmdExam();
                linuxCmdExam.setId(Integer.parseInt(rs.getString("id")));
                linuxCmdExam.setExamples(rs.getString("examples"));
                linuxCmdExam.setParameterNote(rs.getString("parameterNote"));
                try {
                    linuxCmdExam.setUpdateDate(simpleDateFormat.parse(rs.getString("updateDate")));
                    linuxCmdExam.setInsertDate(simpleDateFormat.parse(rs.getString("insertDate")));
                } catch (ParseException e) {
                    logger.warn("日期转换 异常 " + e);
                    linuxCmdExam.setUpdateDate(new Date());
                }
                linuxCmdExam.setUpdateTimes(rs.getInt("updateTimes"));
                return linuxCmdExam;
            }
        } catch (SQLException e) {
            logger.warn("执行 Sql[" + linuxCmdExamByIdSql + "] 异常 " + e);
            checkStatic(e);
            return null;
        } finally {
            closeDBAnd(ps, rs, con);
        }
        return null;
    }

    /**
     * 获取数据库某一列数据
     *
     * @param singleColumnSql sql
     * @return 列数据
     */
    public static List<String> connectionSingleField(String singleColumnSql) {
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> returnValue = new ArrayList<>();
        try {
            ps = con.prepareStatement(singleColumnSql);
            rs = ps.executeQuery();
            logger.info("开始封装数据 ...  ");
            while (rs.next()) {
                returnValue.add(rs.getString("single"));
            }
            if (!returnValue.isEmpty()) {
                return returnValue;
            }
        } catch (SQLException e) {
            logger.warn("执行 Sql[" + singleColumnSql + "] 异常 " + e);
            checkStatic(e);
            return null;
        } finally {
            closeDBAnd(ps, rs, con);
        }
        return null;
    }

    /**
     * 查询笔记信息
     *
     * @param sql         Sql
     * @param objectClass 类
     * @param object      对象
     * @return 笔记信息
     */
    public static List<Object> connectionNoteMsg(String sql, String objectClass, Object object) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = DBUtil.getConnection();
        List<Object> objectList = new ArrayList<>();
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            logger.info("开始封装数据 NoteMsg ... ");
            while (rs.next()) {
                //反射赋值
                reflectSetValue(object, rs);
                objectList.add(object);
            }
            if (!objectList.isEmpty()) {
                return objectList;
            }
        } catch (SQLException e) {
            logger.warn("执行 Sql[" + sql + "] 异常 " + e);
            checkStatic(e);
            return null;
        } catch (Exception e) {
            logger.warn("执行 字段转换 [" + object.getClass().getName() + "] 异常 " + e);
        } finally {
            closeDBAnd(ps, rs, con);
        }
        return null;
    }

    /**
     * @param object 对象
     * @param rs     数据库
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static void reflectSetValue(Object object, ResultSet rs) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SQLException {
        //利用反射
        Class className = object.getClass();
        //  获取两个实体类的所有属性
        Field[] fields = className.getDeclaredFields();
        // 遍历class1Bean，获取逐个属性值，然后赋值
        for (Field field : fields) {
            //设置访问权限
            field.setAccessible(true);
            // 获取属性的类型
            String type = field.getGenericType().toString();
            //字段名字
            String name = field.getName();
            //getter方法
            java.lang.reflect.Method method = type.toLowerCase().contains("boolean") ? null :
                    object.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
            //setter方法名字
            String setMethod = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
            // 如果type是类类型，则前面包含"class "，后面跟类名
            if ("class java.lang.String".equals(type)) {
                // 调用getter方法获取属性值
                String value = (String) method.invoke(object);
                if (value == null) {
                    method = object.getClass().getMethod(setMethod, String.class);
                    method.invoke(object, rs.getString(field.getName()));
                }
            }
            if ("class java.lang.Integer".equals(type) || "class java.lang.Int".equals(type)) {
                Integer value = (Integer) method.invoke(object);
                if (value == null) {
                    method = object.getClass().getMethod(setMethod, Integer.class);
                    method.invoke(object, Integer.parseInt(rs.getString(field.getName())));
                }
            }
            if ("class java.lang.Long".equals(type)) {
                Long value = (Long) method.invoke(object);
                if (value == null) {
                    method = object.getClass().getMethod(setMethod, Long.class);
                    method.invoke(object, Long.parseLong(rs.getString(field.getName())));
                }
            }
            if ("class java.lang.Boolean".equals(type)) {
                Boolean value =
                        (Boolean) object.getClass().getMethod("is" + name.substring(0, 1).toUpperCase() + name.substring(1)).invoke(object);
                if (value == null) {
                    method = object.getClass().getMethod(setMethod, Boolean.class);
                    method.invoke(object, (rs.getBoolean(field.getName())));
                }
            }
            if ("class java.util.Date".equals(type)) {
                Date value = (Date) method.invoke(object);
                if (value == null) {
                    method = object.getClass().getMethod(setMethod, Date.class);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        //解决方法：一Date startReportDate = sdf.parse(startDate.toString());
                        // 对字符串进行toString
                        method.invoke(object, sdf.parse(rs.getString(field.getName()).toString()));
                    } catch (ParseException e) {
                        logger.info(e);
                        method.invoke(object, new Date());
                    }
                }
            }
        }
    }

    public Connection connect() {//way 2
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.info("执行 driver[" + driver + "] 异常 ");
        }
        Connection conn = null;
        try {
            logger.info("与数据库建立连接.... ");
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            logger.warn("与数据库建立连接失败 [url = " + url + "][usernme = " + username + "][password = " + password + "].... " + e);
        }
        return conn;
    }

    public static void getClose(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.warn(e);
            }
        }
    }
}
