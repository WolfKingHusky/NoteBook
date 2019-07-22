package com.huang.notetool.tool;

import com.huang.notetool.util.Constants;
import com.huang.notetool.util.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * 导出文件
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-05-25 14:04:17
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-25   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class WriteFile {
    /**
     * 日志
     */
    private static Logger logger = Logger.getLogger(WriteFile.class);

    /**
     * 导出为Txt文件
     *
     * @param msgArr      导出的信息
     * @param objectClass 类
     * @param filePath    文件路径
     * @return 成功与否
     */
    public static boolean writeTxtFile(String[] msgArr, Class objectClass, String filePath) {
        logger.info("导出" + objectClass.getName() + " 数据到TXT.. ");
        String title = null;
        long time = System.currentTimeMillis();
        if (objectClass.getName().contains("ExceptionWay")) {
            title = Constants.exportExceptionExplain;
            filePath = filePath + "\\exceptionMsg." + time + ".txt";
        } else if (objectClass.getName().contains("Method")) {
            title = Constants.exportMethodExplain;
            filePath = filePath + "\\method." + time + ".txt";
        } else if (objectClass.getName().contains("LinuxCmd")) {
            title = Constants.exportLinuxCmdExplain;
            filePath = filePath + "\\linuxCmd." + time + ".txt";
        } else if (objectClass.getName().contains("NoteMsg")) {
            title = Constants.exportNoteMsgExplain;
            filePath = filePath + "\\noteMsg." + time + ".txt";
        }
        return doWriteTxtFile(msgArr, filePath, title);
    }

    /**
     * 写exceptionMsg
     * 字符串通过，分隔各个属性
     *
     * @param msgArr   信息数组
     * @param filePath 文件路径
     * @param title    标题
     * @return 成功与否
     */
    private static boolean doWriteTxtFile(String[] msgArr, String filePath, String title) {
        //换行
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder writeStr = new StringBuilder();
        if (StringUtils.isNotEmpty(title)) {
            writeStr.append(title).append(lineSeparator);
        }
        for (String msg : msgArr) {
            writeStr.append(msg).append(lineSeparator);
        }
        File file = new File(filePath);
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        PrintWriter printWriter = null;
        try {
            if (file.createNewFile()) {
                fileOutputStream = new FileOutputStream(file);
                outputStreamWriter = new OutputStreamWriter(fileOutputStream, Constants.GB_2312);
                bufferedWriter = new BufferedWriter(outputStreamWriter);
                printWriter = new PrintWriter(bufferedWriter);
                printWriter.write(writeStr.toString());
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            logger.warn(e);
        } finally {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
            } catch (IOException e) {
                logger.warn(e);
            }
        }
        return false;
    }

    public static boolean writeXlsFile(String[] msgArr, Class objectClass, String filePath) {
        return false;
    }
}
