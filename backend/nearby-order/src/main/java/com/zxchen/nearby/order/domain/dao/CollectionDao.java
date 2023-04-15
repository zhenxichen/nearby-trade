package com.zxchen.nearby.order.domain.dao;

/**
 * 访问收藏商品数据库的DAO
 */
public class CollectionDao {

    private Long uid;

    private Long cid;

    public CollectionDao() {
    }

    public CollectionDao(Long uid, Long cid) {
        this.uid = uid;
        this.cid = cid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }
}
