package com.zxchen.nearby.controllers.order;

import com.zxchen.nearby.common.domain.web.HttpResult;
import com.zxchen.nearby.common.util.SecurityUtil;
import com.zxchen.nearby.order.domain.body.OrderCreateBody;
import com.zxchen.nearby.order.domain.body.OrderListBody;
import com.zxchen.nearby.order.domain.body.OrderRateBody;
import com.zxchen.nearby.order.domain.dto.OrderCreateDto;
import com.zxchen.nearby.order.domain.dto.OrderListQueryDto;
import com.zxchen.nearby.order.domain.dto.OrderOpsDto;
import com.zxchen.nearby.order.domain.dto.RateDto;
import com.zxchen.nearby.order.domain.po.Order;
import com.zxchen.nearby.order.domain.vo.OrderDetailVo;
import com.zxchen.nearby.order.domain.vo.OrderListVo;
import com.zxchen.nearby.order.service.IOrderService;
import com.zxchen.nearby.order.service.IRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单相关接口
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private IOrderService orderService;

    private IRateService rateService;

    /**
     * 获取订单列表
     *
     * @param body 筛选条件（用户角色和订单状态）
     * @return 订单列表
     */
    @PostMapping("/list")
    public HttpResult orderList(@RequestBody OrderListBody body) {
        Long uid = SecurityUtil.getUserId();
        List<OrderListVo> orderList =
                orderService.getOrderList(new OrderListQueryDto(uid, body.getRole(), body.getStatus()));
        return HttpResult.success(orderList);
    }

    /**
     * 建立订单
     *
     * @param body 包括交易的商品，交易时间、地点，交易形式，交易价格或交换物信息
     * @return 若成功返回200，若商品已下架返回400
     */
    @PostMapping("/create")
    public HttpResult createOrder(@RequestBody OrderCreateBody body) {
        Long uid = SecurityUtil.getUserId();
        orderService.createOrder(new OrderCreateDto(body, uid));
        return HttpResult.success();
    }

    /**
     * 取消订单
     *
     * @param oid 待取消订单的订单号
     * @return 若成功返回200，若订单状态已不可取消返回400，若操作者无权操作（非交易双方）返回403，若订单号不存在返回404
     */
    @RequestMapping("/cancel/{oid}")
    public HttpResult cancelOrder(@PathVariable Long oid) {
        orderService.cancelOrder(new OrderOpsDto(SecurityUtil.getUserId(), oid));
        return HttpResult.success();
    }

    /**
     * 卖家接受订单
     *
     * @param oid 待接受订单的订单号
     * @return 若操作成功返回200，若商品已下架或订单状态错误返回400，若操作者无权操作（非卖家）返回403，若订单号不存在返回404
     */
    @RequestMapping("/accept/{oid}")
    public HttpResult acceptOrder(@PathVariable Long oid) {
        orderService.acceptOrder(new OrderOpsDto(SecurityUtil.getUserId(), oid));
        return HttpResult.success();
    }

    /**
     * 买家或卖家确认结单
     *
     * @param oid 订单号
     * @return 若操作成功返回200，若订单状态错误返回400，若用户无权操作（非交易双方）返回403，若订单号不存在返回404
     */
    @RequestMapping("/confirm/{oid}")
    public HttpResult confirmOrder(@PathVariable Long oid) {
        orderService.confirmOrder(new OrderOpsDto(SecurityUtil.getUserId(), oid));
        return HttpResult.success();
    }

    /**
     * 查看订单详情
     *
     * @param oid 订单号
     * @return 若操作成功返回200和订单详情，若用户无权查看（非交易双方）返回403，若订单号不存在返回404
     */
    @GetMapping("/detail/{oid}")
    public HttpResult detail(@PathVariable Long oid) {
        OrderDetailVo vo = orderService.orderDetail(new OrderOpsDto(SecurityUtil.getUserId(), oid));
        return HttpResult.success(vo);
    }

    /**
     * 对订单对方进行评价
     *
     * @param body 包括订单号和评分
     * @return 若操作成功返回200，若订单状态错误或已评价返回400，若用户无权评价（非交易双方）返回403
     */
    @PostMapping("/rate")
    public HttpResult rate(@RequestBody OrderRateBody body) {
        rateService.rate(new RateDto(SecurityUtil.getUserId(), body));
        return HttpResult.success();
    }

    @Autowired
    public void setOrderService(IOrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setRateService(IRateService rateService) {
        this.rateService = rateService;
    }
}
