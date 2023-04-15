package com.zxchen.nearby.common.domain.cache;

import java.io.Serializable;

/**
 * 缓存用户的简要信息
 */
public class UserInfoCache implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long uid;

    private String username;

    private String avatar;

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
}
