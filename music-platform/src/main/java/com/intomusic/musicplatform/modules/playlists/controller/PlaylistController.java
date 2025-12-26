package com.intomusic.musicplatform.modules.playlists.controller;

import com.intomusic.musicplatform.common.result.PageResult;
import com.intomusic.musicplatform.common.result.Result;
import com.intomusic.musicplatform.common.utils.TokenUtil;
import com.intomusic.musicplatform.modules.playlists.model.dto.PlaylistCreateReq;
import com.intomusic.musicplatform.modules.playlists.model.dto.PlaylistQueryReq;
import com.intomusic.musicplatform.modules.playlists.model.dto.PlaylistUpdateReq;
import com.intomusic.musicplatform.modules.playlists.model.vo.PlaylistVO;
import com.intomusic.musicplatform.modules.playlists.service.PlaylistService;
import com.intomusic.musicplatform.modules.music.model.vo.SongVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.intomusic.musicplatform.common.utils.MinioUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.IdUtil;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    @Resource
    private PlaylistService playlistService;

    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private MinioUtil minioUtil;

    /**
     * 从请求头获取当前用户ID
     */
    private Long getCurrentUserId(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new RuntimeException("未登录，请先登录");
        }

        Long userId = tokenUtil.getUserIdFromToken(token);
        if (userId == null) {
            throw new RuntimeException("登录已过期，请重新登录");
        }

        return userId;
    }

    /**
     * 创建歌单
     */
    @PostMapping("/create")
    public Result<String> createPlaylist(@RequestBody PlaylistCreateReq req,
                                         @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        return playlistService.createPlaylist(req, userId);
    }

    /**
     * 获取歌单详情
     */
    @GetMapping("/{id}")
    public Result<PlaylistVO> getDetail(@PathVariable("id") Long id) {
        return playlistService.getDetail(id);
    }

    /**
     * 获取我的歌单列表
     */
    @GetMapping("/my")
    public Result<List<PlaylistVO>> getMyPlaylists(@RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        return playlistService.getMyPlaylists(userId);
    }

    /**
     * 获取我的默认歌单 (i_like)
     */
    @GetMapping("/my/default")
    public Result<PlaylistVO> getDefaultPlaylist(@RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        return playlistService.getDefaultPlaylist(userId);
    }

    /**
     * 获取公开歌单列表 (分页)
     */
    @GetMapping("/public")
    public Result<PageResult<PlaylistVO>> getPublicPlaylists(PlaylistQueryReq req) {
        return playlistService.getPublicPlaylists(req);
    }

    /**
     * 更新歌单信息
     */
    @PutMapping("/{id}")
    public Result<String> updatePlaylist(@PathVariable("id") Long id,
                                         @RequestBody PlaylistUpdateReq req,
                                         @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        req.setId(id);
        return playlistService.updatePlaylist(req, userId);
    }

    /**
     * 删除歌单
     */
    @DeleteMapping("/{id}")
    public Result<String> deletePlaylist(@PathVariable("id") Long id,
                                         @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        return playlistService.deletePlaylist(id, userId);
    }

    /**
     * 添加歌曲到歌单
     */
    @PostMapping("/{id}/songs/{songId}")
    public Result<String> addSong(@PathVariable("id") Long playlistId,
                                  @PathVariable("songId") Long songId,
                                  @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        return playlistService.addSongToPlaylist(playlistId, songId, userId);
    }

    /**
     * 从歌单移除歌曲
     */
    @DeleteMapping("/{id}/songs/{songId}")
    public Result<String> removeSong(@PathVariable("id") Long playlistId,
                                     @PathVariable("songId") Long songId,
                                     @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        return playlistService.removeSongFromPlaylist(playlistId, songId, userId);
    }

    /**
     * 获取歌单的所有歌曲
     */
    @GetMapping("/{id}/songs")
    public Result<List<SongVO>> getPlaylistSongs(@PathVariable("id") Long playlistId) {
        return playlistService.getPlaylistSongs(playlistId);
    }

    /**
     * 上传歌单封面
     */
    @PostMapping("/upload-cover")
    public Result<Map<String, String>> uploadCover(@RequestParam("cover") MultipartFile cover) {
        try {
            // 1. 参数校验
            if (cover == null || cover.isEmpty()) {
                return Result.error("封面文件不能为空");
            }

            // 2. 获取文件信息
            String originalFilename = cover.getOriginalFilename();
            String suffix = StrUtil.isNotBlank(originalFilename)
                    ? FileUtil.getSuffix(originalFilename)
                    : "jpg";

            String contentType = cover.getContentType();
            if (StrUtil.isBlank(contentType) || "application/octet-stream".equals(contentType)) {
                contentType = FileUtil.getMimeType(originalFilename);
            }
            if (contentType == null) {
                contentType = "image/jpeg";
            }

            // 3. 生成唯一文件名并上传到 MinIO
            String uniqueFileName = IdUtil.fastSimpleUUID() + "." + suffix;
            String coverUrl = minioUtil.uploadFile(cover, "playlist-covers/" + uniqueFileName, contentType);

            // 4. 返回封面 URL
            Map<String, String> result = new HashMap<>();
            result.put("url", coverUrl);

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("上传封面失败: " + e.getMessage());
        }
    }
}