package com.zxchen.nearby.common.service;

import com.zxchen.nearby.common.domain.SysUser;

/**
 * 系统用户 服务层
 */
public interface ISysUserService {

    /**
     * 通过 UID 查找用户
     */
    SysUser selectUserByUid(Long uid);

    /**
     * 通过手机号码查找用户
     */
    SysUser selectUserByPhone(String phone);

    /**
     * 通过 OpenID 查找用户
     */
    SysUser selectUserByOpenId(String openId);

    /**
     * 更新用户信息
     *
     * @param user SysUser对象
     * @return 更新成功的数据条数
     */
    int updateUser(SysUser user);

    /**
     * 插入用户信息
     * @param user SysUser对象
     * @return 插入成功的行数
     */
    int insertUser(SysUser user);

    /**
     * 校验手机号码是否已被绑定
     *
     * @param phone 手机号
     * @return 若已被绑定，返回true，否则返回false
     */
    boolean checkPhoneNumberBound(String phone);

    /**
     * 校验微信 OpenID 是否已被绑定
     *
     * @param openId 微信 OpenID
     * @return 若已被绑定，返回true，否则返回false
     */
    boolean checkOpenIdBound(String openId);

    /**
     * 将用户账号与微信账号解除绑定
     *
     * @param uid 需要解绑的用户UID
     * @return 修改条数
     */
    int unbindWechat(Long uid);

}
