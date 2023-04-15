package com.zxchen.nearby.order.util.builder;

import com.zxchen.nearby.common.util.StringUtil;
import com.zxchen.nearby.order.constant.OrderConstants;
import com.zxchen.nearby.order.domain.dao.CommodityDetailDao;
import com.zxchen.nearby.order.domain.vo.CommodityDetailVo;
import com.zxchen.nearby.order.util.PriceUtil;

public class CommodityDetailVoBuilder {

    /**
     * 将商品详情的DAO转换为返回给前端的VO
     *
     * @param dao 从数据库中读取出的数据DAO
     * @param uid 当前访问用户的UID
     * @return 返回给前端的VO
     */
    public static CommodityDetailVo build(CommodityDetailDao dao, Long uid) {
        CommodityDetailVo vo = new CommodityDetailVo();
        vo.setCid(dao.getCid());
        vo.setDescription(dao.getDescription());
        // 将数据库中存储的用;分割的图片划分为字符串列表
        vo.setImages(StringUtil.split(dao.getImages() ,OrderConstants.IMAGE_SEPARATOR));
        // 将价格由分为单位的int转为元为单位的字符串
        vo.setPrice(PriceUtil.priceIntegerToString(dao.getPrice()));

        vo.setExchange(dao.getExchange());
        vo.setLatitude(dao.getLatitude());
        vo.setLongitude(dao.getLongitude());
        vo.setLocation(dao.getLocation());
        vo.setAddress(dao.getAddress());
        vo.setSellerId(dao.getSellerId());
        vo.setSellerName(dao.getSellerName());
        vo.setSellerAvatar(dao.getSellerAvatar());
        vo.setTime(dao.getTime());
        vo.setOffShelf(dao.getOffShelf());

        // 判断当前访问用户是否是当前商品的卖家
        vo.setSeller(uid.equals(dao.getSellerId()));

        return vo;
    }

    /**
     * 将商品详情的DAO，以及判断用户是否收藏商品的结果，转换为返回给前端的VO
     * @param dao 从数据库中读取出的数据DAO
     * @param uid 当前访问用户的UID
     * @param collected 判断用户是否收藏商品的结果
     * @return 返回给前端的VO
     */
    public static CommodityDetailVo build(CommodityDetailDao dao, Long uid, boolean collected) {
        CommodityDetailVo vo = build(dao, uid);
        vo.setCollected(collected);
        return vo;
    }

}
