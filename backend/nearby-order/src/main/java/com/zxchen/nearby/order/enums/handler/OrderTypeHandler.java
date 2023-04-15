package com.zxchen.nearby.order.enums.handler;

import com.zxchen.nearby.order.enums.OrderType;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * 将 OrderType 通过序数转换
 */
@MappedTypes(OrderType.class)
@MappedJdbcTypes(JdbcType.TINYINT)
public class OrderTypeHandler extends EnumOrdinalTypeHandler<OrderType> {

    public OrderTypeHandler() {
        super(OrderType.class);
    }

    public OrderTypeHandler(Class<OrderType> type) {
        super(type);
    }

}
