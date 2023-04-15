package com.zxchen.nearby.common.service.impl;

import com.zxchen.nearby.common.domain.SysUser;
import com.zxchen.nearby.common.mapper.SysUserMapper;
import com.zxchen.nearby.common.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements ISysUserService {

    private SysUserMapper sysUserMapper;

    @Autowired
    public void setSysUserMapper(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    /**
     * 通过 UID 查找用户
     */
    @Override
    public SysUser selectUserByUid(Long uid) {
        return sysUserMapper.selectUserByUid(uid);
    }

    /**
     * 通过手机号码查找用户
     */
    @Override
    public SysUser selectUserByPhone(String phone) {
        return sysUserMapper.selectUserByPhone(phone);
    }

    /**
     * 通过 OpenID 查找用户
     *
     * @param openId
     */
    @Override
    public SysUser selectUserByOpenId(String openId) {
        return sysUserMapper.selectUserByOpenId(openId);
    }

    /**
     * 更新用户信息
     *
     * @param user SysUser对象
     * @return 更新成功的数据条数
     */
    @Override
    public int updateUser(SysUser user) {
        return sysUserMapper.updateUser(user);
    }

    /**
     * 插入用户信息
     *
     * @param user SysUser对象
     * @return 插入成功的行数
     */
    @Override
    public int insertUser(SysUser user) {
        return sysUserMapper.insertUser(user);
    }

    /**
     * 校验手机号码是否已被绑定
     *
     * @param phone 手机号
     * @return 若已被绑定，返回true，否则返回false
     */
    @Override
    public boolean checkPhoneNumberBound(String phone) {
        int bindCount = sysUserMapper.checkPhoneNumberBound(phone);     // 该手机号在用户表中的出现次数
        return bindCount > 0;
    }

    /**
     * 校验微信 OpenID 是否已被绑定
     *
     * @param openId 微信 OpenID
     * @return 若已被绑定，返回true，否则返回false
     */
    @Override
    public boolean checkOpenIdBound(String openId) {
        int bindCount = sysUserMapper.checkOpenIdBound(openId);
        return bindCount > 0;
    }

    /**
     * 将用户账号与微信账号解除绑定
     *
     * @param uid 需要解绑的用户UID
     * @return 修改条数
     */
    @Override
    public int unbindWechat(Long uid) {
        return sysUserMapper.unbindWechat(uid);
    }


}
