package com.zxchen.nearby.controllers.commodity;

import com.zxchen.nearby.common.domain.web.HttpResult;
import com.zxchen.nearby.common.util.SecurityUtil;
import com.zxchen.nearby.order.domain.body.CommodityFindBody;
import com.zxchen.nearby.order.domain.body.CommoditySearchBody;
import com.zxchen.nearby.order.domain.body.CommoditySellBody;
import com.zxchen.nearby.order.domain.dto.CommodityFindDto;
import com.zxchen.nearby.order.domain.vo.*;
import com.zxchen.nearby.order.service.ICommodityService;
import com.zxchen.nearby.order.service.IOrderService;
import com.zxchen.nearby.order.util.builder.CommodityFindDtoBuilder;
import com.zxchen.nearby.order.util.builder.CommoditySellDtoBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品相关操作的 controller
 */
@RestController
@RequestMapping("/commodity")
public class CommodityController {

    private ICommodityService commodityService;

    private IOrderService orderService;

    /**
     * 用户发布商品的接口
     *
     * @param body 包含商品描述，商品图片列表，是否支持交换，预期价位，交易地点等
     * @return
     */
    @PostMapping("/sell")
    public HttpResult sell(@RequestBody CommoditySellBody body) {
        Long uid = SecurityUtil.getUserId();
        commodityService.sell(CommoditySellDtoBuilder.build(uid, body));
        return HttpResult.success();
    }

    /**
     * 用户快速查看附近在售商品的接口
     *
     * @param body 包含用户的经纬度数据以及查询的半径（默认为5km）
     * @return 按距离排序的商品列表，包含列表长度以及数据（商品ID，商品名，图片，卖家ID，卖家头像，距离）
     */
    @RequestMapping("/find")
    public HttpResult find(@RequestBody CommodityFindBody body) {
        Long uid = SecurityUtil.getUserId();
        CommodityFindDto dto = CommodityFindDtoBuilder.build(body, uid);
        List<CommodityVo> voList = commodityService.find(dto);
        return HttpResult.success(new CommodityListVo(voList));
    }

    /**
     * 用户根据关键词搜索附近商品的接口
     *
     * @param body 包含用户的经纬度数据、查询的半径（默认为5km）、搜索的关键词（多个关键词以空格分割）
     * @return 按距离排序的商品列表，包含列表长度以及数据（商品ID，商品名，图片，卖家ID，卖家头像，距离）
     */
    @RequestMapping("/search")
    public HttpResult search(@RequestBody CommoditySearchBody body) {
        Long uid = SecurityUtil.getUserId();
        CommodityFindDto dto = CommodityFindDtoBuilder.build(body, uid);
        List<CommodityVo> voList = commodityService.search(dto);
        return HttpResult.success(new CommodityListVo(voList));
    }

    /**
     * 用户查看商品详细信息的接口
     *
     * @param cid 商品ID
     * @return
     */
    @GetMapping("/detail/{cid}")
    public HttpResult detail(@PathVariable Long cid) {
        Long uid = SecurityUtil.getUserId();
        CommodityDetailVo vo = commodityService.detail(cid, uid);
        return HttpResult.success(vo);
    }

    /**
     * 用户编辑商品信息的接口
     *
     * @param body 包含商品描述，商品图片列表，是否支持交换，预期价位，交易地点等
     * @param cid 编辑的商品ID
     * @return 若编辑成功返回200，若无权限返回403
     */
    @PostMapping("/edit/{cid}")
    public HttpResult edit(@RequestBody CommoditySellBody body, @PathVariable Long cid) {
        Long uid = SecurityUtil.getUserId();
        commodityService.edit(cid, CommoditySellDtoBuilder.build(uid, body));
        return HttpResult.success();
    }

    /**
     * 用户下架商品的接口
     *
     * @param cid 下架的商品ID
     * @return 若下架成功返回200，若当前用户无操作权限返回403
     */
    @RequestMapping("/off/{cid}")
    public HttpResult offShelf(@PathVariable Long cid) {
        orderService.offShelfAndCloseRelatedOrder(SecurityUtil.getUserId(), cid);
        return HttpResult.success();
    }

    /**
     * 获取商品交易界面显示的商品基本信息
     *
     * @param cid 商品ID
     * @return 包括商品ID、描述、图片以及是否支持交换
     */
    @RequestMapping("/trade/info/{cid}")
    public HttpResult tradeInfo(@PathVariable Long cid) {
        CommodityTradeVo vo = commodityService.getTradeSimpleInfo(cid);
        return HttpResult.success(vo);
    }

    @Autowired
    public void setCommodityService(ICommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @Autowired
    public void setOrderService(IOrderService orderService) {
        this.orderService = orderService;
    }
}
