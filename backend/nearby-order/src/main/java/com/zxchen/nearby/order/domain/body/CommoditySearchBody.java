package com.zxchen.nearby.order.domain.body;

public class CommoditySearchBody {

    // 用户当前位置纬度
    private double latitude;

    // 用户当前位置经度
    private double longitude;

    // 用户搜索范围半径
    private int radius;

    // 搜索关键词
    private String keywords;

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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

}
