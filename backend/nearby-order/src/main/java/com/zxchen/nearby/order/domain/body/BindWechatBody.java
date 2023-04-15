package com.zxchen.nearby.order.domain.body;

/**
 * 从前端接收绑定微信数据的请求体
 */
public class BindWechatBody {

    private String openid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
