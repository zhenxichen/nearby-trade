package com.zxchen.nearby.order.domain.body;

import com.zxchen.nearby.order.enums.OrderRole;
import com.zxchen.nearby.order.enums.OrderStatus;

/**
 * 获取订单列表的请求体（筛选条件）
 */
public class OrderListBody {

    // 用户在订单中的角色（买家或卖家）
    private OrderRole role;

    // 订单状态
    private OrderStatus status;

    public OrderRole getRole() {
        return role;
    }

    public void setRole(OrderRole role) {
        this.role = role;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

}
