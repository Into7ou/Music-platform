package com.intomusic.musicplatform.modules.admin.controller;

import com.intomusic.musicplatform.common.result.PageResult;
import com.intomusic.musicplatform.common.result.Result;
import com.intomusic.musicplatform.modules.admin.model.dto.UserQueryDTO;
import com.intomusic.musicplatform.modules.admin.model.vo.AdminUserVO;
import com.intomusic.musicplatform.modules.admin.model.vo.RoleVO;
import com.intomusic.musicplatform.modules.admin.model.vo.UserListVO;
import com.intomusic.musicplatform.modules.admin.service.AdminUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员用户管理控制器
 */
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @Resource
    private AdminUserService adminUserService;

    /**
     * 获取用户列表（分页）
     *
     * @param dto 查询参数
     * @return 用户列表分页结果
     */
    @GetMapping("/list")
    public Result<PageResult<UserListVO>> getUserList(UserQueryDTO dto) {
        return adminUserService.getUserList(dto);
    }

    /**
     * 获取用户详情（含角色信息）
     *
     * @param userId 用户ID
     * @return 用户详情
     */
    @GetMapping("/detail/{userId}")
    public Result<AdminUserVO> getUserDetail(@PathVariable Long userId) {
        return adminUserService.getUserDetail(userId);
    }

    /**
     * 禁用用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PutMapping("/disable/{userId}")
    public Result<String> disableUser(@PathVariable Long userId) {
        return adminUserService.disableUser(userId);
    }

    /**
     * 启用用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PutMapping("/enable/{userId}")
    public Result<String> enableUser(@PathVariable Long userId) {
        return adminUserService.enableUser(userId);
    }

    /**
     * 修改用户角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 操作结果
     */
    @PutMapping("/role/{userId}")
    public Result<String> updateUserRole(@PathVariable Long userId,
                                         @RequestParam Long roleId) {
        return adminUserService.updateUserRole(userId, roleId);
    }

    /**
     * 获取所有角色列表
     *
     * @return 角色列表
     */
    @GetMapping("/roles")
    public Result<List<RoleVO>> getAllRoles() {
        return adminUserService.getAllRoles();
    }
}