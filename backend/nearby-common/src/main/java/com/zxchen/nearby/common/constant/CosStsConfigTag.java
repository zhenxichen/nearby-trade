package com.zxchen.nearby.common.constant;

/**
 * 腾讯云 COS 临时密钥生成配置的TAG
 */
public class CosStsConfigTag {

    /**
     * 密钥ID
     */
    public static final String SECRET_ID = "secretId";

    /**
     * 密钥key
     */
    public static final String SECRET_KEY = "secretKey";

    /**
     * 临时密钥有效时长(s)
     */
    public static final String DURATION = "durationSeconds";

    /**
     * 授权的桶
     */
    public static final String BUCKET = "bucket";

    /**
     * 桶所在地域
     */
    public static final String REGION = "region";

    /**
     * 允许的前缀
     */
    public static final String ALLOW_PREFIX = "allowPrefixes";

    /**
     * 允许进行的操作
     */
    public static final String ALLOW_ACTIONS = "allowActions";

}
