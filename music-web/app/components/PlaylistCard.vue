<template>
    <div class="group relative flex flex-col gap-3 p-3 rounded-2xl hover:bg-white/5 transition-all duration-300 border border-transparent hover:border-white/5"
        @click="navigateToDetail">

        <!-- 封面区域 -->
        <div class="relative aspect-square rounded-xl overflow-hidden shadow-2xl border border-white/5 bg-[#151925]">
            <!-- 图片 -->
            <img :src="playlist.coverUrl || defaultCover" :alt="playlist.name"
                class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110 opacity-90 group-hover:opacity-100" />

            <!-- 渐变遮罩 (底部向上) -->
            <div class="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent opacity-60">
            </div>

            <!-- 悬浮播放按钮 -->
            <div
                class="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-all duration-300 bg-black/20 backdrop-blur-[1px]">
                <div
                    class="w-12 h-12 bg-white/90 rounded-full flex items-center justify-center pl-1 shadow-[0_0_20px_rgba(255,255,255,0.3)] scale-50 group-hover:scale-100 transition-transform duration-300 hover:scale-110">
                    <span class="text-black text-xl ml-0.5">▶</span>
                </div>
            </div>

            <!-- 状态标签组 -->
            <div class="absolute top-2 right-2 flex flex-col gap-1 items-end">
                <!-- 歌曲数量 -->
                <div
                    class="px-2 py-1 rounded-md bg-black/60 backdrop-blur-md text-[10px] font-mono text-gray-300 border border-white/5">
                    {{ playlist.songCount || 0 }} tracks
                </div>
            </div>

            <!-- 私有/公开标识 -->
            <div v-if="playlist.isPublic === 0"
                class="absolute top-2 left-2 px-2 py-1 rounded-md bg-red-500/80 backdrop-blur-md text-[10px] text-white font-bold shadow-sm border border-red-400/20">
                PRIVATE
            </div>
        </div>

        <!-- 底部信息 -->
        <div class="px-1 space-y-1">
            <h3 class="text-sm font-bold text-gray-100 group-hover:text-blue-300 transition-colors line-clamp-1">
                {{ playlist.name }}
            </h3>
            <p class="text-xs text-gray-500 line-clamp-1 group-hover:text-gray-400 transition-colors">
                {{ playlist.description || 'No description provided.' }}
            </p>
        </div>
    </div>
</template>

<script setup lang="ts">
import type { Playlist } from '~/types/music'

const props = defineProps<{
    playlist: Playlist
}>()

const router = useRouter()
// 使用更符合 MyGO 氛围的默认封面
const defaultCover = 'https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?q=80&w=600&auto=format&fit=crop'

const navigateToDetail = () => {
    router.push(`/playlists/${props.playlist.id}`)
}
</script>