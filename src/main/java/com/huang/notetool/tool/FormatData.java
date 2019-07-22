package com.huang.notetool.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huang.notetool.util.Constants;
import com.huang.notetool.util.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 格式化JSON数据
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-05-25 21:35:28
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-25   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class FormatData {
    /**
     * 格式化JSON数据
     *
     * @param jsonStr jsonStr字符串
     * @return 字符串
     */
    public static String jsonFormat(String jsonStr) throws Exception {
        int level = 0;
        //存放格式化的json字符串
        StringBuilder jsonFormatStr = new StringBuilder();
        //判断是否是正确的字符串
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        //计数是否在双引号内
        int markNum = 1;
        //将字符串中的字符逐个按行输出
        for (int index = 0; index < jsonStr.length(); index++) {
            //获取s中的每个字符
            char c = jsonStr.charAt(index);

            //level大于0并且jsonForMatStr中的最后一个字符为\n,jsonForMatStr加入\t
            if (level > 0 && '\n' == jsonFormatStr.charAt(jsonFormatStr.length() - 1)) {
                jsonFormatStr.append(getJsonLevelStr(level));
            }
            //遇到"{"和"["要增加空格和换行，遇到"}"和"]"要减少空格，以对应，遇到","要换行
            switch (c) {
                case '{':
                case '[':
                    jsonFormatStr.append(c).append("\n");
                    level++;
                    break;
                case ',':
                    jsonFormatStr.append(c).append("\n");
                    break;
                case '}':
                case ']':
                    jsonFormatStr.append("\n");
                    level--;
                    jsonFormatStr.append(getJsonLevelStr(level));
                    jsonFormatStr.append(c);
                    break;
                case '"':
                    markNum++;
                    jsonFormatStr.append(c);
                    break;
                default:
                    if (markNum % 2 == 0) {
                        //移除节点内的空格和换行
                        if (c != '\n' && c != ' ' && c != '\t' && c != '\r') {
                            jsonFormatStr.append(c);
                        }
                    } else {
                        jsonFormatStr.append(c);
                    }
                    break;
            }
        }
        String returnStr = jsonFormatStr.toString();
        //去掉引号里面的空格 (?=[性别]{2})([\s\S]*?).+?(?<=[年龄]{2})
        returnStr = returnStr.replaceAll("(?=[\"]{2})([\\s\\S]*?).+?(?<=[\"]{2})", "");
        return returnStr;
    }

    /**
     * 加空格换行进位
     *
     * @param level
     * @return
     */
    private static String getJsonLevelStr(int level) {
        StringBuilder levelStr = new StringBuilder();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("   ");
        }
        return levelStr.toString();
    }

    /**
     * xml格式化
     *
     * @param xmlStr xml字符串
     * @return 格式化后的字符串
     * @throws Exception 格式化异常
     */
    public static String formatXml(String xmlStr) throws Exception {
        Document document;
        XMLWriter xmlWriter = null;
        StringWriter writer = null;
        try {
            document = DocumentHelper.parseText(xmlStr);
            // 格式化输出格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding(Constants.GB_2312);
            writer = new StringWriter();
            // 格式化输出流
            xmlWriter = new XMLWriter(writer, format);
            // 将document写入到输出流
            xmlWriter.write(document);
            return writer.toString();
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (xmlWriter != null) {
                xmlWriter.close();
            }
        }
    }

    /**
     * 转码unicode到中文
     *
     * @param unicode unicode编码的字符串
     * @return 解码后的字符串
     */
    public static String unicodeToChina(String unicode) throws Exception {
        //以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格
        String[] unicodeArray = unicode.split("\\\\u");

        StringBuilder returnStr = new StringBuilder();
        int num = 0;
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (String unicodeStr : unicodeArray) {
            if (num == 0) {
                returnStr.append(unicodeStr);
                num++;
            } else {
                //多余4个字符就存在其他非unicode字符，或者是英文
                String otherStr = null;
                if (unicodeStr.length() > 4) {
                    otherStr = unicodeStr.substring(4);
                    unicodeStr = unicodeStr.substring(0, 4);
                }
                if (StringUtils.isNotEmpty(unicodeStr)) {
                    returnStr.append((char) Integer.valueOf(unicodeStr, 16).intValue());
                }
                if (StringUtils.isNotEmpty(otherStr)) {
                    returnStr.append(otherStr);
                }
            }
        }
        return returnStr.toString();
    }

    /**
     * 字符串转换unicode(英文不转换)
     *
     * @param str 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String chinaToUnicode(String str) throws Exception {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        StringBuilder unicode = new StringBuilder();
        for (int charNum = 0; charNum < str.length(); charNum++) {
            // 取出每一个字符
            char c = str.charAt(charNum);
            //中文才转换
            if (isChinese(c)) {
                // 转换为unicode
                unicode.append("\\u");
                unicode.append(Integer.toHexString(c));
            } else {
                unicode.append(c);
            }
        }
        return unicode.toString();
    }

    /**
     * 判断字符是否为中文
     *
     * @param c 入参字符
     * @return true or false
     */
    public static boolean isChinese(char c) {
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(String.valueOf(c));
        return matcher.find();
    }

    /**
     * 正则测试
     *
     * @param reg     正则表达式
     * @param content 匹配内容
     * @return [0]:成功与否 [1]:正则表达式  [2]:匹配到的内容
     */
    public static String[] regTest(String reg, String content) {
        String[] returnValue = new String[3];
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(content);
        boolean result = matcher.find();
        returnValue[0] = result ? "匹配成功" : "匹配失败";
        if (result) {
            returnValue[1] = reg;
            returnValue[2] = matcher.group();
        }
        return returnValue;
    }

    /**
     * 利用反射将JSON转换为对应的已知的JavaBean
     * 比较耗时。反射遍历比较慢
     *
     * @param jsonObject 需要转换的JSON对象
     * @param t          需要转换的JavaBean
     * @param <T>        泛型
     * @return 返回转换后的JavaBean
     * @throws IllegalAccessException 抛出IllegalAccessException异常
     */
    public static <T extends Object> T jsonToObject(JSONObject jsonObject, T t) throws IllegalAccessException {

        // 反射获取该JavaBean所有的属性
        Field[] fields = t.getClass().getDeclaredFields();

        // 遍历所有属性
        for (Field field : fields) {
            // 如果该属性对应了JSON中的某个值,则对JavaBean进行赋值
            if (jsonObject.get(field.getName()) != null) {
                // 将JavaBean属性的Accessible设为True,避免Private属性无法读取
                field.setAccessible(true);
                // 对相应属性进行赋值
                if (field.getType() == java.util.Date.class) {
                    field.set(t, new Date(Long.parseLong(jsonObject.get(field.getName()).toString())));
                } else if (field.getType() == java.lang.Byte.class) {
                    field.set(t, Byte.parseByte(jsonObject.get(field.getName()).toString()));
                } else {
                    field.set(t, jsonObject.get(field.getName()));
                }

            }
        }
        // 返回转换后的JavaBean
        return t;

    }

    /**
     * 生成正则表达式
     *
     * @param regContent         需要匹配到数据，不为空
     * @param regNoChangeContent 固定不变的数据 可为空
     * @param withLine           是否换行匹配  可为空
     * @param isRegAll           是否贪婪模式  可为空
     * @param origStr            匹配的文本  不为空
     * @return 正则表达式，匹配到的数据
     */
    public static String[] regSet(String regContent, String regNoChangeContent, Object withLine, Object isRegAll, String origStr) {
        StringBuilder regBuilder = new StringBuilder();
//        regBuilder.append()
        String[] regReturn = new String[2];
        //普通情况
        if ((StringUtils.isEmpty(regNoChangeContent)
                && isRegAll.toString().equals("非贪婪模式"))
                ||
                (StringUtils.isNotEmpty(regNoChangeContent)
                        && regNoChangeContent.equals(regContent)
                        && isRegAll.toString().equals("非贪婪模式"))) {
            regReturn[0] = regBuilder.append("[").append(regContent).append("]{").append(regContent.length()).append("}").toString();
            regReturn[1] = regTest(regReturn[0], origStr)[2];
            return regReturn;
        }
        if ((StringUtils.isEmpty(regNoChangeContent)
                && !isRegAll.toString().contains("非贪婪模式"))
                ||
                (StringUtils.isNotEmpty(regNoChangeContent)
                        && regNoChangeContent.equals(regContent)
                        && !isRegAll.toString().contains("非贪婪模式"))) {
            regReturn[0] = regBuilder.append("[").append(regContent).append("]{").append(regContent.length()).append(",}").toString();
            regReturn[1] = regTest(regReturn[0], origStr)[2];
            return regReturn;
        }

        //存在固定字符
        if (StringUtils.isNotEmpty(regNoChangeContent)) {
            String[] position = regContent.split(regNoChangeContent);
            if (position.length == 2 && StringUtils.isEmpty(position[0])) {
                //固定不变的数据在开始
                String endStr = regContent.replaceFirst(regNoChangeContent, "");
                if (isRegAll.toString().equals("非贪婪模式")) {
                    regBuilder.append("[").append(regNoChangeContent).append("]{").append(regNoChangeContent.length()).append("}");
                } else {
                    regBuilder.append("[").append(regNoChangeContent).append("]{").append(regNoChangeContent.length()).append(",}");
                }
                if (withLine.toString().equals("匹配数据存在换行")) {
                    regBuilder.append("([\\s\\S]*?)");
                }
                regBuilder.append("[").append(endStr).append("]{0,").append(endStr.length()).append("}");
            } else if (position.length == 2 && StringUtils.isEmpty(position[1])) {
                //固定不变的数据在结尾
                String startStr = regContent.replaceFirst(regNoChangeContent, "");
                regBuilder.append("[").append(startStr).append("]{0,").append(startStr.length()).append("}");
                setPro(regNoChangeContent, withLine, isRegAll, regBuilder);
            } else if (position.length == 2
                    && StringUtils.isNotEmpty(position[1])
                    && StringUtils.isNotEmpty(position[0])) {
                //固定不变的数据在中间
                String startStr = regContent.substring(0, regContent.indexOf(regNoChangeContent));
                String endStr = regContent.substring(regContent.indexOf(regNoChangeContent) + 1);
                regBuilder.append("[").append(startStr).append("]{0,").append(startStr.length()).append("}");
                setPro(regNoChangeContent, withLine, isRegAll, regBuilder);
                if (withLine.toString().equals("匹配数据存在换行")) {
                    regBuilder.append("([\\s\\S]*?)");
                }
                regBuilder.append("[").append(endStr).append("]{0,").append(endStr.length()).append("}");
            }
        }

        regReturn[0] = regBuilder.toString();
        regReturn[1] = regTest(regReturn[0], origStr)[2];
        return regReturn;
    }

    private static void setPro(String regNoChangeContent, Object withLine, Object isRegAll, StringBuilder regBuilder) {
        if (withLine.toString().equals("匹配数据存在换行")) {
            regBuilder.append("([\\s\\S]*?)");
        }
        if (isRegAll.toString().equals("非贪婪模式")) {
            regBuilder.append("[").append(regNoChangeContent).append("]{").append(regNoChangeContent.length()).append("}");
        } else {
            regBuilder.append("[").append(regNoChangeContent).append("]{").append(regNoChangeContent.length()).append(",}");
        }
    }
}
