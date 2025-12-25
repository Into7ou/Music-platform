# 🌙 MoonDrop 音乐播放网站

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen?style=flat-square&logo=spring)
![Vue](https://img.shields.io/badge/Vue-3.5.25-4FC08D?style=flat-square&logo=vue.js)
![Nuxt](https://img.shields.io/badge/Nuxt-4.2.2-00DC82?style=flat-square&logo=nuxt.js)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)

一个现代化的音乐流媒体平台，支持音乐上传、在线播放、歌单管理等功能

</div>

---

## 📖 项目简介

MoonDrop 是一个基于 **Spring Boot** 和 **Nuxt.js** 开发的全栈音乐流媒体平台。采用前后端分离架构，提供完整的音乐管理、播放、歌单管理等功能。后端使用 MyBatis + MySQL 进行数据持久化，MinIO 作为对象存储服务，前端使用 Vue 3 + Tailwind CSS 构建现代化用户界面。（网站名称取自水月雨品牌！！！！！）

## ✨ 功能特性

- 🎧 **音乐播放** - 支持在线播放多种音频格式
- 📤 **音乐上传** - MP3/FLAC 等格式上传，自动提取元数据
- 🎼 **歌单管理** - 创建、编辑、分享个人歌单
- 🔍 **智能搜索** - 支持歌曲、歌手、专辑多维度搜索
- 👤 **用户系统** - 注册登录、JWT 认证
- 🎨 **现代化 UI** - 响应式设计，流畅动画效果

## 🛠 技术栈

### 后端

- **Java 21** - JDK 运行环境
- **Spring Boot 3.2.3** - 核心框架
- **MyBatis 3.0.3** - ORM 框架
- **MySQL 8.x** - 关系型数据库
- **Redis** - 缓存中间件
- **MinIO** - 对象存储服务
- **JWT** - Token 认证
- **Hutool** - Java 工具类库

### 前端

- **Vue 3** - 渐进式前端框架
- **Nuxt 4** - Vue 全栈框架
- **Pinia** - 状态管理
- **Tailwind CSS** - 原子化 CSS 框架
- **DaisyUI** - UI 组件库
- **Howler.js** - 音频播放引擎

## 📁 项目结构

```
music-platform/
├── music-platform/        # 后端项目（Spring Boot + Maven）
│   ├── src/main/java/     # Java 源代码
│   └── src/main/resources/# 配置文件和 SQL
├── music-web/             # 前端项目（Nuxt + Vue）
│   ├── app/components/    # Vue 组件
│   ├── app/pages/         # 页面路由
│   └── app/stores/        # 状态管理
├── DevLogs/               # 开发文档
└── README.md              # 项目说明
```

## 🚀 快速开始

### 环境要求

- JDK 21+
- Node.js 18+
- MySQL 8.0+
- Redis 6.0+
- MinIO

### 启动后端

```bash
cd music-platform
mvn clean install
mvn spring-boot:run
```

### 启动前端

```bash
cd music-web
npm install
npm run dev
```

访问 `http://localhost:3000` 开始使用

## 📝 代码规范

- **后端**: 遵循阿里巴巴 Java 开发手册
- **前端**: 使用 ESLint + Prettier 进行代码格式化
- **提交**: 遵循 Conventional Commits 规范
- **分支**: 使用 Git Flow 工作流

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 👨‍💻 作者

**Into7ou**

- GitHub: [@Into7ou](https://github.com/Into7ou)
- Email: 1528179251@qq.com

## 📄 许可证

本项目采用 [MIT](LICENSE) 许可证

## 🙏 致谢

感谢以下开源项目：

- [Spring Boot](https://spring.io/projects/spring-boot) - 后端核心框架
- [Vue.js](https://vuejs.org/) - 前端框架
- [Nuxt](https://nuxt.com/) - Vue 全栈解决方案
- [MyBatis](https://mybatis.org/) - 持久层框架
- [MinIO](https://min.io/) - 对象存储服务
- [Tailwind CSS](https://tailwindcss.com/) - CSS 框架


## ⚠️ 商标声明

**项目名称说明**：

本项目名称 "MoonDrop" 源自对**水月雨（Moondrop）耳机品牌**的喜爱与致敬。

**重要声明**：
- ❌ 本项目与**成都市水月雨科技有限公司**无任何关联
- ❌ 非官方项目，未获授权
- ❌ 不用于任何商业用途
- ✅ 纯属个人学习项目

"Moondrop"、"水月雨" 是**成都市水月雨科技有限公司**的注册商标，版权归其所有。

如商标持有方认为本项目名称不妥，请联系我，我将立即更改。
---

<div align="center">

Made with ❤️ by Into7ou

</div>