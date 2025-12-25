<template>
    <AuroraBackground class="min-h-screen w-full bg-[#050A14] block">
        <div class="relative z-10 w-full min-h-screen pt-24 pb-32 px-4 lg:px-8 max-w-7xl mx-auto space-y-8">

            <!-- 页面标题 -->
            <div class="flex items-center justify-between">
                <h1 class="text-3xl font-bold text-white flex items-center gap-3">
                    <span class="text-blue-400">#</span> 歌单广场
                </h1>
            </div>

            <!-- 加载状态 -->
            <div v-if="isLoading" class="flex justify-center items-center py-20">
                <div class="text-gray-400 animate-pulse text-lg">正在加载歌单...</div>
            </div>

            <template v-else>
                <!-- 无歌单 -->
                <div v-if="playlists.length === 0" class="flex flex-col items-center py-20 text-gray-500">
                    <p class="text-xl">暂无公开歌单</p>
                </div>

                <!-- 歌单网格 -->
                <div v-else class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-6">
                    <PlaylistCard v-for="playlist in playlists" :key="playlist.id" :playlist="playlist" />
                </div>

                <!-- 加载更多 -->
                <div v-if="hasMore" class="flex justify-center pt-8">
                    <button @click="loadMore" :disabled="isLoadingMore"
                        class="btn btn-outline border-white/20 text-gray-300 hover:bg-white/10 rounded-lg px-8">
                        {{ isLoadingMore ? '加载中...' : '加载更多' }}
                    </button>
                </div>
            </template>

        </div>
    </AuroraBackground>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { Playlist, PageResult, Result } from '~/types/music'
import PlaylistCard from '~/components/PlaylistCard.vue'

const playlists = ref<Playlist[]>([])
const isLoading = ref(true)
const isLoadingMore = ref(false)
const page = ref(1)
const size = 15
const total = ref(0)

const hasMore = computed(() => playlists.value.length < total.value)

const fetchPublicPlaylists = async (append = false) => {
    if (append) {
        isLoadingMore.value = true
    } else {
        isLoading.value = true
    }

    try {
        const response = await $fetch<Result<PageResult<Playlist>>>('/api/playlist/public', {
            params: {
                page: page.value,
                size: size
            }
        })

        if (response.code === 200 && response.data) {
            total.value = response.data.total
            if (append) {
                playlists.value = [...playlists.value, ...response.data.list]
            } else {
                playlists.value = response.data.list
            }
        }
    } catch (error) {
        console.error('获取公开歌单失败:', error)
    } finally {
        isLoading.value = false
        isLoadingMore.value = false
    }
}

const loadMore = () => {
    page.value++
    fetchPublicPlaylists(true)
}

onMounted(() => {
    fetchPublicPlaylists()
})
</script>