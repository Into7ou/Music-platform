# Music 模块 - 逻辑流程文档

> **模块**: Music 模块  
> **版本**: v0.2  
> **创建时间**: 2025-12-18  
> **状态**: ✅ 已实现（歌曲上传 + 分页查询）

---

## 一、功能概述

Music 模块负责音乐平台的核心业务，包括歌曲文件上传（存储至 MinIO）和歌曲列表的分页查询。所有数据持久化到 `music_db` 数据库的 `sys_song` 表中。

---

## 二、接口清单

| 接口 | 方法 | 功能 | 状态 |
|------|------|------|------|
| `/music/upload` | `POST` | 上传歌曲文件及元数据 | ✅ 已完成 |
| `/music/list` | `GET` | 分页查询歌曲列表 | ✅ 已完成 |

---

## 三、功能 A：歌曲上传

### 请求/响应规范

| 项目 | 说明 |
|------|------|
| **URL** | `/music/upload` |
| **Method** | `POST` |
| **Content-Type** | `multipart/form-data` |

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `file` | MultipartFile | ✅ | 音频文件 (支持 mp3/flac 等) |
| `title` | String | ✅ | 歌曲标题 |
| `artist` | String | ❌ | 歌手名 |
| `album` | String | ❌ | 专辑名 |

**成功响应** (HTTP 200):
```json
{
    "id": 1,
    "title": "晴天",
    "artist": "周杰伦",
    "album": "叶惠美",
    "fileUrl": "http://localhost:9000/music-platform/music/晴天.mp3"
}
```

**失败响应**:
```json
{
    "error": "上传失败: 文件为空"
}
```

---

### 上传流程图

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              歌曲上传流程                                    │
└─────────────────────────────────────────────────────────────────────────────┘

    ┌──────────┐
    │  Client  │
    └────┬─────┘
         │ POST /music/upload
         │ FormData: file, title, artist, album
         ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│ [Controller] MusicController.uploadMusic()                                  │
│  - 接收 MultipartFile 和表单参数                                             │
│  - 调用 musicService.uploadSong()                                           │
└────────────────────────────────┬────────────────────────────────────────────┘
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│ [Service] MusicService.uploadSong()                                         │
│  @DS("music_db") 切换到 music_db 数据源                                      │
│                                                                             │
│  Step 1: 文件上传到 MinIO                                                    │
│          ├── 使用 Hutool 自动推断 Content-Type (如 audio/flac)               │
│          ├── 上传至 music-platform/music/ 目录                               │
│          └── 返回完整 URL: http://localhost:9000/music-platform/music/xxx    │
│                                                                             │
│  Step 2: 构建 Song 实体对象                                                  │
│          ├── song.setTitle(title)                                           │
│          ├── song.setArtist(artist)                                         │
│          ├── song.setAlbum(album)                                           │
│          ├── song.setFileUrl(minioUrl)                                      │
│          └── song.setFileSize(file.getSize())                               │
│                                                                             │
│  Step 3: 持久化到数据库                                                      │
│          ├── 调用 songMapper.insert(song)                                   │
│          └── 返回 Song 实体                                                  │
└────────────────────────────────┬────────────────────────────────────────────┘
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│ [MinIO] Object Storage                                                      │
│                                                                             │
│  Bucket: music-platform (Public Read)                                       │
│  Path: music/{filename}                                                     │
│  URL: http://localhost:9000/music-platform/music/{filename}                 │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 四、功能 B：分页查询歌曲列表

### 请求/响应规范

| 项目 | 说明 |
|------|------|
| **URL** | `/music/list` |
| **Method** | `GET` |
| **Content-Type** | `application/json` |

**请求参数** (Query String):

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| `keyword` | String | ❌ | - | 搜索关键词 (模糊匹配 title/artist/album) |
| `page` | Integer | ❌ | 1 | 当前页码 |
| `size` | Integer | ❌ | 10 | 每页条数 |

**请求示例**:
```
GET /music/list?keyword=周杰伦&page=1&size=10
```

**成功响应** (HTTP 200):
```json
{
    "code": 200,
    "msg": "Success",
    "data": {
        "total": 25,
        "list": [
            {
                "id": 1,
                "title": "晴天",
                "artist": "周杰伦",
                "album": "叶惠美",
                "fileUrl": "http://localhost:9000/music-platform/music/晴天.mp3",
                "duration": 269,
                "createTime": "2025-12-18T10:30:00"
            }
        ]
    }
}
```

**空结果响应**:
```json
{
    "code": 200,
    "msg": "Success",
    "data": {
        "total": 0,
        "list": []
    }
}
```

---

### 查询流程图

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           分页查询歌曲流程                                    │
└─────────────────────────────────────────────────────────────────────────────┘

    ┌──────────┐
    │  Client  │
    └────┬─────┘
         │ GET /music/list?keyword=xxx&page=1&size=10
         ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│ [Controller] MusicController.list(SongQueryReq req)                         │
