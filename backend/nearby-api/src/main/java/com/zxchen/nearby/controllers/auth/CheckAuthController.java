package com.zxchen.nearby.controllers.auth;

import com.zxchen.nearby.common.domain.LoginUser;
import com.zxchen.nearby.common.domain.web.HttpResult;
import com.zxchen.nearby.common.util.SecurityUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于判断用户是否有访问权限的接口
 */
@RestController
@RequestMapping("/auth")
public class CheckAuthController {

    /**
     * 用于快速校验用户的登录态是否有效
     *
     * @return 若用户态有效，会正常收到200，否则会返回401
     */
    @RequestMapping("/check")
    public HttpResult check() {
        LoginUser user = SecurityUtil.getLoginUser();
        return HttpResult.success("用户登录态有效，UID：" + user.getUser().getUid());
    }
}
