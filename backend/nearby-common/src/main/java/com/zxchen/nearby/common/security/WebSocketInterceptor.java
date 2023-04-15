package com.zxchen.nearby.common.security;

import com.zxchen.nearby.common.domain.LoginUser;
import com.zxchen.nearby.common.service.TokenService;
import com.zxchen.nearby.common.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * Web Socket 过滤器
 */
@Component
public class WebSocketInterceptor extends HttpSessionHandshakeInterceptor {

    private TokenService tokenService;

    /**
     * Invoked before the handshake is processed.
     *
     * @param request the current request
     * @param response the current response
     * @param wsHandler the target WebSocket handler
     * @param attributes the attributes from the HTTP handshake to associate with the WebSocket session;
     *                  the provided attributes are copied, the original map is not used.
     * @return whether to proceed with the handshake (true) or abort (false)
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        tokenService.verifyTokenTime(loginUser);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        authenticationToken.setDetails(loginUser);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return true;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }
}
