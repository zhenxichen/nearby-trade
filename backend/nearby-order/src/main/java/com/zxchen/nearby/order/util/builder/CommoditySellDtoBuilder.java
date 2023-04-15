package com.zxchen.nearby.order.util.builder;

import com.zxchen.nearby.order.domain.dto.CommoditySellDto;
import com.zxchen.nearby.order.domain.body.CommoditySellBody;

/**
 * 用于将商品发布的VO对象转换为DTO对象
 */
public class CommoditySellDtoBuilder {

    /**
     * 将商品发布的VO对象转换为DTO对象
     *
     * @param sellerId 卖家的UID
     * @param vo 前端传来的VO
     * @return
     */
    public static CommoditySellDto build(Long sellerId, CommoditySellBody vo) {
        CommoditySellDto dto = new CommoditySellDto();
        dto.setSellerId(sellerId);
        dto.setDescription(vo.getDescription());
        dto.setImages(vo.getImages());
        dto.setExchange(vo.getExchange());
        dto.setPrice(vo.getPrice());
        dto.setLatitude(vo.getLatitude());
        dto.setLongitude(vo.getLongitude());
        dto.setLocation(vo.getLocation());
        dto.setAddress(vo.getAddress());
        return dto;
    }
}
