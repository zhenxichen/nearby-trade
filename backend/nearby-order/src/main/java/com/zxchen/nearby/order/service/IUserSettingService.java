package com.zxchen.nearby.order.service;

import com.zxchen.nearby.order.domain.dto.SettingPasswordDto;
import com.zxchen.nearby.order.domain.dto.SettingPhoneDto;
import com.zxchen.nearby.order.domain.vo.SettingInfoVo;

/**
 * 用户设置 服务层
 */
public interface IUserSettingService {

    /**
     * 获取用户设置界面展示的基本信息
     *
     * @param uid 用户的UID
     * @return 返回用户的用户名、头像、部分加密手机号、是否绑定微信
     */
    SettingInfoVo getUserSettingInfo(Long uid);

    /**
     * 更新用户头像地址
     *
     * @param avatarPath
     * @return 是否更新成功
     */
    boolean updateAvatar(Long uid, String avatarPath);

    /**
     * 更新用户的用户名
     *
     * @param uid 用户的UID
     * @param username 用户新的用户名
     * @return 是否更新成功
     */
    boolean updateUsername(Long uid, String username);

    /**
     * 修改用户的密码
     *
     * @param dto 包括用户的UID，旧的密码以及新的密码
     * @return 是否更新成功
     */
    boolean updatePassword(SettingPasswordDto dto);

    /**
     * 将用户与微信账号绑定
     *
     * @param uid 用户的UID
     * @param openId 用户的微信OpenID
     * @return 是否成功绑定
     */
    boolean bindWechat(Long uid, String openId);

    /**
     * 将用户账号与微信OpenID解除绑定
     *
     * @param uid 用户的UID
     * @return 是否成功解除绑定
     */
    boolean unbindWechat(Long uid);

    /**
     * 用户更新手机号前，向用户发送短信验证码
     *
     * @param uid 用户UID
     * @param phone 用户填写的手机号
     */
    void sendVerificationCodeForUpdatePhone(Long uid, String phone);

    /**
     * 更新用户的手机号
     *
     * @param dto 包括用户UID、用户填写的手机号、用户填写的验证码、用户设置的新手机号
     * @return 是否成功更新
     */
    boolean updatePhone(SettingPhoneDto dto);

}
