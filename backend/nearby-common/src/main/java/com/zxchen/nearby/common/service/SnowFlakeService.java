package com.zxchen.nearby.common.service;

import com.zxchen.nearby.common.exception.SnowFlakeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 使用 SnowFlake 算法生成唯一 ID 的服务
 * 根据 Twitter 官方开源的 scala 版本实现
 * https://github.com/twitter-archive/snowflake/tree/scala_28
 */
@Service
public class SnowFlakeService {

    private static final Logger log = LoggerFactory.getLogger(SnowFlakeService.class);

    // 起始时间戳
    private static final Long TWEPOCH = 1288834974657L;

    // 机器ID位数
    private static final Long WORKER_ID_BITS = 5L;
    // 机房ID位数
    private static final Long DATA_CENTER_ID_BITS = 5L;
    // 序列号位数
    private static final Long SEQUENCE_BITS = 12L;

    // 最大机器ID
    private static final Long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);
    // 最大机房ID
    private static final Long MAX_DATA_CENTER_ID = -1L ^ (-1L << DATA_CENTER_ID_BITS);

    // 机器ID的偏移量
    private static final Long WORKER_ID_SHIFT = SEQUENCE_BITS;
    // 机房ID的偏移量
    private static final Long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    // 时间戳的偏移量
    private static final Long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;
    // 序列号的掩码，用于确保序列号不会超过上限
    private static final Long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);

    // 上次操作的时间戳
    private long lastTimeStamp = -1L;
    // 当前时间戳的ID序列
    private long sequence = 0L;

    @Value("${snowflake.worker-id}")
    private Long workerId;

    @Value("${snowflake.datacenter-id}")
    private Long datacenterId;

    /**
     * 使用 SnowFlake 算法生成下一个ID
     *
     * @return SnowFlake算法生成的64位ID
     */
    public synchronized Long nextId() {
        long timeStamp = System.currentTimeMillis();

        // 生成序列号
        if (lastTimeStamp == timeStamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            // 若超过序列号上限
            if (sequence == 0) {
                // 则等待至下一毫秒
                timeStamp = tilNextMillis(lastTimeStamp);
            }
        } else {
            sequence = 0L;
        }

        // 检查时间戳是否正确
        if (timeStamp < lastTimeStamp) {
            log.error("Clock is moving backwards. Rejecting requests until %d.", lastTimeStamp);
            throw new SnowFlakeException();
        }

        lastTimeStamp = timeStamp;
        return ((timeStamp - TWEPOCH) << TIMESTAMP_LEFT_SHIFT) |
                (datacenterId << DATA_CENTER_ID_SHIFT) |
                (workerId << WORKER_ID_SHIFT) |
                sequence;
    }

    /**
     * 自旋等待到下一毫秒
     *
     * @param lastTimeStamp 上一次生成ID的时间戳
     * @return 自旋结束后的时间戳
     */
    private Long tilNextMillis(Long lastTimeStamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimeStamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

}
