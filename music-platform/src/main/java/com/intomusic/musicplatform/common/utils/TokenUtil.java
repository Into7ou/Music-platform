package com.intomusic.musicplatform.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * JWT Token 工具类
 * 用于生成和解析用户 Token
 */
@Component
public class TokenUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Token 前缀（用于 Redis 黑名单）
     */
    private static final String TOKEN_PREFIX = "token:blacklist:";

    /**
     * JWT 密钥（生产环境应该从配置文件读取）
     */
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Token 过期时间（7天，单位：毫秒）
     */
    private static final long TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;

    /**
     * 生成 JWT Token
     *
     * @param userId 用户ID
     * @return JWT Token 字符串
     */
    public String generateToken(Long userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))  // 用户ID存储在 subject
                .claim("userId", userId)              // 同时存储在自定义字段
                .setIssuedAt(now)                     // 签发时间
                .setExpiration(expiration)            // 过期时间
                .signWith(SECRET_KEY)                 // 签名
                .compact();
    }

    /**
     * 从 JWT Token 中获取用户ID
     *
     * @param token JWT Token 字符串
     * @return 用户ID，如果 Token 无效或过期则返回 null
     */
    public Long getUserIdFromToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }

        try {
            // 检查是否在黑名单中（已登出）
            String blacklistKey = TOKEN_PREFIX + token;
            if (Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey))) {
                return null;
            }

            // 解析 Token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 从 claims 中获取 userId
            Object userIdObj = claims.get("userId");
            if (userIdObj != null) {
                return Long.valueOf(userIdObj.toString());
            }

            // 如果没有 userId 字段，尝试从 subject 获取
            String subject = claims.getSubject();
            return subject != null ? Long.valueOf(subject) : null;

        } catch (Exception e) {
            // Token 解析失败（过期、签名错误等）
            return null;
        }
    }

    /**
     * 刷新 Token 过期时间（JWT 不支持刷新，返回新 Token）
     *
     * @param token 旧 Token
     * @return 新 Token，如果旧 Token 无效则返回 null
     */
    public String refreshToken(String token) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return null;
        }
        return generateToken(userId);
    }

    /**
     * 删除 Token（用于登出，将 Token 加入黑名单）
     *
     * @param token Token 字符串
     * @return 是否删除成功
     */
    public boolean removeToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        try {
            // 解析 Token 获取过期时间
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            long ttl = expiration.getTime() - System.currentTimeMillis();

            if (ttl > 0) {
                // 将 Token 加入黑名单，过期时间与 Token 一致
                String blacklistKey = TOKEN_PREFIX + token;
                redisTemplate.opsForValue().set(blacklistKey, "1", ttl, TimeUnit.MILLISECONDS);
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证 Token 是否有效
     *
     * @param token Token 字符串
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        return getUserIdFromToken(token) != null;
    }
}