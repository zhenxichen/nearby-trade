package com.zxchen.nearby.order.domain.dto;

import com.zxchen.nearby.order.domain.body.SettingPhoneBody;

/**
 * 将用户更新手机号请求从 controller 层传输到 service 层的 DTO
 */
public class SettingPhoneDto {

    private Long uid;

    private String phone;

    private String verificationCode;

    private String newPhone;

    public SettingPhoneDto() {
    }

    public SettingPhoneDto(Long uid, String phone, String verificationCode, String newPhone) {
        this.uid = uid;
        this.phone = phone;
        this.verificationCode = verificationCode;
        this.newPhone = newPhone;
    }

    public SettingPhoneDto(Long uid, SettingPhoneBody body) {
        this(uid, body.getPhone(), body.getVerificationCode(), body.getNewPhone());
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getNewPhone() {
        return newPhone;
    }

    public void setNewPhone(String newPhone) {
        this.newPhone = newPhone;
    }
}
