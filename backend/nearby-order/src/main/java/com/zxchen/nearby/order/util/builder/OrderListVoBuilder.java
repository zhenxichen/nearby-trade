package com.zxchen.nearby.order.util.builder;

import com.zxchen.nearby.order.domain.dao.OrderListDao;
import com.zxchen.nearby.order.domain.po.Order;
import com.zxchen.nearby.order.domain.vo.OrderListVo;
import com.zxchen.nearby.order.enums.OrderRate;
import com.zxchen.nearby.order.enums.OrderStatus;
import com.zxchen.nearby.order.enums.OrderType;
import com.zxchen.nearby.order.util.PriceUtil;
import com.zxchen.nearby.order.util.RateUtil;

/**
 * 将从数据库读取的 OrderListDao 转换为返回给前端的 OrderListVo
 */
public class OrderListVoBuilder {

    /**
     * 将从数据库读取的 OrderListDao 转换为返回给前端的 OrderListVo
     *
     * @param dao 从数据库读取的 OrderListDao
     * @param uid 用户的UID
     * @return 返回给前端的 OrderListVo
     */
    public static OrderListVo build(OrderListDao dao, Long uid) {
        OrderListVo vo = new OrderListVo();

        // 填充不需改变的部分
        vo.setOid(dao.getOid().toString());
        vo.setImage(dao.getImage());
        vo.setDescription(dao.getDescription());
        vo.setStatus(dao.getStatus());

        // 将价格从 Integer 转为字符串格式
        if (dao.getPrice() != null) {
            vo.setPrice(PriceUtil.priceIntegerToString(dao.getPrice()));
        }
        // 设置是否为交换单
        vo.setExchange(isExchange(dao.getType()));
        // 设置是否为卖家
        vo.setSeller(isSeller(dao, uid));
        // 设置对方UID
        vo.setOppositeId(isSeller(dao, uid) ? dao.getBuyer() : dao.getSeller());
        // 判断用户目前是否可以进行评价
        vo.setRate(couldRateNow(dao, uid));

        return vo;
    }

    /**
     * 判断订单是否为交换单
     */
    private static Boolean isExchange(OrderType type) {
        return type == OrderType.EXCHANGE;
    }

    /**
     * 判断当前用户是否为订单的卖家
     */
    private static Boolean isSeller(OrderListDao dao, Long uid) {
        return dao.getSeller().equals(uid);
    }

    /**
     * 判断当前用户是否是订单的买家
     */
    private static Boolean isBuyer(OrderListDao dao, Long uid) {
        return dao.getBuyer().equals(uid);
    }

    /**
     * 判断用户当前是否可进行评价
     */
    private static Boolean couldRateNow(OrderListDao dao, Long uid) {
        Order order = new Order();
        order.setOid(dao.getOid());
        order.setBuyer(dao.getBuyer());
        order.setSeller(dao.getSeller());
        order.setStatus(dao.getStatus());
        order.setRated(dao.getRated());

        return RateUtil.couldRate(order, uid);
    }

}
