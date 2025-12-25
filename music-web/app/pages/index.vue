<template>
  <!-- 
    主页容器
    1. 使用 AuroraBackground 作为全屏背景
    2. pt-24 为顶部全局 Navbar 留出空间
    3. pb-32 为底部 PlayerBar 留出空间
  -->
  <AuroraBackground class="min-h-screen w-full bg-[#050A14] block">
    <div class="relative z-10 w-full min-h-screen pt-24 pb-32 px-4 lg:px-8 max-w-7xl mx-auto space-y-12">

      <!-- 1. 顶部 Banner / 每日推荐 (轮播组件) -->
      <IndexCarousel />

      <!-- 2. 推荐歌单 (Horizontal Scroll / Grid) -->
      <section>
        <div class="flex items-center justify-between mb-6 px-1">
          <h2 class="text-2xl font-bold text-white flex items-center gap-2">
            <span class="text-blue-400">#</span> 推荐歌单
          </h2>
          <NuxtLink to="/playlists" class="text-sm text-gray-400 hover:text-white transition-colors">更多 ></NuxtLink>
        </div>

        <div class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-6">
          <div v-for="(playlist, idx) in recommendedPlaylists" :key="idx"
            class="group relative flex flex-col gap-3 cursor-pointer">
            <!-- 封面 -->
            <div class="relative aspect-square rounded-xl overflow-hidden shadow-lg border border-white/5 bg-[#151925]">
              <img :src="playlist.cover"
                class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110 opacity-80 group-hover:opacity-100" />
              <!-- 悬浮播放按钮 -->
              <div
                class="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-all duration-300 bg-black/40 backdrop-blur-[2px]">
                <div
                  class="w-12 h-12 bg-white/90 rounded-full flex items-center justify-center pl-1 shadow-xl scale-50 group-hover:scale-100 transition-transform duration-300">
                  <span class="text-black text-xl">▶</span>
                </div>
              </div>
              <!-- 播放量标签 -->
              <div
                class="absolute top-2 right-2 px-2 py-0.5 rounded bg-black/50 backdrop-blur-sm text-[10px] text-white flex items-center gap-1">
                <span>🎧</span> {{ playlist.playCount }}
              </div>
            </div>
            <!-- 标题 -->
            <div class="space-y-1">
              <h3
                class="text-sm font-bold text-gray-200 group-hover:text-blue-300 transition-colors line-clamp-2 leading-tight">
                {{ playlist.title }}
              </h3>
              <p class="text-xs text-gray-500 hover:underline">{{ playlist.creator }}</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 3. 最新单曲 & 热门榜单 (Grid Layout) -->
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-12">

        <!-- 左侧：当前趋势 (列表形式) -->
        <div class="lg:col-span-2 space-y-6">
          <div class="flex items-center justify-between px-1">
            <h2 class="text-2xl font-bold text-white flex items-center gap-2">
              <span class="text-blue-400">♪</span> 当前趋势
            </h2>
            <button @click="fetchTrendingSongs"
              class="text-sm text-gray-400 hover:text-white transition-colors flex items-center gap-2"
              :disabled="isLoading">
              <span v-if="isLoading">加载中...</span>
              <span v-else>🔄 刷新</span>
            </button>
          </div>

          <!-- 加载状态 -->
          <div v-if="isLoading" class="flex justify-center items-center py-12">
            <div class="text-gray-400 animate-pulse">正在加载趋势歌曲...</div>
          </div>

          <!-- 无数据状态 -->
          <div v-else-if="trendingSongs.length === 0" class="flex justify-center items-center py-12">
            <div class="text-gray-400">暂无歌曲数据</div>
          </div>

          <!-- 使用 IndexMusicList 组件显示歌曲列表 -->
          <IndexMusicList v-else :songs="trendingSongs" />
        </div>


        <!-- 右侧：推荐歌手/热门艺人 -->
        <div class="space-y-6">
          <div class="flex items-center justify-between px-1">
            <h2 class="text-2xl font-bold text-white flex items-center gap-2">
              <span class="text-blue-400">★</span> 热门歌手
            </h2>
          </div>

          <div class="grid grid-cols-3 gap-4">
            <div v-for="(artist, idx) in artists" :key="idx"
              class="flex flex-col items-center gap-2 group cursor-pointer">
              <div
                class="w-20 h-20 rounded-full overflow-hidden border-2 border-white/5 group-hover:border-blue-400 transition-all shadow-lg">
                <img :src="artist.avatar"
                  class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500" />
              </div>
              <span class="text-xs font-medium text-gray-400 group-hover:text-white text-center">{{ artist.name
              }}</span>
            </div>
          </div>

          <!-- 侧边广告/活动位 -->
          <div
            class="mt-8 p-6 rounded-2xl bg-gradient-to-br from-blue-900/30 to-purple-900/30 border border-white/10 backdrop-blur-md">
            <h3 class="font-bold text-white text-lg mb-2">上传你的作品</h3>
            <p class="text-xs text-gray-400 mb-4">加入 MoonDrop 创作者计划，让世界听到你的声音。</p>
            <NuxtLink to="/upload" class="btn btn-sm w-full bg-white/10 hover:bg-white/20 border-0 text-white">立即上传
            </NuxtLink>
          </div>
        </div>
      </div>
    </div>
  </AuroraBackground>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { Song, Result } from '~/types/music'
