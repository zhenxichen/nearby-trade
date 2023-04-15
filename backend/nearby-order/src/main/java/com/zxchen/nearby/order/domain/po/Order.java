package com.zxchen.nearby.order.domain.po;

import com.zxchen.nearby.order.enums.OrderRate;
import com.zxchen.nearby.order.enums.OrderStatus;
import com.zxchen.nearby.order.enums.OrderType;

import java.util.Date;

/**
 * 订单 PO
 */
public class Order {

    // 订单号
    private Long oid;

    // 商品ID
    private Long cid;

    // 订单类型
    private OrderType type;

    // 创建时间
    private Date createTime;

    // 交易时间
    private Date tradeTime;

    // 完成时间
    private Date finishTime;

    // 订单状态
    private OrderStatus status;

    // 订单是否已评价
    private OrderRate rated;

    // 买家ID
    private Long buyer;

    // 卖家ID
    private Long seller;

    // 交易地点坐标-纬度
    private Double latitude;

    // 交易地点坐标-经度
    private Double longitude;

    // 交易地点名称
    private String locationTitle;

    // 交易地点地址
    private String locationAddress;

    // 价格（购买单）
    private Integer price;

    // 交换物描述（交换单）
    private String exchangeDescription;

    // 交换物图片（交换单）
    private String exchangeImage;

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderRate getRated() {
        return rated;
    }

    public void setRated(OrderRate rated) {
        this.rated = rated;
    }

    public Long getBuyer() {
        return buyer;
    }

    public void setBuyer(Long buyer) {
        this.buyer = buyer;
    }

    public Long getSeller() {
        return seller;
    }

    public void setSeller(Long seller) {
        this.seller = seller;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getExchangeDescription() {
        return exchangeDescription;
    }

    public void setExchangeDescription(String exchangeDescription) {
        this.exchangeDescription = exchangeDescription;
    }

    public String getExchangeImage() {
        return exchangeImage;
    }

    public void setExchangeImage(String exchangeImage) {
        this.exchangeImage = exchangeImage;
    }
}
