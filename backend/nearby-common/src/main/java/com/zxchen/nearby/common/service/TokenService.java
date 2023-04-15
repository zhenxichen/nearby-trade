package com.zxchen.nearby.common.service;

import com.zxchen.nearby.common.constant.Constants;
import com.zxchen.nearby.common.constant.NetworkConstants;
import com.zxchen.nearby.common.domain.LoginUser;
import com.zxchen.nearby.common.redis.RedisCache;
import com.zxchen.nearby.common.util.CheckUtil;
import com.zxchen.nearby.common.util.TokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Token鉴权相关服务
 */
@Component
public class TokenService {

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;

    private RedisCache redisCache;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TWENTY = 20 * 60 * 1000L;

    @Autowired
    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * 获取当前登录用户信息
     *
     * @param request HTTP请求
     * @return 当前用户的 UserDetail
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = TokenUtil.getToken(request, header);
        return getLoginUser(token);
    }

    public LoginUser getLoginUser(ServerHttpRequest request) {
        String token = TokenUtil.getToken(request, header);
        return getLoginUser(token);
    }

    /**
     * 通过 token 获取当前登录用户信息
     *
     * @param token 用户的token
     * @return
     */
    public LoginUser getLoginUser(String token) {
        // 若未获取到token, 返回null
        if (CheckUtil.isEmpty(token)) {
            return null;
        }
        String userKey = TokenUtil.getUserKeyFromToken(token, secret);
        LoginUser user = redisCache.getCacheObject(userKey);
        return user;
    }

    /**
     * 生成 token
     *
     * @param loginUser 登录用户的 LoginUser 对象
     */
    public String createToken(LoginUser loginUser) {
        // 随机生成 UUID 作为存储用户登录信息的 key
        String userKey = UUID.randomUUID().toString();
        loginUser.setKey(userKey);
        refreshToken(loginUser);

        // 对UUID进行加密，生成用户的token
        Map<String, Object> claims = new HashMap<>();
        claims.put(NetworkConstants.JWT_USER_KEY, userKey);
        String token = TokenUtil.createToken(claims, secret);
        return token;
    }

    /**
     * 校验用户的 token 是否处于有效期内
     * 若距离过期时间小于20分钟则刷新当前用户有效期
     *
     * @param loginUser 当前用户的登录态对象
     * @return 若处于有效期内返回true, 否则返回 false
     */
    public boolean verifyTokenTime(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (currentTime > expireTime) {
            return false;
        }
        if (expireTime - currentTime <= MILLIS_MINUTE_TWENTY) {
            refreshToken(loginUser);
        }
        return true;
    }

    /**
     * 删除缓存中的用户登录态信息
     *
     * @param loginUser 需要删除的用户登录态对象
     * @return 删除是否成功
     */
    public boolean deleteLoginUser(LoginUser loginUser) {
        if (loginUser == null) {
            return false;
        }
        String userKey = loginUser.getKey();
        if (CheckUtil.isEmpty(userKey)) {
            return false;
        }
        return redisCache.deleteObject(NetworkConstants.REDIS_LOGIN_USER_KEY_PREFIX + userKey);
    }

    /**
     * 刷新 Token 的有效期
     */
    private void refreshToken(LoginUser loginUser) {
        long expire = System.currentTimeMillis() + expireTime * MILLIS_MINUTE;
        loginUser.setExpireTime(expire);
        // 将用户的登录态信息缓存到redis当中
        String key = NetworkConstants.REDIS_LOGIN_USER_KEY_PREFIX + loginUser.getKey();
        redisCache.setCacheObject(key, loginUser, expireTime, TimeUnit.MINUTES);
    }

}
