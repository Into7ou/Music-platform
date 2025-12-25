<template>
  <AuroraBackground class="min-h-screen w-full block">
    <div class="relative z-10 w-full min-h-screen pt-24 pb-32 px-4 lg:px-8 max-w-7xl mx-auto">
      <!-- 页面标题 -->
      <h1 class="text-3xl font-bold text-white mb-8">排行榜</h1>

      <!-- 歌曲列表框架 -->
      <div class="space-y-3">
        <!-- 加载状态 -->
        <div v-if="pending" class="flex justify-center py-8">
          <span class="loading loading-spinner loading-lg"></span>
        </div>

        <!-- 错误状态 -->
        <div v-else-if="error" class="alert alert-error">
          <span>加载失败: {{ error.message }}</span>
        </div>

        <!-- 歌曲列表 -->
        <div v-else-if="songs.length > 0" class="space-y-3">
          <div v-for="(song, index) in songs" :key="song.id"
               class="flex items-center p-3 rounded-xl hover:bg-white/5 border border-transparent hover:border-white/5 transition-all group cursor-pointer">
            <!-- 排名 -->
            <div class="w-10 text-right font-bold text-gray-400 mr-4">
              <span>{{ index + 1 }}</span>
            </div>

            <!-- 封面/播放按钮 -->
            <div class="relative w-12 h-12 rounded-lg overflow-hidden flex-shrink-0 mr-4 bg-gray-800">
              <img :src="song.coverUrl" class="w-full h-full object-cover" />
              <div
                  class="absolute inset-0 flex items-center justify-center bg-black/50 opacity-0 group-hover:opacity-100 transition-opacity"
                  @click="playSong(song)"
              >
                <span class="text-white">▶</span>
              </div>
            </div>

            <!-- 歌曲信息 -->
            <div class="flex-1 min-w-0">
              <h4 class="text-sm font-bold text-gray-100 truncate group-hover:text-blue-300">
                {{ song.title }}
              </h4>
              <p class="text-xs text-gray-500 truncate">
                {{ song.artist }} - {{ song.album || '-' }}
              </p>
            </div>

            <!-- 时长 -->
            <div class="text-gray-500 text-xs">
              {{ formatDuration(song.duration) }}
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-else class="text-center py-12 text-gray-500">
          <p>暂无歌曲数据</p>
        </div>
      </div>
    </div>
  </AuroraBackground>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { usePlayerStore } from '../stores/usePlayer'

const playerStore = usePlayerStore()

// 歌曲数据
const songs = ref([])
const pending = ref(false)
const error = ref(null)

// 获取歌曲数据
const fetchSongs = async () => {
  pending.value = true
  try {
    // 调用随机获取歌曲API，获取5首歌曲
    const { data } = await useFetch('/api/music/random', {
      query: {
        count: 5
      }
    })

    if (data.value && data.value.data) {
      songs.value = data.value.data
    }
  } catch (err: any) {
    error.value = err
  } finally {
    pending.value = false
  }
}

// 播放歌曲
const playSong = (song) => {
  playerStore.play(song)
}

// 格式化时长
const formatDuration = (seconds: number): string => {
  if (!seconds) return '0:00'
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${mins}:${secs.toString().padStart(2, '0')}`
}

// 页面加载时获取数据
onMounted(() => {
  fetchSongs()
})
</script>