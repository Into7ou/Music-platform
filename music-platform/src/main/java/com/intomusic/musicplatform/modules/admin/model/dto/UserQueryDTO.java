package com.intomusic.musicplatform.modules.admin.model.dto;

import lombok.Data;

/**
 * 用户查询请求 DTO（管理员使用）
 */
@Data
public class UserQueryDTO {

    /**
     * 用户名（模糊查询）
     */
    private String username;

    /**
     * 角色ID（精确筛选）
     */
    private Long roleId;

    /**
     * 账号状态（精确筛选）：0-正常，1-禁用
     */
    private Integer status;

    /**
     * 页码（默认第1页）
     */
    private Integer page = 1;

    /**
     * 每页大小（默认10条）
     */
    private Integer size = 10;

    /**
     * 计算 MySQL 分页偏移量
     * @return (page - 1) * size
     */
    public Integer getOffset() {
        return (page - 1) * size;
    }
}