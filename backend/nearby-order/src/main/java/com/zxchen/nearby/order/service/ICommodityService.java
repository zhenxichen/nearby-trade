package com.zxchen.nearby.order.service;

import com.zxchen.nearby.order.domain.dto.CommodityCache;
import com.zxchen.nearby.order.domain.dto.CommodityFindDto;
import com.zxchen.nearby.order.domain.dto.CommoditySellDto;
import com.zxchen.nearby.order.domain.dto.OnSellListDto;
import com.zxchen.nearby.order.domain.vo.CommodityDetailVo;
import com.zxchen.nearby.order.domain.vo.CommodityTradeVo;
import com.zxchen.nearby.order.domain.vo.CommodityVo;

import java.util.List;

/**
 * 商品 服务层
 */
public interface ICommodityService {

    /**
     * 发布商品
     *
     * @param dto 存储了卖家ID，商品信息，交易地点信息等内容{@link CommoditySellDto}
     * @return 发布操作是否成功
     */
    boolean sell(CommoditySellDto dto);

    /**
     * 查看附近正在出售的商品
     *
     * @param dto 包括用户ID以及用户所处位置
     * @return
     */
    List<CommodityVo> find(CommodityFindDto dto);

    /**
     * 通过关键词搜索附近正在出售的商品
     *
     * @param dto 包括用户ID以及用户所处位置，以及用户搜索的关键词
     * @return
     */
    List<CommodityVo> search(CommodityFindDto dto);

    /**
     * 获取商品的详细信息
     *
     * @param cid 商品ID
     * @param uid 提交请求的用户ID
     * @return
     */
    CommodityDetailVo detail(Long cid, Long uid);

    /**
     * 查看正在出售的商品列表
     *
     * @param dto 包含要查看的用户UID，以及查看者当前的位置信息
     * @return
     */
    List<CommodityVo> onSellList(OnSellListDto dto);

    /**
     * 获取缓存的商品信息
     *
     * @param cid 商品ID
     * @return 商品信息
     */
    CommodityCache getCommodityCacheInfo(Long cid);

    /**
     * 将商品状态设为上架
     *
     * @param cid 商品ID
     */
    void putCommodityOnSell(Long cid);

    /**
     * 将商品状态设为下架（系统调用）
     * 
     * @param cid 商品ID
     */
    void putCommodityOffShelf(Long cid);

    /**
     * 校验商品是否处于上架状态
     * 
     * @param cid 商品ID
     * @return 商品是否处于上架状态
     */
    boolean isCommodityOnSell(Long cid);

    /**
     * 校验商品是否可以进行交换
     *
     * @param cid 商品ID
     * @return 商品是否可以进行交换
     */
    boolean commodityCouldExchange(Long cid);

    /**
     * 编辑商品信息
     *
     * @param cid 商品ID
     * @param dto 存储了卖家ID，商品信息，交易地点信息等内容{@link CommoditySellDto}
     * @return 编辑操作是否成功
     */
    boolean edit(Long cid, CommoditySellDto dto);

    /**
     * 获取下单界面显示的商品简略信息
     *
     * @param cid 商品ID
     * @return 商品的ID、描述、图片，以及是否支持交换
     */
    CommodityTradeVo getTradeSimpleInfo(Long cid);

}
