package com.intomusic.musicplatform.modules.admin.mapper;

import com.intomusic.musicplatform.modules.admin.model.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper接口
 */
@Mapper
public interface RoleMapper {

    /**
     * 根据ID查询角色
     */
    Role selectById(@Param("id") Long id);

    /**
     * 根据角色编码查询角色
     */
    Role selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 查询所有角色
     */
    List<Role> selectAll();
}