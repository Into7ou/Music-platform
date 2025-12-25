# 前端开发与播放逻辑 - 逻辑流程文档

> **模块**: Frontend & Player 模块  
> **版本**: v0.1  
> **创建时间**: 2025-12-18  
> **状态**: 🚧 开发中（播放器核心已完成，UI 重构中）

---

## 一、功能概述

本阶段主要完成了前端项目的初始化、全局音频播放逻辑的构建以及核心播放控制条（PlayerBar）的实现。同时，为了支持更丰富的 UI 展示（如网易云风格的歌曲卡片），对后端 Music 模块进行了字段扩展。

---

## 二、前端技术栈

| 技术 | 说明 |
|------|------|
| **Nuxt 3** | Vue 3 框架，支持 SSR 和自动导入 |
| **TailwindCSS** | 原子化 CSS 框架 |
| **DaisyUI** | 基于 Tailwind 的 UI 组件库 |
| **Pinia** | 全局状态管理（用于播放器状态） |
| **Howler.js** | 跨平台音频播放库 |
| **Iconify** | 图标方案 |

---

## 三、核心组件：PlayerBar (播放控制条)

### 3.1 功能特性
- **毛玻璃效果**: 使用 `backdrop-blur-2xl` 实现现代感底栏。
- **音频控制**: 播放/暂停、进度跳转、音量调节、播放模式切换（列表循环/单曲循环/随机播放）。
- **动态交互**: 专辑封面旋转动画（播放时旋转，暂停时停止）。
- **响应式设计**: 适配不同屏幕宽度的布局。

### 3.2 播放逻辑流 (Pinia + Howler.js)
```mermaid
graph TD
    A[Page/Component] -->|play(song)| B[usePlayerStore]
    B -->|New Howl| C[Howler.js Instance]
    C -->|onplay| D[Update isPlaying = true]
    C -->|onplay| E[Start Progress Update]
    E -->|requestAnimationFrame| F[Update progress state]
    B -->|seek/setVolume| C
    D -->|UI Sync| G[PlayerBar / Animations]
```

---

## 四、后端字段扩展 (Music 模块)

为了实现类似网易云音乐的 **SongCard (歌曲卡片)** 组件，对后端进行了以下增强：

### 4.1 数据库变更 (需手动执行)
```sql
ALTER TABLE sys_song ADD COLUMN cover_url VARCHAR(512) DEFAULT NULL COMMENT '封面图URL' AFTER file_url;
```

### 4.2 代码同步
- **Entity (`Song.java`)**: 新增 `coverUrl` 字段。
- **VO (`SongVO.java`)**: 新增 `coverUrl` 字段，确保前端可接收。
- **Mapper (`SongMapper.xml`)**:
    - `BaseResultMap`: 添加 `cover_url` 映射。
    - `insert`: 插入语句包含 `cover_url`。
    - `selectById` / `selectList`: 查询字段包含 `cover_url`。

---

## 五、下一步开发计划

### 5.1 核心组件重构
- [ ] **SongCard.vue**: 实现带封面、标题、艺术家和悬浮播放按钮的卡片组件。
- [ ] **Carousel.vue**: 基于 DaisyUI 或 Swiper 实现首页大屏轮播图。
- [ ] **NavTabs.vue**: 实现首页顶部的分类导航（推荐、排行榜、歌单等）。

### 5.2 首页 (index.vue) 升级
- 按照网易云音乐布局重构首页。
- 引入“热门推荐”和“个性化推荐”区块。
- 右侧边栏集成用户信息与入驻歌手展示。

### 5.3 功能增强
- [ ] 完善播放列表（Playlist）管理逻辑。
- [ ] 实现上一曲/下一曲切换功能。
- [ ] 增加搜索框功能。

---

## 六、文件清单

| 文件路径 | 职责 |
|----------|------|
| `app/stores/usePlayer.ts` | 播放器全局状态与 Howler.js 封装 |
| `app/components/PlayerBar.vue` | 底部播放控制条 UI 与交互 |
| `app/pages/index.vue` | 平台首页 |
| `app/pages/songs.vue` | 歌曲列表页 |
| `app/types/music.ts` | 前端音乐相关类型定义 |

---

## 七、版本记录

| 版本 | 日期 | 修改内容 |
|------|------|----------|
| v0.1 | 2025-12-18 | 初始化前端，完成 PlayerBar 与后端 coverUrl 扩展 |
