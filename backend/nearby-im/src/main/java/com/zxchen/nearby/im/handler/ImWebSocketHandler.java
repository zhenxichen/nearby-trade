package com.zxchen.nearby.im.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxchen.nearby.common.domain.LoginUser;
import com.zxchen.nearby.common.util.SessionUtil;
import com.zxchen.nearby.im.domain.body.MessageLocation;
import com.zxchen.nearby.im.domain.body.MessageBody;
import com.zxchen.nearby.im.domain.dto.MessageDto;
import com.zxchen.nearby.im.enums.MessageType;
import com.zxchen.nearby.im.manager.ImWebSocketManager;
import com.zxchen.nearby.im.service.ImService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Date;

/**
 * 即时通讯模块 WebSocket 处理类
 */
@Component
public class ImWebSocketHandler extends TextWebSocketHandler {

    private ImWebSocketManager imWebSocketManager;

    private ImService imService;

    /**
     * 响应连接建立
     *
     * @param session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        imWebSocketManager.onConnectionOpen(session);
    }

    /**
     * 处理消息
     * @param session Session
     * @param message 消息
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        MessageBody messageBody = new ObjectMapper().readValue(message.asBytes(), MessageBody.class);
        Long fromUid = SessionUtil.getLoginUserId(session);
        MessageDto dto = new MessageDto(fromUid, messageBody.getTo(),
                messageBody.getType(), messageBody.getContent(), new Date());
        imService.handleMessage(dto);
    }

    /**
     * 响应连接关闭
     *
     * @param session
     * @param status
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        imWebSocketManager.onConnectionClose(session);
    }

    @Autowired
    public void setImWebSocketManager(ImWebSocketManager imWebSocketManager) {
        this.imWebSocketManager = imWebSocketManager;
    }

    @Autowired
    public void setImService(ImService imService) {
        this.imService = imService;
    }
}
