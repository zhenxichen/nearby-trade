package com.zxchen.nearby.common.domain;

import java.io.Serializable;

/**
 * 用户对象 对应 user 表
 */
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    // 用户ID
    private Long uid;

    // 用户名
    private String username;

    // 密码
    private String password;

    // 手机号
    private String phone;

    // 头像地址
    private String avatar;

    // 微信 OpenID
    private String openId;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
