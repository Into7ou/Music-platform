package com.intomusic.musicplatform.modules.music.model.vo;

import lombok.Data;

/**
 * 歌手视图对象
 * 用于搜索结果中的歌手信息展示
 */
@Data
public class ArtistVO {

    /**
     * 歌手名称
     */
    private String name;

    /**
     * 该歌手的歌曲数量（在搜索结果中）
     */
    private Integer songCount;

    /**
     * 头像 URL（可选，可从第一首歌曲的封面获取）
     */
    private String avatar;
}