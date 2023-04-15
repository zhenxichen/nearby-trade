package com.zxchen.nearby.im.domain.body;

/**
 * 用于获取聊天记录请求的请求体
 */
public class MessageHistoryBody {

    private Long opposite;

    private Long last;

    public Long getOpposite() {
        return opposite;
    }

    public void setOpposite(Long opposite) {
        this.opposite = opposite;
    }

    public Long getLast() {
        return last;
    }

    public void setLast(Long last) {
        this.last = last;
    }

}
