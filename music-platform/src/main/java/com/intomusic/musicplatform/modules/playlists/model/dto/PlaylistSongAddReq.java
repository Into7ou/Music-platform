package com.intomusic.musicplatform.modules.playlists.model.dto;

import lombok.Data;

/**
 * 添加歌曲到歌单请求 DTO
 */
@Data
public class PlaylistSongAddReq {
    /**
     * 歌单ID
     */
    private Long playlistId;

    /**
     * 歌曲ID
     */
    private Long songId;
}