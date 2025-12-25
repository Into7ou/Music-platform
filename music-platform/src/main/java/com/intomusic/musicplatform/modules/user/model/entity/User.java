package com.intomusic.musicplatform.modules.user.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data // 关键注解：自动生成 getters, setters, toString
public class User {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}