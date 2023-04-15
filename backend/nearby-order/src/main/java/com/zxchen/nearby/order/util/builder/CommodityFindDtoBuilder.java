package com.zxchen.nearby.order.util.builder;

import com.zxchen.nearby.order.domain.dto.CommodityFindDto;
import com.zxchen.nearby.order.domain.body.CommodityFindBody;
import com.zxchen.nearby.order.domain.body.CommoditySearchBody;

/**
 * 用于将前端传来的请求转换为 CommodityFindDto
 */
public class CommodityFindDtoBuilder {

    /**
     * 将前端传来的find请求转换为CommodityFindDto
     *
     * @param body 请求体
     * @param uid 解析请求头得到的用户UID
     * @return 封装得到的DTO
     */
    public static CommodityFindDto build(CommodityFindBody body, Long uid) {
        CommodityFindDto dto = new CommodityFindDto();
        dto.setUid(uid);
        dto.setLatitude(body.getLatitude());
        dto.setLongitude(body.getLongitude());
        dto.setRadius(body.getRadius());
        return dto;
    }

    /**
     * 将前端传来的search请求转换为CommodityFindDto
     *
     * @param body 请求体
     * @param uid 解析请求头得到的用户UID
     * @return 封装得到的DTO
     */
    public static CommodityFindDto build(CommoditySearchBody body, Long uid) {
        CommodityFindDto dto = new CommodityFindDto();
        dto.setUid(uid);
        dto.setLatitude(body.getLatitude());
        dto.setLongitude(body.getLongitude());
        dto.setRadius(body.getRadius());
        dto.setKeyword(body.getKeywords());
        return dto;
    }

}
