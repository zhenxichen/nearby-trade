package com.zxchen.nearby.order.domain.vo;

/**
 * 返回给前端用户用户信息页展示的VO
 */
public class UserInfoVo {

    private Long uid;

    private String username;

    private String avatar;

    private double rate;

    private Boolean myself;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Boolean getMyself() {
        return myself;
    }

    public void setMyself(Boolean myself) {
        this.myself = myself;
    }

}
