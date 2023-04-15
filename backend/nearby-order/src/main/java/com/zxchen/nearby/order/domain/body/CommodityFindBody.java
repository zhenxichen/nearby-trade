package com.zxchen.nearby.order.domain.body;

/**
 * 前端传输的附近商品查询请求的VO
 */
public class CommodityFindBody {

    // 用户当前位置纬度
    private double latitude;

    // 用户当前位置经度
    private double longitude;

    // 用户搜索范围半径
    private int radius;

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
}
