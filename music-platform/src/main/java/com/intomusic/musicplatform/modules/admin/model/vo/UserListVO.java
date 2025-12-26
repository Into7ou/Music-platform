package com.intomusic.musicplatform.modules.admin.model.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户列表视图对象（管理员使用）
 * 用于用户列表展示，包含用户基本信息和角色信息
 */
@Data
public class UserListVO {

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
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 账号状态：0-正常，1-禁用
     */
    private Integer status;

    /**
     * 注册时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}