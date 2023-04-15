package com.zxchen.nearby.order.domain.vo;

/**
 * 返回给前端交易界面的 VO
 */
public class CommodityTradeVo {

    private Long cid;

    private String description;

    private String image;

    private Boolean exchange;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getExchange() {
        return exchange;
    }

    public void setExchange(Boolean exchange) {
        this.exchange = exchange;
    }

}
