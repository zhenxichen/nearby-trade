package com.zxchen.nearby.order.service.impl;

import com.zxchen.nearby.common.domain.cache.UserInfoCache;
import com.zxchen.nearby.common.service.CacheService;
import com.zxchen.nearby.order.domain.vo.MineVo;
import com.zxchen.nearby.order.domain.vo.UserInfoVo;
import com.zxchen.nearby.order.mapper.CollectionMapper;
import com.zxchen.nearby.order.mapper.CommodityMapper;
import com.zxchen.nearby.order.service.IRateService;
import com.zxchen.nearby.order.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户信息相关 服务层实现
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService {

    private CacheService cacheService;

    private IRateService rateService;

    private CollectionMapper collectionMapper;

    private CommodityMapper commodityMapper;

    /**
     * 获取“我的”页面中所需的所有信息
     *
     * @return 返回给前端的数据
     */
    @Override
    public MineVo mine(Long uid) {
        MineVo vo = new MineVo();
        UserInfoCache userInfo = cacheService.getUserInfoFromCache(uid);
        List<String> collectionImages = collectionMapper.selectCollectionImages(uid);
        List<String> onSellImages = commodityMapper.selectSellImages(uid);
        vo.setUid(userInfo.getUid());
        vo.setUsername(userInfo.getUsername());
        vo.setAvatar(userInfo.getAvatar());
        vo.setCollectionImages(collectionImages);
        vo.setSellImages(onSellImages);
        return vo;
    }

    /**
     * 获取用户信息界面的数据，包括用户基本信息、用户评分以及用户在售的商品信息，并判断是否是用户自己
     *
     * @param uid     要查看的用户UID
     * @param fromUid 发起请求的用户UID
     * @return 包括用户基本信息、用户评分以及用户在售的商品信息，以及是否是用户自己的页面
     */
    @Override
    public UserInfoVo getUserInfoPageData(Long uid, Long fromUid) {
        UserInfoVo vo = new UserInfoVo();

        // 获取用户基本信息
        UserInfoCache simpleInfo = cacheService.getUserInfoFromCache(uid);
        vo.setUid(simpleInfo.getUid());
        vo.setUsername(simpleInfo.getUsername());
        vo.setAvatar(simpleInfo.getAvatar());

        // 判断是不是用户本人
        vo.setMyself(uid.equals(fromUid));

        // 获取用户评分
        double rate = rateService.getUserRate(uid);
        vo.setRate(rate);

        return vo;
    }

    @Autowired
    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Autowired
    public void setRateService(IRateService rateService) {
        this.rateService = rateService;
    }

    @Autowired
    public void setCollectionMapper(CollectionMapper collectionMapper) {
        this.collectionMapper = collectionMapper;
    }

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }
}
