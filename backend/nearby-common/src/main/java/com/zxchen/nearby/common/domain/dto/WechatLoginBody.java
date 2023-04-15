package com.zxchen.nearby.common.domain.dto;

/**
 * 传输微信快速登录请求的DTO
 */
public class WechatLoginBody {

    private String openid;      // 微信openid

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

}
