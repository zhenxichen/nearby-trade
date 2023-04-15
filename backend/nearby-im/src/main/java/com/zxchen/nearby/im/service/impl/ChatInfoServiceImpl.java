package com.zxchen.nearby.im.service.impl;

import com.zxchen.nearby.common.domain.cache.UserInfoCache;
import com.zxchen.nearby.common.service.CacheService;
import com.zxchen.nearby.im.domain.vo.ChatInfoVo;
import com.zxchen.nearby.im.service.IChatInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 聊天信息 服务层实现
 */
@Service
public class ChatInfoServiceImpl implements IChatInfoService {

    private CacheService cacheService;

    /**
     * 获取聊天双方的用户信息
     *
     * @param myUid       "我"的UID
     * @param oppositeUid 对方的UID
     * @return 返回双方的UID、用户名和头像
     */
    @Override
    public ChatInfoVo getChatInfo(Long myUid, Long oppositeUid) {
        UserInfoCache myInfo = cacheService.getUserInfoFromCache(myUid);
        UserInfoCache oppositeInfo = cacheService.getUserInfoFromCache(oppositeUid);
        return new ChatInfoVo(myInfo, oppositeInfo);
    }

    @Autowired
    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
}
