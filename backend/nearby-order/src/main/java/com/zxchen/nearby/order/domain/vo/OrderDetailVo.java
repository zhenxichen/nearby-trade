package com.zxchen.nearby.order.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxchen.nearby.order.enums.OrderStatus;
import com.zxchen.nearby.order.enums.OrderType;

import java.util.Date;

/**
 * 向前端返回商品详情信息的 VO
 */
public class OrderDetailVo {

    // 订单号
    private String oid;

    // 订单状态
    private OrderStatus status;

    // 对方的UID
    private Long oppositeId;

    // 对方的用户名
    private String oppositeName;

    // 对方的头像
    private String oppositeAvatar;

    // 交易时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date tradeTime;

    // 交易地点名称
    private String location;

    // 交易地点地址
    private String address;

    // 交易地点纬度
    private Double latitude;

    // 交易地点经度
    private Double longitude;

    // 商品ID
    private Long cid;

    // 商品图片
    private String commodityImage;

    // 商品描述
    private String commodityDescription;

    // 交易类型
    private Boolean isExchange;

    // 交换物图片（交换单）
    private String exchangeImage;

    // 交换物描述（交换单）
    private String exchangeDescription;

    // 交易金额（购买单）
    private String price;

    // 订单创建时间
    private Date createTime;

    // 订单完成时间
    private Date finishTime;

    // 用户当前是否可以评价对方
    private Boolean rate;

    // 用户是否是卖家
    private Boolean isSeller;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getOppositeId() {
        return oppositeId;
    }

    public void setOppositeId(Long oppositeId) {
        this.oppositeId = oppositeId;
    }

    public String getOppositeName() {
        return oppositeName;
    }

    public void setOppositeName(String oppositeName) {
        this.oppositeName = oppositeName;
    }

    public String getOppositeAvatar() {
        return oppositeAvatar;
    }

    public void setOppositeAvatar(String oppositeAvatar) {
        this.oppositeAvatar = oppositeAvatar;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
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

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getCommodityImage() {
        return commodityImage;
    }

    public void setCommodityImage(String commodityImage) {
        this.commodityImage = commodityImage;
    }

    public String getCommodityDescription() {
        return commodityDescription;
    }

    public void setCommodityDescription(String commodityDescription) {
        this.commodityDescription = commodityDescription;
    }

    public Boolean getExchange() {
        return isExchange;
    }

    public void setExchange(Boolean exchange) {
        isExchange = exchange;
    }

    public String getExchangeImage() {
        return exchangeImage;
    }

    public void setExchangeImage(String exchangeImage) {
        this.exchangeImage = exchangeImage;
    }

    public String getExchangeDescription() {
        return exchangeDescription;
    }

    public void setExchangeDescription(String exchangeDescription) {
        this.exchangeDescription = exchangeDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Boolean getRate() {
        return rate;
    }

    public void setRate(Boolean rate) {
        this.rate = rate;
    }

    public Boolean getSeller() {
        return isSeller;
    }

    public void setSeller(Boolean seller) {
        isSeller = seller;
    }
}
