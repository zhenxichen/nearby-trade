package com.zxchen.nearby.common.exception;

import com.zxchen.nearby.common.constant.StatusCode;

public class WrongPasswordException extends CustomException {

    public WrongPasswordException() {
        super("密码错误", StatusCode.FORBIDDEN);
    }

}
