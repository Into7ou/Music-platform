package com.intomusic.musicplatform.modules.admin.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.intomusic.musicplatform.common.result.Result;
import com.intomusic.musicplatform.modules.admin.model.dto.SongUpdateDTO;
import com.intomusic.musicplatform.modules.admin.service.AdminMusicService;
import com.intomusic.musicplatform.modules.music.mapper.SongMapper;
import com.intomusic.musicplatform.modules.music.model.entity.Song;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 管理员音乐管理服务实现类
 */
@Service
@DS("music_db") // 使用 music_db 数据源
public class AdminMusicServiceImpl implements AdminMusicService {

    @Resource
    private SongMapper songMapper;

    /**
     * 编辑歌曲信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateSong(SongUpdateDTO dto) {
        // 参数校验
        if (dto.getId() == null || dto.getId() <= 0) {
            return Result.error("歌曲ID不合法");
        }

        // 检查歌曲是否存在
        Song existSong = songMapper.selectById(dto.getId());
        if (existSong == null) {
            return Result.error("歌曲不存在");
        }

        // 构建更新对象
        Song song = new Song();
        song.setId(dto.getId());
        song.setTitle(dto.getTitle());
        song.setArtist(dto.getArtist());
        song.setAlbum(dto.getAlbum());
        song.setGenre(dto.getGenre());
        song.setCoverUrl(dto.getCoverUrl());

        // 执行更新
        int rows = songMapper.update(song);
        if (rows > 0) {
            return Result.success("歌曲信息更新成功");
        } else {
            return Result.error("歌曲信息更新失败");
        }
    }

    /**
     * 删除歌曲
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteSong(Long songId) {
        if (songId == null || songId <= 0) {
            return Result.error("歌曲ID不合法");
        }

        // 检查歌曲是否存在
        Song song = songMapper.selectById(songId);
        if (song == null) {
            return Result.error("歌曲不存在");
        }

        // 执行删除
        int rows = songMapper.deleteById(songId);
        if (rows > 0) {
            return Result.success("歌曲删除成功");
        } else {
            return Result.error("歌曲删除失败");
        }
    }

    /**
     * 批量删除歌曲
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> batchDeleteSongs(List<Long> songIds) {
        // 参数校验
        if (songIds == null || songIds.isEmpty()) {
            return Result.error("歌曲ID列表不能为空");
        }

        // 执行批量删除
        int rows = songMapper.batchDelete(songIds);
        if (rows > 0) {
            return Result.success("批量删除成功，共删除 " + rows + " 首歌曲");
        } else {
            return Result.error("批量删除失败");
        }
    }
}