package com.zxchen.nearby.order.service.impl;

import com.zxchen.nearby.common.constant.StatusCode;
import com.zxchen.nearby.common.exception.CustomException;
import com.zxchen.nearby.order.domain.dao.RateDao;
import com.zxchen.nearby.order.domain.dto.RateDto;
import com.zxchen.nearby.order.domain.po.Order;
import com.zxchen.nearby.order.enums.OrderRate;
import com.zxchen.nearby.order.enums.OrderRole;
import com.zxchen.nearby.order.mapper.OrderMapper;
import com.zxchen.nearby.order.mapper.RateMapper;
import com.zxchen.nearby.order.service.IOrderService;
import com.zxchen.nearby.order.service.IRateService;
import com.zxchen.nearby.order.util.RateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RateServiceImpl implements IRateService {

    // 评分保留一位小数
    private static final int SCALE = 1;

    // 若没有评分，则默认评分为0.0
    private static final double DEFAULT_RATE = 0.0;

    private RateMapper rateMapper;

    private OrderMapper orderMapper;

    /**
     * 查看用户的评分
     *
     * @param uid 用户UID
     * @return 用户的平均评分
     */
    @Override
    public double getUserRate(Long uid) {
        BigDecimal rate = rateMapper.selectUserRate(uid);
        if (rate == null) {
            return DEFAULT_RATE;
        }
        return rate.setScale(SCALE).doubleValue();
    }

    /**
     * 对订单进行评分
     *
     * @param dto 包含操作者UID、订单号以及
     */
    @Override
    public void rate(RateDto dto) {
        Order order = orderMapper.selectOrderByOid(dto.getOid());
        OrderRole userRole = roleOfUser(order, dto.getUid());
        // 校验评价权限
        if (userRole == null) {
            throw new CustomException("用户不是当前订单参与者，无评价权限", StatusCode.FORBIDDEN);
        }
        // 校验订单状态
        if (!RateUtil.couldRate(order, dto.getUid())) {
            throw new CustomException("当前不能进行评价", StatusCode.BAD_REQUEST);
        }

        // 更新用户评分
        RateDao dao = new RateDao(opposite(order, userRole), dto.getRate());
        rateMapper.addRate(dao);

        // 更新订单的评价状态
        OrderRate rateStatus = rateStatusAfterOperation(order.getRated(), userRole);
        Order orderOps = new Order();
        orderOps.setOid(dto.getOid());
        orderOps.setRated(rateStatus);
        orderMapper.updateOrder(orderOps);
    }

    @Autowired
    public void setRateMapper(RateMapper rateMapper) {
        this.rateMapper = rateMapper;
    }

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    /**
     * 返回用户在订单中是买家或是卖家
     */
    private OrderRole roleOfUser(Order order, Long uid) {
        if (order.getBuyer().equals(uid)) {
            return OrderRole.BUYER;
        }
        if (order.getSeller().equals(uid)) {
            return OrderRole.SELLER;
        }
        return null;
    }

    /**
     * 获取对方的UID
     */
    private Long opposite(Order order, OrderRole role) {
        switch (role) {
            case BUYER:
                return order.getSeller();
            case SELLER:
                return order.getBuyer();
            default:
                return null;
        }
    }

    /**
     * 获取评价操作之后的评价状态
     */
    private OrderRate rateStatusAfterOperation(OrderRate rate, OrderRole role) {
        switch (rate) {
            case UNRATED:
                // 若双方均未评价，状态更新为某一方已评价
                return role == OrderRole.BUYER ? OrderRate.BUYER_RATED : OrderRate.SELLER_RATED;
            default:
                // 其他状态下均会更新为双方都已评价状态
                return OrderRate.RATED;
        }
    }
}
