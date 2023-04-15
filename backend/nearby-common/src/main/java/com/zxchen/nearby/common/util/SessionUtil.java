package com.zxchen.nearby.common.util;

import com.zxchen.nearby.common.domain.LoginUser;
import com.zxchen.nearby.common.security.WebSocketInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.WebSocketSession;

/**
 * 处理 Session 相关操作的工具类
 */
public class SessionUtil {

    /**
     * 获取 Authentication
     *
     * @param session WebSocket 的 Session
     * @return
     */
    public static Authentication getAuthentication(WebSocketSession session) {
        return (UsernamePasswordAuthenticationToken) session.getPrincipal();
    }

    /**
     * 获取用户登录信息
     *
     * @param session WebSocket的Session
     * @return 当前登录用户的LoginUser对象
     */
    public static LoginUser getLoginUser(WebSocketSession session) {
        return (LoginUser) getAuthentication(session).getPrincipal();
    }

    /**
     * 获取当前登录用户的UID
     *
     * @param session WebSocket的Session
     * @return 当前登录用户的UID
     */
    public static Long getLoginUserId(WebSocketSession session) {
        return getLoginUser(session).getUser().getUid();
    }

}
