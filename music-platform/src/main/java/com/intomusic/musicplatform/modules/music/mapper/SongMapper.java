package com.intomusic.musicplatform.modules.music.mapper;

import com.intomusic.musicplatform.modules.music.model.dto.SongQueryReq;
import com.intomusic.musicplatform.modules.music.model.entity.Song;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SongMapper {
    int insert(Song song);

    /**
     * 分页查询歌曲列表
     *
     * @param req 查询参数（包含 keyword, page, size）
     * @return 歌曲列表
     */
    List<Song> selectList(@Param("req") SongQueryReq req);

    /**
     * 统计符合条件的歌曲总数（用于分页计算）
     *
     * @param req 查询参数
     * @return 总记录数
     */
    Long countList(@Param("req") SongQueryReq req);

    /**
     * 根据 ID 查询歌曲详情
     *
     * @param id 歌曲 ID
     * @return 歌曲实体，不存在返回 null
     */
    Song selectById(@Param("id") Long id);

    /**
     * 随机获取指定数量的歌曲
     *
     * @param count 获取数量
     * @return 随机歌曲列表
     */
    List<Song> selectRandomSongs(@Param("count") Integer count);
}