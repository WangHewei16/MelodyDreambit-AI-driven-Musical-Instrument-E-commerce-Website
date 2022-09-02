package com.group13.utils;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author xujinfengxu
 */
@Component
public class ConstantWxUtils implements InitializingBean {

    @Value("${wx.open.app_id}")
    private String appId;

    @Value("${wx.open.app_secret}")
    private String appSecret;

    @Value("${wx.open.redirect_url}")
    private String redirectUrl;

    @Value("${wxLogin.tempUserIdUrl}")
    private String tempUserIdUrl;

    @Value("${wxLogin.secretKey}")
    private String wxLoginSecretKey;

    public static String WX_OPEN_APP_ID;
    public static String WX_OPEN_APP_SECRET;
    public static String WX_OPEN_REDIRECT_URL;
    public static String WX_LOGIN_SECRET_KEY;
    public static String TEMP_USER_ID_URL;

    @Override
    public void afterPropertiesSet() throws Exception {
        WX_OPEN_APP_ID = appId;
        WX_OPEN_APP_SECRET = appSecret;
        WX_OPEN_REDIRECT_URL = redirectUrl;
        WX_LOGIN_SECRET_KEY = wxLoginSecretKey;
        TEMP_USER_ID_URL = tempUserIdUrl;
    }

}
