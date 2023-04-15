package com.zxchen.nearby.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zxchen.nearby.common.domain.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class LoginUser implements UserDetails {

    private static final long serialVersionUID = 1L;

    // 用户信息
    private SysUser user;

    // 存储用户登录态的key
    private String key;

    // token的过期时间
    private Long expireTime;

    public LoginUser() {
    }

    public LoginUser(SysUser user) {
        this.user = user;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String getUsername() {
        return user.getUid().toString();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }



    /**
     * 判断账号是否过期，本项目无需求
     *
     * @return 直接返回true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 判断账号是否被锁定，本项目无需求
     *
     * @return 直接返回true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 系统业务需求不涉及禁用业务，因此直接返回 true
     *
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * 系统没有权限设置，因此直接返回 null
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
