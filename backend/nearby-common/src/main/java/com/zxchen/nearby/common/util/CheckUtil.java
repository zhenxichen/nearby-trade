package com.zxchen.nearby.common.util;

public class CheckUtil {

    private static final String EMPTY_STRING = "";

    /**
     * 判断对象是否为 null
     *
     * @param object 需要判断的对象
     * @return 对象是否为 null
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 需要判断的字符串
     * @return 字符串是否为 null 或 空值
     */
    public static boolean isEmpty(String str) {
        return str == null || EMPTY_STRING.equals(str);
    }

    /**
     * 判断字符串是否非空
     *
     * @param str 需要判断的字符串
     * @return 字符串不是 null 或 空值
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
