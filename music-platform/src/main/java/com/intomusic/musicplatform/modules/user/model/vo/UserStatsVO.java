package com.intomusic.musicplatform.modules.user.model.vo;

import lombok.Data;

/**
 * 用户统计信息视图对象
 */
@Data
public class UserStatsVO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 创建的歌单数（不含默认歌单）
     */
    private Integer playlistCount;

    /**
     * 喜欢的歌曲数（i_like歌单中的歌曲数量）
     */
    private Integer likedSongCount;
}