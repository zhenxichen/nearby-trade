package com.zxchen.nearby.common.service;

import com.zxchen.nearby.common.domain.LoginUser;
import com.zxchen.nearby.common.domain.SysUser;
import com.zxchen.nearby.common.util.CheckUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户验证逻辑
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private ISysUserService sysUserService;

    /**
     * 通过手机号获取用户信息（由于系统设计是通过手机号登录）
     *
     * @param phone 手机号
     * @return 返回当前用户的 LoginUser 对象
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        SysUser user = sysUserService.selectUserByPhone(phone);
        if (CheckUtil.isNull(user)) {
            throw new UsernameNotFoundException("登录所用手机号码：" + phone + " 未绑定");
        }
        return createLoginUser(user);
    }

    @Autowired
    public void setSysUserService(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    public LoginUser createLoginUser(SysUser user) {
        return new LoginUser(user);
    }
}
