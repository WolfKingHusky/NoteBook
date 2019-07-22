package com.huang.notetool.tool;

import com.huang.notetool.po.*;
import com.huang.notetool.util.*;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 读文件
 *
 * @author huang
 */
public class ReadFile {
    private static Logger logger = Logger.getLogger(ReadFile.class);

    public static String[] getMethodType() {
        logger.info("正在获取 Method 的 Type ");
        String Type = getFileMsg(FilePath.getMethodTypes());

        return Type.split(",");
    }

    public static List<Method> fileMsg(String fileAbsolutePath) {
        List<Method> methodList = new ArrayList<Method>();
        String fileMsgBuffer = getFileMsg(fileAbsolutePath);
        if (null != fileMsgBuffer && fileMsgBuffer.length() > 1) {
            logger.info("读取文件 [" + fileAbsolutePath + "] 成功 --> ReadFile");
            String[] methods = fileMsgBuffer.split(";");
            for (int i = 0; i < methods.length; i++) {
                //              格式[mId,mUseTimes,mName,mType,mLangage,mKeyWord(key_key_..),
                //              mAnswer,mDescription(.._.._..),mJoinDate,mIsOutOfDate
                //                method.getmId() + "," + method.getmName() + "," + method
                //                .getmLangage() + "," + method.getmType() + "," + method
                //                .getmAnswer() + "," + method.getmKeyWord().replaceAll(",","_")
                //                + "," + method.getmJoinDate() + ",使用" + method.getmUseTimes() +
                //                "次," + method.getmDescription().replaceAll(",","_") + "," +
                //                method.getIsOutOfDate() + ";");
                Method method = new Method();
                String[] methodItemStrs = methods[i].split(",");
                method.setUseTimes(Integer.parseInt(methodItemStrs[7]));
                method.setName(methodItemStrs[1]);
                method.setType(methodItemStrs[3]);
                method.setLanguage(methodItemStrs[2]);
                method.setKeyWord(methodItemStrs[5].replaceAll("_", ","));
                method.setAnswer(methodItemStrs[4]);
                method.setDescription(methodItemStrs[8].replaceAll("_", ","));
                try {
                    method.setUpdateDate(new SimpleDateFormat("yyyy-MM-dd").parse(methodItemStrs[6]));
                } catch (ParseException e) {
                    logger.info("字符串转日期 [yyyy-MM-dd] 出错 --> ReadFile");
                    e.printStackTrace();
                    method.setUpdateDate(new Date());
                }
                method.setIsOutOfDate(Integer.parseInt(methodItemStrs[9]));
                methodList.add(method);
            }
        } else {
            logger.info("读取文件 [" + fileAbsolutePath + "] 失败 --> ReadFile");
        }
        return methodList;
    }

    public static String getFileMsg(String filePath) {
        BufferedReader in;
        String lineStr;
        int n = 0;
        StringBuilder fileMsg = new StringBuilder();
        in = null;
        try {
            //指定路径
            File dirFile = new File(filePath);
            String charSet = FileConvert.getFileEncoding(dirFile);
            if (charSet.equals("GBK")) {
                InputStreamReader reader = new InputStreamReader(
                        new FileInputStream(new File(filePath)), "gb2312");
                in = new BufferedReader(reader);
            }
            if (charSet.equals("UTF-8")) {
                in = new BufferedReader(new InputStreamReader(
                        new FileInputStream(filePath), "UTF-8"));
            }

            while ((lineStr = in.readLine()) != null) {
                fileMsg.append(lineStr);
                n++;
            }
            logger.info("成功读取 [" + filePath + "] 下的 [" + n + "] 行数据，数据长度为 [" + fileMsg.length() + "] --> ReadFile");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.info("读取文件错误：找不到= [" + filePath + "] 路径下的文件 --> ReadFile");
        } catch (IOException e) {
            logger.info("读取文件错误：读文件= [" + filePath + "] IOException --> ReadFile");
            e.printStackTrace();
        }
        return fileMsg.toString();
    }

    public static String[] getMethodLanguage() {
        logger.info("正在获取 Method 的 Language --> ReadFile");
        String Language = getFileMsg(FilePath.getMethodLanguages());
        return Language.split(",");
    }

