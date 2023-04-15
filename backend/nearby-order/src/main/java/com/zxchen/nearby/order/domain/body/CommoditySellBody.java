package com.zxchen.nearby.order.domain.body;

import java.util.List;

/**
 * 用于从发布商品界面接收数据的 VO
 */
public class CommoditySellBody {

    // 商品描述（不多于255个字）
    private String description;

    // 用户上传的图片列表
    private List<String> images;

    // 是否支持交换
    private Boolean exchange;

    // 预期价格（以字符串形式传输，避免精度损失）
    private String price;

    // 交易地点纬度
    private double latitude;

    // 交易地点经度
    private double longitude;

    // 交易地点名称
    private String location;

    // 交易地点地址
    private String address;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Boolean getExchange() {
        return exchange;
    }

    public void setExchange(Boolean exchange) {
        this.exchange = exchange;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
