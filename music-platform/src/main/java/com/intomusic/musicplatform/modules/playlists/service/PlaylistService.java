package com.intomusic.musicplatform.modules.playlists.service;

import com.intomusic.musicplatform.common.result.PageResult;
import com.intomusic.musicplatform.common.result.Result;
import com.intomusic.musicplatform.modules.playlists.model.dto.PlaylistCreateReq;
import com.intomusic.musicplatform.modules.playlists.model.dto.PlaylistQueryReq;
import com.intomusic.musicplatform.modules.playlists.model.dto.PlaylistUpdateReq;
import com.intomusic.musicplatform.modules.playlists.model.vo.PlaylistVO;
import com.intomusic.musicplatform.modules.music.model.vo.SongVO;

import java.util.List;

/**
 * 歌单服务接口
 */
public interface PlaylistService {

    /**
     * 创建默认歌单 (i_like)
     * 用于用户注册时自动创建
     *
     * @param userId 用户ID
     */
    void createDefaultPlaylist(Long userId);

    /**
     * 创建自定义歌单
     *
     * @param req    创建请求
     * @param userId 用户ID
     * @return 创建结果
     */
    Result<String> createPlaylist(PlaylistCreateReq req, Long userId);

    /**
     * 获取歌单详情
     *
     * @param playlistId 歌单ID
     * @return 歌单详情
     */
    Result<PlaylistVO> getDetail(Long playlistId);

    /**
     * 获取用户的所有歌单
     *
     * @param userId 用户ID
     * @return 歌单列表
     */
    Result<List<PlaylistVO>> getMyPlaylists(Long userId);

    /**
     * 获取用户的默认歌单 (i_like)
     *
     * @param userId 用户ID
     * @return 默认歌单
     */
    Result<PlaylistVO> getDefaultPlaylist(Long userId);

    /**
     * 获取公开歌单列表 (分页)
     *
     * @param req 分页请求
     * @return 公开歌单列表
     */
    Result<PageResult<PlaylistVO>> getPublicPlaylists(PlaylistQueryReq req);

    /**
     * 更新歌单信息
     *
     * @param req    更新请求
     * @param userId 用户ID
     * @return 更新结果
     */
    Result<String> updatePlaylist(PlaylistUpdateReq req, Long userId);

    /**
     * 删除歌单
     *
     * @param playlistId 歌单ID
     * @param userId     用户ID
     * @return 删除结果
     */
    Result<String> deletePlaylist(Long playlistId, Long userId);

    /**
     * 添加歌曲到歌单
     *
     * @param playlistId 歌单ID
     * @param songId     歌曲ID
     * @param userId     用户ID
     * @return 添加结果
     */
    Result<String> addSongToPlaylist(Long playlistId, Long songId, Long userId);

    /**
     * 从歌单移除歌曲
     *
     * @param playlistId 歌单ID
     * @param songId     歌曲ID
     * @param userId     用户ID
     * @return 移除结果
     */
    Result<String> removeSongFromPlaylist(Long playlistId, Long songId, Long userId);

    /**
     * 获取歌单的所有歌曲
     *
     * @param playlistId 歌单ID
     * @return 歌曲列表
     */
    Result<List<SongVO>> getPlaylistSongs(Long playlistId);
}