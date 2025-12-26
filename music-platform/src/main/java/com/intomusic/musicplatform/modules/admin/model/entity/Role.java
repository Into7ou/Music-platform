package com.intomusic.musicplatform.modules.admin.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 角色实体类
 * 对应数据库表：sys_role
 */
@Data
public class Role {
    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色名称（如：管理员、普通用户）
     */
    private String roleName;

    /**
     * 角色编码（如：ROLE_ADMIN、ROLE_USER）
     */
    private String roleCode;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}