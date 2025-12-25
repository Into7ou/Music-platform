package com.intomusic.musicplatform.modules.playlists.model.dto;

import lombok.Data;

/**
 * 创建歌单请求 DTO
 */
@Data
public class PlaylistCreateReq {
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
     * 是否公开: 0=私密, 1=公开
     */
    private Integer isPublic = 0;
}