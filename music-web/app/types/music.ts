// types/music.ts
export interface Song {
    id: number;
    title: string;
    artist: string;
    album?: string;
    duration: number; // 单位：秒
    fileUrl: string; // MinIO 返回的文件 URL
    coverUrl?: string; // 封面图 URL
    createTime?: string;
    updateTime?: string;
}

export interface SongQueryReq {
    keyword?: string;
    page: number;
    size: number;
}

// ⭐ 修改：匹配后端的 PageResult 结构
export interface PageResult<T> {
    total: number;
    list: T[];  // 改为 list
}

// ⭐ 新增：后端的统一返回结构
export interface Result<T> {
    code: number;
    msg: string;
    data: T;
}

// ==================== 歌单相关类型 ====================

/**
 * 歌单信息
 */
export interface Playlist {
    id: number;
    userId: number;
    name: string;
    description?: string;
    coverUrl?: string;
    isPublic: number;       // 0=私有, 1=公开
    isDefault: number;      // 1=默认歌单（我喜欢的音乐）
    playlistType: string;   // 'i_like' | 'custom'
    songCount?: number;
    playCount?: number;
    createTime?: string;
    updateTime?: string;
}

/**
 * 创建歌单请求
 */
export interface PlaylistCreateReq {
    name: string;
    description?: string;
    coverUrl?: string;
    isPublic?: number;
}

/**
 * 更新歌单请求
 */
export interface PlaylistUpdateReq {
    id?: number;
    name?: string;
    description?: string;
    coverUrl?: string;
    isPublic?: number;
}

/**
 * 歌单分页查询请求
 */
export interface PlaylistQueryReq {
    page: number;
    size: number;
}