package com.zxchen.nearby.controllers.user;

import com.zxchen.nearby.common.domain.web.HttpResult;
import com.zxchen.nearby.common.util.SecurityUtil;
import com.zxchen.nearby.im.domain.vo.ChatInfoVo;
import com.zxchen.nearby.im.service.IChatInfoService;
import com.zxchen.nearby.order.domain.body.LocationBody;
import com.zxchen.nearby.order.domain.dto.OnSellListDto;
import com.zxchen.nearby.order.domain.vo.*;
import com.zxchen.nearby.order.service.ICommodityService;
import com.zxchen.nearby.order.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户信息相关接口
 */
@RestController
@RequestMapping("/user/info")
public class UserInfoController {

    private IUserInfoService userInfoService;

    private ICommodityService commodityService;

    private IChatInfoService chatInfoService;

    /**
     * 获取“我的”页面信息
     *
     * @return
     */
    @RequestMapping("/mine")
    public HttpResult mine() {
        Long uid = SecurityUtil.getUserId();
        MineVo vo = userInfoService.mine(uid);
        return HttpResult.success(vo);
    }

    /**
     * 获取个人信息界面的数据
     *
     * @param uid 要查看的用户UID
     * @return 返回个人信息界面上需要显示的数据 {@link UserInfoVo}
     * 包括用户头像、用户名、UID、用户评分
     */
    @RequestMapping("/info/{uid}")
    public HttpResult info(@PathVariable Long uid) {
        Long fromUid = SecurityUtil.getUserId();
        UserInfoVo vo = userInfoService.getUserInfoPageData(uid, fromUid);
        return HttpResult.success(vo);
    }

    /**
     * 获取用户的在售商品列表
     *
     * @param uid      用户ID
     * @param location 包含查询者当前的位置信息
     * @return
     */
    @PostMapping("/selling/{uid}")
    public HttpResult onSell(@PathVariable Long uid, @RequestBody LocationBody location) {
        List<CommodityVo> onSellList = commodityService
                .onSellList(new OnSellListDto(uid, location.getLatitude(), location.getLongitude()));
        return HttpResult.success(new CommodityListVo(onSellList));
    }

    /**
     * 获取聊天双方用户信息的接口
     *
     * @param opposite 对方的UID
     * @return
     */
    @GetMapping("/chat/info")
    public HttpResult chatInfo(@RequestParam("opposite") Long opposite) {
        Long uid = SecurityUtil.getUserId();
        ChatInfoVo vo = chatInfoService.getChatInfo(uid, opposite);
        return HttpResult.success(vo);
    }


    @Autowired
    public void setUserInfoService(IUserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Autowired
    public void setCommodityService(ICommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @Autowired
    public void setChatInfoService(IChatInfoService chatInfoService) {
        this.chatInfoService = chatInfoService;
    }
}
