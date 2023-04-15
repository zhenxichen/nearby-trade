package com.zxchen.nearby.order.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderRole {
    BUYER(1),
    SELLER(2);

    private Integer code;

    OrderRole(Integer code) {
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
    public static OrderRole ofCode(Integer code) {
        for (OrderRole role : values()) {
            if (role.getCode() == code) {
                return role;
            }
        }
        return null;
    }
}
