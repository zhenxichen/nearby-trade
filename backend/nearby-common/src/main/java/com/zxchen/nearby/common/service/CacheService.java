package com.zxchen.nearby.common.service;

import com.zxchen.nearby.common.constant.NetworkConstants;
import com.zxchen.nearby.common.domain.SysUser;
import com.zxchen.nearby.common.domain.cache.UserInfoCache;
import com.zxchen.nearby.common.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 对部分数据进行缓存操作
 */
@Service
public class CacheService {

    private RedisCache redisCache;

    private ISysUserService sysUserService;

    @Value("${cache.time.user-info}")
    private int userInfoCacheTime;

    /**
     * 从缓存中读取用户的用户名和头像信息，如果该用户信息未被缓存，则将其加入缓存中
     *
     * @param uid 用户UID
     * @return 缓存的用户基本信息
     */
    public UserInfoCache getUserInfoFromCache(Long uid) {
        UserInfoCache cache =
                redisCache.getCacheObject(NetworkConstants.REDIS_USER_INFO_KEY_PREFIX + uid);
        // 若用户数据不在缓存中
        if (cache == null) {
            // 则从数据库中读取并缓存用户信息
            cache = cacheUserInfo(uid);
        }
        return cache;
    }

    /**
     * 从数据库中读取用户简要信息，并将其缓存到 Redis 中
     *
     * @param uid 用户 UID
     * @return 缓存的结构体
     */
    public UserInfoCache cacheUserInfo(Long uid) {
        SysUser user = sysUserService.selectUserByUid(uid);
        UserInfoCache cache = new UserInfoCache();
        cache.setUid(uid);
        cache.setUsername(user.getUsername());
        cache.setAvatar(user.getAvatar());
        // 缓存到Redis中使用的 key
        String key = NetworkConstants.REDIS_USER_INFO_KEY_PREFIX + uid;
        redisCache.setCacheObject(key, cache, userInfoCacheTime, TimeUnit.MINUTES);
        return cache;
    }

    /**
     * 移除Redis中该用户的信息缓存
     *
     * @param uid 用户UID
     */
    public void removeUserInfoCache(Long uid) {
        redisCache.deleteObject(NetworkConstants.REDIS_USER_INFO_KEY_PREFIX + uid);
    }

    @Autowired
    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Autowired
    public void setSysUserService(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

}
