package com.intomusic.musicplatform.modules.music.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.intomusic.musicplatform.common.result.PageResult;
import com.intomusic.musicplatform.common.utils.MinioUtil;
import com.intomusic.musicplatform.modules.music.model.dto.SongQueryReq;
import com.intomusic.musicplatform.modules.music.model.entity.Song;
import com.intomusic.musicplatform.modules.music.mapper.SongMapper;
import com.intomusic.musicplatform.modules.music.model.vo.SongVO;
import com.intomusic.musicplatform.modules.music.model.vo.ArtistVO;
import com.intomusic.musicplatform.modules.music.model.vo.AlbumVO;
import com.intomusic.musicplatform.modules.music.model.vo.SearchResultVO;
import java.util.stream.Collectors;
import java.util.Map;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import cn.hutool.core.io.FileUtil; // 1. 引入 Hutool 工具类
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.IdUtil;
import com.intomusic.musicplatform.common.utils.AudioUtil;
import java.io.File;

import java.util.List;

@Service
@DS("music_db") // 关键：切换到音乐数据源
public class MusicService {

    @Resource
    private MinioUtil minioUtil;

    @Resource
    private SongMapper songMapper;

    @Transactional(rollbackFor = Exception.class)
    public Song uploadSong(MultipartFile file, String title, String artist, String album,
                           String genre, MultipartFile cover) {

        // 参数校验
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        if (StrUtil.isBlank(title)) {
            throw new IllegalArgumentException("歌曲标题不能为空");
        }

        File tempFile = null;

        try {
            // ========== Step 1: 创建临时文件 ==========
            String originalFilename = file.getOriginalFilename();
            String suffix = StrUtil.isNotBlank(originalFilename)
                    ? FileUtil.getSuffix(originalFilename)
                    : "mp3";

            // 创建临时文件
            tempFile = FileUtil.createTempFile("audio_", "." + suffix, true);
            file.transferTo(tempFile);

            // ========== Step 2: 解析音频时长 ==========
            int duration = AudioUtil.getDuration(tempFile);

            // ========== Step 3: 校正 Content-Type ==========
            String contentType = file.getContentType();
            if (StrUtil.isBlank(contentType) || "application/octet-stream".equals(contentType)) {
                contentType = FileUtil.getMimeType(originalFilename);
            }
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // ========== Step 4: 使用临时文件上传到 MinIO（修改点）==========
            String uniqueFileName = IdUtil.fastSimpleUUID() + "." + suffix;
            String objectName = "music/" + uniqueFileName;
            // 使用 File 版本的上传方法，而不是 MultipartFile 版本
            String fileUrl = minioUtil.uploadFile(tempFile, objectName, contentType);

            // ========== Step 5: 上传封面到 MinIO ==========
            String coverUrl = null;
            if (cover != null && !cover.isEmpty()) {
                String coverOriginalName = cover.getOriginalFilename();
                String coverSuffix = StrUtil.isNotBlank(coverOriginalName)
                        ? FileUtil.getSuffix(coverOriginalName)
                        : "jpg";

                String coverContentType = cover.getContentType();
                if (StrUtil.isBlank(coverContentType) || "application/octet-stream".equals(coverContentType)) {
                    coverContentType = FileUtil.getMimeType(coverOriginalName);
                }
                if (coverContentType == null) {
                    coverContentType = "image/jpeg";
                }

                String uniqueCoverName = IdUtil.fastSimpleUUID() + "." + coverSuffix;
                // 封面可以直接使用 MultipartFile 版本，因为没有被 transferTo 消耗
                coverUrl = minioUtil.uploadFile(cover, "covers/" + uniqueCoverName, coverContentType);
            }

            // ========== Step 6: 构建实体并保存到数据库 ==========
            Song song = new Song();
            song.setTitle(title);
            song.setArtist(StrUtil.blankToDefault(artist, "未知歌手"));
            song.setAlbum(StrUtil.blankToDefault(album, "未知专辑"));
            song.setGenre(genre);
            song.setFileUrl(fileUrl);
            song.setFileSize(file.getSize());
            song.setDuration(duration);
            song.setCoverUrl(coverUrl);

            songMapper.insert(song);

            return song;

        } catch (Exception e) {
            throw new RuntimeException("上传歌曲失败: " + e.getMessage(), e);
        } finally {
            // ========== Step 7: 清理临时文件 ==========
            if (tempFile != null && tempFile.exists()) {
                FileUtil.del(tempFile);
            }
        }
    }

