package com.group13.controller;

import com.google.gson.Gson;
import com.group13.entity.Staff;
import com.group13.handler.exception.Group13Exception;
import com.group13.service.StaffService;
import com.group13.utils.ConstantWxUtils;
import com.group13.utils.HttpClientUtils;
import com.group13.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author xujinfengxu
 */
@CrossOrigin
@Controller
@RequestMapping("/api/api/ucenter/wx")
@Api(tags = "微信api")
public class WxApiController {

    private StaffService service;

    @Autowired
    public WxApiController(StaffService service) {
        this.service = service;
    }

    /**
     * 获取扫描人信息，添加数据
     * @return
     */
    @ApiOperation("获取扫描人信息，添加数据")
    @RequestMapping("callback")
    public String callback(String code, String state){
        try{
            // 1 获取code值， 临时票据， 类似验证码
            // 2 使用code请求微信固定地址，获得access_token 和 openid
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            // 3 传入参数 id， key， code
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code
            );
            // 4 请求这个地址，得到返回的值
            // 使用httpclient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessTokenInfo : " + accessTokenInfo);
            // 从accessTokenInfo字符串中获取access_token和openid
            // 把accessTokenInfo字符串转换为map集合，根据key获取value
            // 使用json转换工具Gson
            Gson gson = new Gson();
            HashMap accessTokenMap = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) accessTokenMap.get("access_token");
            String openid = (String) accessTokenMap.get("openid");

            //把扫描人信息添加数据库里面
            //判断数据表里面是否存在相同微信信息，根据openid判断
            Staff member = service.getOpenIdMember(openid);
            if (ObjectUtils.isEmpty(member)){
                // 5 使用access_token和openid，请求wx提供的固定地址，获取扫码人信息
                // 访问微信资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //拼接两个参数
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        access_token,
                        openid
                );
                String userInfo = HttpClientUtils.get(userInfoUrl);
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                System.out.println("userInfo : " + userInfo);
                String nickname = (String) userInfoMap.get("nickname");
                String avatar = (String) userInfoMap.get("headimgurl");
                // 如果未注册
                member = new Staff();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(avatar);
                service.save(member);
            }
            //使用jwt根据member对象生成token字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            //最后：返回首页面，通过路径传递token字符串
            return "redirect:http://localhost:9528/middlepage?token="+jwtToken;
        }catch (Exception e){
            throw new Group13Exception(20001,"登陆失败");
        }
    }

    /**
     * 获取扫描人信息，添加数据
     * @return
     */
    @ApiOperation("获取扫描人信息，添加数据")
    @RequestMapping("callbackMS")
    public String callbackMS(String code, String state){
        try{
            // 1 获取code值， 临时票据， 类似验证码
            // 2 使用code请求微信固定地址，获得access_token 和 openid
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            // 3 传入参数 id， key， code
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code
            );
            // 4 请求这个地址，得到返回的值
            // 使用httpclient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessTokenInfo : " + accessTokenInfo);
            // 从accessTokenInfo字符串中获取access_token和openid
            // 把accessTokenInfo字符串转换为map集合，根据key获取value
            // 使用json转换工具Gson
            Gson gson = new Gson();
            HashMap accessTokenMap = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) accessTokenMap.get("access_token");
            String openid = (String) accessTokenMap.get("openid");

            //把扫描人信息添加数据库里面
            //判断数据表里面是否存在相同微信信息，根据openid判断
            Staff member = service.getOpenIdMember(openid);
            if (ObjectUtils.isEmpty(member)){
                // 5 使用access_token和openid，请求wx提供的固定地址，获取扫码人信息
                // 访问微信资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //拼接两个参数
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        access_token,
                        openid
                );
                String userInfo = HttpClientUtils.get(userInfoUrl);
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                System.out.println("userInfo : " + userInfo);
                String nickname = (String) userInfoMap.get("nickname");
                String avatar = (String) userInfoMap.get("headimgurl");
                // 如果未注册
                member = new Staff();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(avatar);
                service.save(member);
            }
            //使用jwt根据member对象生成token字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            //最后：返回首页面，通过路径传递token字符串
            return "redirect:http://localhost:9528/middlepage?token="+jwtToken;
        }catch (Exception e){
            throw new Group13Exception(20001,"登陆失败");
        }
    }
    /**
     * 生成vx登陆二维码
     * @return
     */
    @ApiOperation("生成vx登陆二维码")
    @RequestMapping("login")
    public String getWxCode(){
        // 微信开放平台授权baseUrl  %s相当于?代表占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //对redirect_url进行URLEncoder编码
        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        }catch(Exception e) {
        }

        //设置%s里面值
        String url = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "qintong"
        );
        // 请求微信地址
        return "redirect:" + url;
    }

}
