package com.zxchen.nearby.common.service;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import com.zxchen.nearby.common.constant.NetworkConstants;
import com.zxchen.nearby.common.constant.StatusCode;
import com.zxchen.nearby.common.exception.CustomException;
import com.zxchen.nearby.common.redis.RedisCache;
import com.zxchen.nearby.common.util.VerificationCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 短信发送相关服务
 */
@Service
public class SmsService {

    private static final String AREA_CODE_CN = "+86";

    // 验证码的有效期（5分钟）
    private static final int VERIFICATION_CODE_TIME_OUT = 5;

    private RedisCache redisCache;

    @Value("${qcloud.sms.verification-code-length}")
    private Integer verificationCodeLength;

    @Value("${qcloud.secret.id}")
    private String secretId;

    @Value("${qcloud.secret.key}")
    private String secretKey;

    @Value("${qcloud.sms.app-id}")
    private String appId;

    @Value("${qcloud.sms.modify-phone.template-id}")
    private String modifyPhoneTemplate;

    // 腾讯云认证对象
    private Credential credential;

    // 短信服务客户端
    private SmsClient smsClient;

    @PostConstruct
    public void init() {
        credential = new Credential(secretId, secretKey);
        smsClient = new SmsClient(credential, "ap-guangzhou");
    }

    /**
     * 向用户的手机发送修改手机号验证码
     *
     * @param phone 用户的手机号
     */
    public void sendModifyPhoneSms(String phone) {
        // 生成动态验证码
        String verificationCode = VerificationCodeUtil.buildVerificationCode(verificationCodeLength);
        // 将验证码存储到 redis 当中
        redisCache.setCacheObject(NetworkConstants.REDIS_MODIFY_PHONE_KEY_PREFIX + phone,
                verificationCode, VERIFICATION_CODE_TIME_OUT, TimeUnit.MINUTES);
        // 发送验证码短信
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setSmsSdkAppId(appId);
        sendSmsRequest.setSignName("陈枕熙日常分享录");
        sendSmsRequest.setSenderId("");
        sendSmsRequest.setSessionContext("");
        sendSmsRequest.setExtendCode("");
        String phoneNumber = AREA_CODE_CN + phone;
        sendSmsRequest.setPhoneNumberSet(new String[]{ phoneNumber });
        sendSmsRequest.setTemplateId(modifyPhoneTemplate);
        sendSmsRequest.setTemplateParamSet(new String[]{ verificationCode });
        try {
            SendSmsResponse response = smsClient.SendSms(sendSmsRequest);
        } catch (TencentCloudSDKException e) {
            throw new CustomException("发送短信验证码失败", StatusCode.INTERNAL_ERROR);
        }
    }

    /**
     * 读取当前缓存的用户修改手机号验证码
     *
     * @param phone 用户的手机号
     * @return 之前发送给用户的验证码
     */
    public String queryModifyPhoneVerificationCode(String phone) {
        return redisCache.getCacheObject(NetworkConstants.REDIS_MODIFY_PHONE_KEY_PREFIX + phone);
    }

    @Autowired
    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }
}
