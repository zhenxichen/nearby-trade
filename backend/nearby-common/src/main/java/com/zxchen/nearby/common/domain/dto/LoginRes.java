package com.zxchen.nearby.common.domain.dto;

/**
 * 返回登录结果的 DTO
 */
public class LoginRes {

    private String token;

    private String uid;

    public LoginRes(String token, String uid) {
        this.token = token;
        this.uid = uid;
    }

    public LoginRes(String token, Long uid) {
        this.token = token;
        this.uid = uid.toString();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
