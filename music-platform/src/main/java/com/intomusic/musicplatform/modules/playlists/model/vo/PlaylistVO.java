package com.intomusic.musicplatform.modules.playlists.model.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 歌单信息 VO
 */
@Data
public class PlaylistVO {
    /**
     * 歌单ID
     */
    private Long id;

    /**
     * 创建者用户ID
     */
    private Long userId;

    /**
     * 歌单名称
     */
    private String name;

    /**
     * 歌单描述
     */
    private String description;

    /**
     * 歌单封面URL
     */
    private String coverUrl;

    /**
     * 是否公开
     */
    private Integer isPublic;

    /**
     * 是否为默认歌单
     */
    private Integer isDefault;

    /**
     * 歌单类型
     */
    private String playlistType;

    /**
     * 歌曲数量
     */
    private Integer songCount;

    /**
     * 播放次数
     */
    private Long playCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}