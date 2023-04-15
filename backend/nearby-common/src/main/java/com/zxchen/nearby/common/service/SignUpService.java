package com.zxchen.nearby.common.service;

import com.zxchen.nearby.common.domain.SysUser;
import com.zxchen.nearby.common.exception.PhoneNumberBoundException;
import com.zxchen.nearby.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    private ISysUserService sysUserService;

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param phone 手机号
     * @param password 密码
     */
    public void signUp(String username, String phone, String password) {
        if (sysUserService.checkPhoneNumberBound(phone)) {
            throw new PhoneNumberBoundException();
        }
        SysUser user = new SysUser();
        user.setPhone(phone);
        user.setUsername(username);
        user.setPassword(SecurityUtil.encryptPassword(password));
        sysUserService.insertUser(user);
    }

    @Autowired
    public void setSysUserService(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }
}
