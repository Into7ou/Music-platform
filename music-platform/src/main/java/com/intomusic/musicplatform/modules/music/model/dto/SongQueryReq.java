package com.intomusic.musicplatform.modules.music.model.dto;

import lombok.Data;

/**
 * 歌曲分页查询请求 DTO
 */
@Data
public class SongQueryReq {

    /**
     * 搜索关键词（用于模糊匹配 title 或 artist）
     */
    private String keyword;

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
     * @return (page - 1) * size
     */
    public Integer getOffset() {
        return (page - 1) * size;
    }
}