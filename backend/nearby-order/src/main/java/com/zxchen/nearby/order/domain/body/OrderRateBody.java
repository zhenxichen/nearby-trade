package com.zxchen.nearby.order.domain.body;

/**
 * 对订单进行评价的请求体
 */
public class OrderRateBody {

    // 订单号
    private String oid;

    // 评分
    private Integer rate;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

}
