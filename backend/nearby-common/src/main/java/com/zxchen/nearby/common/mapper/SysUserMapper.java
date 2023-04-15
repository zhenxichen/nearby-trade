package com.zxchen.nearby.common.mapper;

import com.zxchen.nearby.common.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表 Mapper层
 */
@Mapper
public interface SysUserMapper {

    /**
     * 根据 UID 查询用户
     *
     * @param uid 用户的 UID
     * @return
     */
    SysUser selectUserByUid(Long uid);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return
     */
    SysUser selectUserByPhone(String phone);

    /**
     * 通过 OpenID 查询用户
     *
     * @param openId 微信 OpenID
     * @return
     */
    SysUser selectUserByOpenId(String openId);

    /**
     * 根据 UID 更新用户信息
     *
     * @param user SysUser对象
     * @return 插入成功的行数
     */
    int updateUser(SysUser user);

    /**
     * 插入用户信息
     *
     * @param user SysUser对象
     * @return 插入成功的行数
     */
    int insertUser(SysUser user);

    /**
     * 校验手机号是否已被绑定
     *
     * @param phone 手机号
     * @return 若已被绑定，返回绑定数量（应为 1），若未绑定，返回 0
     */
    int checkPhoneNumberBound(String phone);

    /**
     * 校验微信 OpenID 是否已被绑定
     *
     * @param openId 微信OpenID
     * @return 若已被绑定，返回绑定数量（应为 1），若未绑定，返回 0
     */
    int checkOpenIdBound(String openId);

    /**
     * 将用户的微信解绑，即将OpenID字段设为 null
     *
     * @param uid 需要解绑的用户UID
     * @return 操作成功的行数
     */
    int unbindWechat(Long uid);

}
