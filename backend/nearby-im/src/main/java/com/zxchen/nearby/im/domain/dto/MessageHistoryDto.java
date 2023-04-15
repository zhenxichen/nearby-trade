package com.zxchen.nearby.im.domain.dto;

/**
 * 将查询消息历史的请求传递到 service 层的DTO
 */
public class MessageHistoryDto {

    // 查询用户的UID
    private Long uid;

    // 对方的UID
    private Long oppositeId;

    // 查询的起点（若查询全部则为null）
    private Long last;

    public MessageHistoryDto() {
    }

    public MessageHistoryDto(Long uid, Long oppositeId, Long start) {
        this.uid = uid;
        this.oppositeId = oppositeId;
        this.last = start;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getOppositeId() {
        return oppositeId;
    }

    public void setOppositeId(Long oppositeId) {
        this.oppositeId = oppositeId;
    }

    public Long getLast() {
        return last;
    }

    public void setLast(Long last) {
        this.last = last;
    }
}
