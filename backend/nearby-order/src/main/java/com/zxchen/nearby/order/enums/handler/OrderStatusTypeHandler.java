package com.zxchen.nearby.order.enums.handler;

import com.zxchen.nearby.order.enums.OrderStatus;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * 将 OrderStatus 通过序数转换
 */
@MappedTypes(OrderStatus.class)
@MappedJdbcTypes(JdbcType.TINYINT)
public class OrderStatusTypeHandler extends EnumOrdinalTypeHandler<OrderStatus> {

    public OrderStatusTypeHandler() {
        super(OrderStatus.class);
    }

    public OrderStatusTypeHandler(Class<OrderStatus> type) {
        super(type);
    }

}
