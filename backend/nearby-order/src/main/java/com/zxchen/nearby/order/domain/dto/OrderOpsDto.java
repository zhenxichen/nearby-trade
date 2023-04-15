package com.zxchen.nearby.order.domain.dto;

/**
 * 将对订单操作从 controller 层传输到 service 层的 DTO
 */
public class OrderOpsDto {

    // 操作者的UID
    private Long uid;

    // 进行操作的订单号
    private Long oid;

    public OrderOpsDto(Long uid, Long oid) {
        this.uid = uid;
        this.oid = oid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

}
