package com.zxchen.nearby.order.domain.vo;

import com.zxchen.nearby.order.enums.OrderStatus;

/**
 * 向前端返回订单列表的 VO
 */
public class OrderListVo {

    // 订单号（需要以字符串形式返回，否则前端无法正确接收）
    private String oid;

    // 用户是否为卖方
    private Boolean isSeller;

    // 对方的UID
    private Long oppositeId;

    // 订单状态
    private OrderStatus status;

    // 商品图片
    private String image;

    // 商品描述
    private String description;

    // 订单是否为交换单
    private Boolean exchange;

    // 订单金额（若为购买单）
    private String price;

    // 当前是否可评价对方
    private Boolean rate;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Boolean getSeller() {
        return isSeller;
    }

    public void setSeller(Boolean seller) {
        isSeller = seller;
    }

    public Long getOppositeId() {
        return oppositeId;
    }

    public void setOppositeId(Long oppositeId) {
        this.oppositeId = oppositeId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean getRate() {
        return rate;
    }

    public void setRate(Boolean rate) {
        this.rate = rate;
    }
}
