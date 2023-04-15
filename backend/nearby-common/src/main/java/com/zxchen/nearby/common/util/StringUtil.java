package com.zxchen.nearby.common.util;

import com.zxchen.nearby.common.constant.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * 对字符串进行操作的工具类
 */
public class StringUtil {

    /**
     * 隐藏手机号码的中间四位
     *
     * @param phone 11位手机号码
     * @return 将中间四位隐藏后的手机号码
     */
    public static String maskPhoneNumber(String phone) {
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 将 List 当中的字符串串接成一个字符串
     *
     * @param list 字符串列表
     * @param separator 字符串之间的分隔符
     * @return 串接后得到的字符串
     */
    public static String concat(List<String> list, char separator) {
        return String.join(Character.toString(separator), list);
    }

    /**
     * 将 List 当中的字符串串接成一个字符串
     *
     * @param list 字符串列表
     * @param separator 字符串之间的分隔符
     * @return 串接后得到的字符串
     */
    public static String concat(List<String> list, String separator) {
        return String.join(separator, list);
    }

    /**
     * 将字符串分割为字符串列表
     *
     * @param str 待分割的字符串
     * @param separator 分隔符
     * @return 分割后得到的List
     */
    public static List<String> split(String str, String separator) {
        String[] arr = str.split(separator);
        return Arrays.asList(arr);
    }

    /**
     * 将字符串以空格（一个或多个）划分为字符串列表
     *
     * @param str 待分割的字符串
     * @return 分割完成后得到的List
     */
    public static List<String> splitBySpace(String str) {
        // 去除首尾空格
        String trimStr = str.trim();
        // 以空格划分为List
        return Arrays.asList(trimStr.split("\\s+"));
    }

    /**
     * 将字符串超过长度限制的部分用省略号省略
     *
     * @param src 字符串
     * @param maxLength 最大长度限制
     * @return
     */
    public static String omit(String src, int maxLength) {
        return omit(src, maxLength, Constants.ELLIPSIS);
    }

    /**
     * 将字符串超过长度限制的部分用指定的省略符省略
     *
     * @param src 字符串
     * @param maxLength 最大长度限制
     * @return
     */
    public static String omit(String src, int maxLength, String ellipsis) {
        // 若未超过最大长度限制，直接返回
        if (src.length() <= maxLength) {
            return src;
        }
        // 初始化StringBuilder，提前确定分配空间大小
        return new StringBuilder(maxLength + ellipsis.length())
                .append(src, 0, maxLength)
                .append(ellipsis)
                .toString();
    }

}
