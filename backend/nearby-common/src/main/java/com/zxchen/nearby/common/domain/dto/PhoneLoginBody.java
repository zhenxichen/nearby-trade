package com.zxchen.nearby.common.domain.dto;

/**
 * 用户手机登录对象
 */
public class PhoneLoginBody {

    private String phone;   // 用户手机号

    private String password;        // 用户密码

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
