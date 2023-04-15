package com.zxchen.nearby.order.exception;

import com.zxchen.nearby.common.constant.StatusCode;
import com.zxchen.nearby.common.exception.CustomException;

/**
 * OpenID已与其他账号绑定的异常
 */
public class OpenIdBoundException extends CustomException {

    public OpenIdBoundException() {
        super("该微信账号已与其他账号绑定", StatusCode.BAD_REQUEST);
    }

}

