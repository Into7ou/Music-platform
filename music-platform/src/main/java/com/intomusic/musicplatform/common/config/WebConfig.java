package com.intomusic.musicplatform.common.config;

import com.intomusic.musicplatform.modules.admin.interceptor.AdminAuthInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类
 * 用于配置拦截器、CORS 等
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private AdminAuthInterceptor adminAuthInterceptor;

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/admin/**")  // 拦截所有 /admin/* 路径
                .excludePathPatterns(
                        "/admin/login",         // 排除管理员登录接口（如果有单独的管理员登录）
                        "/admin/test/**"        // 排除测试接口（可选）
                );
    }

    /**
     * 配置 CORS 跨域（可选）
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000") // 允许的前端地址
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}