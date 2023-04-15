package com.zxchen.nearby.order.enums.handler;

import com.zxchen.nearby.order.enums.OrderRate;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * 将 OrderRate 通过序数转换
 */
@MappedTypes(OrderRate.class)
@MappedJdbcTypes(JdbcType.TINYINT)
public class OrderRateTypeHandler extends EnumOrdinalTypeHandler<OrderRate> {

    public OrderRateTypeHandler() {
        super(OrderRate.class);
    }

    public OrderRateTypeHandler(Class<OrderRate> type) {
        super(type);
    }

}
