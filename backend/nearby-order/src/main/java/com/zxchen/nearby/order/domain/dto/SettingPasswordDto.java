package com.zxchen.nearby.order.domain.dto;

/**
 * 将用户修改密码的请求从 controller 层 传输到 service 层的DTO
 */
public class SettingPasswordDto {

    private Long uid;

    private String oldPassword;

    private String newPassword;

    public SettingPasswordDto(Long uid, String oldPassword, String newPassword) {
        this.uid = uid;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
