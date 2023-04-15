package com.zxchen.nearby.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCache {

    private RedisTemplate redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键
     * @param value 缓存的值
     * @param timeout 有效时间
     * @param timeUnit 时间单位
     */
    public <T> void setCacheObject(final String key, final T value,
                                   final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key 键值
     * @param timeout 有效时间
     * @param timeUnit 时间单位
     * @return 设置是否成功
     */
    public boolean expire(final String key, final long timeout, final TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 对键中存储的值进行原子的递增操作
     *
     * @param key 键值
     * @param delta 递增因子
     * @return 键递增后的值
     */
    public Long increment(final String key, final long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 对键中存储的值进行原子的加一递增操作
     *
     * @param key 键值
     * @return 键递增后的值
     */
    public Long increment(final String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 获取对象
     *
     * @param key 键
     * @return 值
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除对象
     *
     * @param key 要删除的对象的 key
     * @return 是否成功删除
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除多个对象
     *
     * @param keys 删除对象 key 的集合
     * @return 成功删除的对象数
     */
    public long deleteObjects(final Collection keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 添加地理位置信息
     *
     * @param geoKey 地理位置的key
     * @param point 地理位置
     * @param member 对应地理位置存储的信息
     */
    public <T> void addGeoPoint(String geoKey, Point point, T member) {
        BoundGeoOperations<String, T> boundGeoOps = redisTemplate.boundGeoOps(geoKey);
        boundGeoOps.add(point, member);
    }

    /**
     * 添加地理位置信息
     *
     * @param geoKey 地理位置的key
     * @param latitude 纬度
     * @param longitude 经度
     * @param member 对应地理位置存储的信息
     */
    public <T> void addGeoPoint(String geoKey, double latitude, double longitude, T member) {
        addGeoPoint(geoKey, new Point(longitude, latitude), member);
    }

    /**
     * 获取指定坐标半径距离内的所有点
     *
     * @param geoKey 地理位置的key
     * @param latitude 纬度
     * @param longitude 经度
     * @param radius 半径（默认单位为米）
     * @return
     */
    public GeoResults geoRadius(String geoKey, double latitude, double longitude, double radius) {
        Point point = new Point(latitude, longitude);
        Distance distance = new Distance(radius, RedisGeoCommands.DistanceUnit.METERS);
        Circle circle = new Circle(point, distance);
        return redisTemplate.boundGeoOps(geoKey).radius(circle);
    }

    /**
     * 获取指定坐标半径距离内的所有点，并按从小到大排序
     *
     * @param geoKey 地理位置的key
     * @param latitude 纬度
     * @param longitude 经度
     * @param radius 半径（默认单位为米）
     * @return
     */
    public GeoResults geoRadiusAsc(String geoKey, double latitude, double longitude, double radius) {
        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(radius, RedisGeoCommands.DistanceUnit.METERS);
        Circle circle = new Circle(point, distance);
        GeoRadiusCommandArgs args =
                GeoRadiusCommandArgs.newGeoRadiusArgs()
                        .includeDistance()
                        .sortAscending();
        return redisTemplate.boundGeoOps(geoKey).radius(circle, args);
    }

    /**
     * 对 ZSet 中的对象进行删除
     *
     * @param key zset 的 key
     * @param objects 要删除的对象（可为多个）
     * @return 删除的对象数量
     */
    public Long deleteObjectFromZSet(String key, Object... objects) {
        return redisTemplate.opsForZSet().remove(key, objects);
    }


    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
