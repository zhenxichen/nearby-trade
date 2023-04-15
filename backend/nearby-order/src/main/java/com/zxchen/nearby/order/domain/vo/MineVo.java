package com.zxchen.nearby.order.domain.vo;

import java.util.List;

/**
 * 将数据返回给前端“我的”页面的VO
 */
public class MineVo {

    private Long uid;

    private String username;

    private String avatar;

    private List<String> collectionImages;

    private List<String> sellImages;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getCollectionImages() {
        return collectionImages;
    }

    public void setCollectionImages(List<String> collectionImages) {
        this.collectionImages = collectionImages;
    }

    public List<String> getSellImages() {
        return sellImages;
    }

    public void setSellImages(List<String> sellImages) {
        this.sellImages = sellImages;
    }

}
