<template>
    <AuroraBackground class="min-h-screen w-full bg-[#050A14] block">
        <div class="relative z-10 w-full min-h-screen pt-24 pb-32 px-4 lg:px-8 max-w-7xl mx-auto">

            <!-- 加载状态 -->
            <div v-if="isLoading" class="flex justify-center items-center py-20">
                <div class="text-gray-400 animate-pulse text-lg">正在搜索...</div>
            </div>

            <div v-else-if="!result || result.totalSongs === 0"
                class="flex flex-col items-center justify-center py-20 text-gray-500">
                <Icon icon="solar:music-note-2-linear" class="text-6xl mb-4 opacity-30" />
                <p class="text-xl mb-2">未找到相关结果</p>
                <p class="text-sm">试试其他关键词吧</p>
            </div>

            <template v-else>
                <div class="mb-8 border-b border-white/10 pb-6">
                    <h1 class="text-3xl font-bold text-white mb-2">
                        搜索"<span class="text-blue-400">{{ keyword }}</span>"
                    </h1>
                    <p class="text-gray-500">
                        找到 {{ result.totalSongs }} 首单曲，{{ result.totalArtists }} 位歌手，{{ result.totalAlbums }} 张专辑
                    </p>
                </div>

                <div class="flex gap-2 mb-8 border-b border-white/10">
                    <button @click="activeTab = 'songs'" :class="[
                        'px-6 py-3 font-medium transition-all relative',
                        activeTab === 'songs'
                            ? 'text-blue-400'
                            : 'text-gray-500 hover:text-gray-300'
                    ]">
                        单曲 ({{ result.totalSongs }})
                        <div v-if="activeTab === 'songs'" class="absolute bottom-0 left-0 right-0 h-0.5 bg-blue-400">
                        </div>
                    </button>

                    <button @click="activeTab = 'artists'" :class="[
                        'px-6 py-3 font-medium transition-all relative',
                        activeTab === 'artists'
                            ? 'text-blue-400'
                            : 'text-gray-500 hover:text-gray-300'
                    ]">
                        歌手 ({{ result.totalArtists }})
                        <div v-if="activeTab === 'artists'" class="absolute bottom-0 left-0 right-0 h-0.5 bg-blue-400">
                        </div>
                    </button>

                    <button @click="activeTab = 'albums'" :class="[
                        'px-6 py-3 font-medium transition-all relative',
                        activeTab === 'albums'
                            ? 'text-blue-400'
                            : 'text-gray-500 hover:text-gray-300'
                    ]">
                        专辑 ({{ result.totalAlbums }})
                        <div v-if="activeTab === 'albums'" class="absolute bottom-0 left-0 right-0 h-0.5 bg-blue-400">
                        </div>
                    </button>
                </div>

                <div class="min-h-[400px]">
                    <div v-if="activeTab === 'songs'">
                        <IndexMusicList :songs="result.songs" />
                    </div>

                    <div v-if="activeTab === 'artists'" class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4">
                        <div v-for="artist in result.artists" :key="artist.name"
                            class="group p-4 rounded-xl bg-[#151925]/50 hover:bg-[#1a2035] border border-white/5 hover:border-white/10 transition-all cursor-pointer">
                            <div class="text-center">
                                <div
                                    class="w-20 h-20 rounded-full mx-auto mb-3 overflow-hidden bg-gradient-to-br from-blue-500/20 to-purple-500/20 flex items-center justify-center">
                                    <img v-if="artist.avatar" :src="artist.avatar" :alt="artist.name"
                                        class="w-full h-full object-cover" />
                                    <Icon v-else icon="solar:user-bold" class="text-3xl text-blue-400/50" />
                                </div>

                                <h3 class="text-white font-medium truncate group-hover:text-blue-300 transition-colors">
                                    {{ artist.name }}
                                </h3>
                                <p class="text-xs text-gray-500 mt-1">{{ artist.songCount }} 首歌曲</p>
                            </div>
                        </div>
                    </div>

                    <div v-if="activeTab === 'albums'" class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-4">
                        <div v-for="album in result.albums" :key="album.name"
                            class="group p-3 rounded-xl bg-[#151925]/50 hover:bg-[#1a2035] border border-white/5 hover:border-white/10 transition-all cursor-pointer">
                            <div class="w-full aspect-square rounded-lg mb-3 overflow-hidden bg-[#0a0f1a]">
                                <img :src="album.coverUrl || defaultCover" :alt="album.name"
                                    class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300" />
                            </div>

                            <h3
                                class="text-white font-medium text-sm truncate group-hover:text-blue-300 transition-colors">
                                {{ album.name }}
                            </h3>
                            <p class="text-xs text-gray-500 truncate mt-1">{{ album.artist }}</p>
                            <p class="text-xs text-gray-600 mt-0.5">{{ album.songCount }} 首歌曲</p>
                        </div>
                    </div>
                </div>
            </template>
        </div>
    </AuroraBackground>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { Icon } from '@iconify/vue'
import type { Song } from '~/types/music'

const route = useRoute()
const keyword = computed(() => route.query.q as string)
const activeTab = ref('songs')
const isLoading = ref(true)
const result = ref<SearchResultVO | null>(null)

const defaultCover = 'https://images.unsplash.com/photo-1470225620780-dba8ba36b745?q=80&w=300&auto=format&fit=crop'

interface ArtistVO {
    name: string
    songCount: number
    avatar?: string
}

interface AlbumVO {
    name: string
    artist: string
    coverUrl: string
    songCount: number
}

interface SearchResultVO {
    songs: Song[]
    artists: ArtistVO[]
    albums: AlbumVO[]
    totalSongs: number
    totalArtists: number
    totalAlbums: number
}

interface Result<T> {
    code: number
    msg: string
    data: T
}

const performSearch = async () => {
    if (!keyword.value) {
        isLoading.value = false
        return
    }

    isLoading.value = true

    try {
        const response = await $fetch<Result<SearchResultVO>>(`/api/music/search?keyword=${encodeURIComponent(keyword.value)}`)

        if (response.code === 200) {
            result.value = response.data
        } else {
            console.error('搜索失败:', response.msg)
            result.value = null
        }
    } catch (error) {
        console.error('搜索请求失败:', error)
        result.value = null
    } finally {
        isLoading.value = false
    }
}

watch(keyword, () => {
    performSearch()
})

onMounted(() => {
    performSearch()
})
</script>