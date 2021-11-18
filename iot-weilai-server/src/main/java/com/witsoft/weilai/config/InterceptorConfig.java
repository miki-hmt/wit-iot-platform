package com.witsoft.weilai.config;

import com.witsoft.weilai.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wit
 * @create 2020/9/15
 * @Description: 拦截器配置
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> patterns = new ArrayList();
        patterns.add("/login");

        registry.addInterceptor(authInterceptor()).addPathPatterns("/**").excludePathPatterns(patterns);
    }
}

