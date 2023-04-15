package com.zxchen.nearby.controllers.user;

import com.zxchen.nearby.common.domain.web.HttpResult;
import com.zxchen.nearby.common.util.SecurityUtil;
import com.zxchen.nearby.order.domain.body.*;
import com.zxchen.nearby.order.domain.dto.SettingPasswordDto;
import com.zxchen.nearby.order.domain.dto.SettingPhoneDto;
import com.zxchen.nearby.order.domain.vo.*;
import com.zxchen.nearby.order.service.IUserSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户设置界面操作的API
 */
@RestController
@RequestMapping("/setting")
public class SettingController {

    private IUserSettingService userSettingService;

    /**
     * 获取用户设置界面显示的信息，包括头像、用户名、手机号以及是否绑定微信
     *
     * @return
     */
    @RequestMapping("/info")
    public HttpResult info() {
        Long uid = SecurityUtil.getUserId();
        SettingInfoVo settingInfo = userSettingService.getUserSettingInfo(uid);
        return HttpResult.success(settingInfo);
    }

    /**
     * 更新用户头像
     *
     * @param body 包含头像地址
     * @return
     */
    @PostMapping("/avatar")
    public HttpResult setAvatar(@RequestBody AvatarBody body) {
        Long uid = SecurityUtil.getUserId();
        userSettingService.updateAvatar(uid, body.getAvatar());
        return HttpResult.success();
    }

    /**
     * 更新用户名
     *
     * @param body 包含用户新的用户名
     * @return
     */
    @PostMapping("/username")
    public HttpResult setUsername(@RequestBody SettingUsernameBody body) {
        Long uid = SecurityUtil.getUserId();
        userSettingService.updateUsername(uid, body.getUsername());
        return HttpResult.success();
    }

    /**
     * 修改密码接口
     *
     * @param body 包含用户的旧密码，以及要修改的新密码
     * @return 若修改成功返回200，若旧密码错误则返回403
     */
    @PostMapping("/password")
    public HttpResult setPassword(@RequestBody SettingPasswordBody body) {
        Long uid = SecurityUtil.getUserId();
        userSettingService
                .updatePassword(new SettingPasswordDto(uid, body.getOldPassword(), body.getNewPassword()));
        return HttpResult.success();
    }

    /**
     * 绑定微信账号接口
     *
     * @param body 其中包含将进行绑定的openid
     * @return 若操作成功返回200，若该微信已和其他账号绑定，返回400
     */
    @PostMapping("/wechat/bind")
    public HttpResult bindWechat(@RequestBody BindWechatBody body) {
        Long uid = SecurityUtil.getUserId();
        userSettingService.bindWechat(uid, body.getOpenid());
        return HttpResult.success();
    }

    /**
     * 与微信解绑的接口
     *
     * @return 若操作成功返回200
     */
    @RequestMapping("/wechat/unbind")
    public HttpResult unbindWechat() {
        Long uid = SecurityUtil.getUserId();
        userSettingService.unbindWechat(uid);
        return HttpResult.success();
    }

    /**
     * 向用户手机发送获取用于更新手机号码的验证码
     *
     * @return 若操作成功返回200
     */
    @GetMapping("/phone/vc")
    public HttpResult queryVerificationCodeForUpdatePhone(@RequestParam("phone") String phone) {
        userSettingService.sendVerificationCodeForUpdatePhone(SecurityUtil.getUserId(), phone);
        return HttpResult.success();
    }

    /**
     * 更新手机号码接口
     *
     * @param body 请求体，包括用户填写的手机号、验证码以及新的手机号
     * @return 若操作成功，返回200，若填写手机号错误或验证码错误返回400，若新手机号已被绑定返回500
     */
    @PostMapping("/phone/update")
    public HttpResult updatePhone(@RequestBody SettingPhoneBody body) {
        userSettingService.updatePhone(new SettingPhoneDto(SecurityUtil.getUserId(), body));
        return HttpResult.success();
    }

    @Autowired
    public void setUserSettingService(IUserSettingService userSettingService) {
        this.userSettingService = userSettingService;
    }

}
