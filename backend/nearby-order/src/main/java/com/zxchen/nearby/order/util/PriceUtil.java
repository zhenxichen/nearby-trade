package com.zxchen.nearby.order.util;

import java.math.BigDecimal;

/**
 * 对价格数值进行操作的工具类
 */
public class PriceUtil {

    // 分到元的转换关系（100分=1元）
    private static final long CENT_TO_YUAN = 100L;

    // 保留两位小数
    private static final int SCALE = 2;

    /**
     * 将价格字符串转为int型数据（以分为单位）
     *
     * @param str 价格字符串
     * @return int型数据（分为单位）
     */
    public static int priceStringToInteger(String str) {
        BigDecimal priceYuan = new BigDecimal(str);
        BigDecimal price = priceYuan.multiply(BigDecimal.valueOf(CENT_TO_YUAN));
        return price.intValue();
    }

    /**
     * 将以分为单位存储的int型价格数据转为字符串
     *
     * @param price int型价格数据（单位为分）
     * @return 价格字符串（单位为元）
     */
    public static String priceIntegerToString(Integer price) {
        if (price == null) {
            return null;
        }
        BigDecimal priceCent = new BigDecimal(price);
        BigDecimal priceYuan = priceCent.divide(BigDecimal.valueOf(CENT_TO_YUAN));
        priceYuan.setScale(SCALE, BigDecimal.ROUND_HALF_UP);
        return String.format("%.2f", priceYuan);
    }

}
