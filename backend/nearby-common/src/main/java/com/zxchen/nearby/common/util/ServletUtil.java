package com.zxchen.nearby.common.util;

import com.zxchen.nearby.common.constant.ContentType;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 进行 Servlet 相关操作的工具类
 */
public class ServletUtil {

    /**
     * 将字符串添加到响应体中
     * @param response 响应体
     * @param string 要添加的字符串
     */
    public static void renderString(HttpServletResponse response, String string) throws IOException {
        try {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(ContentType.JSON);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            throw e;
        }
    }
}
