package com.intomusic.musicplatform.modules.music.controller;

import com.intomusic.musicplatform.common.result.PageResult;
import com.intomusic.musicplatform.common.result.Result;
import com.intomusic.musicplatform.modules.music.model.dto.SongQueryReq;
import com.intomusic.musicplatform.modules.music.model.entity.Song;
import com.intomusic.musicplatform.modules.music.model.vo.SongVO;
import com.intomusic.musicplatform.modules.music.model.vo.SearchResultVO;
import com.intomusic.musicplatform.modules.music.service.MusicService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/music")
public class MusicController {

    @Resource
    private MusicService musicService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadMusic(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "artist", required = false) String artist,
            @RequestParam(value = "album", required = false) String album,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "cover", required = false) MultipartFile cover) {

        try {
            Song song = musicService.uploadSong(file, title, artist, album,genre,cover);
            return ResponseEntity.ok(song);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("上传失败: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public Result<PageResult<SongVO>> list(SongQueryReq req) {
        PageResult<SongVO> pageResult = musicService.queryList(req);
        return Result.success(pageResult);
    }

    /**
     * 获取歌曲详情
     *
     * @param id 歌曲 ID
     * @return 歌曲详情
     */
    @GetMapping("/{id}")
    public Result<SongVO> getDetail(@PathVariable("id") Long id) {
        SongVO songVO = musicService.getDetail(id);
        return Result.success(songVO);
    }

    /**
     * 随机获取歌曲
     *
     * @param count 获取数量
     * @return 随机歌曲列表
     */
    @GetMapping("/random")
    public Result<List<SongVO>> getRandom(@RequestParam(value = "count", defaultValue = "3") Integer count) {
        List<SongVO> randomSongs = musicService.getRandomSongs(count);
        return Result.success(randomSongs);
    }

    /**
     * 综合搜索（歌曲、歌手、专辑）
     *
     * @param keyword 搜索关键词
     * @return 搜索结果
     */
    @GetMapping("/search")
    public Result<SearchResultVO> search(@RequestParam("keyword") String keyword) {
        try {
            SearchResultVO result = musicService.search(keyword);
            return Result.success(result);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("搜索失败: " + e.getMessage());
        }
    }
}