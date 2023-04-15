package com.zxchen.nearby.im.service;

import com.zxchen.nearby.im.domain.vo.ChatInfoVo;

/**
 * 聊天信息 服务层
 */
public interface IChatInfoService {

    /**
     * 获取聊天双方的用户信息
     *
     * @param myUid "我"的UID
     * @param oppositeUid 对方的UID
     * @return 返回双方的UID、用户名和头像
     */
    ChatInfoVo getChatInfo(Long myUid, Long oppositeUid);

}
