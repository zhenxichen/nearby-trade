package com.zxchen.nearby.im.domain.body;

import com.zxchen.nearby.im.enums.MessageType;

/**
 * 从前端传递的消息体
 */
public class MessageBody {

    private Long to;

    private MessageType type;

    private String content;

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
}
