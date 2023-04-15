package com.zxchen.nearby.order.service;

import com.zxchen.nearby.order.domain.dto.CollectionListDto;
import com.zxchen.nearby.order.domain.vo.CommodityVo;

import java.util.List;

/**
 * 收藏 服务层
 */
public interface ICollectionService {

    /**
     * 判断用户是否收藏某一商品
     *
     * @param uid 用户的UID
     * @param cid 商品的CID
     * @return 若收藏，返回true，否则返回false
     */
    boolean checkCollected(Long uid, Long cid);

    /**
     * 添加收藏
     *
     * @param uid 用户ID
     * @param cid 商品ID
     * @return 返回操作是否成功
     */
    boolean addCollection(Long uid, Long cid);

    /**
     * 取消收藏
     *
     * @param uid 用户ID
     * @param cid 商品ID
     * @return 返回操作是否成功
     */
    boolean cancelCollection(Long uid, Long cid);

    /**
     * 获取收藏列表
     *
     * @param dto 包含用户UID，以及用户当前所处位置经纬度
     * @return 返回CommodityVo的列表{@link CommodityVo}
     */
    List<CommodityVo> collectionList(CollectionListDto dto);

    /**
     * 取消所有与该商品有关的收藏
     *
     * @param cid 商品ID
     * @return 取消收藏数量
     */
    int cancelCommodityCollection(Long cid);

}
