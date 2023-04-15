package com.zxchen.nearby.im.service;

import com.zxchen.nearby.im.domain.dto.MessageDto;
import com.zxchen.nearby.im.domain.dto.MessageHistoryDto;
import com.zxchen.nearby.im.domain.vo.MessageListVo;

import java.util.List;

/**
 * 消息 服务层
 */
public interface IMessageService {

    /**
     * 将消息插入到数据库表中
     *
     * @param dto
     * @return 返回生成的消息ID
     */
    Long insertMessage(MessageDto dto);

    /**
     * 获取用户的消息列表
     *
     * @param uid 用户UID
     * @return 消息列表数据
     */
    List<MessageListVo> getMessageListVo(Long uid);

    /**
     * 获取用户的历史消息记录
     *
     * @param dto 包含用户UID，对方UID，以及小程序端缓存的最新消息ID（可为null）
     * @return 返回给前端的消息列表
     */
    List<MessageDto> getMessageHistory(MessageHistoryDto dto);

    /**
     * 获取用户的未读消息条数
     *
     * @param uid 用户UID
     * @return 未读消息条数
     */
    int getUnreadMessageCount(Long uid);

    /**
     * 将消息状态设为已读
     *
     * @param mid 消息ID
     * @return
     */
    int setMessageHaveBeenRead(Long mid);

}
