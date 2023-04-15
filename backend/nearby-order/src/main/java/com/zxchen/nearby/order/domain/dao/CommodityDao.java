package com.zxchen.nearby.order.domain.dao;

/**
 * 对 commodity 表进行数据读写的DAO
 */
public class CommodityDao {

    // 商品ID
    private Long cid;

    // 商品简述
    private String description;

    // 交易地点纬度
    private Double latitude;

    // 交易地点经度
    private Double longitude;

    // 交易地点坐标的Geo Hash值
    private String geoHash;

    // 交易地点名称
    private String locationTitle;

    // 交易地点地址
    private String locationAddress;

    // 是否支持交换
    private Boolean exchange;

    // 价格（单位为分）
    private int price;

    // 描述图片（多张用;分割）
    private String images;

    // 卖家ID
    private Long sellerId;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public void setLocationTitle(String locationTitle) {
        this.locationTitle = locationTitle;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public Boolean getExchange() {
        return exchange;
    }

    public void setExchange(Boolean exchange) {
        this.exchange = exchange;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
}
