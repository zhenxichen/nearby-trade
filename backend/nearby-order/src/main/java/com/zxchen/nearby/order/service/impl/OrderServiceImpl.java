package com.zxchen.nearby.order.service.impl;

import com.zxchen.nearby.common.constant.StatusCode;
import com.zxchen.nearby.common.domain.cache.UserInfoCache;
import com.zxchen.nearby.common.exception.CustomException;
import com.zxchen.nearby.common.exception.SnowFlakeException;
import com.zxchen.nearby.common.service.CacheService;
import com.zxchen.nearby.common.service.SnowFlakeService;
import com.zxchen.nearby.order.domain.dto.CommodityCache;
import com.zxchen.nearby.order.domain.dto.OrderCreateDto;
import com.zxchen.nearby.order.domain.dto.OrderListQueryDto;
import com.zxchen.nearby.order.domain.dao.OrderListDao;
import com.zxchen.nearby.order.domain.dto.OrderOpsDto;
import com.zxchen.nearby.order.domain.po.Order;
import com.zxchen.nearby.order.domain.vo.CommodityDetailVo;
import com.zxchen.nearby.order.domain.vo.OrderDetailVo;
import com.zxchen.nearby.order.domain.vo.OrderListVo;
import com.zxchen.nearby.order.enums.OrderStatus;
import com.zxchen.nearby.order.enums.OrderType;
import com.zxchen.nearby.order.mapper.CommodityMapper;
import com.zxchen.nearby.order.mapper.OrderMapper;
import com.zxchen.nearby.order.service.ICommodityService;
import com.zxchen.nearby.order.service.IOrderService;
import com.zxchen.nearby.order.util.RateUtil;
import com.zxchen.nearby.order.util.builder.OrderBuilder;
import com.zxchen.nearby.order.util.builder.OrderDetailVoBuilder;
import com.zxchen.nearby.order.util.builder.OrderListVoBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单 服务层实现
 */
@Service
public class OrderServiceImpl implements IOrderService {

    private SnowFlakeService snowFlakeService;

    private OrderMapper orderMapper;

    private ICommodityService commodityService;

    private CacheService cacheService;

    /**
     * 获取用户订单列表
     *
     * @param dto 包含用户UID，以及筛选条件（包括用户角色和订单状态）
     * @return 订单列表
     */
    @Override
    public List<OrderListVo> getOrderList(OrderListQueryDto dto) {
        List<OrderListDao> daoList = orderMapper.selectOrderList(dto);
        Long uid = dto.getUid();
        List<OrderListVo> voList = new ArrayList<>();
        daoList.forEach(dao -> {
            OrderListVo vo = OrderListVoBuilder.build(dao, uid);
            voList.add(vo);
        });
        return voList;
    }

    /**
     * 建立订单
     *
     * @param dto 包括交易的商品，交易时间、地点，交易形式，交易价格或交换物信息
     */
    @Override
    public void createOrder(OrderCreateDto dto) {
        checkCommodityOnSell(dto.getCid());
        checkCommodityTypeAvailable(dto.getType(), dto.getCid());
        Long oid;
        try {
            oid = snowFlakeService.nextId();
        } catch (SnowFlakeException e) {
            throw new CustomException("订单号生成失败", StatusCode.INTERNAL_ERROR);
        }
        CommodityCache commodityInfo = commodityService.getCommodityCacheInfo(dto.getCid());
        Order order = OrderBuilder.build(oid, dto, commodityInfo.getSellerId());
        orderMapper.insertOrder(order);
    }

    /**
     * 取消订单
     *
     * @param dto 包括操作者的UID以及订单号
     */
    @Override
    @Transactional
    public void cancelOrder(OrderOpsDto dto) {
        Order order = orderMapper.selectOrderByOid(dto.getOid());
        // 校验订单号是否存在
        if (order == null) {
            throw new CustomException("未找到该订单号", StatusCode.NOT_FOUND);
        }
        // 校验用户权限
        if (!isBuyerOrSeller(order, dto.getUid())) {
            throw new CustomException("用户不是订单的参与者，无权进行操作", StatusCode.FORBIDDEN);
        }
        // 校验订单状态
        if (!orderStatusCouldBeCancel(order)) {
            throw new CustomException("当前订单状态无法被取消", StatusCode.BAD_REQUEST);
        }
        // 对于正在进行中的订单
        if (order.getStatus() == OrderStatus.IN_PROGRESS) {
            // 取消后应该将状态重新调整为上架
            commodityService.putCommodityOnSell(order.getCid());
        }
        // 新建对象用于对订单表进行操作
        Order orderOps = new Order();
        orderOps.setOid(dto.getOid());
        orderOps.setStatus(OrderStatus.CLOSED);
        orderMapper.updateOrder(orderOps);
    }

    /**
     * 卖家接受订单
     *
     * @param dto 包括操作者的UID和订单号
     */
    @Override
    @Transactional
    public void acceptOrder(OrderOpsDto dto) {
        Order order = orderMapper.selectOrderByOid(dto.getOid());
        // 若订单号不存在
        if (order == null) {
            throw new CustomException("未找到该订单号", StatusCode.NOT_FOUND);
        }
        // 若操作者不是商品的卖家
        if (!order.getSeller().equals(dto.getUid())) {
            throw new CustomException("用户不是商品卖家，无权进行操作", StatusCode.FORBIDDEN);
        }
        if (order.getStatus() != OrderStatus.WAIT_ACCEPT) {
            throw new CustomException("订单未处于待接单状态，无法接单", StatusCode.BAD_REQUEST);
        }
        checkCommodityOnSell(order.getCid());

        // 新建对象用于对数据库进行操作
        Order orderOps = new Order();

        // 修改当前订单的订单状态
        orderOps.setOid(dto.getOid());
        orderOps.setStatus(OrderStatus.IN_PROGRESS);
        orderMapper.updateOrder(orderOps);
        // 将当前商品相关的其他未接受订单设为已关闭
        orderOps.setCid(order.getCid());
        orderMapper.updateOtherOrderToBeClosed(orderOps);

        // 将商品下架
        commodityService.putCommodityOffShelf(order.getCid());
    }