    @DS("music_db")
    public PageResult<SongVO> queryList(SongQueryReq req) {
        // 1. 查询总数
        long total = songMapper.countList(req);

        // 2. 如果总数为0，直接返回空结果
        if (total == 0) {
            return PageResult.empty();
        }

        // 3. 查询当前页数据
        List<Song> entityList = songMapper.selectList(req);

        // 4. Entity -> VO 转换
        List<SongVO> voList = BeanUtil.copyToList(entityList, SongVO.class);

        // 5. 返回分页结果
        return new PageResult<>(total, voList);
    }

    /**
     * 获取歌曲详情
     *
     * @param id 歌曲 ID
     * @return 歌曲 VO
     * @throws RuntimeException 歌曲不存在时抛出
     */
    public SongVO getDetail(Long id) {
        // 1. 参数校验
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("歌曲 ID 不合法");
        }

        // 2. 查询数据库
        Song song = songMapper.selectById(id);

        // 3. 空值校验
        if (song == null) {
            throw new RuntimeException("歌曲不存在，ID: " + id);
        }

        // 4. Entity -> VO 转换
        SongVO vo = new SongVO();
        BeanUtil.copyProperties(song, vo);

        return vo;
    }

    /**
     * 随机获取歌曲
     *
     * @param count 获取数量
     * @return 随机歌曲列表
     */
    @DS("music_db")
    public List<SongVO> getRandomSongs(Integer count) {
        // 1. 参数校验和默认值处理
        if (count == null || count <= 0) {
            count = 3;
        }
        // 限制最大获取数量，防止性能问题
        if (count > 50) {
            count = 50;
        }

        // 2. 从 Mapper 查询随机歌曲
        List<Song> randomSongs = songMapper.selectRandomSongs(count);

        // 3. Entity -> VO 转换
        List<SongVO> voList = BeanUtil.copyToList(randomSongs, SongVO.class);

        return voList;
    }

    /**
     * 综合搜索（歌曲、歌手、专辑）
     *
     * @param keyword 搜索关键词
     * @return 搜索结果（包含歌曲、歌手、专辑）
     */
    @DS("music_db")
    public SearchResultVO search(String keyword) {
        // 1. 参数校验
        if (StrUtil.isBlank(keyword)) {
            throw new IllegalArgumentException("搜索关键词不能为空");
        }

        // 2. 创建查询请求（搜索歌曲）
        SongQueryReq req = new SongQueryReq();
        req.setKeyword(keyword.trim());
        req.setSize(100); // 限制最多返回 100 首歌曲

        // 3. 查询歌曲列表
        List<Song> songEntities = songMapper.selectList(req);
        List<SongVO> songs = BeanUtil.copyToList(songEntities, SongVO.class);

        // 4. 从歌曲中提取并聚合歌手信息
        Map<String, Long> artistCountMap = songs.stream()
                .collect(Collectors.groupingBy(SongVO::getArtist, Collectors.counting()));

        List<ArtistVO> artists = artistCountMap.entrySet().stream()
                .map(entry -> {
                    ArtistVO vo = new ArtistVO();
                    vo.setName(entry.getKey());
                    vo.setSongCount(entry.getValue().intValue());

                    // 使用该歌手第一首歌的封面作为头像
                    songs.stream()
                            .filter(song -> entry.getKey().equals(song.getArtist()))
                            .findFirst()
                            .ifPresent(song -> vo.setAvatar(song.getCoverUrl()));

                    return vo;
                })
                .collect(Collectors.toList());

        // 5. 从歌曲中提取并聚合专辑信息
        Map<String, List<SongVO>> albumGroupMap = songs.stream()
                .filter(song -> StrUtil.isNotBlank(song.getAlbum()))
                .collect(Collectors.groupingBy(SongVO::getAlbum));

        List<AlbumVO> albums = albumGroupMap.entrySet().stream()
                .map(entry -> {
                    AlbumVO vo = new AlbumVO();
                    vo.setName(entry.getKey());
                    vo.setSongCount(entry.getValue().size());

                    // 使用专辑内第一首歌的信息
                    SongVO firstSong = entry.getValue().get(0);
                    vo.setArtist(firstSong.getArtist());
                    vo.setCoverUrl(firstSong.getCoverUrl());

                    return vo;
                })
                .collect(Collectors.toList());

        // 6. 构建返回结果
        SearchResultVO result = new SearchResultVO();
        result.setSongs(songs);
        result.setArtists(artists);
        result.setAlbums(albums);
        result.setTotalSongs(songs.size());
        result.setTotalArtists(artists.size());
        result.setTotalAlbums(albums.size());

        return result;
    }

}