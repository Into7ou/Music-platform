package com.intomusic.musicplatform.modules.user.model.dto;

import lombok.Data;

/**
 * 用户信息更新请求DTO
 */
@Data
public class UserUpdateDTO {
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
}