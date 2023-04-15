package com.zxchen.nearby.order.enums;

/**
 * 订单评价 枚举
 */
public enum OrderRate {

    /**
     * 双方未评价
     */
    UNRATED,

    /**
     * 买家已评价，卖家未评价
     */
    BUYER_RATED,

    /**
     * 买家未评价，卖家已评价
     */
    SELLER_RATED,

    /**
     * 双方已评价
     */
    RATED

}
