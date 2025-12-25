package com.intomusic.musicplatform.modules.user.mapper;

import com.intomusic.musicplatform.modules.user.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户 (用于登录和检查用户名是否重复)
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据用户ID查询用户
     */
    User selectById(@Param("id") Long id);

    /**
     * 新增用户 (注册)
     */
    int insert(User user);

    /**
     * 更新用户信息
     */
    int update(User user);

    /**
     * 更新头像
     */
    int updateAvatar(@Param("id") Long id, @Param("avatar") String avatar);

    /**
     * 更新个人简介
     */
    int updateBio(@Param("id") Long id, @Param("bio") String bio);
}