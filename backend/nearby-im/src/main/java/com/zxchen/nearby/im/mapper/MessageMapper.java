package com.zxchen.nearby.im.mapper;

import com.zxchen.nearby.im.domain.dao.MessageListDao;
import com.zxchen.nearby.im.domain.dto.MessageDto;
import com.zxchen.nearby.im.domain.dto.MessageHistoryDto;
import com.zxchen.nearby.im.domain.vo.MessageListVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 消息 数据层
 */
@Mapper
public interface MessageMapper {

    /**
     * 将消息数据插入到数据表中
     */
    int insertMessage(MessageDto dto);

    /**
     * 从数据库中提取消息列表
     *
     * @param uid 用户的UID
     * @return 消息列表显示信息
     */
    List<MessageListDao> selectMessageList(Long uid);

    /**
     * 从数据库中读取历史消息
     *
     * @param dto 包含用户UID、对方的UID、小程序端缓存的最新消息ID
     * @return 历史消息数据
     */
    List<MessageDto> selectMessageHistory(MessageHistoryDto dto);

    /**
     * 将数据库中消息的状态设为已读
     *
     * @param dto
     * @return 影响的行数
     */
    int updateMessageRead(MessageHistoryDto dto);

    /**
     * 从数据库中读取用户未读消息的总条数
     *
     * @param uid 用户UID
     * @return 未读消息的总条数
     */
    int selectUnreadMessageCount(Long uid);

    /**
     * 将单条ID的状态设为已读
     *
     * @param mid 消息ID
     * @return 影响的行数
     */
    int updateMessageReadByMid(Long mid);

}
