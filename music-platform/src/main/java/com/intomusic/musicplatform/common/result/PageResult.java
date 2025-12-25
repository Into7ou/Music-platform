package com.intomusic.musicplatform.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Collections;
import java.util.List;

/**
 * 通用分页结果包装类
 *
 * @param <T> 列表数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    /**
     * 总记录数
     */
    private long total;

    /**
     * 当前页数据列表
     */
    private List<T> list;

    /**
     * 返回一个空的分页结果
     */
    public static <T> PageResult<T> empty() {
        return new PageResult<>(0, Collections.emptyList());
    }
}