<template>
    <Teleport to="body">
        <Transition name="slide-left">
            <div v-if="visible" class="fixed right-0 top-0 bottom-0 w-[400px] bg-[#0a0e17]/95 backdrop-blur-xl 
                        border-l border-white/10 shadow-[0_0_50px_rgba(0,0,0,0.5)] z-[9998] flex flex-col">

                <!-- 头部 -->
                <div class="flex items-center justify-between p-6 border-b border-white/10">
                    <div class="flex items-center gap-3">
                        <h3 class="text-lg font-bold text-white">播放列表</h3>
                        <span class="text-sm font-mono text-gray-500">
                            {{ playerStore.currentIndex + 1 }}/{{ playerStore.playlist.length }}
                        </span>
                    </div>

                    <div class="flex items-center gap-2">
                        <!-- 收藏全部按钮 -->
                        <button @click="collectAll" class="btn btn-xs btn-ghost text-gray-400 hover:text-blue-400"
                            title="收藏全部">
                            <Icon icon="solar:heart-linear" class="text-base" />
                        </button>

                        <!-- 清空按钮 -->
                        <button @click="clearPlaylist" class="btn btn-xs btn-ghost text-gray-400 hover:text-red-400"
                            title="清空列表">
                            <Icon icon="solar:trash-bin-trash-linear" class="text-base" />
                        </button>

                        <!-- 关闭按钮 -->
                        <button @click="close" class="btn btn-xs btn-ghost text-gray-400 hover:text-white">
                            <Icon icon="solar:close-circle-linear" class="text-xl" />
                        </button>
                    </div>
                </div>

                <!-- 歌曲列表 -->
                <div ref="listContainer" class="flex-1 overflow-y-auto px-4 py-2">
                    <div v-for="(song, index) in playerStore.playlist" :key="`${song.id}-${index}`"
                        :ref="el => { if (index === playerStore.currentIndex) currentSongRef = el as HTMLElement }"
                        @click="playSongAt(index)" :class="[
                            'group flex items-center gap-3 p-3 rounded-lg transition-all cursor-pointer mb-1',
                            index === playerStore.currentIndex
                                ? 'bg-blue-500/20 border border-blue-400/30'
                                : 'hover:bg-white/5 border border-transparent'
                        ]">

                        <!-- 封面 -->
                        <div class="relative w-12 h-12 rounded-md overflow-hidden flex-shrink-0 bg-[#1a2035]">
                            <img :src="song.coverUrl || defaultCover" :alt="song.title"
                                class="w-full h-full object-cover" />

                            <!-- 播放中动画 -->
                            <div v-if="index === playerStore.currentIndex && playerStore.isPlaying"
                                class="absolute inset-0 flex items-center justify-center bg-black/40">
                                <span class="text-blue-400 text-xs animate-pulse">♪</span>
                            </div>
                        </div>

                        <!-- 歌曲信息 -->
                        <div class="flex-1 min-w-0">
                            <h4 :class="[
                                'text-sm font-medium truncate transition-colors',
                                index === playerStore.currentIndex
                                    ? 'text-blue-400'
                                    : 'text-white group-hover:text-blue-300'
                            ]">
                                {{ song.title }}
                            </h4>
                            <p class="text-xs text-gray-500 truncate">
                                {{ song.artist }}
                            </p>
                        </div>

                        <!-- 时长 -->
                        <div class="text-xs text-gray-500 font-mono">
                            {{ formatDuration(song.duration) }}
                        </div>

                        <!-- 删除按钮 -->
                        <button @click.stop="removeSong(index)"
                            class="opacity-0 group-hover:opacity-100 transition-opacity btn btn-xs btn-ghost text-gray-500 hover:text-red-400">
                            <Icon icon="solar:close-circle-linear" class="text-base" />
                        </button>
                    </div>

                    <!-- 空列表提示 -->
                    <div v-if="playerStore.playlist.length === 0"
                        class="flex flex-col items-center justify-center py-20 text-gray-500">
                        <Icon icon="solar:music-note-2-linear" class="text-6xl mb-4 opacity-30" />
                        <p class="text-sm">播放列表是空的</p>
                        <p class="text-xs mt-2">去发现一些好音乐吧</p>
                    </div>
                </div>

                <!-- 底部定位按钮 -->
                <div v-if="playerStore.playlist.length > 0" class="absolute right-6 bottom-6">
                    <button @click="scrollToCurrentSong"
                        class="btn btn-circle bg-blue-600 hover:bg-blue-500 text-white border-0 shadow-lg shadow-blue-900/50"
                        title="定位到当前播放">
                        <Icon icon="solar:map-arrow-down-bold" class="text-xl" />
                    </button>
                </div>
            </div>
        </Transition>
    </Teleport>
</template>

<script setup lang="ts">
import { ref, nextTick } from 'vue'
import { Icon } from '@iconify/vue'
import { usePlayerStore } from '~/stores/usePlayer'

const props = defineProps<{
    visible: boolean
}>()

const emit = defineEmits(['update:visible'])

const playerStore = usePlayerStore()
const listContainer = ref<HTMLElement | null>(null)
const currentSongRef = ref<HTMLElement | null>(null)

const defaultCover = 'https://images.unsplash.com/photo-1470225620780-dba8ba36b745?q=80&w=100&auto=format&fit=crop'

// 播放指定歌曲
const playSongAt = (index: number) => {
    playerStore.playAtIndex(index)
}

// 移除歌曲
const removeSong = (index: number) => {
    playerStore.removeFromPlaylist(index)
}

// 清空播放列表
const clearPlaylist = () => {
    if (confirm('确定要清空播放列表吗？')) {
        playerStore.clearPlaylist()
    }
}

// 收藏全部到"我喜欢"
const collectAll = async () => {
    // TODO: 实现批量收藏功能
    console.log('收藏全部歌曲')
    alert('功能开发中...')
}

// 关闭面板
const close = () => {
    emit('update:visible', false)
}

// 格式化时长
const formatDuration = (seconds?: number): string => {
    if (!seconds) return '0:00'
    const mins = Math.floor(seconds / 60)
    const secs = seconds % 60
    return `${mins}:${String(secs).padStart(2, '0')}`
}

// 滚动到当前播放的歌曲
const scrollToCurrentSong = async () => {
    await nextTick()
    if (currentSongRef.value && listContainer.value) {
        currentSongRef.value.scrollIntoView({
            behavior: 'smooth',
            block: 'center'
        })
    }
}
</script>

<style scoped>
/* 滑入滑出动画 */
.slide-left-enter-active,
.slide-left-leave-active {
    transition: transform 0.3s ease, opacity 0.3s ease;
}

.slide-left-enter-from {
    transform: translateX(100%);
    opacity: 0;
}

.slide-left-leave-to {
    transform: translateX(100%);
    opacity: 0;
}

/* 自定义滚动条 */
::-webkit-scrollbar {
    width: 6px;
}

::-webkit-scrollbar-track {
    background: transparent;
}

::-webkit-scrollbar-thumb {
    background: rgba(255, 255, 255, 0.1);
    border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
    background: rgba(255, 255, 255, 0.2);
}
</style>