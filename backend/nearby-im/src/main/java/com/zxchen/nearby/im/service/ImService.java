package com.zxchen.nearby.im.service;

import com.zxchen.nearby.im.domain.dto.MessageDto;
import com.zxchen.nearby.im.manager.ImWebSocketManager;
import com.zxchen.nearby.im.service.impl.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 即时通讯 服务层
 */
@Service
public class ImService {

    private IMessageService messageService;

    private ImWebSocketManager imWebSocketManager;

    /**
     * 对消息进行处理
     *
     * @param dto 包含消息发送方、接收方、类型、内容以及时间
     */
    public void handleMessage(MessageDto dto) {
        switch(dto.getType()) {
            case ACK:
                handleAckMessage(dto);
                return;
            default:
                handleChatMessage(dto);
                return;
        }
    }

    /**
     * 处理用户聊天发送的消息
     *
     * @param dto 包含消息发送方、接收方、类型、内容以及时间
     */
    private void handleChatMessage(MessageDto dto) {
        // 将消息落库
        Long mid = messageService.insertMessage(dto);
        dto.setMid(mid);
        // 若对方在线，通过websocket将消息发送给对方
        imWebSocketManager.sendMessage(dto.getTo(), dto);
        // 同时向发送者回包，确认已经发送消息
        imWebSocketManager.sendMessage(dto.getFrom(), dto);
    }

    /**
     * 处理 WebSocket 返回的 ACK 消息，将消息标为已读
     *
     * @param dto 包含消息发送方、接收方、类型、内容以及时间，其中内容为已读的消息ID
     */
    private void handleAckMessage(MessageDto dto) {
        Long mid = Long.parseLong(dto.getContent());
        messageService.setMessageHaveBeenRead(mid);
    }

    @Autowired
    public void setMessageService(MessageServiceImpl messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setImWebSocketManager(ImWebSocketManager imWebSocketManager) {
        this.imWebSocketManager = imWebSocketManager;
    }
}
