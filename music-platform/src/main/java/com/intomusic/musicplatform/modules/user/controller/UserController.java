package com.intomusic.musicplatform.modules.user.controller;

import com.intomusic.musicplatform.common.result.Result;
import com.intomusic.musicplatform.modules.user.model.dto.UserUpdateDTO;
import com.intomusic.musicplatform.modules.user.model.vo.UserDetailVO;
import com.intomusic.musicplatform.modules.user.model.vo.UserStatsVO;
import com.intomusic.musicplatform.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户详情
     */
    @GetMapping("/detail/{userId}")
    public Result<UserDetailVO> getUserDetail(@PathVariable Long userId) {
        return userService.getUserDetail(userId);
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/me")
    public Result<UserDetailVO> getCurrentUserInfo(@RequestHeader("Authorization") String token) {
        // 移除 "Bearer " 前缀（如果有）
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return userService.getCurrentUserInfo(token);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update/{userId}")
    public Result<String> updateUserInfo(@PathVariable Long userId,
                                         @RequestBody UserUpdateDTO dto) {
        return userService.updateUserInfo(userId, dto);
    }

    /**
     * 上传并更新头像
     */
    @PostMapping("/avatar/{userId}")
    public Result<String> uploadAvatar(@PathVariable Long userId,
                                       @RequestParam("file") MultipartFile file) {
        return userService.uploadAvatar(userId, file);
    }

    /**
     * 更新个人简介
     */
    @PutMapping("/bio/{userId}")
    public Result<String> updateBio(@PathVariable Long userId,
                                    @RequestBody(required = false) String bio) {
        return userService.updateBio(userId, bio);
    }

    /**
     * 获取用户统计信息
     */
    @GetMapping("/stats/{userId}")
    public Result<UserStatsVO> getUserStats(@PathVariable Long userId) {
        return userService.getUserStats(userId);
    }
}