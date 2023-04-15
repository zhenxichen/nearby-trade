package com.zxchen.nearby.im.domain.vo;

import com.zxchen.nearby.common.domain.cache.UserInfoCache;

/**
 * 将聊天双方用户信息返回给前端的VO
 */
public class ChatInfoVo {

    UserInfoCache myInfo;

    UserInfoCache oppositeInfo;

    public ChatInfoVo() {
    }

    public ChatInfoVo(UserInfoCache myInfo, UserInfoCache oppositeInfo) {
        this.myInfo = myInfo;
        this.oppositeInfo = oppositeInfo;
    }

    public UserInfoCache getMyInfo() {
        return myInfo;
    }

    public void setMyInfo(UserInfoCache myInfo) {
        this.myInfo = myInfo;
    }

    public UserInfoCache getOppositeInfo() {
        return oppositeInfo;
    }

    public void setOppositeInfo(UserInfoCache oppositeInfo) {
        this.oppositeInfo = oppositeInfo;
    }
}
