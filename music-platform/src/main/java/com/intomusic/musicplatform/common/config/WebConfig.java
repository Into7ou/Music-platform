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
     * 配置 CORS 跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 🔧 修复：使用 allowedOriginPatterns 代替 allowedOrigins
                .allowedOriginPatterns(
                        "http://localhost:*",      // 允许 localhost 的任意端口
                        "http://127.0.0.1:*"        // 允许 127.0.0.1 的任意端口
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}