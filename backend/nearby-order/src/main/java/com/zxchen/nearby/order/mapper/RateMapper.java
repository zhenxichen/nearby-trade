package com.zxchen.nearby.order.mapper;

import com.zxchen.nearby.order.domain.dao.RateDao;
import com.zxchen.nearby.order.domain.dto.RateDto;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

/**
 * 用户评分 数据层
 */
@Mapper
public interface RateMapper {

    /**
     * 获取当前用户的平均评分
     *
     * @param uid 用户UID
     * @return 平均评分
     */
    BigDecimal selectUserRate(Long uid);

    /**
     * 添加用户的评价
     *
     * @param dao 包括目标用户以及评分
     * @return 影响的行数
     */
    int addRate(RateDao dao);

}
