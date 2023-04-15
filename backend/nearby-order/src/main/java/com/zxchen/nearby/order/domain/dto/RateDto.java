package com.zxchen.nearby.order.domain.dto;

import com.zxchen.nearby.order.domain.body.OrderRateBody;

/**
 * 用户进行评价的 DTO
 */
public class RateDto {

    private Long uid;

    private Long oid;

    private Integer rate;

    public RateDto() {
    }

    public RateDto(Long uid, Long oid, Integer rate) {
        this.uid = uid;
        this.oid = oid;
        this.rate = rate;
    }

    public RateDto(Long uid, OrderRateBody body) {
        this.uid = uid;
        this.oid = Long.parseLong(body.getOid());
        this.rate = body.getRate();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
}
