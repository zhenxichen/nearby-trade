package com.zxchen.nearby.common.handler;

import com.alibaba.fastjson.JSON;
import com.zxchen.nearby.common.constant.StatusCode;
import com.zxchen.nearby.common.domain.web.HttpResult;
import com.zxchen.nearby.common.util.ServletUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 认证失败处理类
 */
@Component
public class UnauthorizedHandler implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    private static final String UNAUTHORIZED_MSG = "认证失败，无法访问该地址";

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        ServletUtil.renderString(httpServletResponse,
                JSON.toJSONString(HttpResult.error(StatusCode.UNAUTHORIZED, UNAUTHORIZED_MSG)));
    }
}
