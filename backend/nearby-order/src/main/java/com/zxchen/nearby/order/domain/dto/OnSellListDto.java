package com.zxchen.nearby.order.domain.dto;

/**
 * 搜索用户在售商品的DTO
 */
public class OnSellListDto {

    private Long uid;

    private double latitude;

    private double longitude;

    public OnSellListDto() {
    }

    public OnSellListDto(Long uid, double latitude, double longitude) {
        this.uid = uid;
        this.latitude = latitude;
        this.longitude = longitude;
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
}
