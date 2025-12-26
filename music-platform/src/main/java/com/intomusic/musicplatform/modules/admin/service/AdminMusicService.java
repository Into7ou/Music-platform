package com.intomusic.musicplatform.modules.admin.service;

import com.intomusic.musicplatform.common.result.Result;
import com.intomusic.musicplatform.modules.admin.model.dto.SongUpdateDTO;

import java.util.List;

/**
 * 管理员音乐管理服务接口
 */
public interface AdminMusicService {

    /**
     * 编辑歌曲信息
     *
     * @param dto 歌曲更新数据
     * @return 操作结果
     */
    Result<String> updateSong(SongUpdateDTO dto);

    /**
     * 删除歌曲
     *
     * @param songId 歌曲ID
     * @return 操作结果
     */
    Result<String> deleteSong(Long songId);

    /**
     * 批量删除歌曲
     *
     * @param songIds 歌曲ID列表
     * @return 操作结果
     */
    Result<String> batchDeleteSongs(List<Long> songIds);
}