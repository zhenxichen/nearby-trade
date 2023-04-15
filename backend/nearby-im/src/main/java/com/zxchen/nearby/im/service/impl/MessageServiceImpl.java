package com.zxchen.nearby.im.service.impl;

import com.zxchen.nearby.common.util.StringUtil;
import com.zxchen.nearby.im.constant.ImConstants;
import com.zxchen.nearby.im.domain.dao.MessageListDao;
import com.zxchen.nearby.im.domain.dto.MessageDto;
import com.zxchen.nearby.im.domain.dto.MessageHistoryDto;
import com.zxchen.nearby.im.domain.vo.MessageListVo;
import com.zxchen.nearby.im.mapper.MessageMapper;
import com.zxchen.nearby.im.service.IMessageService;
import com.zxchen.nearby.im.util.builder.MessageListVoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息 服务层实现
 */
@Service
public class MessageServiceImpl implements IMessageService {

    private MessageMapper messageMapper;

    /**
     * 将消息插入到数据库表中
     *
     * @param dto
     * @return 返回生成的消息ID
     */
    @Override
    public Long insertMessage(MessageDto dto) {
        messageMapper.insertMessage(dto);
        return dto.getMid();
    }

    /**
     * 获取用户的消息列表
     *
     * @param uid 用户UID
     * @return 消息列表数据
     */
    @Override
    public List<MessageListVo> getMessageListVo(Long uid) {
        List<MessageListDao> messageList = messageMapper.selectMessageList(uid);
        List<MessageListVo> voList = new ArrayList<>();
        messageList.forEach(dao -> {
            MessageListVo vo = MessageListVoBuilder.build(dao);
            voList.add(vo);
        });
        return voList;
    }

    /**
     * 获取用户的历史消息记录
     *
     * @param dto 包含用户UID，对方UID，以及小程序端缓存的最新消息ID（可为null）
     * @return 返回给前端的消息列表
     */
    @Override
    @Transactional
    public List<MessageDto> getMessageHistory(MessageHistoryDto dto) {
        // 先更新已读状态
        messageMapper.updateMessageRead(dto);
        // 然后返回消息列表
        return messageMapper.selectMessageHistory(dto);
    }

    /**
     * 获取用户的未读消息条数
     *
     * @param uid 用户UID
     * @return 未读消息条数
     */
    @Override
    public int getUnreadMessageCount(Long uid) {
        return messageMapper.selectUnreadMessageCount(uid);
    }

    /**
     * 将消息状态设为已读
     *
     * @param mid 消息ID
     * @return 影响的行数
     */
    @Override
    public int setMessageHaveBeenRead(Long mid) {
        return messageMapper.updateMessageReadByMid(mid);
    }

    @Autowired
    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

}

