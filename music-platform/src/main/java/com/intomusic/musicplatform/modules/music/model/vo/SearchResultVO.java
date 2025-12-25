package com.intomusic.musicplatform.modules.music.model.vo;

import lombok.Data;
import java.util.List;

/**
 * 综合搜索结果视图对象
 * 包含歌曲、歌手、专辑三类搜索结果
 */
@Data
public class SearchResultVO {

    /**
     * 匹配的歌曲列表
     */
    private List<SongVO> songs;

    /**
     * 从歌曲中提取的歌手列表（去重）
     */
    private List<ArtistVO> artists;

    /**
     * 从歌曲中提取的专辑列表（去重）
     */
    private List<AlbumVO> albums;

    /**
     * 歌曲总数
     */
    private Integer totalSongs;

    /**
     * 歌手总数（去重后）
     */
    private Integer totalArtists;

    /**
     * 专辑总数（去重后）
     */
    private Integer totalAlbums;
}