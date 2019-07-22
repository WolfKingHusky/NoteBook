package com.huang.notetool.util;

/**
 * 字符串空判断
 *
 * @author 黄先生
 * @date 2019-03-04
 */
public class StringUtils {
    /**
     * 不为空判断
     *
     * @param value 字符串
     * @return 是否为空
     */
    public static boolean isNotEmpty(String value) {
        return (null != value && !"".equals(value));
    }

    /**
     * 为空判断
     *
     * @param value 字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String value) {
        return (null == value || "".equals(value));
    }
}