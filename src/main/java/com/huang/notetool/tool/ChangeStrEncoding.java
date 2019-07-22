package com.huang.notetool.tool;

import com.huang.notetool.util.StringUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;

/**
 * 字符串编码解码
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-05-28 09:50:44
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-28   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class ChangeStrEncoding {
    private static Logger logger = Logger.getLogger(ChangeStrEncoding.class);
    /**
     * 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁块
     */
    private static final String US_ASCII = "US-ASCII";

    /**
     * ISO 拉丁字母表 No.1，也叫作 ISO-LATIN-1
     */
    private static final String ISO_8859_1 = "ISO-8859-1";

    /**
     * 8 位 UCS 转换格式
     */
    private static final String UTF_8 = "UTF-8";

    /**
     * 16 位 UCS 转换格式，Big Endian（最低地址存放高位字节）字节顺序
     */
    private static final String UTF_16BE = "UTF-16BE";

    /**
     * 16 位 UCS 转换格式，Little-endian（最高地址存放低位字节）字节顺序
     */
    private static final String UTF_16LE = "UTF-16LE";

    /**
     * 16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识
     */
    private static final String UTF_16 = "UTF-16";

    /**
     * 中文超大字符集
     */
    private static final String GBK = "GBK";
    /**
     * 中文超大字符集
     */
    private static final String GB2312 = "GB2312";

    /**
     * 将字符编码转换成US-ASCII码
     */
    public static String toASCII(String str) throws UnsupportedEncodingException {
        return changeCharset(str, US_ASCII);
    }

    /**
     * 将字符编码转换成US-ASCII码
     */
    public static String toASCIIWithOrigEncoding(String str, String origEncoding) throws UnsupportedEncodingException {
        return changeCharset(str, origEncoding, US_ASCII);
    }

    /**
     * 将字符编码转换成ISO-8859-1码
     */
    public static String toISO_8859_1(String str) throws UnsupportedEncodingException {
        return changeCharset(str, ISO_8859_1);
    }

    /**
     * 将字符编码转换成ISO-8859-1码
     */
    public static String toISO_8859_1WithOrigEncoding(String str, String origEncoding) throws UnsupportedEncodingException {
        return changeCharset(str, origEncoding, ISO_8859_1);
    }

    /**
     * 将字符编码转换成UTF-8码
     */
    public static String toUTF_8(String str) throws UnsupportedEncodingException {
        return changeCharset(str, UTF_8);
    }

    /**
     * 将字符编码转换成UTF-8码
     */
    public static String toUTF_8(String str, String origEncoding) throws UnsupportedEncodingException {
        return changeCharset(str, origEncoding, UTF_8);
    }

    /**
     * 将字符编码转换成UTF-16BE码
     */
    public static String toUTF_16BE(String str) throws UnsupportedEncodingException {
        return changeCharset(str, UTF_16BE);
    }

    /**
     * 将字符编码转换成UTF-16BE码
     */
    public static String toUTF_16BE(String str, String origEncoding) throws UnsupportedEncodingException {
        return changeCharset(str, origEncoding, UTF_16BE);
    }

    /**
     * 将字符编码转换成UTF-16LE码
     */
    public static String toUTF_16LE(String str) throws UnsupportedEncodingException {
        return changeCharset(str, UTF_16LE);
    }

    /**
     * 将字符编码转换成UTF-16LE码
     */
    public static String toUTF_16LE(String str, String origEncoding) throws UnsupportedEncodingException {
        return changeCharset(str, origEncoding, UTF_16LE);
    }

    /**
     * 将字符编码转换成UTF-16码
     */
    public static String toUTF_16(String str) throws UnsupportedEncodingException {
        return changeCharset(str, UTF_16);
    }

    /**
     * 将字符编码转换成UTF-16码
     */
    public static String toUTF_16(String str, String origEncoding) throws UnsupportedEncodingException {
        return changeCharset(str, origEncoding, UTF_16);
    }

    /**
     * 将字符编码转换成GBK码
     */
    public static String toGBK(String str) throws UnsupportedEncodingException {
        return changeCharset(str, GBK);
    }

    /**
     * 将字符编码转换成GBK码
     */
    public static String toGBK(String str, String origEncoding) throws UnsupportedEncodingException {
        return changeCharset(str, origEncoding, GBK);
    }

    /**
     * 将字符编码转换成GB2312码
     */
    public static String toGB2312(String str) throws UnsupportedEncodingException {
        return changeCharset(str, GB2312);
    }

    /**
     * 将字符编码转换成GB2312码
     */
    public static String toGB2312(String str, String origEncoding) throws UnsupportedEncodingException {
        return changeCharset(str, origEncoding, GB2312);
    }

    /**
     * 字符串编码转换的实现方法
     *
     * @param str        待转换编码的字符串
     * @param newCharset 目标编码
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String changeCharset(String str, String newCharset)
            throws UnsupportedEncodingException {
        if (StringUtils.isNotEmpty(str)) {
            // 用默认字符编码解码字符串。
            byte[] bs = str.getBytes();
            logger.info("将字符串从转换为" + newCharset);
            // 用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }

    /**
     * 字符串编码转换的实现方法
     *
     * @param str        待转换编码的字符串
     * @param oldCharset 原编码
     * @param newCharset 目标编码
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String changeCharset(String str, String oldCharset, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            // 用旧的字符编码解码字符串。解码可能会出现异常。
            byte[] bs = str.getBytes(oldCharset);
            logger.info("将字符串从" + oldCharset + "转换为" + newCharset);
            // 用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }


}
