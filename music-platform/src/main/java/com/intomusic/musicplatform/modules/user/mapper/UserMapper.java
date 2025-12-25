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
     * 新增用户 (注册)
     */
    int insert(User user);
}