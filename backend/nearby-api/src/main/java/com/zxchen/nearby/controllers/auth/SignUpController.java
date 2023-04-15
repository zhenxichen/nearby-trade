package com.zxchen.nearby.controllers.auth;

import com.zxchen.nearby.common.domain.dto.SignUpBody;
import com.zxchen.nearby.common.domain.web.HttpResult;
import com.zxchen.nearby.common.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户注册相关接口
 */
@RestController
public class SignUpController {

    private SignUpService signUpService;

    /**
     * 用户注册接口
     * @param signUpBody 注册数据的DTO，其中包含以下数据：
     *          phone: 用户的手机号
     *          username: 用户名
     *          password: 用户设置的密码
     * @return 若注册成功，返回(200, "注册成功")
     *          否则返回(500, "该手机号已被绑定")
     */
    @PostMapping("/signup")
    public HttpResult signUp(@RequestBody SignUpBody signUpBody) {
        signUpService.signUp(signUpBody.getUsername(),
                signUpBody.getPhone(), signUpBody.getPassword());
        return HttpResult.success("注册成功");
    }

    @Autowired
    public void setSignUpService(SignUpService signUpService) {
        this.signUpService = signUpService;
    }
}
