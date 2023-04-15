package com.zxchen.nearby.common.handler;

import com.zxchen.nearby.common.constant.StatusCode;
import com.zxchen.nearby.common.domain.web.HttpResult;
import com.zxchen.nearby.common.exception.CustomException;
import com.zxchen.nearby.common.exception.PhoneNumberBoundException;
import com.zxchen.nearby.common.exception.WrongPasswordException;
import com.zxchen.nearby.common.util.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理 CustomException
     *
     * @return 返回给前端的错误数据
     */
    @ExceptionHandler(CustomException.class)
    public HttpResult customException(CustomException e) {
        log.error(e.getMessage());
        if (e.getCode() == null) {
            return HttpResult.error(e.getMessage());
        }
        return HttpResult.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理手机号已被绑定的错误
     */
    @ExceptionHandler(PhoneNumberBoundException.class)
    public HttpResult phoneNumberBoundException(PhoneNumberBoundException e) {
        log.error(e.getMessage(), e);
        if (CheckUtil.isEmpty(e.getMessage())) {
            return HttpResult.error("该手机号已被绑定");
        }
        return HttpResult.error(e.getMessage());
    }

    /**
     * 处理密码错误的错误
     */
    @ExceptionHandler(WrongPasswordException.class)
    public HttpResult wrongPasswordException(WrongPasswordException e) {
        log.error(e.getMessage(), e);
        if (CheckUtil.isEmpty(e.getMessage())) {
            return HttpResult.error(StatusCode.FORBIDDEN, "密码错误");
        }
        return HttpResult.error(e.getCode(), e.getMessage());
    }
}
