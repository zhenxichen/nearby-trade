package com.zxchen.nearby.common.domain.web;

import com.zxchen.nearby.common.constant.StatusCode;

import java.util.HashMap;

/**
 * 用于向前端返回 HTTP 请求
 * 即只要网络请求正确，返回给前端的 HTTP 状态码统一为 200
 * 业务上的错误通过对象中的 code 字段来体现
 *
 * @date 2021/12/9
 */
public class HttpResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    // 状态码字段
    public static final String CODE_TAG = "code";

    // 消息字段
    public static final String MSG_TAG = "msg";

    // 数据字段
    public static final String DATA_TAG = "data";

    /**
     * 空构造方法
     */
    public HttpResult() { }

    /**
     * 构造方法
     *
     * @param code 状态码
     * @param msg 消息
     * @param data 数据
     */
    public HttpResult(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (data != null) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 不传递信息的对象
     *
     * @param code
     * @param msg
     */
    public HttpResult(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 返回成功的响应
     *
     * @param msg 消息
     * @param data 数据
     * @return
     */
    public static HttpResult success(String msg, Object data) {
        return new HttpResult(StatusCode.SUCCESS, msg, data);
    }


    public static HttpResult success(Object data) {
        return HttpResult.success("操作成功", data);
    }

    public static HttpResult success(String msg) {
        return HttpResult.success(msg, null);
    }

    public static HttpResult success() {
        return HttpResult.success("操作成功");
    }

    /**
     * 操作失败的响应（默认状态码为500）
     *
     * @param msg 消息
     * @param data 数据
     * @return
     */
    public static HttpResult error(String msg, Object data) {
        return new HttpResult(StatusCode.INTERNAL_ERROR, msg, data);
    }

    public static HttpResult error(Object data) {
        return HttpResult.error("出现错误", data);
    }

    public static HttpResult error(String msg) {
        return HttpResult.error(msg, null);
    }

    /**
     * 操作失败的响应（自定义状态码）
     *
     * @param code 自定义状态码
     * @param msg 错误信息
     * @param data 数据
     * @return
     */
    public static HttpResult error(int code, String msg, Object data) {
        return new HttpResult(code, msg, data);
    }

    /**
     * 操作失败的响应（自定义状态码）
     *
     * @param code 自定义状态码
     * @param msg 错误信息
     * @return
     */
    public static HttpResult error(int code, String msg) {
        return HttpResult.error(code, msg, null);
    }

}
