package com.zxchen.nearby.order.constant;

/**
 * 订单交易模块涉及的常量
 */
public class OrderConstants {

    /**
     * 存储商品地理位置信息的 geo set的键
     */
    public static final String REDIS_GEO_KEY = "commodity_geo";

    /**
     * 存储商品缓存信息的键的前缀（key格式为前缀+cid）
     */
    public static final String REDIS_COMMODITY_PREFIX = "commodity:";

    /**
     * 数据库中多张商品图片的分隔符
     */
    public static final String IMAGE_SEPARATOR = ";";

    /**
     * 默认的搜索半径
     */
    public static final int DEFAULT_RADIUS_METERS = 5000;

    /**
     * 若已收藏商品，数据库的返回值
     */
    public static final int COLLECTED = 1;

}
