package com.huang.notetool.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Url工具
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-06-06 16:42:08
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-06   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class UrlUtils {
    /**
     * URL加密
     *
     * @param msg      明文
     * @param encoding 编码
     * @return 密文
     */
    public static String urlEncode(String msg, String encoding) throws UnsupportedEncodingException {
        return URLEncoder.encode(msg, encoding);
    }

    /**
     * URL解密
     *
     * @param msg      密文
     * @param encoding 编码
     * @return 密文
     */
    public static String urlDecode(String msg, String encoding) throws UnsupportedEncodingException {
        return URLDecoder.decode(msg, encoding);
    }
}
