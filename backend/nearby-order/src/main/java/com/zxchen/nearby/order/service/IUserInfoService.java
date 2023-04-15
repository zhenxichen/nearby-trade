package com.zxchen.nearby.order.service;

import com.zxchen.nearby.order.domain.vo.MineVo;
import com.zxchen.nearby.order.domain.vo.UserInfoVo;

/**
 * 用户信息页面相关 服务层
 */
public interface IUserInfoService {

    /**
     * 获取“我的”页面中所需的所有信息
     *
     * @param uid 用户的UID
     * @return
     */
    MineVo mine(Long uid);

    /**
     * 获取用户信息界面的数据，包括用户基本信息、用户评分以及用户在售的商品信息，并判断是否是用户自己
     *
     * @param uid 要查看的用户UID
     * @param fromUid 发起请求的用户UID
     * @return 包括用户基本信息、用户评分以及用户在售的商品信息，以及是否是用户自己的页面
     */
    UserInfoVo getUserInfoPageData(Long uid, Long fromUid);

}
