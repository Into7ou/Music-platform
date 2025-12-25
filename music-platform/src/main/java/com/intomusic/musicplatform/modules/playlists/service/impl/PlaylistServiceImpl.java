package com.intomusic.musicplatform.modules.playlists.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.intomusic.musicplatform.common.result.PageResult;
import com.intomusic.musicplatform.common.result.Result;
import com.intomusic.musicplatform.modules.playlists.mapper.PlaylistMapper;
import com.intomusic.musicplatform.modules.playlists.mapper.PlaylistSongMapper;
import com.intomusic.musicplatform.modules.playlists.model.dto.PlaylistCreateReq;
import com.intomusic.musicplatform.modules.playlists.model.dto.PlaylistQueryReq;
import com.intomusic.musicplatform.modules.playlists.model.dto.PlaylistUpdateReq;
import com.intomusic.musicplatform.modules.playlists.model.entity.Playlist;
import com.intomusic.musicplatform.modules.playlists.model.entity.PlaylistSong;
import com.intomusic.musicplatform.modules.playlists.model.vo.PlaylistVO;
import com.intomusic.musicplatform.modules.playlists.service.PlaylistService;
import com.intomusic.musicplatform.modules.music.mapper.SongMapper;
import com.intomusic.musicplatform.modules.music.model.entity.Song;
import com.intomusic.musicplatform.modules.music.model.vo.SongVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 歌单服务实现类
 */
@Service
@DS("music_db") // 使用 music_db 数据源
public class PlaylistServiceImpl implements PlaylistService {

    @Resource
    private PlaylistMapper playlistMapper;

    @Resource
    private PlaylistSongMapper playlistSongMapper;

    @Resource
    private SongMapper songMapper;

    /**
     * 创建默认歌单 (i_like)
     * 用于用户注册时自动创建
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createDefaultPlaylist(Long userId) {
        Playlist playlist = new Playlist();
        playlist.setUserId(userId);
        playlist.setName("我喜欢的音乐");
        playlist.setDescription("默认收藏歌单");
        playlist.setIsPublic(0);
        playlist.setIsDefault(1);
        playlist.setPlaylistType("i_like");

        playlistMapper.insert(playlist);
    }

    /**
     * 创建自定义歌单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> createPlaylist(PlaylistCreateReq req, Long userId) {
        // 参数校验
        if (req.getName() == null || req.getName().trim().isEmpty()) {
            return Result.error("歌单名称不能为空");
        }

        Playlist playlist = new Playlist();
        playlist.setUserId(userId);
        playlist.setName(req.getName());
        playlist.setDescription(req.getDescription());
        playlist.setCoverUrl(req.getCoverUrl());
        playlist.setIsPublic(req.getIsPublic() != null ? req.getIsPublic() : 0);
        playlist.setIsDefault(0);
        playlist.setPlaylistType("custom");

        playlistMapper.insert(playlist);

        return Result.success("创建成功");
    }

    /**
     * 获取歌单详情
     */
    @Override
    public Result<PlaylistVO> getDetail(Long playlistId) {
        if (playlistId == null || playlistId <= 0) {
            return Result.error("歌单ID不合法");
        }

        Playlist playlist = playlistMapper.selectById(playlistId);
        if (playlist == null) {
            return Result.error("歌单不存在");
        }

        PlaylistVO vo = new PlaylistVO();
        BeanUtil.copyProperties(playlist, vo);

        return Result.success(vo);
    }

    /**
     * 获取用户的所有歌单
     */
    @Override
    public Result<List<PlaylistVO>> getMyPlaylists(Long userId) {
        List<Playlist> playlists = playlistMapper.selectByUserId(userId);

        List<PlaylistVO> voList = BeanUtil.copyToList(playlists, PlaylistVO.class);

        return Result.success(voList);
    }

    /**
     * 获取用户的默认歌单 (i_like)
     */
    @Override
    public Result<PlaylistVO> getDefaultPlaylist(Long userId) {
        Playlist playlist = playlistMapper.selectDefaultByUserId(userId);
        if (playlist == null) {
            return Result.error("默认歌单不存在");
        }

        PlaylistVO vo = new PlaylistVO();
        BeanUtil.copyProperties(playlist, vo);

        return Result.success(vo);
    }

    /**
     * 获取公开歌单列表 (分页)
     */
    @Override
    public Result<PageResult<PlaylistVO>> getPublicPlaylists(PlaylistQueryReq req) {
        // 1. 查询总数
        long total = playlistMapper.countPublicList();

        // 2. 如果总数为0，直接返回空结果
        if (total == 0) {
            return Result.success(PageResult.empty());
        }

        // 3. 查询当前页数据
        List<Playlist> playlists = playlistMapper.selectPublicList(req.getOffset(), req.getSize());

        // 4. Entity -> VO 转换
        List<PlaylistVO> voList = BeanUtil.copyToList(playlists, PlaylistVO.class);

        // 5. 返回分页结果
        PageResult<PlaylistVO> pageResult = new PageResult<>(total, voList);
        return Result.success(pageResult);
    }

