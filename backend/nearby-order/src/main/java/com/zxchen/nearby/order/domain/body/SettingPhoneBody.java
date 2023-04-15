package com.zxchen.nearby.order.domain.body;

/**
 * 用户更新手机号码的请求体
 */
public class SettingPhoneBody {

    private String phone;

    private String verificationCode;

    private String newPhone;

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
