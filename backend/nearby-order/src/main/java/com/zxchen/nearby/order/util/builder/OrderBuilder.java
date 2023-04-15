package com.zxchen.nearby.order.util.builder;

import com.zxchen.nearby.order.domain.dto.OrderCreateDto;
import com.zxchen.nearby.order.domain.po.Order;
import com.zxchen.nearby.order.enums.OrderType;
import com.zxchen.nearby.order.util.PriceUtil;

/**
 * 将各类对象转换为订单 PO 对象
 */
public class OrderBuilder {

    /**
     * 将订单号以及订单创建的 DTO 对象，转换为对应数据库信息的 PO 对象
     *
     * @param orderNo 订单号，由 SnowFlake 算法实现 {@link com.zxchen.nearby.common.service.SnowFlakeService}
     * @param dto 订单创建的 DTO
     * @param seller 商品的卖家UID
     * @return
     */
    public static Order build(Long orderNo, OrderCreateDto dto, Long seller) {
        Order order = new Order();
        order.setOid(orderNo);
        order.setBuyer(dto.getUid());
        order.setSeller(seller);
        order.setCid(dto.getCid());
        order.setTradeTime(dto.getTradeTime());
        order.setLatitude(dto.getLatitude());
        order.setLongitude(dto.getLongitude());
        order.setLocationTitle(dto.getLocation());
        order.setLocationAddress(dto.getAddress());
        order.setType(dto.getType());
        switch (dto.getType()) {
            case PURCHASE:
                order.setPrice(PriceUtil.priceStringToInteger(dto.getPrice()));
                break;
            case EXCHANGE:
                order.setExchangeImage(dto.getImage());
                order.setExchangeDescription(dto.getDescription());
                break;
        }
        return order;
    }

}
