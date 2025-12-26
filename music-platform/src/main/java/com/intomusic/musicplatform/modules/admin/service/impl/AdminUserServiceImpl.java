package com.intomusic.musicplatform.modules.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.intomusic.musicplatform.common.result.PageResult;
import com.intomusic.musicplatform.common.result.Result;
import com.intomusic.musicplatform.modules.admin.mapper.RoleMapper;
import com.intomusic.musicplatform.modules.admin.model.dto.UserQueryDTO;
import com.intomusic.musicplatform.modules.admin.model.entity.Role;
import com.intomusic.musicplatform.modules.admin.model.vo.AdminUserVO;
import com.intomusic.musicplatform.modules.admin.model.vo.RoleVO;
import com.intomusic.musicplatform.modules.admin.model.vo.UserListVO;
import com.intomusic.musicplatform.modules.admin.service.AdminUserService;
import com.intomusic.musicplatform.modules.user.mapper.UserMapper;
import com.intomusic.musicplatform.modules.user.model.entity.User;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员用户管理服务实现类
 */
@Service
@DS("user_db") // 使用 user_db 数据源
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    /**
     * 获取用户列表（分页）
     */
    @Override
    public Result<PageResult<UserListVO>> getUserList(UserQueryDTO dto) {
        // 1. 查询总数
        Long total = userMapper.countAllUsers(
                dto.getUsername(),
                dto.getRoleId(),
                dto.getStatus()
        );

        // 2. 如果总数为0，直接返回空结果
        if (total == 0) {
            return Result.success(PageResult.empty());
        }

        // 3. 查询当前页数据
        List<User> users = userMapper.selectAllUsers(
                dto.getOffset(),
                dto.getSize(),
                dto.getUsername(),
                dto.getRoleId(),
                dto.getStatus()
        );

        // 4. Entity -> VO 转换，并关联角色信息
        List<UserListVO> voList = users.stream()
                .map(user -> {
                    UserListVO vo = new UserListVO();
                    BeanUtil.copyProperties(user, vo);

                    // 关联角色信息
                    if (user.getRoleId() != null) {
                        Role role = roleMapper.selectById(user.getRoleId());
                        if (role != null) {
                            vo.setRoleName(role.getRoleName());
                            vo.setRoleCode(role.getRoleCode());
                        }
                    }

                    return vo;
                })
                .collect(Collectors.toList());

        // 5. 返回分页结果
        PageResult<UserListVO> pageResult = new PageResult<>(total, voList);
        return Result.success(pageResult);
    }

    /**
     * 获取用户详情（含角色信息）
     */
    @Override
    public Result<AdminUserVO> getUserDetail(Long userId) {
        if (userId == null || userId <= 0) {
            return Result.error("用户ID不合法");
        }

        // 1. 查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 2. Entity -> VO 转换
        AdminUserVO vo = new AdminUserVO();
        BeanUtil.copyProperties(user, vo);

        // 3. 关联角色信息
        if (user.getRoleId() != null) {
            Role role = roleMapper.selectById(user.getRoleId());
            if (role != null) {
                vo.setRoleName(role.getRoleName());
                vo.setRoleCode(role.getRoleCode());
                vo.setRoleDescription(role.getDescription());
            }
        }

        return Result.success(vo);
    }

    /**
     * 禁用用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> disableUser(Long userId) {
        if (userId == null || userId <= 0) {
            return Result.error("用户ID不合法");
        }

        // 检查用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 检查是否已禁用
        if (user.getStatus() != null && user.getStatus() == 1) {
            return Result.error("用户已被禁用");
        }

        // 更新状态为禁用（status = 1）
        int rows = userMapper.updateUserStatus(userId, 1);
        if (rows > 0) {
            return Result.success("禁用成功");
        } else {
            return Result.error("禁用失败");
        }
    }

    /**
     * 启用用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> enableUser(Long userId) {
        if (userId == null || userId <= 0) {
            return Result.error("用户ID不合法");
        }

        // 检查用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 检查是否已启用
        if (user.getStatus() != null && user.getStatus() == 0) {
            return Result.error("用户已是启用状态");
        }

        // 更新状态为启用（status = 0）
        int rows = userMapper.updateUserStatus(userId, 0);
        if (rows > 0) {
            return Result.success("启用成功");
        } else {
            return Result.error("启用失败");
        }
    }

    /**
     * 修改用户角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateUserRole(Long userId, Long roleId) {
        if (userId == null || userId <= 0) {
            return Result.error("用户ID不合法");
        }
        if (roleId == null || roleId <= 0) {
            return Result.error("角色ID不合法");
        }

        // 检查用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 检查角色是否存在
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            return Result.error("角色不存在");
        }

        // 更新用户角色
        int rows = userMapper.updateUserRole(userId, roleId);
        if (rows > 0) {
            return Result.success("角色修改成功");
        } else {
            return Result.error("角色修改失败");
        }
    }

    /**
     * 获取所有角色列表
     */
    @Override
    public Result<List<RoleVO>> getAllRoles() {
        List<Role> roles = roleMapper.selectAll();

        // Entity -> VO 转换
        List<RoleVO> voList = BeanUtil.copyToList(roles, RoleVO.class);

        return Result.success(voList);
    }
}