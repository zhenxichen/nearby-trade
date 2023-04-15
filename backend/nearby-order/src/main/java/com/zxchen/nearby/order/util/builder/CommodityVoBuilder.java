package com.zxchen.nearby.order.util.builder;

import com.zxchen.nearby.common.domain.cache.UserInfoCache;
import com.zxchen.nearby.order.domain.dao.CommodityListDao;
import com.zxchen.nearby.order.domain.dto.CommodityCache;
import com.zxchen.nearby.order.domain.vo.CommodityVo;
import com.zxchen.nearby.order.util.DistanceUtil;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;

/**
 * 生成用于前端展示商品数据的 CommodityVo {@link CommodityVo}
 */
public class CommodityVoBuilder {

    /**
     * 将缓存信息封装为VO以便返回给前端
     *
     * @param cache Redis以 "commodity:cid"键缓存的数据
     * @param user Redis以 "user_info:uid"键缓存的数据
     * @param distance Redis GeoRadius 返回的距离对象
     * @return 生成的VO
     */
    public static CommodityVo build(CommodityCache cache, UserInfoCache user, Distance distance) {
        CommodityVo vo = new CommodityVo();
        vo.setCid(cache.getCid().toString());
        vo.setImage(cache.getImage());
        vo.setTitle(cache.getTitle());
        vo.setSellerName(user.getUsername());
        vo.setSellerAvatar(user.getAvatar());
        vo.setDistance(DistanceUtil.convertMeterDistanceToString(distance));
        return vo;
    }

    /**
     * 将从数据库中读取的 CommodityListDao 转换为发送给前端的 CommodityVo
     *
     * @param dao {@link CommodityListDao}
     * @return 生成的VO
     */
    public static CommodityVo build(CommodityListDao dao) {
        CommodityVo vo = new CommodityVo();
        vo.setCid(dao.getCid());
        vo.setImage(dao.getImage());
        vo.setTitle(dao.getTitle());
        vo.setSellerName(dao.getSellerName());
        vo.setSellerAvatar(dao.getSellerAvatar());
        vo.setDistance(DistanceUtil.convertMeterDistanceToString(dao.getDistance()));
        return vo;
    }



}
