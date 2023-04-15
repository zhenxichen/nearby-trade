package com.zxchen.nearby.im.config;

import com.zxchen.nearby.common.security.WebSocketInterceptor;
import com.zxchen.nearby.im.handler.ImWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 即时通讯模块 WebSocket 配置
 */
@Configuration
@EnableWebSocket
public class ImWebSocketConfig implements WebSocketConfigurer {

    private ImWebSocketHandler imWebSocketHandler;

    private WebSocketInterceptor webSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(imWebSocketHandler, "/im/chat")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*");
    }

    @Autowired
    public void setImWebSocketHandler(ImWebSocketHandler imWebSocketHandler) {
        this.imWebSocketHandler = imWebSocketHandler;
    }

    @Autowired
    public void setWebSocketInterceptor(WebSocketInterceptor webSocketInterceptor) {
        this.webSocketInterceptor = webSocketInterceptor;
    }
}
