package com.intomusic.musicplatform.modules.user.controller;

import com.intomusic.musicplatform.common.result.Result;
import com.intomusic.musicplatform.modules.user.model.dto.UserLoginDTO;
import com.intomusic.musicplatform.modules.user.model.vo.UserLoginVO;
import com.intomusic.musicplatform.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") // 允许前端跨域调用
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<String> register(@RequestBody @Validated UserLoginDTO req) {
        return userService.register(req);
    }

    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody @Validated UserLoginDTO req) {
        return userService.login(req);
    }
}