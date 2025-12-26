package com.intomusic.musicplatform.modules.admin.service;

import com.intomusic.musicplatform.common.result.PageResult;
import com.intomusic.musicplatform.common.result.Result;
import com.intomusic.musicplatform.modules.admin.model.dto.UserQueryDTO;
import com.intomusic.musicplatform.modules.admin.model.vo.AdminUserVO;
import com.intomusic.musicplatform.modules.admin.model.vo.UserListVO;
import com.intomusic.musicplatform.modules.admin.model.vo.RoleVO;

import java.util.List;

/**
 * 管理员用户管理服务接口
 */
public interface AdminUserService {

    /**
     * 获取用户列表（分页）
     *
     * @param dto 查询参数
     * @return 用户列表分页结果
     */
    Result<PageResult<UserListVO>> getUserList(UserQueryDTO dto);

    /**
     * 获取用户详情（含角色信息）
     *
     * @param userId 用户ID
     * @return 用户详情
     */
    Result<AdminUserVO> getUserDetail(Long userId);

    /**
     * 禁用用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<String> disableUser(Long userId);

    /**
     * 启用用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<String> enableUser(Long userId);

    /**
     * 修改用户角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 操作结果
     */
    Result<String> updateUserRole(Long userId, Long roleId);

    /**
     * 获取所有角色列表
     *
     * @return 角色列表
     */
    Result<List<RoleVO>> getAllRoles();
}