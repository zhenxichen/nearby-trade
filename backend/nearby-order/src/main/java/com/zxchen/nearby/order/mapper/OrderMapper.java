package com.zxchen.nearby.order.mapper;

import com.zxchen.nearby.order.domain.dto.OrderListQueryDto;
import com.zxchen.nearby.order.domain.dao.OrderListDao;
import com.zxchen.nearby.order.domain.po.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 订单 数据层
 */
@Mapper
public interface OrderMapper {

    /**
     * 获取用户的订单列表
     *
     * @param body 包含用户UID以及筛选条件（用户角色、订单状态）
     * @return 订单列表
     */
    List<OrderListDao> selectOrderList(OrderListQueryDto body);

    /**
     * 向数据库中插入订单
     *
     * @param order
     * @return
     */
    int insertOrder(Order order);

    /**
     * 根据订单号查询订单
     *
     * @param oid 订单号
     * @return 订单数据表
     */
    Order selectOrderByOid(Long oid);

    /**
     * 对订单进行更新
     *
     * @param order 填写订单号以及要进行更新的数据
     * @return update操作影响的行数
     */
    int updateOrder(Order order);

    /**
     * 卖家接受其中一个订单时，将同一商品的其他订单状态设为关闭
     *
     * @param order 填写订单号以及关联的商品ID
     * @return update操作影响的条数
     */
    int updateOtherOrderToBeClosed(Order order);

    /**
     * 关闭与该商品关联的所有未接受订单
     *
     * @param cid 商品ID
     * @return update操作影响的行数
     */
    int updateRelatedOrderToBeClosed(Long cid);

}
