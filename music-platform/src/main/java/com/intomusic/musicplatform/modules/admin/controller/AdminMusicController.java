package com.intomusic.musicplatform.modules.admin.controller;

import com.intomusic.musicplatform.common.result.Result;
import com.intomusic.musicplatform.modules.admin.model.dto.SongUpdateDTO;
import com.intomusic.musicplatform.modules.admin.service.AdminMusicService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员音乐管理控制器
 */
@RestController
@RequestMapping("/admin/music")
@CrossOrigin(origins = "*") // 允许前端跨域调用
public class AdminMusicController {

    @Resource
    private AdminMusicService adminMusicService;

    /**
     * 编辑歌曲信息
     *
     * @param dto 歌曲更新数据
     * @return 操作结果
     */
    @PutMapping("/update")
    public Result<String> updateSong(@RequestBody SongUpdateDTO dto) {
        return adminMusicService.updateSong(dto);
    }

    /**
     * 删除歌曲
     *
     * @param songId 歌曲ID
     * @return 操作结果
     */
    @DeleteMapping("/{songId}")
    public Result<String> deleteSong(@PathVariable Long songId) {
        return adminMusicService.deleteSong(songId);
    }

    /**
     * 批量删除歌曲
     *
     * @param songIds 歌曲ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public Result<String> batchDeleteSongs(@RequestBody List<Long> songIds) {
        return adminMusicService.batchDeleteSongs(songIds);
    }
}