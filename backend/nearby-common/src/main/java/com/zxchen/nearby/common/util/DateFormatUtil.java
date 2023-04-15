package com.zxchen.nearby.common.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 对日期格式进行操作的工具类
 */
public class DateFormatUtil {

    private static DateTimeFormatter slashDateFormatter
            = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /**
     * 获取当前日期通过斜线分割的形式
     * @return 如(2021/12/09)
     */
    public static String getCurrentDateSeparateBySlash() {
        return slashDateFormatter.format(ZonedDateTime.now());
    }
}
