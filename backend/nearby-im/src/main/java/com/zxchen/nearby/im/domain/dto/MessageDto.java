package com.zxchen.nearby.im.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxchen.nearby.im.enums.MessageType;

import java.util.Date;

public class MessageDto {

    // 消息ID
    private Long mid;

    private Long from;

    private Long to;

    private MessageType type;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    public MessageDto() {
    }

    public MessageDto(Long from, Long to, MessageType type, String content, Date time) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.content = content;
        this.time = time;
    }

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}
