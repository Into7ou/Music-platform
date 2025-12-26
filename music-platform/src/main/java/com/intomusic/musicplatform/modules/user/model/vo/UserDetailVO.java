package com.intomusic.musicplatform.modules.user.model.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户详情视图对象
 */
@Data
public class UserDetailVO {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 注册时间
     */
    private LocalDateTime createTime;
    private Long roleId;
    private String roleName;
}