package com.zxchen.nearby.order.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 订单状态 枚举
 */
public enum OrderStatus {
    WAIT_ACCEPT(0, "待接单"),
    IN_PROGRESS(1, "进行中"),
    FINISHED(2, "已完成"),
    CLOSED(3, "已关闭");

    private Integer code;

    private String name;

    OrderStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(OrderStatus o) {
        return o.getCode() == getCode();
    }

    @JsonCreator
    public static OrderStatus ofCode(Integer code) {
        for (OrderStatus orderStatus : values()) {
            if (orderStatus.getCode() == code) {
                return orderStatus;
            }
        }
        return null;
    }
}
