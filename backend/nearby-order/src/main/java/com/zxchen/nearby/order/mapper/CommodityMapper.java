package com.zxchen.nearby.order.mapper;

import com.zxchen.nearby.order.domain.dao.CommodityDao;
import com.zxchen.nearby.order.domain.dao.CommodityDetailDao;
import com.zxchen.nearby.order.domain.dao.CommodityLikeDao;
import com.zxchen.nearby.order.domain.dao.CommodityListDao;
import com.zxchen.nearby.order.domain.dto.CommodityCache;
import com.zxchen.nearby.order.domain.dto.CommodityFindDto;
import com.zxchen.nearby.order.domain.dto.OnSellListDto;
import com.zxchen.nearby.order.domain.vo.CommodityTradeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommodityMapper {

    /**
     * 将商品信息插入到商品表中，并在对象中填入自动生成的 cid
     *
     * @param dao 包含商品信息和地理位置信息
     * @return 操作影响的行数
     */
    int insertCommodity(CommodityDao dao);

    /**
     * 从库中读取需要缓存到Redis当中的数据
     *
     * @param cid 当前商品的ID
     * @return {@link CommodityCache}
     */
    CommodityCache selectCommodityCache(Long cid);

    /**
     * 通过 Like 形式从库中读取商品列表
     */
    List<CommodityListDao> selectCommodityListByLike(CommodityLikeDao dao);

    /**
     * 通过 Match 形式从库中读取商品列表
     */
    List<CommodityListDao> selectCommodityListByMatch(CommodityFindDto dto);

    /**
     * 获取商品的详细信息
     */
    CommodityDetailDao selectCommodityDetail(Long cid);

    /**
     * 获取在售的商品图片（最多三张）
     */
    List<String> selectSellImages(Long uid);

    /**
     * 获取用户在售的商品列表
     *
     * @param dto 包含目标用户的UID 以及当前搜索者所处地理位置信息
     * @return {@link CommodityListDao}
     */
    List<CommodityListDao> selectOnSellList(OnSellListDto dto);

    /**
     * 在数据库中将商品状态调整为在售
     *
     * @param cid 商品ID
     * @return update操作影响的行数
     */
    int updateCommodityStatusToOnSell(Long cid);

    /**
     * 在数据库中将商品状态调整为下架
     *
     * @param cid 商品ID
     * @return update操作影响的行数
     */
    int updateCommodityStatusToOffShelf(Long cid);

    /**
     * 校验商品是否处于上架状态，即数据表中是否存在与商品ID符合的上架中商品
     *
     * @param cid 商品ID
     * @return 若商品上架中，返回1，否则返回0
     */
    int checkCommodityIsOnSell(Long cid);

    /**
     * 校验商品是否支持交换
     *
     * @param cid 商品ID
     * @return 若商品支持交换，返回1，否则返回0
     */
    int checkCommodityCouldExchange(Long cid);

    /**
     * 更新商品信息
     *
     * @param dao 包含商品信息和地理位置信息
     * @return 操作影响的行数
     */
    int updateCommodity(CommodityDao dao);

    /**
     * 从数据库中读取用于在交易页面显示的商品简要信息
     *
     * @param cid 商品ID
     * @return 商品的ID、描述、图片，以及是否支持交换
     */
    CommodityTradeVo selectTradePageInfo(Long cid);

}
