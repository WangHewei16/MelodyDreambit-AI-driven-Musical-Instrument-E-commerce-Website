package com.group13.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author xujinfengxu
 * 读取配置文件内容
 * 当项目一启动，spring接口，spring加载之后，执行接口一个方法
 */
@Component
public class ConstantPropertiesUtils implements InitializingBean {


    private String endpoint;

    private String keyId;

    private String keySecret;

    private String bucketName;

    @Value("${aliyun.oss.file.endpoint}")
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Value("${aliyun.oss.file.keyid}")
    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    @Value("${aliyun.oss.file.keysecret}")
    public void setKeySecret(String keySecret) {
        this.keySecret = keySecret;
    }

    @Value("${aliyun.oss.file.bucketname}")
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * 定义公开静态常量
     * @throws Exception
     */
    public static String END_POINT;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String BUCKET_NAME;
    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = endpoint;
        KEY_ID = keyId;
        KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
    }
}
