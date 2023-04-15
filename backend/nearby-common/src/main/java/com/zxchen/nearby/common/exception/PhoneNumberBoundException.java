package com.zxchen.nearby.common.exception;

public class PhoneNumberBoundException extends CustomException {

    public PhoneNumberBoundException() {
        super("该手机号码已被绑定");
    }

}
