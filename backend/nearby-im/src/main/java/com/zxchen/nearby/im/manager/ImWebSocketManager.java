package com.zxchen.nearby.im.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxchen.nearby.common.util.SessionUtil;
import com.zxchen.nearby.im.domain.dto.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ImWebSocketManager {

    private static final Logger log = LoggerFactory.getLogger(ImWebSocketManager.class);

    private static ConcurrentHashMap<Long, WebSocketSession> webSockets
            = new ConcurrentHashMap<>();

    /**
     * 处理连接建立时的逻辑
     *
     * @param session
     */
    public void onConnectionOpen(WebSocketSession session) {
        Long uid = SessionUtil.getLoginUserId(session);
        webSockets.put(uid, session);
    }

    /**
     * 处理连接关闭时的逻辑
     *
     * @param session
     */
    public void onConnectionClose(WebSocketSession session) {
        Long uid = SessionUtil.getLoginUserId(session);
        try {
            session.close();
        } catch (IOException e) {
            log.error("关闭连接失败", e);
        }
        webSockets.remove(uid, session);
    }

    /**
     * 发送消息给对方用户
     *
     * @param to 消息发送目标用户
     * @param dto 消息的DTO结构
     * @return 是否成功发送消息
     */
    public boolean sendMessage(Long to, MessageDto dto) {
        WebSocketSession session = webSockets.get(to);
        // 若对方不在线，则不尝试发送socket信息
        if (session == null || !session.isOpen()) {
            return false;
        }
        try {
            String message = new ObjectMapper().writeValueAsString(dto);
            session.sendMessage(new TextMessage(message));
            return true;
        } catch (Exception e) {
            log.error("消息发送失败", e);
            return false;
        }
    }

}
