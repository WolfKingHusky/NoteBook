package com.huang.notetool.util;

import com.huang.notetool.tool.FindDir;
import org.apache.log4j.Logger;

/**
 * 文件路径配置
 *
 * @author huang
 */
public class FilePath {
    private static String fileSep = System.getProperty("file.separator");
    public static String basePath = FindDir.getProjectDir() + fileSep + "NoteBook" + fileSep;
    public static String sqlitePath = basePath + "db" + fileSep;
    private static Logger logger = Logger.getLogger(FilePath.class);

    public static String getImportMethodDefaultPath() {
        logger.info("获取 Import 默认路径 [" + basePath + "] ");
        return basePath;
    }

    public static String getMethodTypes() {
        logger.info("获取文件 [" + basePath + "Method\\methodtype.txt] ");
        return basePath + "Method\\methodtype.txt";
    }

    public static String getMethodLanguages() {
        logger.info("获取文件 [" + basePath + "Method\\methodlanguage.txt] ");
        return basePath + "Method\\methodlanguage.txt";
    }

    public static String getExportMethodDefaultPath() {
        logger.info("获取 Export 默认路径 [" + basePath + "Method\\method.txt] ");
        return basePath + "Method\\method.txt";
    }

    public static String getExportExceptionDefaultPath() {
        logger.info("获取 Export 默认路径 [" + basePath + "Exception\\exception.txt] ");
        return basePath + "exception\\exception.txt";
    }

    public static String getExceptionTypes() {
        logger.info("获取文件 [" + basePath + "exception\\exceptiontype.txt] ");
        return basePath + "exception\\exceptiontype.txt";
    }
}
