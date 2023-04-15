package com.zxchen.nearby.order.domain.dto;

import java.io.Serializable;

public class CommodityCache implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cid;

    private String image;

    private String title;

    private Long sellerId;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

}
