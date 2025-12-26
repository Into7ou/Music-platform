package com.intomusic.musicplatform.modules.admin.model.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 角色视图对象
 * 用于返回角色信息给前端
 */
@Data
public class RoleVO {

    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
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