    /**
     * 更新歌单信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updatePlaylist(PlaylistUpdateReq req, Long userId) {
        if (req.getId() == null || req.getId() <= 0) {
            return Result.error("歌单ID不合法");
        }

        // 检查歌单是否存在且属于当前用户
        Playlist existPlaylist = playlistMapper.selectById(req.getId());
        if (existPlaylist == null) {
            return Result.error("歌单不存在");
        }
        if (!existPlaylist.getUserId().equals(userId)) {
            return Result.error("无权限修改此歌单");
        }

        Playlist playlist = new Playlist();
        playlist.setId(req.getId());
        playlist.setUserId(userId);
        playlist.setName(req.getName());
        playlist.setDescription(req.getDescription());
        playlist.setCoverUrl(req.getCoverUrl());
        playlist.setIsPublic(req.getIsPublic());

        playlistMapper.update(playlist);

        return Result.success("更新成功");
    }

    /**
     * 删除歌单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deletePlaylist(Long playlistId, Long userId) {
        if (playlistId == null || playlistId <= 0) {
            return Result.error("歌单ID不合法");
        }

        // 检查歌单是否存在
        Playlist playlist = playlistMapper.selectById(playlistId);
        if (playlist == null) {
            return Result.error("歌单不存在");
        }

        // 检查权限
        if (!playlist.getUserId().equals(userId)) {
            return Result.error("无权限删除此歌单");
        }

        // 检查是否为默认歌单
        if (playlist.getIsDefault() == 1) {
            return Result.error("默认歌单不能删除");
        }

        // 先删除歌单歌曲关联
        playlistSongMapper.deleteByPlaylistId(playlistId);

        // 再删除歌单
        playlistMapper.deleteById(playlistId, userId);

        return Result.success("删除成功");
    }

    /**
     * 添加歌曲到歌单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> addSongToPlaylist(Long playlistId, Long songId, Long userId) {
        // 参数校验
        if (playlistId == null || playlistId <= 0) {
            return Result.error("歌单ID不合法");
        }
        if (songId == null || songId <= 0) {
            return Result.error("歌曲ID不合法");
        }

        // 检查歌单是否存在且属于当前用户
        Playlist playlist = playlistMapper.selectById(playlistId);
        if (playlist == null) {
            return Result.error("歌单不存在");
        }
        if (!playlist.getUserId().equals(userId)) {
            return Result.error("无权限操作此歌单");
        }

        // 检查歌曲是否存在
        Song song = songMapper.selectById(songId);
        if (song == null) {
            return Result.error("歌曲不存在");
        }

        // 检查是否已添加
        boolean exists = playlistSongMapper.existsBySongId(playlistId, songId);
        if (exists) {
            return Result.error("歌曲已在歌单中");
        }

        // 添加关联
        PlaylistSong playlistSong = new PlaylistSong();
        playlistSong.setPlaylistId(playlistId);
        playlistSong.setSongId(songId);
        playlistSong.setSortOrder(0);

        playlistSongMapper.insert(playlistSong);

        return Result.success("添加成功");
    }

    /**
     * 从歌单移除歌曲
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> removeSongFromPlaylist(Long playlistId, Long songId, Long userId) {
        // 参数校验
        if (playlistId == null || playlistId <= 0) {
            return Result.error("歌单ID不合法");
        }
        if (songId == null || songId <= 0) {
            return Result.error("歌曲ID不合法");
        }

        // 检查歌单是否存在且属于当前用户
        Playlist playlist = playlistMapper.selectById(playlistId);
        if (playlist == null) {
            return Result.error("歌单不存在");
        }
        if (!playlist.getUserId().equals(userId)) {
            return Result.error("无权限操作此歌单");
        }

        // 检查歌曲是否在歌单中
        boolean exists = playlistSongMapper.existsBySongId(playlistId, songId);
        if (!exists) {
            return Result.error("歌曲不在歌单中");
        }

        // 删除关联
        playlistSongMapper.delete(playlistId, songId);

        return Result.success("移除成功");
    }

    /**
     * 获取歌单的所有歌曲
     */
    @Override
    public Result<List<SongVO>> getPlaylistSongs(Long playlistId) {
        if (playlistId == null || playlistId <= 0) {
            return Result.error("歌单ID不合法");
        }

        // 检查歌单是否存在
        Playlist playlist = playlistMapper.selectById(playlistId);
        if (playlist == null) {
            return Result.error("歌单不存在");
        }

        // 获取歌曲ID列表
        List<Long> songIds = playlistSongMapper.selectSongIdsByPlaylistId(playlistId);

        if (songIds.isEmpty()) {
            return Result.success(List.of());
        }

        // 根据歌曲ID查询歌曲详情
        List<SongVO> songVOList = songIds.stream()
                .map(songId -> {
                    Song song = songMapper.selectById(songId);
                    if (song != null) {
                        SongVO vo = new SongVO();
                        BeanUtil.copyProperties(song, vo);
                        return vo;
                    }
                    return null;
                })
                .filter(vo -> vo != null)
                .collect(Collectors.toList());

        return Result.success(songVOList);
    }
}