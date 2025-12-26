package com.intomusic.musicplatform.modules.admin.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intomusic.musicplatform.common.result.Result;
import com.intomusic.musicplatform.common.utils.TokenUtil;
import com.intomusic.musicplatform.modules.admin.mapper.RoleMapper;
import com.intomusic.musicplatform.modules.admin.model.entity.Role;
import com.intomusic.musicplatform.modules.user.mapper.UserMapper;
import com.intomusic.musicplatform.modules.user.model.entity.User;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * 管理员权限拦截器
 * 拦截所有 /admin/* 请求，验证是否为管理员
 */
@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 1. 允许 OPTIONS 预检请求通过（CORS 跨域支持）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 2. 获取 Token
        String token = request.getHeader("Authorization");

        // 3. Token 为空，返回 401 未登录
        if (token == null || token.trim().isEmpty()) {
            sendErrorResponse(response, 401, "未登录，请先登录");
            return false;
        }

        // 4. 去掉 "Bearer " 前缀（如果有）
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 5. 验证 Token 并获取用户ID
        Long userId;
        try {
            userId = tokenUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            sendErrorResponse(response, 401, "Token无效或已过期");
            return false;
        }

        if (userId == null) {
            sendErrorResponse(response, 401, "Token无效或已过期");
            return false;
        }

        // 6. 查询用户信息
        User user;
        try {
            user = userMapper.selectById(userId);
        } catch (Exception e) {
            sendErrorResponse(response, 500, "查询用户信息失败");
            return false;
        }

        if (user == null) {
            sendErrorResponse(response, 401, "用户不存在");
            return false;
        }

        // 7. 检查账号是否被禁用
        if (user.getStatus() != null && user.getStatus() == 1) {
            sendErrorResponse(response, 403, "账号已被禁用，无法访问");
            return false;
        }

        // 8. 检查是否有角色
        if (user.getRoleId() == null) {
            sendErrorResponse(response, 403, "无权限访问管理后台");
            return false;
        }

        // 9. 查询角色信息
        Role role;
        try {
            role = roleMapper.selectById(user.getRoleId());
        } catch (Exception e) {
            sendErrorResponse(response, 500, "查询角色信息失败");
            return false;
        }

        if (role == null) {
            sendErrorResponse(response, 403, "角色不存在，无权限访问");
            return false;
        }

        // 10. 验证是否为管理员角色
        if (!"ROLE_ADMIN".equals(role.getRoleCode())) {
            sendErrorResponse(response, 403, "需要管理员权限才能访问");
            return false;
        }

        // 11. 验证通过，放行
        return true;
    }

    /**
     * 发送 JSON 格式的错误响应
     */
    private void sendErrorResponse(HttpServletResponse response,
                                   int status,
                                   String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");

        // 使用统一的 Result 格式
        Result<String> result = Result.error(message);

        // 转换为 JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);

        response.getWriter().write(json);
    }
}