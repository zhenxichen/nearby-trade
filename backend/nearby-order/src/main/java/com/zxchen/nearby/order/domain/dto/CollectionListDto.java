package com.zxchen.nearby.order.domain.dto;

/**
 * 将获取收藏列表请求的数据从 controller 传递到 service 的 DTO
 */
public class CollectionListDto {

    private Long uid;

    private double latitude;

    private double longitude;

    public CollectionListDto() {
    }

    public CollectionListDto(Long uid, double latitude, double longitude) {
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
