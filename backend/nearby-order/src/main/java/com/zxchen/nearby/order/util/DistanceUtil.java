package com.zxchen.nearby.order.util;

import org.springframework.data.geo.Distance;

import java.math.BigDecimal;

/**
 * 对距离的格式进行转换
 */
public class DistanceUtil {

    private static final BigDecimal KILO_METER = new BigDecimal(1000);

    // 保留两位小数
    private static final int SCALE = 2;

    private static final int GREATER_THAN = 1;

    private static final String KILO_METER_TAG = "km";
    private static final String METER_TAG = "m";

    /**
     * 将单位为米的 Distance 对象转换为字符串
     *
     * @param distance Distance对象，需设置单位为米
     * @return 字符串表示
     */
    public static String convertMeterDistanceToString(Distance distance) {
        BigDecimal meter = new BigDecimal(distance.getValue());
        // 若超过 1km，则用km为单位表示
        if (meter.compareTo(KILO_METER) == GREATER_THAN) {
            BigDecimal km = meter.divide(KILO_METER).setScale(SCALE, BigDecimal.ROUND_HALF_UP);
            return km.doubleValue() + KILO_METER_TAG;
        }
        return meter.intValue() + METER_TAG;
    }

    /**
     * 将单位为米的 double 格式距离转换为字符串
     *
     * @param distance double格式的距离，单位为米
     * @return 字符串表示
     */
    public static String convertMeterDistanceToString(double distance) {
        BigDecimal meter = new BigDecimal(distance);
        // 若超过 1km，则用km为单位表示
        if (meter.compareTo(KILO_METER) == GREATER_THAN) {
            BigDecimal km = meter.divide(KILO_METER).setScale(SCALE, BigDecimal.ROUND_HALF_UP);
            return km.doubleValue() + KILO_METER_TAG;
        }
        return meter.intValue() + METER_TAG;
    }
}
