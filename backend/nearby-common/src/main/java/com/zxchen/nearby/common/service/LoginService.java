package com.zxchen.nearby.common.service;

import com.zxchen.nearby.common.constant.StatusCode;
import com.zxchen.nearby.common.domain.LoginUser;
import com.zxchen.nearby.common.domain.SysUser;
import com.zxchen.nearby.common.domain.dto.LoginRes;
import com.zxchen.nearby.common.exception.CustomException;
import com.zxchen.nearby.common.exception.WechatNotBoundException;
import com.zxchen.nearby.common.exception.WrongPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.invoke.WrongMethodTypeException;

@Service
public class LoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    private ISysUserService sysUserService;

    private TokenService tokenService;

    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public void setSysUserService(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 通过手机号进行登录
     *
     * @param phone 手机号
     * @param password 密码
     * @return 返回给前端的 token 和 UID
     */
    public LoginRes loginByPhone(String phone, String password) {
        Authentication authentication = null;
        try {
            // 该方法会调用UserDetailsServiceImpl.loadUserByUsername()
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(phone, password));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                throw new WrongPasswordException();
            }
            throw new CustomException(e.getMessage(), StatusCode.INTERNAL_ERROR);
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String token = tokenService.createToken(loginUser);
        Long uid = loginUser.getUser().getUid();
        return new LoginRes(token, uid);
    }

    /**
     * 使用微信进行快捷登录
     *
     * @param openId 微信 OpenID
     * @return 返回给前端的token 和 UID
     */
    public LoginRes loginByWechat(String openId) {
        SysUser sysUser = sysUserService.selectUserByOpenId(openId);
        if (sysUser == null) {
            throw new WechatNotBoundException();
        }
        LoginUser loginUser = userDetailsService.createLoginUser(sysUser);
        String token = tokenService.createToken(loginUser);
        Long uid = loginUser.getUser().getUid();
        return new LoginRes(token, uid);
    }
}
