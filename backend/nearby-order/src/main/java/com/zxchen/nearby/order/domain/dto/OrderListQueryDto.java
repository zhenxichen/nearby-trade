package com.zxchen.nearby.order.domain.dto;

import com.zxchen.nearby.order.enums.OrderRole;
import com.zxchen.nearby.order.enums.OrderStatus;

/**
 * 获取订单列表的参数类DTO
 */
public class OrderListQueryDto {

    private Long uid;

    private OrderRole role;

    private OrderStatus status;

    public OrderListQueryDto(Long uid, OrderRole role, OrderStatus status) {
        this.uid = uid;
        this.role = role;
        this.status = status;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

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
