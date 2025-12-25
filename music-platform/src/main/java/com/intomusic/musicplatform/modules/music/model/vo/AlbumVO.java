package com.intomusic.musicplatform.modules.music.model.vo;

import lombok.Data;

/**
 * 专辑视图对象
 * 用于搜索结果中的专辑信息展示
 */
@Data
public class AlbumVO {

    /**
     * 专辑名称
     */
    private String name;

    /**
     * 所属歌手
     */
    private String artist;

    /**
     * 该专辑的歌曲数量（在搜索结果中）
     */
    private Integer songCount;

    /**
     * 专辑封面 URL（使用专辑内第一首歌曲的封面）
     */
    private String coverUrl;
}