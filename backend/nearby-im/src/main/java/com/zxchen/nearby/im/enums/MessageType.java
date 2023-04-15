package com.zxchen.nearby.im.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageType {
    TEXT(0, "text"),
    IMAGE(1, "image"),
    LOCATION(2, "location"),
    ACK(3, "ack");

    private Integer code;

    private String name;

    MessageType(Integer code, String name) {
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

    public boolean equals(MessageType type) {
        return type.getCode() == getCode();
    }

    @JsonCreator
    public static MessageType ofCode(Integer code) {
        for (MessageType messageType: values()) {
            if (messageType.getCode() == code) {
                return messageType;
            }
        }
        return null;
    }



}
