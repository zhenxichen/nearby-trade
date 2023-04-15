package com.zxchen.nearby.common.service;

import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Response;
import com.zxchen.nearby.common.constant.CosStsConfigTag;
import com.zxchen.nearby.common.constant.StatusCode;
import com.zxchen.nearby.common.exception.CustomException;
import com.zxchen.nearby.common.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.TreeMap;
import java.util.UUID;

/**
 * 腾讯云对象存储 COS 服务
 */
@Service
public class CosService {

    @Value("${qcloud.secret.id}")
    private String secretId;

    @Value("${qcloud.secret.key}")
    private String secretKey;

    @Value("${qcloud.cos.sts.duration}")
    private int duration;

    @Value("${qcloud.cos.bucket}")
    private String bucket;

    @Value("${qcloud.cos.region}")
    private String region;

    /**
     * 生成临时密钥
     */
    public Response createCredential() {
        TreeMap<String, Object> configMap = new TreeMap<>();
        configMap.put(CosStsConfigTag.SECRET_ID, secretId);
        configMap.put(CosStsConfigTag.SECRET_KEY, secretKey);
        configMap.put(CosStsConfigTag.DURATION, duration);
        configMap.put(CosStsConfigTag.BUCKET, bucket);
        configMap.put(CosStsConfigTag.REGION, region);
        configMap.put(CosStsConfigTag.ALLOW_PREFIX, new String[] {
                "2021/*", "2022/*", "2023/*", "2024/*", "2025/*"
        });
        configMap.put(CosStsConfigTag.ALLOW_ACTIONS, new String[] {
                // 简单上传
                "name/cos:PutObject", "name/cos:PostObject",
                // 分片上传
                "name/cos:InitiateMultipartUpload", "name/cos:ListMultipartUploads",
                "name/cos:ListParts", "name/cos:UploadPart", "name/cos:CompleteMultipartUpload"
        });
        try {
            return CosStsClient.getCredential(configMap);
        } catch (IOException e) {
            throw new CustomException("COS临时签名生成失败");
        }
    }

    /**
     * 生成一个文件存储路径
     */
    public String createFilePath() {
        return DateFormatUtil.getCurrentDateSeparateBySlash() + "/" + UUID.randomUUID();
    }

}
