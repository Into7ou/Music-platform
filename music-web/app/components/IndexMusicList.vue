<template>
    <div class="flex flex-col gap-2">
        <div v-for="(song, idx) in songs" :key="song.id || idx" @click="playSong(song)"
            class="flex items-center p-3 rounded-xl hover:bg-white/5 border border-transparent hover:border-white/5 transition-all group cursor-pointer">
            <!-- 序号/封面 -->
            <div class="relative w-12 h-12 rounded-lg overflow-hidden flex-shrink-0 mr-4 bg-gray-800">
                <img :src="song.coverUrl || getDefaultCover()" :alt="song.title" class="w-full h-full object-cover" />
                <div
                    class="absolute inset-0 flex items-center justify-center bg-black/30 opacity-0 group-hover:opacity-100 transition-opacity">
                    <span class="text-white text-xs">▶</span>
                </div>
            </div>

            <!-- 信息 -->
            <div class="flex-1 min-w-0">
                <h4 class="text-sm font-bold text-gray-100 truncate group-hover:text-blue-300">
                    {{ song.title }}
                </h4>
                <p class="text-xs text-gray-500 truncate">
                    {{ song.artist }} - {{ song.album }}
                </p>
            </div>

            <!-- 时长/操作 -->
            <div class="flex items-center gap-4 text-gray-500 text-xs">
                <span class="hidden sm:block font-mono">{{ formatDuration(song.duration) }}</span>
                <button class="text-gray-400 hover:text-blue-400 hover:bg-blue-400/10 rounded-full p-1 transition-all"
                    @click.stop="openPlaylistPicker(song)" title="添加到歌单">
                    <Icon icon="solar:add-circle-bold" class="text-lg" />
                </button>
            </div>
        </div>
    </div>

    <PlaylistPickerModal v-model:visible="showPlaylistPicker" :songId="selectedSong?.id ?? null" @added="onSongAdded" />
</template>

<script setup lang="ts">
import { usePlayerStore } from '~/stores/usePlayer'
import type { Song } from '~/types/music'
import { Icon } from '@iconify/vue'
import { ref } from 'vue'

// 定义 props
const props = defineProps<{
    songs: Song[]
}>()

// 获取播放器 store
const playerStore = usePlayerStore()

// 播放歌曲
const playSong = (song: Song) => {
    // 确保歌曲有必要的字段
    if (!song.fileUrl) {
        console.error('歌曲缺少音频文件 URL:', song)
        return
    }

    // 调用 store 的 play 方法播放歌曲
    playerStore.play(song)
}

// 格式化时长（秒数转为 mm:ss 格式）
const formatDuration = (seconds?: number): string => {
    if (!seconds) return '00:00'
    const mins = Math.floor(seconds / 60)
    const secs = seconds % 60
    return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`
}

// 获取默认封面
const getDefaultCover = (): string => {
    return 'https://images.unsplash.com/photo-1470225620780-dba8ba36b745?q=80&w=100&auto=format&fit=crop'
}

// 收藏切换
const toggleFavorite = (song: Song) => {
    console.log('Toggle favorite for:', song.title)
    // TODO: 实现收藏逻辑
}

const showPlaylistPicker = ref(false)
const selectedSong = ref<Song | null>(null)

// 打开歌单选择弹窗
const openPlaylistPicker = (song: Song) => {
    selectedSong.value = song
    showPlaylistPicker.value = true
}

const onSongAdded = (playlistId: number) => {
    console.log('歌曲已添加到歌单:', playlistId)
    // 可以在这里添加 Toast 提示
}
</script>