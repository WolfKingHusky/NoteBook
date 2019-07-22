package com.huang.notetool.util;

import javax.swing.*;

/**
 * 常量
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-05-24 22:12:25
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-24   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class Constants {
    /**
     * 提示图标
     */
    public static final Icon NOTICE_IMAGE = IconRead.getIconFromImage("/notice.png", 40, 40);
    /**
     * 换行
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    /**
     * 空字符串
     */
    public static final String EMPTY_STR = "";
    /**
     * 日期格式
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * 系统日志路径
     */
    public static final String SYSTEM_LOG_DIR = "C:\\Windows\\System32\\winevt\\Logs";
    /**
     * 异常信息导出说明
     */
    public static String exportExceptionExplain = "编号，异常信息，语言，类型，异常信息解决方法，关键字，根本原因，更新时间，使用次数，是否解决";
    /**
     * 方法信息导出说明
     */
    public static String exportMethodExplain = "编号，方法名，语言，类型，方法使用，关键字，更新时间，使用次数，方法描述，是否过时";
    /**
     * Linux命令信息导出说明
     */
    public static String exportLinuxCmdExplain = "Linux命令，作用，用法，使用权限,参数说明(使用中文；作为换行),多个实例" +
            "(使用中文；作为换行)";
    /**
     * 笔记信息导出说明
     */
    public static String exportNoteMsgExplain = "笔记名称，笔记（BASE64编码），笔记类型，笔记说明，主键，更新方法时间，更新次数";
    /**
     * 字符串： UTF_8
     */
    public static final String UTF_8 = "UTF-8";
    /**
     * 编码： GB2312
     */
    public static final String GB_2312 = "GB2312";
    /**
     * 编码： ISO-8859-1
     */
    public static final String ISO_8859_1 = "ISO-8859-1";
    /**
     * 编码： GBK
     */
    public static final String GBK = "GBK";
    /**
     * 数据库相关
     */
    public static final String ORACLE_EN = "oracle";
    public static final String DB_2_CAPITAL_EN = "DB2";
    public static final String DB_2_EN = "db2";
    public static final String ORACLE_CAPITAL_EN = "ORACLE";

    public static final String HSQLDB_CAPITAL_EN = "HSQLDB";
    public static final String HSQLDB_EN = "hsqldb";
    public static final String POST_GRE_SQL_CAPITAL_EN = "POSTGRESQL";
    public static final String POST_GRE_SQL_EN = "postgresql";
    public static final String INFORMIX_CAPITAL_EN = "INFORMIX";
    public static final String INFORMIX_EN = "informix";
    public static final int NUMBER_SIXTY = 16;
    public static final String AES = "AES";
    public static final String AES_ECB_PKCS5_PADDING = "AES/ECB/PKCS5Padding";
}
