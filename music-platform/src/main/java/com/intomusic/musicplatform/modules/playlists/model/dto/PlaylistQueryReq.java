package com.intomusic.musicplatform.modules.playlists.model.dto;

import lombok.Data;

/**
 * 歌单分页查询请求 DTO
 */
@Data
public class PlaylistQueryReq {
    /**
     * 页码（默认第1页）
     */
    private Integer page = 1;

    /**
     * 每页大小（默认10条）
     */
    private Integer size = 10;

    /**
     * 计算 MySQL 分页偏移量
     */
    public Integer getOffset() {
        return (page - 1) * size;
    }
}