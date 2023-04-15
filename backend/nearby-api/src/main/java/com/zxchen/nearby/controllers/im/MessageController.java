package com.zxchen.nearby.controllers.im;

import com.zxchen.nearby.common.domain.web.HttpResult;
import com.zxchen.nearby.common.util.SecurityUtil;
import com.zxchen.nearby.im.domain.body.MessageHistoryBody;
import com.zxchen.nearby.im.domain.dto.MessageDto;
import com.zxchen.nearby.im.domain.dto.MessageHistoryDto;
import com.zxchen.nearby.im.domain.vo.MessageListVo;
import com.zxchen.nearby.im.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 即时通讯消息相关接口
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    private IMessageService messageService;

    /**
     * 获取用户的消息列表
     *
     * @return 用户消息列表：包含对方的UID、用户名、头像、最新消息、时间、未读消息条数
     */
    @RequestMapping("/list")
    public HttpResult messageList() {
        Long uid = SecurityUtil.getUserId();
        List<MessageListVo> messageList = messageService.getMessageListVo(uid);
        return HttpResult.success(messageList);
    }

    /**
     * 获取用户的聊天记录
     *
     * @param body 包含对方的UID以及小程序端缓存的最新消息的ID
     * @return 返回 last 之后的所有消息记录
     */
    @PostMapping("/history")
    @ResponseBody
    public HttpResult history(@RequestBody MessageHistoryBody body) {
        Long uid = SecurityUtil.getUserId();
        List<MessageDto> messageHistory =
                messageService.getMessageHistory(new MessageHistoryDto(uid, body.getOpposite(), body.getLast()));
        return HttpResult.success(messageHistory);
    }

    /**
     * 获取未读消息条数的接口
     *
     * @return
     */
    @GetMapping("/unread")
    public HttpResult unreadMessage() {
        Long uid = SecurityUtil.getUserId();
        int res = messageService.getUnreadMessageCount(uid);
        return HttpResult.success(res);
    }

    @Autowired
    public void setMessageService(IMessageService messageService) {
        this.messageService = messageService;
    }
}
