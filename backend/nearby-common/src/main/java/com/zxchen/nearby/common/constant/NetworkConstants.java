package com.zxchen.nearby.common.constant;

/**
 * 网络相关常量
 */
public class NetworkConstants {

    /**
     * UTF-8 编码
     */
    public static final String UTF_8 = "UTF-8";

    /**
     * Token 前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 用户 key 在 JWT 的 claims 中存储的key
     */
    public static final String JWT_USER_KEY = "user_key";

    /**
     * Redis中存储登录用户信息的 key 前缀
     */
    public static final String REDIS_LOGIN_USER_KEY_PREFIX = "login_user:";

    /**
     * Redis中缓存用户基本信息的 key 前缀
     */
    public static final String REDIS_USER_INFO_KEY_PREFIX = "user_info:";

    /**
     * Redis中缓存修改手机号的短信验证码的前缀
     */
    public static final String REDIS_MODIFY_PHONE_KEY_PREFIX = "modify_phone:";

}
