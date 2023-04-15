package com.zxchen.nearby.im.domain.vo;

import java.util.Date;

/**
 * 向前端返回消息列表中显示信息的Vo
 */
public class MessageListVo {

    // 对方的UID
    private Long opposite;

    // 最新一条消息的内容
    private String content;

    // 最后一条消息的时间
    private Date time;

    // 对方的头像
    private String avatar;

    // 对方的用户名
    private String username;

    // 未读消息的条数
    private Integer count;

    public Long getOpposite() {
        return opposite;
    }

    public void setOpposite(Long opposite) {
        this.opposite = opposite;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    
}
