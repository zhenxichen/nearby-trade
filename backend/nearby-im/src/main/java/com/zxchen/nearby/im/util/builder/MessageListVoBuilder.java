package com.zxchen.nearby.im.util.builder;

import com.zxchen.nearby.common.util.StringUtil;
import com.zxchen.nearby.im.constant.ImConstants;
import com.zxchen.nearby.im.domain.dao.MessageListDao;
import com.zxchen.nearby.im.domain.vo.MessageListVo;
import com.zxchen.nearby.im.enums.MessageType;

/**
 * 将从数据库读取出的 DAO 对象转为返回给前端的 VO 对象
 */
public class MessageListVoBuilder {

    /**
     * 将从数据库读取出的 DAO 对象转为返回给前端的 VO 对象
     * @param dao 从数据库读取出的 DAO 对象
     * @return 返回给前端的 VO 对象
     */
    public static MessageListVo build(MessageListDao dao) {
        MessageListVo vo = new MessageListVo();

        // 将不需要修改的数据直接放入VO
        vo.setUsername(dao.getUsername());
        vo.setAvatar(dao.getAvatar());
        vo.setOpposite(dao.getOpposite());
        vo.setTime(dao.getTime());
        vo.setCount(dao.getCount());

        // 根据消息类型对内容进行修改
        MessageType type = dao.getType();
        if (type.equals(MessageType.IMAGE)) {
            vo.setContent(ImConstants.IMAGE_CONTENT_ABBREVIATION);
        } else if (type.equals(MessageType.LOCATION)) {
            vo.setContent(ImConstants.LOCATION_CONTENT_ABBREVIATION);
        } else {
            vo.setContent(StringUtil.omit(dao.getContent(), ImConstants.MSG_LIST_CONTENT_LENGTH));
        }

        return vo;
    }
}