│  - Spring 自动将 Query String 绑定到 SongQueryReq                            │
│  - 调用 musicService.queryList(req)                                         │
│  - 返回 Result.success(pageResult)                                          │
└────────────────────────────────┬────────────────────────────────────────────┘
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│ [Service] MusicService.queryList()                                          │
│  @DS("music_db") 切换到 music_db 数据源                                      │
│                                                                             │
│  Step 1: 查询总记录数                                                        │
│          ├── 调用 songMapper.countList(req)                                 │
│          └── 如果 total == 0 → 返回 PageResult.empty()                      │
│                                                                             │
│  Step 2: 查询当前页数据                                                      │
│          ├── 调用 songMapper.selectList(req)                                │
│          └── 返回 List<Song> entityList                                     │
│                                                                             │
│  Step 3: Entity → VO 转换                                                   │
│          ├── BeanUtil.copyToList(entityList, SongVO.class)                  │
│          └── 返回 List<SongVO> voList                                       │
│                                                                             │
│  Step 4: 封装分页结果                                                        │
│          └── return new PageResult<>(total, voList)                         │
└────────────────────────────────┬────────────────────────────────────────────┘
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│ [Mapper] SongMapper (Interface + XML)                                       │
│                                                                             │
│  countList (动态 SQL):                                                       │
│    SELECT COUNT(*) FROM sys_song                                            │
│    <where>                                                                  │
│      <if test="keyword != null and keyword != ''">                          │
│        (title LIKE '%${keyword}%' OR artist LIKE ... OR album LIKE ...)     │
│      </if>                                                                  │
│    </where>                                                                 │
│                                                                             │
│  selectList (动态 SQL + 分页):                                               │
│    SELECT id, title, artist, album, file_url, duration, create_time         │
│    FROM sys_song                                                            │
│    <where>...</where>                                                       │
│    ORDER BY create_time DESC                                                │
│    LIMIT #{offset}, #{size}                                                 │
└────────────────────────────────┬────────────────────────────────────────────┘
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│ [Database] MySQL - music_db.sys_song                                        │
│                                                                             │
│  表结构:                                                                     │
│  ┌────────────┬──────────────┬─────────────────────────────────┐            │
│  │ 字段        │ 类型         │ 说明                            │            │
│  ├────────────┼──────────────┼─────────────────────────────────┤            │
│  │ id         │ BIGINT(PK)   │ 主键，自增                       │            │
│  │ title      │ VARCHAR      │ 歌曲标题                         │            │
│  │ artist     │ VARCHAR      │ 歌手名                           │            │
│  │ album      │ VARCHAR      │ 专辑名                           │            │
│  │ file_url   │ VARCHAR      │ MinIO 完整 URL                   │            │
│  │ file_size  │ BIGINT       │ 文件大小（字节）                   │            │
│  │ duration   │ INT          │ 时长（秒），默认 0                │            │
│  │ create_time│ DATETIME     │ 创建时间                         │            │
│  │ update_time│ DATETIME     │ 更新时间                         │            │
│  └────────────┴──────────────┴─────────────────────────────────┘            │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 五、分层模型规范

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        DTO → Entity → VO 分层模型                            │
└─────────────────────────────────────────────────────────────────────────────┘

  [前端请求]
      │
      ▼
  ┌───────────────┐
  │ SongQueryReq  │  ← DTO (Data Transfer Object)
  │ (入参封装)     │     - keyword, page, size
  │               │     - getOffset() 自动计算偏移量
  └───────┬───────┘
          │
          ▼
  ┌───────────────┐
  │     Song      │  ← Entity (数据库实体)
  │ (数据库映射)   │     - 与 sys_song 表一一对应
  │               │     - 可能包含内部字段
  └───────┬───────┘
          │
          │  BeanUtil.copyToList()
          ▼
  ┌───────────────┐
  │    SongVO     │  ← VO (View Object)
  │ (视图对象)     │     - 返回给前端的字段
  │               │     - 排除敏感/内部字段
  └───────┬───────┘
          │
          ▼
  ┌───────────────┐
  │  PageResult   │  ← 通用分页包装类
  │ (分页封装)     │     - total: 总记录数
  │               │     - list: 当前页数据
  └───────────────┘
```

---

## 六、代码文件清单

| 层级 | 文件路径 | 职责 |
|------|----------|------|
| **Controller** | `modules/music/controller/MusicController.java` | 接收HTTP请求，调用Service |
| **Service** | `modules/music/service/MusicService.java` | 业务逻辑处理，数据源切换 |
| **Mapper** | `modules/music/mapper/SongMapper.java` | MyBatis 接口定义 |
| **Mapper XML** | `resources/mapper/music/SongMapper.xml` | SQL 语句实现 |
| **Entity** | `modules/music/model/entity/Song.java` | 歌曲实体类 |
| **DTO** | `modules/music/model/dto/SongQueryReq.java` | 查询请求参数封装 |
| **VO** | `modules/music/model/vo/SongVO.java` | 视图对象 |
| **PageResult** | `common/result/PageResult.java` | 通用分页结果 |
| **Result** | `common/result/Result.java` | 统一响应格式 |
| **MinioUtil** | `common/util/MinioUtil.java` | MinIO 操作工具类 |

---

## 七、关键代码片段

### 7.1 Controller 层

```java
@RestController
@RequestMapping("/music")
public class MusicController {

    @Resource
    private MusicService musicService;

    @GetMapping("/list")
    public Result<PageResult<SongVO>> list(SongQueryReq req) {
        PageResult<SongVO> pageResult = musicService.queryList(req);
        return Result.success(pageResult);
    }
}
```

---

### 7.2 Service 层

```java
@Service
@DS("music_db")
public class MusicService {

    @Resource
    private SongMapper songMapper;

    public PageResult<SongVO> queryList(SongQueryReq req) {
        // 1. 查询总数
        long total = songMapper.countList(req);
        if (total == 0) {
            return PageResult.empty();
        }

        // 2. 查询列表
        List<Song> entities = songMapper.selectList(req);

        // 3. 转换 VO
        List<SongVO> voList = BeanUtil.copyToList(entities, SongVO.class);

        return new PageResult<>(total, voList);
    }
}
```
