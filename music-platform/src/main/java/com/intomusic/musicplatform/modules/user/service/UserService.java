package com.intomusic.musicplatform.modules.user.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.intomusic.musicplatform.common.result.Result;
import com.intomusic.musicplatform.common.utils.TokenUtil;
import com.intomusic.musicplatform.modules.playlists.service.PlaylistService;
import com.intomusic.musicplatform.modules.user.mapper.UserMapper;
import com.intomusic.musicplatform.modules.user.model.dto.UserLoginDTO;
import com.intomusic.musicplatform.modules.user.model.entity.User;
import com.intomusic.musicplatform.modules.user.model.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

@Service
@DS("user") // 使用 user_db 数据库
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PlaylistService playlistService;

    /**
     * 用户注册
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> register(UserLoginDTO req) {
        // 1. 检查用户名是否已存在
        User existUser = userMapper.selectByUsername(req.getUsername());
        if (existUser != null) {
            return Result.error("用户名已存在");
        }

        // 2. 准备入库对象
        User user = new User();
        user.setUsername(req.getUsername());
        user.setNickname("用户" + System.currentTimeMillis() % 1000); // 默认随机昵称

        // --- 密码处理逻辑 (当前：明文) ---
        // TODO: 后期只需在这里改为: String encoded = BCrypt.hashpw(req.getPassword(), ...);
        String hashedPassword = BCrypt.hashpw(req.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        // -----------------------------

        // 3. 写入数据库
        userMapper.insert(user);

        // 4. 自动创建默认歌单 (i_like)
        try {
            playlistService.createDefaultPlaylist(user.getId());
        } catch (Exception e) {
            // 如果创建默认歌单失败，记录日志但不影响注册流程
            // 可以选择抛出异常回滚整个注册，或者只记录日志继续
            throw new RuntimeException("创建默认歌单失败: " + e.getMessage(), e);
        }

        return Result.success("注册成功");
    }

    /**
     * 用户登录
     */
    @Autowired
    private TokenUtil tokenUtil;
    public Result<UserLoginVO> login(UserLoginDTO req) {

        // 手动校验
        if (req.getUsername() == null || req.getUsername().trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (req.getPassword() == null || req.getPassword().trim().isEmpty()) {
            return Result.error("密码不能为空");
        }

        // 1. 查用户
        User user = userMapper.selectByUsername(req.getUsername());
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 2. 比对密码
        // --- 密码比对逻辑 (当前：明文比对) ---
        // TODO: 后期只需在这里改为: if (!BCrypt.checkpw(req.getPassword(), user.getPassword())) ...
        if (!BCrypt.checkpw(req.getPassword(), user.getPassword())) {
            return Result.error("密码错误");
        }
        // ---------------------------------

        // 3. 生成 Token (这里先生成一个假的 UUID Token，跑通前后端)
        String token = tokenUtil.generateToken(user.getId());

        UserLoginVO vo = new UserLoginVO();
        vo.setToken(token);
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());

        return Result.success(vo);
    }
}