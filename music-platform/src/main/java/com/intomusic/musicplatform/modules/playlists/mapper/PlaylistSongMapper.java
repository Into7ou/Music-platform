package com.intomusic.musicplatform.modules.playlists.mapper;

import com.intomusic.musicplatform.modules.playlists.model.entity.PlaylistSong;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlaylistSongMapper {

    /**
     * 添加歌曲到歌单
     */
    int insert(PlaylistSong playlistSong);

    /**
     * 从歌单移除歌曲
     */
    int delete(@Param("playlistId") Long playlistId, @Param("songId") Long songId);

    /**
     * 查询歌单的所有歌曲ID
     */
    List<Long> selectSongIdsByPlaylistId(@Param("playlistId") Long playlistId);

    /**
     * 检查歌曲是否在歌单中
     */
    boolean existsBySongId(@Param("playlistId") Long playlistId, @Param("songId") Long songId);

    /**
     * 统计歌单歌曲数量
     */
    int countByPlaylistId(@Param("playlistId") Long playlistId);

    /**
     * 删除歌单的所有歌曲关联
     */
    int deleteByPlaylistId(@Param("playlistId") Long playlistId);
}