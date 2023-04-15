package com.zxchen.nearby.common.util;

import java.util.Random;

/**
 * 用于生成动态验证码的工具类
 */
public class VerificationCodeUtil {

    private static final Random random = new Random();

    public static String buildVerificationCode(int length) {
        Integer code = random.nextInt((int) Math.pow(10, length));
        String format = "%0" + length + "d";
        return String.format(format, code);
    }

}
