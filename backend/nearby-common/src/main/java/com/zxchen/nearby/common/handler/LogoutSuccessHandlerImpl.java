package com.zxchen.nearby.common.handler;

import com.alibaba.fastjson.JSON;
import com.zxchen.nearby.common.constant.StatusCode;
import com.zxchen.nearby.common.domain.LoginUser;
import com.zxchen.nearby.common.domain.web.HttpResult;
import com.zxchen.nearby.common.service.TokenService;
import com.zxchen.nearby.common.util.CheckUtil;
import com.zxchen.nearby.common.util.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户退出操作处理类
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    private TokenService tokenService;

    /**
     * 当用户退出时进行的处理
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser = tokenService.getLoginUser(httpServletRequest);
        if (loginUser != null) {
            // 从缓存中删除该用户的登录态信息
            tokenService.deleteLoginUser(loginUser);
        }
        ServletUtil.renderString(httpServletResponse,
                JSON.toJSONString(HttpResult.success("退出成功")));
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }
}
