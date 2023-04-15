package com.zxchen.nearby.order.exception;

import com.zxchen.nearby.common.constant.StatusCode;
import com.zxchen.nearby.common.exception.CustomException;

/**
 * 用户修改密码时输入的旧密码不匹配导致的异常
 */
public class OldPasswordNotMatchException extends CustomException {

    public OldPasswordNotMatchException() {
        super("输入的旧密码错误", StatusCode.FORBIDDEN);
    }

}
