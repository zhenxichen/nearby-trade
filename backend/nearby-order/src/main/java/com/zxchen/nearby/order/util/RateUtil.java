package com.zxchen.nearby.order.util;

import com.zxchen.nearby.order.domain.po.Order;
import com.zxchen.nearby.order.enums.OrderRate;
import com.zxchen.nearby.order.enums.OrderStatus;

/**
 * 判断用户是否具有评价条件的工具类
 */
public class RateUtil {

    /**
     * 判断用户当前是否可对订单进行评论
     *
     * @param order 订单
     * @param uid 用户UID
     * @return 用户当前是否可对订单进行评论
     */
    public static Boolean couldRate(Order order, Long uid) {
        if (order.getStatus() != OrderStatus.FINISHED) {
            return false;
        }
        switch (order.getRated()) {
            case UNRATED:
                return true;
            case BUYER_RATED:
                return isSeller(order, uid);
            case SELLER_RATED:
                return isBuyer(order, uid);
            case RATED:
                return false;
        }
        return false;
    }

    /**
     * 判断用户是否是订单买家
     */
    private static Boolean isBuyer(Order order, Long uid) {
        return order.getBuyer().equals(uid);
    }

    /**
     * 判断用户是否是订单卖家
     */
    private static Boolean isSeller(Order order, Long uid) {
        return order.getSeller().equals(uid);
    }

}
