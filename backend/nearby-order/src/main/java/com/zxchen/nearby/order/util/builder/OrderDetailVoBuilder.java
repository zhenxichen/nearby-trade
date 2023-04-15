package com.zxchen.nearby.order.util.builder;

import com.zxchen.nearby.common.domain.cache.UserInfoCache;
import com.zxchen.nearby.order.domain.dto.CommodityCache;
import com.zxchen.nearby.order.domain.po.Order;
import com.zxchen.nearby.order.domain.vo.OrderDetailVo;
import com.zxchen.nearby.order.domain.vo.UserInfoVo;
import com.zxchen.nearby.order.enums.OrderType;
import com.zxchen.nearby.order.util.PriceUtil;

/**
 * 创建将订单详情返回给的前端的 VO 的工具类
 */
public class OrderDetailVoBuilder {

    /**
     * 通过订单数据建立 VO
     *
     * @param order 订单数据
     * @return VO
     */
    public static OrderDetailVo buildWithOrderInfo(Order order) {
        OrderDetailVo vo = new OrderDetailVo();

        // 订单号以字符串形式返回
        vo.setOid(order.getOid().toString());

        // 将无需调整的数据直接写入
        vo.setStatus(order.getStatus());
        vo.setTradeTime(order.getTradeTime());
        vo.setCreateTime(order.getCreateTime());
        vo.setFinishTime(order.getFinishTime());
        vo.setLatitude(order.getLatitude());
        vo.setLongitude(order.getLongitude());
        vo.setLocation(order.getLocationTitle());
        vo.setAddress(order.getLocationAddress());
        vo.setExchangeImage(order.getExchangeImage());
        vo.setExchangeDescription(order.getExchangeDescription());

        // 判断订单是否为交换单
        vo.setExchange(order.getType() == OrderType.EXCHANGE);

        // 将价格由整形转为字符串
        vo.setPrice(PriceUtil.priceIntegerToString(order.getPrice()));
        return vo;
    }

    /**
     * 将商品数据写入 VO
     *
     * @param vo 要写入数据的VO
     * @param cache 商品简要信息
     * @return VO
     */
    public static OrderDetailVo writeCommodityInfo(OrderDetailVo vo, CommodityCache cache) {
        vo.setCid(cache.getCid());
        vo.setCommodityDescription(cache.getTitle());
        vo.setCommodityImage(cache.getImage());

        return vo;
    }

    /**
     * 将对方用户的数据写入 VO
     *
     * @param vo 要写入数据的VO
     * @param cache 对方用户简要信息
     * @return VO
     */
    public static OrderDetailVo writeOppositeUserInfo(OrderDetailVo vo, UserInfoCache cache) {
        vo.setOppositeId(cache.getUid());
        vo.setOppositeName(cache.getUsername());
        vo.setOppositeAvatar(cache.getAvatar());

        return vo;
    }
}
