package com.zxchen.nearby.order.domain.dto;

/**
 * 传输查找附近商品数据的DTO
 */
public class CommodityFindDto {

    // 当前搜索用户的UID（需要屏蔽用户自己在出售的商品）
    private Long uid;

    // 用户当前方位（纬度）
    private double latitude;

    // 用户当前方位（经度）
    private double longitude;

    // 搜索关键词
    private String keyword;

    // 搜索半径（单位为米）
    private Integer radius;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }
}