    public static String[] getExceptionType() {
        logger.info("正在获取 Exception 的 Type --> ReadFile");
        String Type = getFileMsg(FilePath.getExceptionTypes());

        return Type.split(",");
    }

    /**
     * 读取异常信息文件
     *
     * @param filePath 文件路径
     * @return 异常信息
     */
    public static List<ExceptionWay> readExceptionMsg(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            logger.info("文件不存在 " + filePath);
            return null;
        }
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isEmpty(fileType)) {
            logger.info("文件类型不存在 " + filePath);
            return null;
        }
        fileType = fileType.toLowerCase();
        if (fileType.contains("txt")) {
            return readExceptionMsgByTxt(file);
        } else if (fileType.contains("xls") || fileType.contains("xlsx")) {
            return readExceptionMsgByExcel(file);
        }
        return null;
    }

    /**
     * 读取Excel文件内容
     *
     * @param file
     * @return
     */
    private static List<ExceptionWay> readExceptionMsgByExcel(File file) {

        return null;
    }

    /**
     * 读取txt文件内容
     *
     * @param file
     * @return
     */
    private static List<ExceptionWay> readExceptionMsgByTxt(File file) {
        String exceptionMsgStr = readNormalFile(file.getAbsolutePath());
        //换行
        String lineSeparator = System.getProperty("line.separator");
        if (StringUtils.isNotEmpty(exceptionMsgStr)) {
            String[] exceptionMsgArr = exceptionMsgStr.split(lineSeparator);
            if (exceptionMsgArr.length > 0) {
                //第一行说明
                String exceptionTitle = exceptionMsgArr[0].trim();
                if (exceptionTitle.contains(Constants.exportExceptionExplain)
                        && exceptionTitle.length() <= Constants.exportExceptionExplain.length() + 3) {
                    return generateExceptionMsg(exceptionMsgArr);
                } else {
                    logger.info("导入的文件不符合异常信息标准： " + exceptionTitle);
                    return null;
                }
            } else {
                logger.info("导入的文件为空");
                return null;
            }
        }
        return null;
    }

    /**
     * 组装异常信息
     *
     * @param exceptionMsgArr
     * @return
     */
    private static List<ExceptionWay> generateExceptionMsg(String[] exceptionMsgArr) {
        //编号，异常信息，语言，类型，异常信息解决方法，关键字，根本原因，更新时间，使用次数，是否解决
        List<ExceptionWay> exceptionWayList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int num = 1; num < exceptionMsgArr.length; num++) {
            if (StringUtils.isNotEmpty(exceptionMsgArr[num])) {
                String[] msgArr = exceptionMsgArr[num].split("，");
                if (StringUtils.isNotEmpty(msgArr[1])
                        && StringUtils.isNotEmpty(msgArr[4])
                        && StringUtils.isNotEmpty(msgArr[5])) {
                    ExceptionWay exceptionWay = new ExceptionWay();
                    exceptionWay.setId(GenerateRandom.randomInt());
                    exceptionWay.setName(WordFilter.replaceMarkTONumber(msgArr[1]));
                    if (StringUtils.isNotEmpty(msgArr[2])) {
                        exceptionWay.setLanguage(msgArr[2]);
                    }
                    if (StringUtils.isNotEmpty(msgArr[3])) {
                        exceptionWay.setType(msgArr[3]);
                    }
                    exceptionWay.setAnswer(msgArr[4]);
                    exceptionWay.setKeyWord(msgArr[5]);
                    if (StringUtils.isNotEmpty(msgArr[6])) {
                        exceptionWay.setCause(msgArr[6]);
                    }
                    if (StringUtils.isNotEmpty(msgArr[7])) {
                        try {
                            exceptionWay.setUpdateDate(simpleDateFormat.parse(msgArr[7]));
                        } catch (ParseException e) {
                            logger.warn("导入异常信息的日期转换错误" + e);
                            exceptionWay.setUpdateDate(new Date());
                        }
                    }
                    //使用次数
                    if (StringUtils.isNotEmpty(msgArr[8])) {
                        exceptionWay.setUseTimes(Integer.parseInt(msgArr[8].trim().replaceAll(
                                "使用", "").replaceAll("次", "")));
                    }
                    //是否解决
                    if (StringUtils.isNotEmpty(msgArr[9])) {
                        exceptionWay.setIsSolution(msgArr[9].contains("已解决") ? 0 : 1);
                    }
                    exceptionWayList.add(exceptionWay);
                }
            }
        }
        if (!exceptionWayList.isEmpty()) {
            return exceptionWayList;
        }
        return null;
    }

    /**
     * 读取普通文件
     *
     * @param filePath 文件路径
     * @return
     */
    public static String readNormalFile(String filePath) {
        String line = System.getProperty("line.separator");
        StringBuilder content = new StringBuilder();
        FileInputStream read = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader in = null;
        try {
            File file = new File(filePath);
            String encoding = FileConvert.getFileEncoding(file);
            read = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(read, encoding);
            in = new BufferedReader(inputStreamReader);
            String lineMsg;
            while ((lineMsg = in.readLine()) != null) {
                content.append(lineMsg.trim()).append(line);
            }
            return new String(content.toString().getBytes(), Constants.UTF_8);
        } catch (IOException e2) {
            logger.warn("读取文件异常" + e2);
            JOptionPane.showMessageDialog(null, e2.getMessage(), "打开文件提示框",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (read != null) {
                    read.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (IOException e) {
                logger.warn(e);
            }
        }
    }

    /**
     * 获取字符串编码
     *
     * @param str 字符串
     * @return 字符串编码
     */
    static String getEncoding(String str) {
        String encode = Constants.GB_2312;
        try {
            //判断是不是GB2312
            if (str.equals(new String(str.getBytes(encode), encode))) {
                //是的话，返回“GB2312“
                return encode;
            }
        } catch (Exception exception) {
            logger.info("不是GB2312编码");
        }
        encode = Constants.ISO_8859_1;
        try {
            //判断是不是ISO-8859-1
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception exception1) {
            logger.info("不是ISO-8859-1编码");
        }
        encode = Constants.UTF_8;
        try {
            //判断是不是UTF-8
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception exception2) {
            logger.info("不是UTF-8编码");
        }
        encode = Constants.GBK;
        try {
            //判断是不是GBK
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception exception3) {
            logger.info("不是GBK编码");
        }
        //如果都不是，说明输入的内容不属于常见的编码格式。
        return null;
    }

    public static List<Method> readMethodMsg(String absolutePath) {
        logger.info("导入 Method 信息中... ");
        File methodFile = new File(absolutePath);
        if (!methodFile.exists()) {
            logger.info("文件不存在 " + absolutePath);
            return null;
        }
        String fileName = methodFile.getName();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isEmpty(fileType)) {
            logger.info("文件类型不存在 " + absolutePath);
            return null;
        }
        fileType = fileType.toLowerCase();
        if (fileType.contains("txt")) {
            return readMethodMsgByTxt(methodFile);
        } else if (fileType.contains("xls") || fileType.contains("xlsx")) {
            return readMethodMsgByExcel(methodFile);
        }
        return null;
    }

    /**
     * 从Txt读取方法信息
     *
     * @param methodFile 文件
     * @return 方法信息
     */
    private static List<Method> readMethodMsgByTxt(File methodFile) {
        //编号，方法名，语言，类型，方法使用，关键字，更新时间，方法描述，是否过时
        String methodMsgStr = readNormalFile(methodFile.getAbsolutePath());
        //换行
        String lineSeparator = System.getProperty("line.separator");
        if (StringUtils.isNotEmpty(methodMsgStr)) {
            String[] methodMsgArr = methodMsgStr.split(lineSeparator);
            if (methodMsgArr.length > 0) {
                //第一行说明
                String methodTitle = methodMsgArr[0].trim();
                if (methodTitle.contains(Constants.exportMethodExplain)
                        && methodTitle.length() <= Constants.exportExceptionExplain.length() + 3) {
                    return generateMethodMsg(methodMsgArr);
                } else {
                    logger.info("导入的文件不符合异常信息标准： " + methodTitle);
                    return null;
                }
            } else {
                logger.info("导入的文件为空");
                return null;
            }
        }
        return null;
    }

    /**
     * 组装方法返回数据
     *
     * @param methodMsgArr
     * @return 方法列表
     */
    private static List<Method> generateMethodMsg(String[] methodMsgArr) {
        //编号，方法名，语言，类型，方法使用，关键字，更新时间，使用次数，方法描述，是否过时
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Method> returnMethodList = new ArrayList<>();
        for (String value : methodMsgArr) {
            if (StringUtils.isNotEmpty(value)) {
                String[] methodArr = value.split("，");
                if (methodArr.length > 0
                        && StringUtils.isNotEmpty(methodArr[1])
                        && StringUtils.isNotEmpty(methodArr[4])
                        && StringUtils.isNotEmpty(methodArr[5])
                        && StringUtils.isNotEmpty(methodArr[8])
                ) {
                    Method method = new Method();
                    method.setName(methodArr[1]);
                    //方法使用
                    method.setAnswer(methodArr[4].replaceAll("[\n\t\r]", ""));
                    //关键字
                    method.setKeyWord(methodArr[5]);
                    //方法描述
                    method.setDescription(methodArr[8].replaceAll("[\n\t\r]", ""));
                    //语言
                    if (StringUtils.isNotEmpty(methodArr[2])) {
                        method.setLanguage(methodArr[2]);
                    }
                    //类型
                    if (StringUtils.isNotEmpty(methodArr[3])) {
                        method.setType(methodArr[3]);
                    }
                    //更新时间
                    if (StringUtils.isNotEmpty(methodArr[6])) {
                        try {
                            method.setUpdateDate(simpleDateFormat.parse(methodArr[6]));
                        } catch (ParseException e) {
                            logger.warn(e);
                            method.setUpdateDate(new Date());
                        }
                    } else {
                        method.setUpdateDate(new Date());
                    }
                    //是否过时
                    if (StringUtils.isNotEmpty(methodArr[9])) {
                        method.setIsOutOfDate(methodArr[9].contains("未过时") ? 0 : 1);
                    }
                    returnMethodList.add(method);
                }
            }
        }
        if (!returnMethodList.isEmpty()) {
            return returnMethodList;
        }
        return null;
    }

    /**
     * 从excel读取方法信息
     *
     * @param methodFile 文件
     * @return 方法信息
     */
    private static List<Method> readMethodMsgByExcel(File methodFile) {
        return null;
    }

    /**
     * 导入Linux命令信息
     *
     * @param filePath 文件路径
     * @return Linux信息
     */
    public static List<LinuxCmd> readLinuxCmdMsg(String filePath) {
        logger.info("导入 LinuxCmd 信息中... ");
        File methodFile = new File(filePath);
        if (!methodFile.exists()) {
            logger.warn("文件不存在 " + filePath);
            return null;
        }
        String fileName = methodFile.getName();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isEmpty(fileType)) {
            logger.warn("文件类型不存在 " + filePath);
            return null;
        }
        fileType = fileType.toLowerCase();
        if (fileType.contains("txt")) {
            return readLinuxCmdMsgByTxt(methodFile);
        } else if (fileType.contains("xls") || fileType.contains("xlsx")) {
            return readLinuxCmdMsgByExcel(methodFile, fileType);
        }
        return null;
    }

    /**
     * 通过Txt文件导入信息
     *
     * @param file     文件
     * @param fileType 文件类型
     * @return LinuxCmd
     */
    private static List<LinuxCmd> readLinuxCmdMsgByExcel(File file, String fileType) {
        logger.info("读取 " + fileType + "类型的 LinuxCmd 信息中... ");
        return null;
    }

    /**
     * 通过Txt文件导入信息
     *
     * @param file 文件
     * @return LinuxCmd
     */
    private static List<LinuxCmd> readLinuxCmdMsgByTxt(File file) {
        logger.info("读取 Txt类型的 LinuxCmd 信息中... ");
        //主键编号，Linux命令，作用，用法，使用权限，更新时间，更新次数,参数说明,多个实例(使用中文；作为换行)，添加时间
        String linuxCmdMsgStr = readNormalFile(file.getAbsolutePath());
        //换行
        String lineSeparator = System.getProperty("line.separator");
        if (StringUtils.isNotEmpty(linuxCmdMsgStr)) {
            String[] linuxCmdMsgArr = linuxCmdMsgStr.split(lineSeparator);
            if (linuxCmdMsgArr.length > 1) {
                //第一行说明
                String msgTitle = linuxCmdMsgArr[0].trim();
                if (msgTitle.contains(Constants.exportLinuxCmdExplain)
                        && msgTitle.length() <= Constants.exportLinuxCmdExplain.length() + 3) {
                    try {
                        return generateLinuxCmdMsg(linuxCmdMsgArr);
                    } catch (RuntimeException e) {
                        logger.warn(e);
                        return null;
                    }
                } else {
                    logger.info("导入的文件不符合Linux命令信息标准： " + msgTitle);
                    return null;
                }
            } else {
                logger.info("导入的文件为空");
                return null;
            }
        }
        return null;
    }

    /**
     * 批量导入 Linux命令 数据
     *
     * @param linuxCmdMsgArr Linux信息
     * @return Linux命令
     */
    private static List<LinuxCmd> generateLinuxCmdMsg(String[] linuxCmdMsgArr) {
        List<LinuxCmd> linuxCmdList = new ArrayList<>();
        //Linux命令，作用，用法，使用权限
        // ,参数说明(使用中文；作为换行),多个实例(使用中文；作为换行);
        for (int num = 1; num < linuxCmdMsgArr.length; num++) {
            String linuxMsgStr = linuxCmdMsgArr[num];
            if (StringUtils.isNotEmpty(linuxMsgStr)) {
                String[] properArr = linuxMsgStr.split("，");
                if (properArr.length > 0) {
                    //Linux命令名称
                    String linuxCmdName = properArr[0];
                    //作用
                    String usages = properArr[1];
                    //用法
                    String example = properArr[2];

                    if (StringUtils.isNotEmpty(linuxCmdName)
                            && StringUtils.isNotEmpty(usages)
                            && StringUtils.isNotEmpty(example)) {
                        LinuxCmd linuxCmd = new LinuxCmd();
                        linuxCmd.setLinuxCmd(linuxCmdName);
                        linuxCmd.setUsage(usages);
                        linuxCmd.setExample(example);
                        linuxCmd.setUpdateTimes(1);
                        linuxCmd.setUpdateDate(new Date());
                        linuxCmd.setInsertDate(new Date());
                        if (properArr.length > 3) {
                            //使用权限
                            String permission = properArr[3];
                            linuxCmd.setPermission(StringUtils.isEmpty(permission) ? "所有者" :
                                    permission);
                            if (properArr.length > 4) {
                                LinuxCmdExam linuxCmdExam = new LinuxCmdExam();
                                //参数说明
                                String parameterNote = properArr[4];
                                if (StringUtils.isNotEmpty(parameterNote)) {
                                    linuxCmdExam.setParameterNote(parameterNote);
                                }
                                if (properArr.length > 5) {
                                    //多个实例
                                    String examples = properArr[5];
                                    if (StringUtils.isNotEmpty(examples)) {
                                        linuxCmdExam.setExamples(examples);
                                    }
                                }
                                linuxCmdExam.setUpdateTimes(linuxCmd.getUpdateTimes());
                                linuxCmdExam.setInsertDate(linuxCmd.getInsertDate());
                                linuxCmdExam.setUpdateDate(linuxCmd.getUpdateDate());
                                linuxCmd.setLinuxCmdExam(linuxCmdExam);
                            }
                        }
                        linuxCmdList.add(linuxCmd);
                    }
                }
            }
        }
        //不为空才返回
        return linuxCmdList.isEmpty() ? null : linuxCmdList;
    }

    /**
     * 批量导入 笔记信息 数据
     *
     * @param filePath 笔记文件路径
     * @return 笔记信息
     */
    public static List<NoteMsg> readNoteMsg(String filePath) {
        return null;
    }

    /**
     * 此处说明作用
     *
     * @param systemLogDir 系统日志文件夹
     * @return 文件列表
     * @author 黄先生
     * @date 2019/7/10 15:55
     */
    public static File[] listSystemLogs(String systemLogDir) {
        File file = new File(systemLogDir);
        //判断文件或目录是否存在
        if (!file.exists()) {
            logger.info("【" + systemLogDir + " not exists】");
            return null;
        }
        //获取该文件夹下所有的文件
        return file.listFiles();
    }
}
