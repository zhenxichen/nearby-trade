package com.zxchen.nearby.order.mapper;

import com.zxchen.nearby.order.domain.dao.CollectionDao;
import com.zxchen.nearby.order.domain.dao.CommodityListDao;
import com.zxchen.nearby.order.domain.dto.CollectionListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CollectionMapper {

    /**
     * 判断用户是否收藏了某商品
     *
     * @param dao 包含用户UID 以及商品的CID
     * @return 若已收藏返回1，若未收藏返回0
     */
    int checkIsCollected(CollectionDao dao);

    /**
     * 将收藏记录插入到数据表中
     *
     * @param dao 包含用户UID 以及商品的CID
     * @return 返回影响的行数
     */
    int insertCollection(CollectionDao dao);

    /**
     * 从数据库中删除收藏记录
     *
     * @param dao 包含用户UID 以及商品的CID
     * @return 返回影响的行数
     */
    int deleteCollection(CollectionDao dao);

    /**
     * 从数据库中查询用户的收藏列表
     *
     * @param dto 包含用户UID 以及用户所处位置的经纬度坐标
     * @return 返回商品列表数据 {@link CommodityListDao}
     */
    List<CommodityListDao> selectCollectionList(CollectionListDto dto);

    /**
     * 从数据库中读取用户收藏商品的图片（最多三张）
     *
     * @param uid 用户的UID
     * @return 图片链接的List
     */
    List<String> selectCollectionImages(Long uid);

    /**
     * 从数据库中删除关于该商品的所有收藏记录
     *
     * @param cid 商品的CID
     * @return 返回影响的行数
     */
    int deleteCollectionByCid(Long cid);

}