    /**
     * 卖家下架商品（同时关闭所有有关订单）
     *
     * @param uid 操作者UID
     * @param cid 商品ID
     */
    @Override
    @Transactional
    public void offShelfAndCloseRelatedOrder(Long uid, Long cid) {
        CommodityCache cache = commodityService.getCommodityCacheInfo(cid);
        if (!cache.getSellerId().equals(uid)) {
            throw new CustomException("用户不是商品卖家，无操作权限", StatusCode.FORBIDDEN);
        }
        // 调整商品表中的商品状态
        commodityService.putCommodityOffShelf(cid);
        // 关闭关联订单
        orderMapper.updateRelatedOrderToBeClosed(cid);
    }

    /**
     * 买家或卖家确认订单结单
     *
     * @param dto 包括操作者的UID和订单号
     */
    @Override
    public void confirmOrder(OrderOpsDto dto) {
        Order order = orderMapper.selectOrderByOid(dto.getOid());
        // 校验订单号是否存在
        if (order == null) {
            throw new CustomException("未找到该订单号", StatusCode.NOT_FOUND);
        }
        // 校验用户权限
        if (!isBuyerOrSeller(order, dto.getUid())) {
            throw new CustomException("用户不是订单的参与者，无权进行操作", StatusCode.FORBIDDEN);
        }
        // 校验订单状态
        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new CustomException("订单未处于进行中状态，无法结单", StatusCode.BAD_REQUEST);
        }

        Order orderOps = new Order();
        orderOps.setOid(dto.getOid());
        orderOps.setStatus(OrderStatus.FINISHED);
        orderOps.setFinishTime(new Date());
        orderMapper.updateOrder(orderOps);
    }

    /**
     * 查看订单详情
     *
     * @param dto 包括操作者的UID和订单号
     * @return 订单的详细数据
     */
    @Override
    public OrderDetailVo orderDetail(OrderOpsDto dto) {
        Order order = orderMapper.selectOrderByOid(dto.getOid());
        // 校验订单号是否存在
        if (order == null) {
            throw new CustomException("未找到该订单号", StatusCode.NOT_FOUND);
        }
        // 校验用户权限
        if (!isBuyerOrSeller(order, dto.getUid())) {
            throw new CustomException("用户不是订单的参与者，无权查看订单详情", StatusCode.FORBIDDEN);
        }

        OrderDetailVo vo = OrderDetailVoBuilder.buildWithOrderInfo(order);

        CommodityCache commodityCache = commodityService.getCommodityCacheInfo(order.getCid());
        OrderDetailVoBuilder.writeCommodityInfo(vo, commodityCache);

        // 填写对方的信息
        Long oppositeId = order.getBuyer().equals(dto.getUid()) ? order.getSeller() : order.getBuyer();
        UserInfoCache userInfoCache = cacheService.getUserInfoFromCache(oppositeId);
        OrderDetailVoBuilder.writeOppositeUserInfo(vo, userInfoCache);

        // 判断用户是否是卖家
        vo.setSeller(dto.getUid().equals(order.getSeller()));

        // 判断用户是否可以评价
        vo.setRate(RateUtil.couldRate(order, dto.getUid()));

        return vo;
    }

    @Autowired
    public void setSnowFlakeService(SnowFlakeService snowFlakeService) {
        this.snowFlakeService = snowFlakeService;
    }

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public void setCommodityService(ICommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @Autowired
    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * 判断用户是否是订单的买家或卖家中的一方
     *
     * @param order 订单对象
     * @param uid 用户的UID
     * @return 用户是否是订单的买家或卖家中的一方
     */
    private boolean isBuyerOrSeller(Order order, Long uid) {
        if (order == null) {
            return false;
        }
        return uid.equals(order.getBuyer()) || uid.equals(order.getSeller());
    }

    /**
     * 判断订单当前所处的状态是否可被取消（仅待接单、交易中的订单可被取消）
     *
     * @param order 订单对象
     * @return 当前订单是否可被取消
     */
    private boolean orderStatusCouldBeCancel(Order order) {
        return order.getStatus() == OrderStatus.WAIT_ACCEPT || order.getStatus() == OrderStatus.IN_PROGRESS;
    }

    /**
     * 校验当前商品是否仍处于在售状态，若已下架则抛出错误
     *
     * @param cid 当前商品的cid
     * @throws CustomException 若当前商品已下架，则抛出错误码为 400 的 CustomException
     */
    private void checkCommodityOnSell(Long cid) throws CustomException {
        if (!commodityService.isCommodityOnSell(cid)) {
            throw new CustomException("操作失败，当前商品已下架", StatusCode.BAD_REQUEST);
        }
    }

    /**
     * 校验当前订单的交易形式是否符合商品设置
     *
     * @param type 交易形式（购买或交换）
     * @throws CustomException 若订单类型为交换，而当前商品不支持交换，则抛出错误码为 400 的 CustomException
     */
    private void checkCommodityTypeAvailable(OrderType type, Long cid) throws CustomException {
        if (type == OrderType.EXCHANGE && !commodityService.commodityCouldExchange(cid)) {
            throw new CustomException("操作失败，当前商品不支持交换", StatusCode.BAD_REQUEST);
        }
    }
}
