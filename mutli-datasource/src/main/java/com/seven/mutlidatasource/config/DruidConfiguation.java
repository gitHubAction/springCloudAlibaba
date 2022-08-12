package com.seven.mutlidatasource.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.util.Utils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import java.io.IOException;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(name = "spring.datasource.druid.stat-view-servlet.enabled", havingValue = "true", matchIfMissing = true)
public class DruidConfiguation {
    private static final String  URL_MAP_PATTERN = "/druid/*";
//    @Value("${druid.web.login.userName}")
//    private String druidUserName;
//
//    @Value("${druid.web.login.password}")
//    private String druidUserPwd;
    /**
     * 配置监控服务器
     *
     * @return 返回监控注册的servlet对象
     */
    @Bean
    public ServletRegistrationBean statViewServletDemo() {
        ServletRegistrationBean srb = new ServletRegistrationBean(new StatViewServlet(), URL_MAP_PATTERN);
//        // 添加IP白名单
//        srb.addInitParameter("allow", "127.0.0.1");
//        // 添加IP黑名单，当白名单和黑名单重复时，黑名单优先级更高
//        srb.addInitParameter("deny", "192.168.25.123");
        // 添加控制台管理用户
//        srb.addInitParameter("loginUsername", druidUserName);
//        srb.addInitParameter("loginPassword", druidUserPwd);
        // 是否能够重置数据
        srb.addInitParameter("resetEnable", "false");
        return srb;
    }
    /**
     * 配置服务过滤器
     *
     * @return 返回过滤器配置对象
     */
    @Bean
    public FilterRegistrationBean statFilterDemo() {
        FilterRegistrationBean frb = new FilterRegistrationBean(new WebStatFilter());
        // 添加过滤规则
        frb.addUrlPatterns("/*");
        // 忽略过滤格式
        frb.addInitParameter("exclusions", URL_MAP_PATTERN +",*.js,*.gif,*.jpg,*.png,*.css,*.ico,");
        frb.setOrder(1);
        return frb;
    }

    /**
     * 方法名: removeDruidAdFilterRegistrationBean
     * 方法描述 除去页面底部的广告
     * @return org.springframework.boot.web.servlet.FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean removeDruidAdFilterRegistrationBean() {

        // 提取common.js的配置路径
        String commonJsPattern = URL_MAP_PATTERN.replaceAll("\\*", "js/common.js");

        final String filePath = "support/http/resources/js/common.js";

        //创建filter进行过滤
        Filter filter = new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {}

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                chain.doFilter(request, response);
                // 重置缓冲区，响应头不会被重置
                response.resetBuffer();
                // 获取common.js
                String text = Utils.readFromResource(filePath);
                // 正则替换banner, 除去底部的广告信息
                text = text.replaceAll("<a.*?banner\"></a><br/>", "");
                text = text.replaceAll("powered.*?shrek.wang</a>", "");
                response.getWriter().write(text);
            }

            @Override
            public void destroy() {}
        };

        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setOrder(2);
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns(commonJsPattern);
        return registrationBean;
    }

}
