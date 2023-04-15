package com.zxchen.nearby.controllers.auth;

import com.zxchen.nearby.common.domain.dto.LoginRes;
import com.zxchen.nearby.common.domain.dto.PhoneLoginBody;
import com.zxchen.nearby.common.domain.dto.WechatLoginBody;
import com.zxchen.nearby.common.domain.web.HttpResult;
import com.zxchen.nearby.common.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户登录相关接口
 * 
 * @date 2021/12/14
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    private LoginService loginService;

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 用户使用手机号和密码登录的接口
     *
     * @param body 其中包含： phone: 手机号 password: 密码
     * @return 若登陆成功返回200，否则返回403或500
     */
    @PostMapping("/phone")
    public HttpResult loginByPhone(@RequestBody PhoneLoginBody body) {
        LoginRes res = loginService.loginByPhone(body.getPhone(), body.getPassword());
        return HttpResult.success("登录成功", res);
    }

    /**
     * 使用微信账号快捷登录的接口
     *
     * @param body 其中包含参数 openid: 微信提供的OpenID
     * @return 若登陆成功返回200，否则返回403或500
     */
    @PostMapping("/wechat")
    public HttpResult loginByWechat(@RequestBody WechatLoginBody body) {
        LoginRes res = loginService.loginByWechat(body.getOpenid());
        return HttpResult.success("登录成功", res);
    }
}
