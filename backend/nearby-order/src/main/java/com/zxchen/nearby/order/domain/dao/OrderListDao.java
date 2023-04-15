package com.zxchen.nearby.order.domain.dao;

import com.zxchen.nearby.order.enums.OrderRate;
import com.zxchen.nearby.order.enums.OrderStatus;
import com.zxchen.nearby.order.enums.OrderType;

/**
 * 从数据库读取订单列表的 DAO
 */
public class OrderListDao {

    // 订单号
    private Long oid;

    // 买方
    private Long buyer;

    // 卖方
    private Long seller;

    // 订单状态
    private OrderStatus status;

    // 商品图片
    private String image;

    // 商品描述
    private String description;

    // 订单类型
    private OrderType type;

    // 订单金额（若为购买单）
    private Integer price;

    // 是否已评价
    private OrderRate rated;

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
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

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public OrderRate getRated() {
        return rated;
    }

    public void setRated(OrderRate rated) {
        this.rated = rated;
    }
}
