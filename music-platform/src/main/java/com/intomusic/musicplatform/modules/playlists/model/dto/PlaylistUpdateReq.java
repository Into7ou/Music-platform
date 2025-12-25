package com.intomusic.musicplatform.modules.playlists.model.dto;

import lombok.Data;

/**
 * 更新歌单请求 DTO
 */
@Data
public class PlaylistUpdateReq {
    /**
     * 歌单ID
     */
    private Long id;

    /**
     * 歌单名称
     */
    private String name;

    /**
     * 歌单描述
     */
    private String description;

    /**
     * 封面URL
     */
    private String coverUrl;

    /**
     * 是否公开
     */
    private Integer isPublic;
}