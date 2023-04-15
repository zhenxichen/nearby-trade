package com.zxchen.nearby.order.service.impl;

import com.zxchen.nearby.common.constant.StatusCode;
import com.zxchen.nearby.common.domain.SysUser;
import com.zxchen.nearby.common.exception.CustomException;
import com.zxchen.nearby.common.exception.PhoneNumberBoundException;
import com.zxchen.nearby.common.service.CacheService;
import com.zxchen.nearby.common.service.ISysUserService;
import com.zxchen.nearby.common.service.SmsService;
import com.zxchen.nearby.common.util.CheckUtil;
import com.zxchen.nearby.common.util.SecurityUtil;
import com.zxchen.nearby.common.util.StringUtil;
import com.zxchen.nearby.order.domain.dto.SettingPasswordDto;
import com.zxchen.nearby.order.domain.dto.SettingPhoneDto;
import com.zxchen.nearby.order.domain.vo.SettingInfoVo;
import com.zxchen.nearby.order.exception.OldPasswordNotMatchException;
import com.zxchen.nearby.order.exception.OpenIdBoundException;
import com.zxchen.nearby.order.service.IUserSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSettingServiceImpl implements IUserSettingService {

    private ISysUserService sysUserService;

    private SmsService smsService;

    private CacheService cacheService;

    /**
     * 获取用户设置界面展示的基本信息
     *
     * @param uid 用户的UID
     * @return 返回用户的用户名、头像、部分隐藏手机号、是否绑定微信
     */
    @Override
    public SettingInfoVo getUserSettingInfo(Long uid) {
        SysUser user = sysUserService.selectUserByUid(uid);
        SettingInfoVo dto = new SettingInfoVo();
        dto.setUsername(user.getUsername());
        dto.setAvatar(user.getAvatar());
        // 将手机号隐藏中间四位后返回
        dto.setPhone(StringUtil.maskPhoneNumber(user.getPhone()));
        // 判断是否绑定微信
        dto.setBindWechat(checkWechatBind(user));
        return dto;
    }

    /**
     * 更新用户头像地址
     *
     * @param avatarPath
     * @return
     */
    @Override
    public boolean updateAvatar(Long uid, String avatarPath) {
        SysUser user = new SysUser();
        user.setUid(uid);
        user.setAvatar(avatarPath);
        int num = sysUserService.updateUser(user);
        // 删除旧数据的缓存
        cacheService.removeUserInfoCache(uid);
        return num >= 1;
    }

    /**
     * 更新用户的用户名
     *
     * @param uid      用户的UID
     * @param username 用户新的用户名
     * @return 是否更新成功
     */
    @Override
    public boolean updateUsername(Long uid, String username) {
        SysUser user = new SysUser();
        user.setUid(uid);
        user.setUsername(username);
        int updatedNum = sysUserService.updateUser(user);
        // 删除旧数据的缓存
        cacheService.removeUserInfoCache(uid);
        return updatedNum >= 1;
    }

    /**
     * 修改用户的密码
     *
     * @param dto 包括用户的UID，旧的密码以及新的密码
     * @return 是否更新成功
     */
    @Override
    public boolean updatePassword(SettingPasswordDto dto) {
        Long uid = dto.getUid();
        SysUser user = sysUserService.selectUserByUid(uid);
        String oldPassword = dto.getOldPassword();
        // 判断用户输入的旧密码是否与旧密码匹配
        if (!SecurityUtil.isPasswordMatch(oldPassword, user.getPassword())) {
            // 不匹配则抛出异常
            throw new OldPasswordNotMatchException();
        }
        // 若旧密码匹配，则将密码修改为新密码
        SysUser updater = new SysUser();
        updater.setUid(uid);
        String encryptedPassword = SecurityUtil.encryptPassword(dto.getNewPassword());
        updater.setPassword(encryptedPassword);
        int updatedNum = sysUserService.updateUser(updater);
        return updatedNum >= 1;
    }

    /**
     * 将用户与微信账号绑定
     *
     * @param uid    用户的UID
     * @param openId 用户的微信OpenID
     * @return
     */
    @Override
    public boolean bindWechat(Long uid, String openId) {
        if (sysUserService.checkOpenIdBound(openId)) {
            throw new OpenIdBoundException();
        }
        SysUser user = new SysUser();
        user.setUid(uid);
        user.setOpenId(openId);
        int updatedNum = sysUserService.updateUser(user);
        return updatedNum >= 1;
    }

    /**
     * 将用户账号与微信OpenID解除绑定
     *
     * @param uid 用户的UID
     * @return 是否成功解除绑定
     */
    @Override
    public boolean unbindWechat(Long uid) {
        int num = sysUserService.unbindWechat(uid);
        return num >= 1;
    }

    /**
     * 用户更新手机号前，向用户发送短信验证码
     *
     * @param uid   用户UID
     * @param phone 用户填写的手机号
     */
    @Override
    public void sendVerificationCodeForUpdatePhone(Long uid, String phone) {
        SysUser user = sysUserService.selectUserByUid(uid);
        // 校验手机号是否正确
        if (!user.getPhone().equals(phone)) {
            throw new CustomException("用户输入的手机号码错误", StatusCode.BAD_REQUEST);
        }
        smsService.sendModifyPhoneSms(phone);
    }

    /**
     * 更新用户的手机号
     *
     * @param dto 包括用户UID、用户填写的手机号、用户填写的验证码、用户设置的新手机号
     * @return 是否成功更新
     */
    @Override
    public boolean updatePhone(SettingPhoneDto dto) {
        SysUser user = sysUserService.selectUserByUid(dto.getUid());
        // 校验手机号是否正确
        if (!user.getPhone().equals(dto.getPhone())) {
            throw new CustomException("用户输入的手机号码错误", StatusCode.BAD_REQUEST);
        }
        // 校验用户填写的验证码是否正确
        String verificationCode = smsService.queryModifyPhoneVerificationCode(dto.getPhone());
        if (!verificationCode.equals(dto.getVerificationCode())) {
            throw new CustomException("用户输入的验证码错误", StatusCode.BAD_REQUEST);
        }
        // 校验新的手机号码是否重复
        if (sysUserService.checkPhoneNumberBound(dto.getNewPhone())) {
            throw new PhoneNumberBoundException();
        }

        // 校验无误后，对手机号进行更新
        SysUser updater = new SysUser();
        updater.setUid(dto.getUid());
        updater.setPhone(dto.getNewPhone());
        int updatedNum = sysUserService.updateUser(updater);
        return updatedNum > 0;
    }

    @Autowired
    public void setSysUserService(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Autowired
    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }

    @Autowired
    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * 判断用户是否绑定微信账号
     */
    private boolean checkWechatBind(SysUser sysUser) {
        if (sysUser == null) {
            return false;
        }
        return CheckUtil.isNotEmpty(sysUser.getOpenId());
    }
}
