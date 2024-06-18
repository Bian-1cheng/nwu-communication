package com.bian.nwucommunication.config;

import com.bian.nwucommunication.util.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 处理跨域问题
 *
 * @Author wang suo
 * @Date 2020/10/10 0010 21:37
 * @Version 1.0
 */
//全局配置类--配置跨域请求
@Configuration
public class CrosConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    static final String ORIGINS[] = new String[]{"GET", "POST", "PUT", "DELETE"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 所有的当前站点的请求地址，都支持跨域访问。
                .allowedOriginPatterns("*") // 所有的外部域都可跨域访问。 如果是localhost则很难配置，因为在跨域请求的时候，外部域的解析可能是localhost、127.0.0.1、主机名
                .allowCredentials(true) // 是否支持跨域用户凭证
                .allowedMethods(ORIGINS) // 当前站点支持的跨域请求类型是什么
                .maxAge(3600); // 超时时长设置为1小时。 时间单位是秒。
    }




    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //假设拦截test接口 后续实际遇到拦截的接口是时，再配置真正的拦截接口
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/fs/login")
                .excludePathPatterns("/fs/code")
                .excludePathPatterns("/fs/addinfo");    //拦截所有，排除登录注册接口
    }


}


