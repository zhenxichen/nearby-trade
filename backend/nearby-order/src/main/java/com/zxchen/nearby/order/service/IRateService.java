package com.zxchen.nearby.order.service;

import com.zxchen.nearby.order.domain.dto.RateDto;

/**
 * 用户评分 服务层
 */
public interface IRateService {

    /**
     * 查看用户的评分
     *
     * @param uid 用户UID
     * @return 用户的平均评分
     */
    double getUserRate(Long uid);

    /**
     * 对订单进行评分
     *
     * @param dto 包含操作者UID、订单号以及
     */
    void rate(RateDto dto);

}