import IndexMusicList from '~/components/IndexMusicList.vue'
import IndexCarousel from '~/components/IndexCarousel.vue'

// Mock Data: 推荐歌单 (Placeholder)
const recommendedPlaylists = ref([
  { title: "MyGO!!!!! 必听精选", creator: "Anon_Tokyo", playCount: "125K", cover: "https://images.unsplash.com/photo-1493225255756-d9584f8606e9?q=80&w=300&auto=format&fit=crop" },
  { title: "深夜emo：迷失在雨中", creator: "Soyo_Bass", playCount: "89K", cover: "https://images.unsplash.com/photo-1515162816999-a0c47dc192f7?q=80&w=300&auto=format&fit=crop" },
  { title: "Study with Tomori", creator: "Lamp_Genius", playCount: "230K", cover: "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?q=80&w=300&auto=format&fit=crop" },
  { title: "Rana's Matcha Parfait", creator: "Cat_Guitar", playCount: "50K", cover: "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=300&auto=format&fit=crop" },
  { title: "Taki's Drum Practice", creator: "Panda_Drum", playCount: "45K", cover: "https://images.unsplash.com/photo-1519892300165-cb5542fb4747?q=80&w=300&auto=format&fit=crop" },
])

// 当前趋势歌曲 - 从后端随机获取
const trendingSongs = ref<Song[]>([])
const isLoading = ref(true)

// 获取随机趋势歌曲
const fetchTrendingSongs = async () => {
  try {
    isLoading.value = true

    // 调用后端随机获取接口
    const response = await $fetch<Result<Song[]>>('/api/music/random', {
      params: {
        count: 3  // 随机获取 3 首歌曲
      }
    })

    if (response.code === 200 && response.data) {
      trendingSongs.value = response.data
    } else {
      console.warn('未获取到歌曲数据')
      trendingSongs.value = []
    }
  } catch (error) {
    console.error('获取趋势歌曲失败:', error)
    trendingSongs.value = []
  } finally {
    isLoading.value = false
  }
}

// 页面加载时获取数据
onMounted(() => {
  fetchTrendingSongs()
})

// Mock Data: 歌手
const artists = ref([
  { name: "MyGO!!!!!", avatar: "https://images.unsplash.com/photo-1520523839897-bd0b52f945a0?q=80&w=200&auto=format&fit=crop" },
  { name: "Roselia", avatar: "https://images.unsplash.com/photo-1534330207526-9e4e35041c24?q=80&w=200&auto=format&fit=crop" },
  { name: "Poppin'Party", avatar: "https://images.unsplash.com/photo-1516280440614-6697288d5d38?q=80&w=200&auto=format&fit=crop" },
  { name: "Afterglow", avatar: "https://images.unsplash.com/photo-1493225255756-d9584f8606e9?q=80&w=200&auto=format&fit=crop" },
  { name: "Raise A Suilen", avatar: "https://images.unsplash.com/photo-1511379938547-c1f69419868d?q=80&w=200&auto=format&fit=crop" },
  { name: "Morfonica", avatar: "https://images.unsplash.com/photo-1485579149621-3123dd979885?q=80&w=200&auto=format&fit=crop" },
])
</script>