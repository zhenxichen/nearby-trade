package com.zxchen.nearby.common.exception;

public class SnowFlakeException extends RuntimeException {

    public SnowFlakeException() {
        super("SnowFlake生成ID期间发生错误");
    }

}
