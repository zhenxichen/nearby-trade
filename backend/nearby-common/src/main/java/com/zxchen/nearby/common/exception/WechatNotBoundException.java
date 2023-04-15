package com.zxchen.nearby.common.exception;

import com.zxchen.nearby.common.constant.StatusCode;

/**
 * 微信账号未绑定已有系统账号的问题
 */
public class WechatNotBoundException extends CustomException {

    public WechatNotBoundException() {
        super("该微信账号暂未绑定", StatusCode.FORBIDDEN);
    }

}
