<template>
  <!-- 
    修改点：
    1. bg-[#0a0e17]/90: 改为极深的蓝黑色背景，增加沉浸感
    2. border-t border-white/5: 极细的顶部高光线
  -->
  <div class="fixed bottom-0 left-0 right-0 h-24 
    bg-[#0a0e17]/90 
    backdrop-blur-xl saturate-100
    border-t border-white/5
    px-6 flex items-center shadow-[0_-5px_30px_rgba(0,0,0,0.3)] z-[9999] transition-all duration-300">

    <!-- 左侧：歌曲信息 -->
    <div class="flex items-center gap-4 w-[30%] min-w-[240px]">
      <div class="relative flex-shrink-0 group cursor-pointer">
        <!-- 专辑封面容器：增加光晕 -->
        <div
          class="w-14 h-14 rounded-lg overflow-hidden shadow-lg border border-white/5 group-hover:border-blue-400/50 transition-all duration-300 relative">
          <div class="absolute inset-0 bg-black/20 group-hover:bg-transparent transition-colors z-10"></div>
          <img v-if="playerStore.currentSong?.coverUrl" :src="playerStore.currentSong.coverUrl"
            :class="{ 'pause-animation': !playerStore.isPlaying }" />
          <div v-else class="w-full h-full bg-[#1c2333] flex items-center justify-center text-2xl">🎵</div>
        </div>
      </div>

      <div class="flex flex-col justify-center truncate">
        <h3 class="font-bold text-sm truncate text-gray-100 hover:text-blue-300 transition-colors cursor-pointer">
          {{ playerStore.currentSong?.title || 'Waiting for music...' }}
        </h3>
        <p class="text-xs text-gray-500 truncate cursor-pointer hover:text-gray-300">
          {{ playerStore.currentSong?.artist || 'MoonDrop' }}
        </p>
      </div>

      <button @click="toggleLike" :class="[
        'btn btn-ghost btn-circle btn-xs transition-all ml-2',
        isLiked
          ? 'text-red-500 hover:text-red-400'
          : 'text-gray-500 hover:text-red-400 hover:bg-red-400/10'
      ]" :disabled="!playerStore.currentSong" title="收藏到我喜欢">
        <Icon :icon="isLiked ? 'solar:heart-bold' : 'solar:heart-linear'" class="text-lg" />
      </button>
    </div>

    <!-- 中间：控制核心 -->
    <div class="flex-1 flex flex-col items-center max-w-2xl px-4">
      <div class="flex items-center gap-8 mb-2">

        <button class="btn btn-ghost btn-circle btn-sm text-gray-500 hover:text-white hover:bg-white/5"
          @click="togglePlayMode">
          <Icon :icon="playModeConfig[playerStore.playMode].icon" class="text-lg" />
        </button>

        <button @click="playerStore.playPrevious()"
          class="btn btn-ghost btn-circle btn-sm text-gray-300 hover:text-blue-400 hover:bg-transparent transition-colors">
          <Icon icon="solar:skip-previous-bold" class="text-2xl" />
        </button>

        <!-- 播放/暂停按钮：改为 MyGO 蓝色/白色风格，不再使用红色 -->
        <button @click="playerStore.toggle()"
          class="btn btn-circle w-12 h-12 bg-white text-black hover:bg-blue-100 hover:scale-105 border-none shadow-[0_0_15px_rgba(255,255,255,0.3)] transition-all"
          :disabled="!playerStore.currentSong">
          <Icon :icon="playerStore.isPlaying ? 'solar:pause-bold' : 'solar:play-bold'" class="text-3xl pl-0.5" />
        </button>

        <button @click="playerStore.playNext()"
          class="btn btn-ghost btn-circle btn-sm text-gray-300 hover:text-blue-400 hover:bg-transparent transition-colors">
          <Icon icon="solar:skip-next-bold" class="text-2xl" />
        </button>

        <button class="btn btn-ghost btn-xs text-gray-500 font-bold hover:text-white">词</button>
      </div>

      <!-- 进度条：重构为蓝色系 -->
      <div class="flex items-center gap-3 w-full group h-4">
        <span class="text-[10px] font-mono text-gray-500 w-8 text-right select-none">{{ playerStore.formattedProgress
        }}</span>

        <div class="flex-1 relative h-full flex items-center cursor-pointer">
          <!-- 轨道背景 -->
          <div
            class="absolute w-full h-1 bg-white/10 rounded-full group-hover:h-1.5 transition-all duration-200 overflow-hidden">
          </div>

          <!-- 进度条 (蓝色渐变) -->
          <div
            class="absolute h-1 bg-gradient-to-r from-blue-600 to-cyan-400 rounded-full group-hover:h-1.5 transition-all duration-200"
            :style="{ width: `${(playerStore.progress / playerStore.duration) * 100}%` }"></div>

          <input type="range" min="0" :max="playerStore.duration" :value="playerStore.progress" @input="handleSeek"
            class="custom-range w-full h-full opacity-0 z-10 cursor-pointer" />

          <!-- 滑块 (Glowing Dot) -->
          <div
            class="absolute h-3 w-3 bg-white shadow-[0_0_10px_rgba(34,211,238,0.8)] rounded-full pointer-events-none transition-transform duration-200 group-hover:scale-125 scale-0"
            :style="{ left: `${(playerStore.progress / playerStore.duration) * 100}%`, transform: 'translateX(-50%)' }">
          </div>
        </div>

        <span class="text-[10px] font-mono text-gray-500 w-8 select-none">{{ playerStore.formattedDuration }}</span>
      </div>
    </div>

    <!-- 右侧：音量 -->
    <div class="flex items-center justify-end gap-4 w-[30%] min-w-[200px]">

      <div class="flex items-center gap-2 group hover:w-32 w-24 transition-all duration-300">
        <button @click="toggleMute" class="btn btn-ghost btn-circle btn-xs text-gray-500 hover:text-white">
          <Icon :icon="playerStore.volume === 0 ? 'solar:volume-cross-bold' : 'solar:volume-loud-bold'"
            class="text-lg" />
        </button>

        <div class="flex-1 relative h-6 flex items-center">
          <div class="absolute w-full h-1 bg-white/10 rounded-full"></div>

          <div class="absolute h-1 bg-gray-300 rounded-full" :style="{ width: `${playerStore.volume * 100}%` }"></div>

          <input type="range" min="0" max="1" step="0.01" :value="playerStore.volume" @input="handleVolumeChange"
            class="custom-range-volume w-full h-full opacity-0 z-10 cursor-pointer" />

          <!-- 音量滑块 -->
          <div
            class="absolute h-2.5 w-2.5 bg-white rounded-full pointer-events-none opacity-0 group-hover:opacity-100 transition-opacity shadow-sm"
            :style="{ left: `${playerStore.volume * 100}%`, transform: 'translateX(-50%)' }"></div>
        </div>
      </div>

      <button @click="togglePlayingList" :class="[
        'btn btn-ghost btn-circle btn-sm transition-colors',
        showPlayingList ? 'text-blue-400 bg-blue-400/10' : 'text-gray-500 hover:text-white hover:bg-white/5'
      ]" title="播放列表">
        <Icon icon="solar:hamburger-menu-linear" class="text-xl" />
      </button>
    </div>
  </div>

  <PlayingList v-model:visible="showPlayingList" />
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { Icon } from '@iconify/vue';
import { usePlayerStore } from '../stores/usePlayer';
import type { Result, Playlist, Song } from '~/types/music';
import PlayingList from './PlayingList.vue';

