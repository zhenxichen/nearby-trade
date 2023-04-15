package com.zxchen.nearby.order.domain.dao;

import java.util.List;

/**
 * 通过Like形式搜索商品的 DAO
 */
public class CommodityLikeDao {

    // 当前搜索用户的UID（需要屏蔽用户自己在出售的商品）
    private Long uid;

    // 用户当前方位（纬度）
    private double latitude;

    // 用户当前方位（经度）
    private double longitude;

    // 搜索半径（单位为米）
    private Integer radius;

    // 关键词列表
    private List<String> keywords;

    public CommodityLikeDao() {
    }

    public CommodityLikeDao(Long uid, double latitude, double longitude, Integer radius, List<String> keywords) {
        this.uid = uid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.keywords = keywords;
    }

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

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

}
