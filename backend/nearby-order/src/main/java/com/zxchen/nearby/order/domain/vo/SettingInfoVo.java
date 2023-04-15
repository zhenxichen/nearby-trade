package com.zxchen.nearby.order.domain.vo;

/**
 * 用户设置界面展示信息的 VO
 */
public class SettingInfoVo {

    // 用户名
    private String username;

    // 头像地址
    private String avatar;

    // 用户手机号
    private String phone;

    // 是否绑定微信
    private boolean bindWechat;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isBindWechat() {
        return bindWechat;
    }

    public void setBindWechat(boolean bindWechat) {
        this.bindWechat = bindWechat;
    }

}
