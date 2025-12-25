package com.intomusic.musicplatform.modules.music.model.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 歌曲信息 VO
 */
@Data
public class SongVO {

    private Long id;

    private String title;

    private String artist;

    private String album;

    private String fileUrl;

    /**
     * 时长（秒）
     */
    private Integer duration;

    private LocalDateTime createTime;

    private String coverUrl;
}