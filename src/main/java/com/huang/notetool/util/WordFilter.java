package com.huang.notetool.util;

import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 过滤字符串
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-05-24 17:30:20
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-24   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class WordFilter {

    private static Logger logger = Logger.getLogger(WordFilter.class);

    /**
     * 将单引号替换为双引号
     *
     * @param content
     * @return
     */
    private static String replaceSingleToDoubleMark(String content) {
        return content.replaceAll("'", "\"").replaceAll("[\n\r\t]", "");
    }

    /**
     * 替换单引号和双引号之间的内容为1
     *
     * @param exceptionMsg
     * @return
     */
    public static String replaceMarkTONumber(String exceptionMsg) {
        String reg = "\"(.*?)\"";
        cutAllQuotationMark(exceptionMsg, reg);
        reg = "'(.*?)'";
        cutAllQuotationMark(exceptionMsg, reg);
        return replaceSingleToDoubleMark(exceptionMsg);
    }

    /**
     * @param exceptionMsg 字符串
     *                     替换字符串双引号里面的内容
     * @author HuangZhaoRong
     */
    private static String cutAllQuotationMark(String exceptionMsg, String reg) {
        //正则表达式
        Pattern pattern = Pattern.compile(reg);
        logger.info("截取字符串引号内容 [" + pattern + "] ");
        // 进行匹配
        Matcher matcher = pattern.matcher(exceptionMsg);
        StringBuilder regMatchMsg = new StringBuilder();
        int num = 0;
        while (matcher.find()) {
            exceptionMsg = exceptionMsg.replace(matcher.group(), "1");
            logger.info("正在替换字符串引号内容 [" + matcher.group() + "] ");
            regMatchMsg.append(matcher.group()).append(";");
            matcher = pattern.matcher(exceptionMsg);
        }
        logger.info("字符串引号里面的内容 [" + regMatchMsg + "]  ");
        logger.info("返回字符的内容 [" + exceptionMsg + ";" + regMatchMsg + "]  ");
        return exceptionMsg;
    }
}
