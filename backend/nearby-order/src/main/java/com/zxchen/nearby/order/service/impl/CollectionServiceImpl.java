package com.zxchen.nearby.order.service.impl;

import com.zxchen.nearby.order.constant.OrderConstants;
import com.zxchen.nearby.order.domain.dao.CollectionDao;
import com.zxchen.nearby.order.domain.dao.CommodityListDao;
import com.zxchen.nearby.order.domain.dto.CollectionListDto;
import com.zxchen.nearby.order.domain.vo.CommodityVo;
import com.zxchen.nearby.order.mapper.CollectionMapper;
import com.zxchen.nearby.order.service.ICollectionService;
import com.zxchen.nearby.order.util.builder.CommodityVoBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏 服务层实现
 */
@Service
public class CollectionServiceImpl implements ICollectionService {

    private CollectionMapper collectionMapper;

    /**
     * 判断用户是否收藏某一商品
     *
     * @param uid 用户的UID
     * @param cid 商品的CID
     * @return 若收藏，返回true，否则返回false
     */
    @Override
    public boolean checkCollected(Long uid, Long cid) {
        int res = collectionMapper.checkIsCollected(new CollectionDao(uid, cid));
        return res == OrderConstants.COLLECTED;
    }

    /**
     * 添加收藏
     *
     * @param uid 用户ID
     * @param cid 商品ID
     * @return 返回操作是否成功
     */
    @Override
    public boolean addCollection(Long uid, Long cid) {
        int res = collectionMapper.insertCollection(new CollectionDao(uid, cid));
        return res > 0;
    }

    /**
     * 取消收藏
     *
     * @param uid 用户ID
     * @param cid 商品ID
     * @return 返回操作是否成功
     */
    @Override
    public boolean cancelCollection(Long uid, Long cid) {
        int res = collectionMapper.deleteCollection(new CollectionDao(uid, cid));
        return res > 0;
    }

    /**
     * 获取收藏列表
     *
     * @param dto 包含用户UID，以及用户当前所处位置经纬度
     * @return 返回CommodityVo的列表{@link CommodityVo}
     */
    @Override
    public List<CommodityVo> collectionList(CollectionListDto dto) {
        List<CommodityListDao> daoList = collectionMapper.selectCollectionList(dto);
        List<CommodityVo> voList = new ArrayList<>();
        for (CommodityListDao dao: daoList) {
            CommodityVo vo = CommodityVoBuilder.build(dao);
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 取消所有与该商品有关的收藏
     *
     * @param cid 商品ID
     * @return 取消收藏数量
     */
    @Override
    public int cancelCommodityCollection(Long cid) {
        return collectionMapper.deleteCollectionByCid(cid);
    }

    @Autowired
    public void setCollectionMapper(CollectionMapper collectionMapper) {
        this.collectionMapper = collectionMapper;
    }
}
