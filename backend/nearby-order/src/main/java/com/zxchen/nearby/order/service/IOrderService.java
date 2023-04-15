package com.zxchen.nearby.order.service;

import com.zxchen.nearby.order.domain.dto.OrderCreateDto;
import com.zxchen.nearby.order.domain.dto.OrderListQueryDto;
import com.zxchen.nearby.order.domain.dto.OrderOpsDto;
import com.zxchen.nearby.order.domain.vo.OrderDetailVo;
import com.zxchen.nearby.order.domain.vo.OrderListVo;

import java.util.List;

/**
 * 订单 服务层
 */
public interface IOrderService {

    /**
     * 获取用户订单列表
     *
     * @param dto 包含用户UID，以及筛选条件（包括用户角色和订单状态）
     * @return 订单列表
     */
    List<OrderListVo> getOrderList(OrderListQueryDto dto);

    /**
     * 建立订单
     *
     * @param dto 包括交易的商品，交易时间、地点，交易形式，交易价格或交换物信息
     */
    void createOrder(OrderCreateDto dto);

    /**
     * 取消订单
     *
     * @param dto 包括操作者的UID以及订单号
     */
    void cancelOrder(OrderOpsDto dto);

    /**
     * 卖家接受订单
     *
     * @param dto 包括操作者的UID和订单号
     */
    void acceptOrder(OrderOpsDto dto);

    /**
     * 卖家下架商品（同时关闭所有有关订单）
     *
     * @param uid 操作者UID
     * @param cid 商品ID
     */
    void offShelfAndCloseRelatedOrder(Long uid, Long cid);

    /**
     * 买家或卖家确认订单结单
     *
     * @param dto 包括操作者的UID和订单号
     */
    void confirmOrder(OrderOpsDto dto);

    /**
     * 查看订单详情
     *
     * @param dto 包括操作者的UID和订单号
     * @return 订单的详细数据
     */
    OrderDetailVo orderDetail(OrderOpsDto dto);

}
