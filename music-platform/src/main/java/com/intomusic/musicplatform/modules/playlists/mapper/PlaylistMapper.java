package com.intomusic.musicplatform.modules.playlists.mapper;

import com.intomusic.musicplatform.modules.playlists.model.entity.Playlist;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlaylistMapper {

    /**
     * 插入歌单
     */
    int insert(Playlist playlist);

    /**
     * 根据 ID 查询歌单
     */
    Playlist selectById(@Param("id") Long id);

    /**
     * 查询用户的所有歌单
     */
    List<Playlist> selectByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的默认歌单 (i_like)
     */
    Playlist selectDefaultByUserId(@Param("userId") Long userId);

    /**
     * 查询公开歌单列表 (分页)
     */
    List<Playlist> selectPublicList(@Param("offset") Integer offset, @Param("size") Integer size);

    /**
     * 统计公开歌单总数
     */
    long countPublicList();

    /**
     * 更新歌单信息
     */
    int update(Playlist playlist);

    /**
     * 删除歌单（只能删除非默认歌单）
     */
    int deleteById(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 增加播放次数
     */
    int incrementPlayCount(@Param("id") Long id);

    /**
     * 统计用户创建的歌单数（不含默认歌单）
     */
    int countUserPlaylists(@Param("userId") Long userId);

    /**
     * 获取用户喜欢的歌曲数（i_like歌单中的song_count）
     */
    Integer getLikedSongCount(@Param("userId") Long userId);
}