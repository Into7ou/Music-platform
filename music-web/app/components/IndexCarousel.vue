<template>
    <!-- 顶部 Banner / 每日推荐 (MyGO 风格 Hero) -->
    <section
        class="relative w-full h-[300px] lg:h-[380px] rounded-3xl overflow-hidden group shadow-2xl border border-white/10">
        <!-- Banner 背景图 (使用当前歌单的封面) -->
        <div class="absolute inset-0 bg-cover bg-center transition-transform duration-700 group-hover:scale-105"
            :style="{ backgroundImage: `url('${currentPlaylist?.coverUrl || defaultCover}')` }">
        </div>
        <!-- 渐变遮罩 -->
        <div class="absolute inset-0 bg-gradient-to-t from-[#050A14] via-[#050A14]/40 to-transparent"></div>

        <!-- 轮播指示器 -->
        <div class="absolute top-6 right-6 flex gap-2">
            <button v-for="(_, index) in carouselPlaylists" :key="index" @click="currentCarouselIndex = index" :class="[
                'w-2 h-2 rounded-full transition-all',
                index === currentCarouselIndex ? 'bg-white w-6' : 'bg-white/40 hover:bg-white/60'
            ]">
            </button>
        </div>

        <!-- Banner 内容 -->
        <div class="absolute bottom-0 left-0 p-8 lg:p-12 space-y-4 max-w-3xl">
            <div
                class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-blue-500/20 border border-blue-400/30 backdrop-blur-md">
                <span class="w-2 h-2 rounded-full bg-blue-400 animate-pulse"></span>
                <span class="text-xs font-mono text-blue-200 tracking-wider">DAILY RECOMMENDATION</span>
            </div>
            <div class="space-y-1">
                <h1
                    class="text-4xl lg:text-6xl font-black tracking-tighter italic drop-shadow-[0_0_20px_rgba(59,130,246,0.5)] text-transparent bg-clip-text bg-gradient-to-r from-white via-blue-100 to-cyan-200">
                    {{ currentPlaylist?.name || '加载中...' }}
                </h1>
                <h2
                    class="text-xl lg:text-2xl font-black tracking-tighter drop-shadow-[0_0_20px_rgba(59,130,246,0.5)] text-transparent bg-clip-text bg-gradient-to-r from-white via-blue-100 to-cyan-200">
                    今日歌单推荐
                </h2>
            </div>
            <p class="text-gray-300 text-sm lg:text-base max-w-xl line-clamp-2">
                {{ currentPlaylist?.description || '暂无描述' }}
            </p>
            <div class="pt-4 flex gap-4">
                <button @click="handlePlayAll" :disabled="isLoadingSongs"
                    class="btn border-0 bg-white text-black hover:bg-gray-200 rounded-full px-8 font-bold shadow-[0_0_20px_rgba(255,255,255,0.2)] disabled:opacity-50">
                    <span class="mr-1">▶</span>
                    {{ isLoadingSongs ? '加载中...' : '播放全部' }}
                </button>
                <!-- 删除：收藏歌单按钮 -->
            </div>
        </div>
    </section>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import type { Playlist, Song, Result } from '~/types/music'
import { usePlayerStore } from '~/stores/usePlayer'

const playerStore = usePlayerStore()

// 数据状态
const carouselPlaylists = ref<Playlist[]>([])
const currentCarouselIndex = ref(0)
const playlistSongs = ref<Song[]>([])
const isLoading = ref(true)
const isLoadingSongs = ref(false)

const defaultCover = 'https://images.unsplash.com/photo-1514525253440-b393452e8d26?q=80&w=2800&auto=format&fit=crop'

// 计算当前显示的歌单
const currentPlaylist = computed(() => {
    return carouselPlaylists.value[currentCarouselIndex.value] || null
})

// 轮播定时器
let carouselInterval: ReturnType<typeof setInterval> | null = null

// 获取随机歌单（3个）
const fetchRandomPlaylists = async () => {
    try {
        isLoading.value = true

        // 调用公开歌单接口，获取一定数量后随机抽取3个
        const response = await $fetch<Result<{ list: Playlist[]; total: number }>>('/api/playlist/public', {
            params: {
                page: 1,
                size: 20  // 获取20个，从中随机抽取3个
            }
        })

        if (response.code === 200 && response.data?.list) {
            const allPlaylists = response.data.list
            // 随机抽取3个（或更少，如果列表不足3个）
            const shuffled = allPlaylists.sort(() => Math.random() - 0.5)
            carouselPlaylists.value = shuffled.slice(0, Math.min(3, shuffled.length))
        }
    } catch (error) {
        console.error('获取歌单失败:', error)
        carouselPlaylists.value = []
    } finally {
        isLoading.value = false
    }
}

// 获取当前歌单的歌曲列表
const fetchPlaylistSongs = async (playlistId: number) => {
    try {
        isLoadingSongs.value = true
        const response = await $fetch<Result<Song[]>>(`/api/playlist/${playlistId}/songs`)

        if (response.code === 200 && response.data) {
            playlistSongs.value = response.data
        } else {
            playlistSongs.value = []
        }
    } catch (error) {
        console.error('获取歌单歌曲失败:', error)
        playlistSongs.value = []
    } finally {
        isLoadingSongs.value = false
    }
}

// 播放全部（参考 playlists/[id].vue 的 playAll 函数）
const handlePlayAll = async () => {
    if (!currentPlaylist.value) {
        console.warn('当前没有选中的歌单')
        return
    }

    // 获取歌单歌曲
    await fetchPlaylistSongs(currentPlaylist.value.id)

    if (playlistSongs.value.length === 0) {
        console.warn('歌单中没有歌曲')
        return
    }

    // ✅ 设置播放列表并从第一首开始播放
    playerStore.setPlaylist(playlistSongs.value, 0)

    console.log(`开始播放歌单：${currentPlaylist.value.name}，共 ${playlistSongs.value.length} 首歌曲`)
}

// 启动自动轮播
const startCarousel = () => {
    stopCarousel()
    carouselInterval = setInterval(() => {
        if (carouselPlaylists.value.length > 1) {
            currentCarouselIndex.value = (currentCarouselIndex.value + 1) % carouselPlaylists.value.length
        }
    }, 5000)  // 每5秒切换一次
}

// 停止轮播
const stopCarousel = () => {
    if (carouselInterval) {
        clearInterval(carouselInterval)
        carouselInterval = null
    }
}

onMounted(async () => {
    await fetchRandomPlaylists()
    startCarousel()
})

onUnmounted(() => {
    stopCarousel()
})
</script>