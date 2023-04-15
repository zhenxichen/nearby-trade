package com.zxchen.nearby.order.domain.dao;

/**
 * 对评分进行编辑的 DAO
 */
public class RateDao {

    private Long target;

    private Integer rate;

    public RateDao(Long target, Integer rate) {
        this.target = target;
        this.rate = rate;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

}
