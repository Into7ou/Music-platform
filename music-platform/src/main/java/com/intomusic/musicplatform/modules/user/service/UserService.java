package com.intomusic.musicplatform.modules.user.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.intomusic.musicplatform.common.result.Result;
import com.intomusic.musicplatform.common.utils.MinioUtil;
import com.intomusic.musicplatform.common.utils.TokenUtil;
import com.intomusic.musicplatform.modules.playlists.mapper.PlaylistMapper;
import com.intomusic.musicplatform.modules.playlists.service.PlaylistService;
import com.intomusic.musicplatform.modules.user.mapper.UserMapper;
import com.intomusic.musicplatform.modules.user.model.dto.UserLoginDTO;
import com.intomusic.musicplatform.modules.user.model.dto.UserUpdateDTO;
import com.intomusic.musicplatform.modules.user.model.entity.User;
import com.intomusic.musicplatform.modules.user.model.vo.UserDetailVO;
import com.intomusic.musicplatform.modules.user.model.vo.UserLoginVO;
import com.intomusic.musicplatform.modules.user.model.vo.UserStatsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.Resource;


import java.util.UUID;

@Service
@DS("user") // 使用 user_db 数据库
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private PlaylistMapper playlistMapper;

    @Autowired
    private TokenUtil tokenUtil;

    @Resource
    private MinioUtil minioUtil;

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

        // 2. 校验昵称不能为空
        if (req.getNickname() == null || req.getNickname().trim().isEmpty()) {
            return Result.error("昵称不能为空");
        }

        // 3. 准备入库对象
        User user = new User();
        user.setUsername(req.getUsername());
        user.setNickname(req.getNickname());  // 使用前端传来的昵称
        user.setAvatar("/defaultAvatar.png"); // 设置默认头像

        // 4. 密码加密处理
        String hashedPassword = BCrypt.hashpw(req.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        // 5. 写入数据库
        userMapper.insert(user);

        // 6. 自动创建默认歌单 (i_like)
        try {
            playlistService.createDefaultPlaylist(user.getId());
        } catch (Exception e) {
            throw new RuntimeException("创建默认歌单失败: " + e.getMessage(), e);
        }

        return Result.success("注册成功");
    }

    /**
     * 用户登录
     */
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

        String token = tokenUtil.generateToken(user.getId());

        UserLoginVO vo = new UserLoginVO();
        vo.setToken(token);
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());

        return Result.success(vo);
    }


    /**
     * 上传头像到MinIO并更新用户头像
     * @param userId 用户ID
     * @param file 头像文件
     * @return 上传结果
     */
    public Result<String> uploadAvatar(Long userId, MultipartFile file) {
        try {
            // 1. 参数校验
            if (file == null || file.isEmpty()) {
                return Result.error("文件不能为空");
            }

            if (userId == null || userId <= 0) {
                return Result.error("用户ID不合法");
            }

            // 2. 校验文件类型
            String originalFilename = file.getOriginalFilename();
            String suffix = StrUtil.isNotBlank(originalFilename)
                    ? FileUtil.getSuffix(originalFilename)
                    : "jpg";

            // 只允许图片格式
            if (!suffix.matches("(?i)(jpg|jpeg|png|gif|webp)")) {
                return Result.error("只支持图片格式(jpg, jpeg, png, gif, webp)");
            }

            // 3. 设置Content-Type
            String contentType = file.getContentType();
            if (StrUtil.isBlank(contentType) || "application/octet-stream".equals(contentType)) {
                contentType = FileUtil.getMimeType(originalFilename);
            }
            if (contentType == null) {
                contentType = "image/jpeg";
            }

            // 4. 上传到MinIO的avatar目录
            String uniqueFileName = IdUtil.fastSimpleUUID() + "." + suffix;
            String objectName = "avatar/" + uniqueFileName;
            String avatarUrl = minioUtil.uploadFile(file, objectName, contentType);

            // 5. 更新数据库
            User user = userMapper.selectById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            user.setAvatar(avatarUrl);
            userMapper.update(user);

            return Result.success("头像更新成功");

        } catch (Exception e) {
            return Result.error("头像上传失败: " + e.getMessage());
        }
    }

    /**
     * 1. 获取用户详情
     */
    public Result<UserDetailVO> getUserDetail(Long userId) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        UserDetailVO vo = new UserDetailVO();
        BeanUtils.copyProperties(user, vo);

        return Result.success(vo);
    }

    /**
     * 2. 获取当前登录用户信息
     */
    public Result<UserDetailVO> getCurrentUserInfo(String token) {
        if (token == null || token.trim().isEmpty()) {
            return Result.error("Token不能为空");
        }

        // 从Token解析用户ID
        Long userId = tokenUtil.getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("Token无效或已过期");
        }

        return getUserDetail(userId);
    }

    /**
     * 3. 更新用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateUserInfo(Long userId, UserUpdateDTO dto) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        // 检查用户是否存在
        User existUser = userMapper.selectById(userId);
        if (existUser == null) {
            return Result.error("用户不存在");
        }

        // 构建更新对象
        User user = new User();
        user.setId(userId);
        user.setNickname(dto.getNickname());
        user.setAvatar(dto.getAvatar());
        user.setBio(dto.getBio());

        int rows = userMapper.update(user);
        if (rows > 0) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 4. 更新头像
     */
    public Result<String> updateAvatar(Long userId, String avatarUrl) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }
        if (avatarUrl == null || avatarUrl.trim().isEmpty()) {
            return Result.error("头像URL不能为空");
        }

        // 检查用户是否存在
        User existUser = userMapper.selectById(userId);
        if (existUser == null) {
            return Result.error("用户不存在");
        }

        int rows = userMapper.updateAvatar(userId, avatarUrl);
        if (rows > 0) {
            return Result.success("头像更新成功");
        }
        return Result.error("头像更新失败");
    }

    /**
     * 5. 更新个人简介
     */
    public Result<String> updateBio(Long userId, String bio) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        // 检查用户是否存在
        User existUser = userMapper.selectById(userId);
        if (existUser == null) {
            return Result.error("用户不存在");
        }

        // bio允许为空（清空简介）
        int rows = userMapper.updateBio(userId, bio);
        if (rows > 0) {
            return Result.success("个人简介更新成功");
        }
        return Result.error("个人简介更新失败");
    }

    /**
     * 6. 获取用户统计信息
     * 包括：创建的歌单数、喜欢的歌曲数
     */
    @DS("music") // 歌单数据在 music 数据库中
    public Result<UserStatsVO> getUserStats(Long userId) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        UserStatsVO statsVO = new UserStatsVO();
        statsVO.setUserId(userId);

        // 统计用户创建的歌单数（不含默认歌单）
        int playlistCount = playlistMapper.countUserPlaylists(userId);
        statsVO.setPlaylistCount(playlistCount);

        // 获取喜欢的歌曲数（i_like歌单中的song_count）
        Integer likedSongCount = playlistMapper.getLikedSongCount(userId);
        statsVO.setLikedSongCount(likedSongCount != null ? likedSongCount : 0);

        return Result.success(statsVO);
    }
}