package com.intomusic.musicplatform.modules.admin.model.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户详情视图对象（管理员使用）
 * 包含用户完整信息和角色信息
 */
@Data
public class AdminUserVO {

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
     * 角色描述
     */
    private String roleDescription;

    /**
     * 账号状态：0-正常，1-禁用
     */
    private Integer status;

    /**
     * 账号状态描述
     */
    private String statusText;

    /**
     * 注册时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 获取状态文本描述
     */
    public String getStatusText() {
        return status != null && status == 1 ? "已禁用" : "正常";
    }
}