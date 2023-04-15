package com.zxchen.nearby.order.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 订单类型
 */
public enum OrderType {
    PURCHASE(0),
    EXCHANGE(1);

    private Integer code;

    OrderType(Integer code) {
        this.code = code;
    }

    @JsonValue
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @JsonCreator
    public static OrderType ofCode(Integer code) {
        for (OrderType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}
