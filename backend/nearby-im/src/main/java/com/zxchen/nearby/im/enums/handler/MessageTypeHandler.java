package com.zxchen.nearby.im.enums.handler;

import com.zxchen.nearby.im.enums.MessageType;
import org.apache.ibatis.type.*;

/**
 * 将 MessageType 通过序数进行转换
 */
@MappedTypes(MessageType.class)
@MappedJdbcTypes(JdbcType.TINYINT)
public class MessageTypeHandler extends EnumOrdinalTypeHandler<MessageType> {

    public MessageTypeHandler() {
        super(MessageType.class);
    }

    public MessageTypeHandler(Class<MessageType> type) {
        super(type);
    }

}
