package com.intomusic.musicplatform.modules.admin.model.dto;

import lombok.Data;

/**
 * 歌曲更新请求 DTO（管理员使用）
 */
@Data
public class SongUpdateDTO {

    /**
     * 歌曲ID（必填）
     */
    private Long id;

    /**
     * 歌曲标题
     */
    private String title;

    /**
     * 艺术家/歌手
     */
    private String artist;

    /**
     * 专辑名称
     */
    private String album;

    /**
     * 音乐类型/流派
     */
    private String genre;

    /**
     * 封面URL
     */
    private String coverUrl;
}