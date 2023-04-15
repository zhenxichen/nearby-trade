package com.zxchen.nearby.common.util;

import com.zxchen.nearby.common.domain.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityUtil {

    /**
     * 将密码通过 BCryptPasswordEncoder 进行加密
     *
     * @param password 原密码
     * @return 加密后得到的字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    /**
     * 判断密码与加密字符串是否匹配
     *
     * @param password 未加密的密码
     * @param encodedPassword 加密后的密码
     * @return 两者是否匹配
     */
    public static boolean isPasswordMatch(String password, String encodedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, encodedPassword);
    }

    /**
     * 获取 Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前用户的 LoginUser 对象
     */
    public static LoginUser getLoginUser() {
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取当前用户的 UID
     */
    public static Long getUserId() {
        return getLoginUser().getUser().getUid();
    }
}
