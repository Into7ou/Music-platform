<template>
    <Teleport to="body">
        <div v-if="visible" class="fixed inset-0 z-[10000] flex items-center justify-center">
            <!-- 遮罩层 -->
            <div class="absolute inset-0 bg-black/60 backdrop-blur-sm" @click="close"></div>

            <!-- 弹窗主体 -->
            <div class="relative bg-[#1a1f2e] rounded-2xl p-6 w-[360px] max-h-[70vh] overflow-hidden 
                        border border-white/10 shadow-2xl animate-scale-in">
                <h3 class="text-lg font-bold text-white mb-4">添加到歌单</h3>

                <!-- 歌单列表 -->
                <div class="space-y-2 max-h-[50vh] overflow-y-auto">
                    <div v-for="playlist in playlists" :key="playlist.id" @click="addToPlaylist(playlist.id)"
                        class="flex items-center gap-3 p-3 rounded-lg hover:bg-white/5 cursor-pointer group">
                        <img :src="playlist.coverUrl || defaultCover" class="w-12 h-12 rounded-lg object-cover" />
                        <div class="flex-1">
                            <p class="text-sm text-white font-medium">{{ playlist.name }}</p>
                            <p class="text-xs text-gray-500">{{ playlist.songCount || 0 }} 首歌曲</p>
                        </div>
                        <Icon icon="solar:alt-arrow-right-linear" class="text-gray-500 group-hover:text-blue-400" />
                    </div>
                </div>

                <!-- 新建歌单按钮 -->
                <button class="w-full mt-4 py-3 rounded-xl bg-blue-600 hover:bg-blue-500 text-white font-medium">
                    + 新建歌单
                </button>
            </div>
        </div>
    </Teleport>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { Icon } from '@iconify/vue'
import type { Playlist } from '~/types/music'
import type { Result } from '~/types/music'

const props = defineProps<{
    visible: boolean
    songId: number | null
}>()

const emit = defineEmits(['update:visible', 'added'])

const playlists = ref<Playlist[]>([])

const defaultCover = 'https://images.unsplash.com/photo-1470225620780-dba8ba36b745?q=80&w=100&auto=format&fit=crop'

const isLiked = ref(false)
const defaultPlaylistId = ref<number | null>(null)

const getToken = (): string | null => {
    return localStorage.getItem('token') || sessionStorage.getItem('token')
}

// 当弹窗打开时，拉取歌单列表
watch(() => props.visible, async (val) => {
    if (val) {
        await fetchPlaylists()
    }
})

// 获取用户歌单
const fetchPlaylists = async () => {
    const token = getToken()

    if (!token) {
        console.warn('未登录，无法获取歌单')
        return
    }

    const res = await $fetch<Result<Playlist[]>>('/api/playlist/my', {  // ✅ 添加类型
        headers: { 'Authorization': token }
    })
    if (res.code === 200) {
        playlists.value = res.data
    }
}

// 添加歌曲到歌单
const addToPlaylist = async (playlistId: number) => {
    const token = getToken()

    if (!token) {
        console.warn('未登录，无法获取歌单')
        return
    }

    const res = await $fetch<Result<string>>(`/api/playlist/${playlistId}/songs/${props.songId}`, {  // ✅ 添加类型
        method: 'POST',
        headers: { 'Authorization': token }
    })
    if (res.code === 200) {
        emit('added', playlistId)
        close()
        // 可以加一个 Toast 提示
    }
}

const close = () => {
    emit('update:visible', false)
}
</script>