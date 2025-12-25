package com.intomusic.musicplatform.modules.playlists.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlaylistSong {
    /**
     * 关联ID
     */
    private Long id;

    /**
     * 歌单ID
     */
    private Long playlistId;

    /**
     * 歌曲ID
     */
    private Long songId;

    /**
     * 添加时间
     */
    private LocalDateTime addTime;

    /**
     * 排序顺序
     */
    private Integer sortOrder;
}