package com.group13.config;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.util.Utils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import java.io.IOException;


/**
 * 去除druid底部的广告配置类
 * @author xujinfengxu
 * @Date: 2020年12月10日 19:55:42
 */

@Configuration
@ConditionalOnWebApplication
@AutoConfigureAfter(DruidDataSourceAutoConfigure.class)
@ConditionalOnProperty(name = "spring.datasource.druid.stat-view-servlet.enabled", havingValue = "true", matchIfMissing = true)
public class RemoveDruidOtherConfig {


    /**
     * 方法描述:  除去页面底部的广告
     */
    @Bean
    public FilterRegistrationBean removeDruidAdFilterBean(DruidStatProperties properties) {
        final String filePath = "support/http/resources/js/common.js";

        // 获取web监控页面的参数
        DruidStatProperties.StatViewServlet config = properties.getStatViewServlet();
        // 提取common.js的配置路径
        String pattern = config.getUrlPattern() != null ? config.getUrlPattern() : "/druid/*";
        String jsPattern = pattern.replaceAll("\\*", "js/common.js");

        //创建filter进行过滤
        Filter filter = new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
            }

            @Override
            public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {
                chain.doFilter(req, rep);
                // 重置缓冲区，响应头不会被重置
                rep.resetBuffer();
                // 获取common.js
                String text = Utils.readFromResource(filePath);
                // 正则替换, 除去底部的广告信息
                text = text.replaceAll("<a.*?banner\"></a><br/>", "");
                text = text.replaceAll("powered.*?shrek.wang</a>", "");
                rep.getWriter().write(text);
            }

            @Override
            public void destroy() {
            }
        };
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns(jsPattern);
        return registrationBean;
    }
}
