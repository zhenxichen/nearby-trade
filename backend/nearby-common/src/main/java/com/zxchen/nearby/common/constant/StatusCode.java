package com.zxchen.nearby.common.constant;

/**
 * 状态码，用于表示业务状态，与 HTTP 状态码不一定重合
 */
public class StatusCode {

    // 成功
    public static final int SUCCESS = 200;

    // 成功
    public static final int OK = 200;


    // 参数列表错误（缺少，格式不匹配）
    public static final int BAD_REQUEST = 400;

    // 未授权
    public static final int UNAUTHORIZED = 401;

    // 拒绝访问
    public static final int FORBIDDEN = 403;

    // 未找到
    public static final int NOT_FOUND = 404;

    // 请求方法被禁止
    public static final int METHOD_NOT_ALLOWED = 405;

    // 系统内部错误
    public static final int INTERNAL_ERROR = 500;
}