const playerStore = usePlayerStore();

const showPlayingList = ref(false)

// ✅ 新增：切换播放列表显示
const togglePlayingList = () => {
  showPlayingList.value = !showPlayingList.value
}

// --- 播放模式逻辑 ---
const playModeConfig = {
  'loop': { label: '列表循环', icon: 'solar:repeat-bold' },
  'repeat-one': { label: '单曲循环', icon: 'solar:repeat-one-bold' },
  'shuffle': { label: '随机播放', icon: 'solar:shuffle-bold' }
}

const isLiked = ref(false)
const defaultPlaylistId = ref<number | null>(null)

const getToken = (): string | null => {
  return localStorage.getItem('token') || sessionStorage.getItem('token')
}

const checkIfLiked = async (songId: number) => {
  const token = getToken()
  if (!token) {
    isLiked.value = false
    return
  }

  try {
    // 获取默认歌单
    const defaultRes = await $fetch<Result<Playlist>>('/api/playlist/my/default', {
      headers: { Authorization: token }
    })
    if (defaultRes.code === 200) {
      defaultPlaylistId.value = defaultRes.data.id

      // 获取歌单内歌曲列表
      const songsRes = await $fetch<Result<Song[]>>(`/api/playlist/${defaultRes.data.id}/songs`)
      if (songsRes.code === 200) {
        isLiked.value = songsRes.data.some((s: any) => s.id === songId)
      }
    }
  } catch (e) {
    console.error('检查收藏状态失败', e)
  }
}

// 切换收藏状态
const toggleLike = async () => {
  const token = getToken()
  if (!token || !playerStore.currentSong) {
    alert('请先登录')
    return
  }

  const songId = playerStore.currentSong.id

  if (!defaultPlaylistId.value) {
    await checkIfLiked(songId) // 确保有 playlistId
  }

  try {
    if (isLiked.value) {
      // 取消收藏
      await $fetch(`/api/playlist/${defaultPlaylistId.value}/songs/${songId}`, {
        method: 'DELETE',
        headers: { Authorization: token }
      })
      isLiked.value = false
    } else {
      // 添加收藏
      await $fetch(`/api/playlist/${defaultPlaylistId.value}/songs/${songId}`, {
        method: 'POST',
        headers: { Authorization: token }
      })
      isLiked.value = true
    }
  } catch (e) {
    console.error('收藏操作失败', e)
  }
}

function togglePlayMode() {
  playerStore.togglePlayMode()
}

function toggleMute() {
  if (playerStore.volume > 0) playerStore.setVolume(0);
  else playerStore.setVolume(0.5);
}

function handleSeek(event: Event) {
  const target = event.target as HTMLInputElement;
  playerStore.seek(Number(target.value));
}

function handleVolumeChange(event: Event) {
  const target = event.target as HTMLInputElement;
  playerStore.setVolume(Number(target.value));
}
</script>