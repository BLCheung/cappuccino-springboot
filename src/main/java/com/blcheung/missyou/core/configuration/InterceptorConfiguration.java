package com.blcheung.missyou.core.configuration;

import com.blcheung.missyou.core.interceptors.APIPermissionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
    @Bean
    public HandlerInterceptor getAPIPermissionInterceptor() { return new APIPermissionInterceptor(); }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.getAPIPermissionInterceptor());
    }
}
