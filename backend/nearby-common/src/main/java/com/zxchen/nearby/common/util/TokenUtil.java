package com.zxchen.nearby.common.util;

import com.zxchen.nearby.common.constant.Constants;
import com.zxchen.nearby.common.constant.NetworkConstants;
import com.zxchen.nearby.common.domain.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.server.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 对 JWT 进行操作的工具类
 */
public class TokenUtil {

    /**
     * 从 HTTP 请求中获取 token
     *
     * @param request HTTP请求
     * @param header token的请求头
     * @return token字符串
     */
    public static String getToken(HttpServletRequest request, String header) {
        String token = request.getHeader(header);
        if (CheckUtil.isNotEmpty(token) && token.startsWith(NetworkConstants.TOKEN_PREFIX)) {
            // 将token中的前缀去除
            token = token.replace(NetworkConstants.TOKEN_PREFIX, Constants.EMPTY_STRING);
        }
        return token;
    }

    /**
     * 从 HTTP 请求中获取 token
     *
     * @param request HTTP请求
     * @param header token的请求头
     * @return token字符串
     */
    public static String getToken(ServerHttpRequest request, String header) {
        String token = request.getHeaders().getFirst(header);
        if (CheckUtil.isNotEmpty(token) && token.startsWith(NetworkConstants.TOKEN_PREFIX)) {
            // 将token中的前缀去除
            token = token.replace(NetworkConstants.TOKEN_PREFIX, Constants.EMPTY_STRING);
        }
        return token;
    }

    /**
     * 对 token 进行解析
     *
     * @param token 需进行解析的 token
     * @param secret 密钥
     * @return 解析后得到的数据
     */
    public static Claims parseToken(String token, String secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从 token 中获取用户的 key
     * @param token 用户携带的token
     * @param secret 服务端存储的密钥
     * @return
     */
    public static String getUserKeyFromToken(String token, String secret) {
        Claims claims = parseToken(token, secret);
        String userKey = (String) claims.get(NetworkConstants.JWT_USER_KEY);
        return NetworkConstants.REDIS_LOGIN_USER_KEY_PREFIX + userKey;
    }

    /**
     * 创建 token
     *
     * @param claims 要在JWT中存储的claim
     * @param secret 用于加密的密钥
     */
    public static String createToken(Map<String, Object> claims, String secret) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

}
