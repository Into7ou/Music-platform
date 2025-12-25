package com.intomusic.musicplatform.modules.music.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Song {
    private Long id;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private String fileUrl;
    private Long fileSize;
    private Integer duration;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String coverUrl;
}