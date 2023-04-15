package com.zxchen.nearby.order.util.builder;

import com.zxchen.nearby.common.util.StringUtil;
import com.zxchen.nearby.order.constant.OrderConstants;
import com.zxchen.nearby.order.domain.dao.CommodityDao;
import com.zxchen.nearby.order.domain.dto.CommoditySellDto;
import com.zxchen.nearby.order.util.PriceUtil;

public class CommodityDaoBuilder {

    /**
     * 将商品出售的 DTO 转换为 用于访问数据库的 DAO
     *
     * @param dto 存储了卖家ID，商品信息，交易地点信息等内容{@link CommoditySellDto}
     * @return 用于访问数据库的 DAO （未填写cid字段）
     */
    public static CommodityDao build(CommoditySellDto dto) {
        CommodityDao dao = new CommodityDao();
        dao.setDescription(dto.getDescription());
        dao.setLatitude(dto.getLatitude());
        dao.setLongitude(dto.getLongitude());
        dao.setLocationTitle(dto.getLocation());
        dao.setLocationAddress(dto.getAddress());
        dao.setSellerId(dto.getSellerId());
        // 将DTO中的images列表合并为一个字符串，便于存储到数据库中
        dao.setImages(StringUtil.concat(dto.getImages(), OrderConstants.IMAGE_SEPARATOR));
        dao.setExchange(dto.getExchange());
        dao.setPrice(PriceUtil.priceStringToInteger(dto.getPrice()));
        return dao;
    }

    /**
     * 将商品出售的 DTO 转换为 用于访问数据库的 DAO
     *
     * @param dto 存储了卖家ID，商品信息，交易地点信息等内容{@link CommoditySellDto}
     * @return 用于访问数据库的 DAO （已填写cid字段）
     */
    public static CommodityDao build(Long cid, CommoditySellDto dto) {
        CommodityDao dao = build(dto);
        dao.setCid(cid);
        return dao;
    }

}
