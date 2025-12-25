<template>
    <AuroraBackground class="min-h-screen w-full bg-[#050A14] block">
        <div class="relative z-10 w-full min-h-screen pt-24 pb-32 px-4 lg:px-8 max-w-7xl mx-auto">

            <!-- 加载状态 -->
            <div v-if="isLoading" class="flex justify-center items-center py-20">
                <div class="text-gray-400 animate-pulse text-lg">正在加载歌单...</div>
            </div>

            <!-- 歌单不存在 -->
            <div v-else-if="!playlist" class="flex flex-col items-center justify-center py-20">
                <p class="text-gray-400 text-xl mb-6">歌单不存在或已被删除</p>
                <NuxtLink to="/" class="btn bg-blue-500 hover:bg-blue-600 text-white border-0 rounded-lg">
                    返回首页
                </NuxtLink>
            </div>

            <template v-else>
                <!-- 歌单头部信息 -->
                <section class="flex flex-col md:flex-row gap-8 mb-12">
                    <!-- 封面 -->
                    <div class="w-full md:w-64 lg:w-80 flex-shrink-0">
                        <div class="aspect-square rounded-2xl overflow-hidden shadow-2xl border border-white/10">
                            <img :src="playlist.coverUrl || defaultCover" :alt="playlist.name"
                                class="w-full h-full object-cover" />
                        </div>
                    </div>

                    <!-- 歌单信息 -->
                    <div class="flex-1 flex flex-col justify-center space-y-4">
                        <div class="flex items-center gap-3">
                            <span
                                class="px-3 py-1 rounded-full bg-blue-500/20 border border-blue-400/30 text-xs text-blue-200">
                                {{ playlist.isPublic === 1 ? '公开歌单' : '私有歌单' }}
                            </span>
                            <span v-if="playlist.isDefault === 1"
                                class="px-3 py-1 rounded-full bg-pink-500/20 border border-pink-400/30 text-xs text-pink-200">
                                默认歌单
                            </span>
                        </div>

                        <h1 class="text-3xl lg:text-4xl font-black text-white">
                            {{ playlist.name }}
                        </h1>

                        <p class="text-gray-400 text-sm lg:text-base max-w-xl">
                            {{ playlist.description || '暂无描述' }}
                        </p>

                        <div class="flex items-center gap-6 text-sm text-gray-500">
                            <span>🎵 {{ songs.length }} 首歌曲</span>
                            <span v-if="playlist.playCount">🎧 {{ playlist.playCount }} 次播放</span>
                            <span>📅 {{ formatDate(playlist.createTime) }}</span>
                        </div>

                        <!-- 操作按钮 -->
                        <div class="flex gap-4 pt-4">
                            <button @click="playAll"
                                class="btn border-0 bg-white text-black hover:bg-gray-200 rounded-full px-8 font-bold shadow-lg">
                                <span class="mr-1">▶</span> 播放全部
                            </button>

                            <button v-if="isOwner" @click="showEditModal = true"
                                class="btn btn-outline border-white/30 text-white hover:bg-white/10 rounded-full px-6">
                                编辑歌单
                            </button>

                            <button v-if="isOwner && playlist.isDefault !== 1" @click="handleDeletePlaylist"
                                class="btn btn-outline border-red-500/50 text-red-400 hover:bg-red-500/20 rounded-full px-6">
                                删除歌单
                            </button>
                        </div>
                    </div>
                </section>

                <!-- 歌曲列表 -->
                <section class="space-y-4">
                    <h2 class="text-xl font-bold text-white flex items-center gap-2">
                        <span class="text-blue-400">♪</span> 歌曲列表
                    </h2>

                    <div v-if="songs.length === 0" class="py-12 text-center text-gray-500">
                        歌单里还没有歌曲
                    </div>

                    <div v-else class="space-y-2">
                        <div v-for="(song, index) in songs" :key="song.id" @click="playSong(song)"
                            class="group flex items-center gap-4 p-4 rounded-xl bg-[#151925]/50 hover:bg-[#1a2035] border border-white/5 hover:border-white/10 transition-all cursor-pointer">
                            <!-- 序号 -->
                            <div class="w-8 text-center text-gray-500 group-hover:text-blue-400 font-mono">
                                {{ String(index + 1).padStart(2, '0') }}
                            </div>

                            <!-- 封面 -->
                            <div class="w-12 h-12 rounded-lg overflow-hidden bg-[#0a0f1a] flex-shrink-0">
                                <img :src="song.coverUrl || defaultCover" :alt="song.title"
                                    class="w-full h-full object-cover" />
                            </div>

                            <!-- 歌曲信息 -->
                            <div class="flex-1 min-w-0">
                                <h4 class="text-white font-medium truncate group-hover:text-blue-300 transition-colors">
                                    {{ song.title }}
                                </h4>
                                <p class="text-sm text-gray-500 truncate">{{ song.artist }}</p>
                            </div>

                            <!-- 专辑 -->
                            <div class="hidden md:block w-40 text-sm text-gray-500 truncate">
                                {{ song.album || '-' }}
                            </div>

                            <!-- 时长 -->
                            <div class="text-sm text-gray-500 w-16 text-right">
                                {{ formatDuration(song.duration) }}
                            </div>

                            <!-- 操作按钮 -->
                            <div class="flex items-center gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                                <button v-if="isOwner" @click.stop="handleRemoveSong(song.id)"
                                    class="btn btn-sm btn-ghost text-red-400 hover:bg-red-500/20" title="从歌单移除">
                                    ✕
                                </button>
                            </div>
                        </div>
                    </div>
                </section>
            </template>

            <!-- 编辑歌单弹窗 -->
            <div v-if="showEditModal && playlist"
                class="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm"
                @click.self="showEditModal = false">
                <div class="bg-[#151925] rounded-2xl p-8 w-full max-w-md border border-white/10 shadow-2xl">
                    <h3 class="text-xl font-bold text-white mb-6">编辑歌单</h3>

                    <form @submit.prevent="handleUpdatePlaylist" class="space-y-5">
                        <div>
                            <label class="block text-sm text-gray-400 mb-2">歌单名称</label>
                            <input v-model="editForm.name" type="text"
                                class="w-full px-4 py-3 rounded-lg bg-[#0a0f1a] border border-white/10 text-white focus:border-blue-500 focus:outline-none transition-colors" />
                        </div>

                        <div>
                            <label class="block text-sm text-gray-400 mb-2">歌单描述</label>
                            <textarea v-model="editForm.description" rows="3"
                                class="w-full px-4 py-3 rounded-lg bg-[#0a0f1a] border border-white/10 text-white focus:border-blue-500 focus:outline-none transition-colors resize-none"></textarea>
                        </div>

                        <!-- ✅ 新增：封面上传 -->
                        <div>
                            <label class="block text-sm text-gray-400 mb-2">歌单封面</label>
                            <div class="flex items-center gap-4">
                                <!-- 封面预览 -->
                                <div v-if="coverPreviewUrl"
                                    class="relative w-24 h-24 rounded-lg overflow-hidden border border-white/20 shadow-lg flex-shrink-0">
                                    <img :src="coverPreviewUrl" alt="封面预览" class="w-full h-full object-cover" />
                                    <button type="button" @click="removeCover"
                                        class="absolute top-1 right-1 w-6 h-6 rounded-full bg-black/70 text-white flex items-center justify-center hover:bg-red-500 transition-colors text-xs">
                                        ✕
                                    </button>
                                </div>

                                <!-- 上传按钮 -->
                                <div :class="coverPreviewUrl ? '' : 'flex-1'">
                                    <input type="file" ref="coverFileInput" accept="image/*"
                                        @change="handleCoverSelected" class="hidden" />
                                    <button type="button" @click="($refs.coverFileInput as HTMLInputElement)?.click()"
                                        class="px-4 py-2 rounded-lg bg-[#0a0f1a] border border-white/10 text-gray-300 hover:border-blue-500 hover:text-white transition-all text-sm">
                                        {{ coverPreviewUrl ? '更换封面' : '选择封面' }}
                                    </button>
                                </div>
                            </div>
                            <p class="text-xs text-gray-600 mt-2">支持 JPG、PNG 格式，建议尺寸 300x300</p>
                        </div>

                        <div class="flex items-center gap-3">
                            <input v-model="editForm.isPublic" type="checkbox" id="editIsPublic"
                                class="w-5 h-5 rounded bg-[#0a0f1a] border-white/20 text-blue-500" />
                            <label for="editIsPublic" class="text-gray-300">公开歌单</label>
                        </div>

                        <div class="flex gap-4 pt-4">
                            <button type="button" @click="showEditModal = false"
                                class="flex-1 btn btn-outline border-white/20 text-gray-300 hover:bg-white/10 rounded-lg">
                                取消
                            </button>
                            <button type="submit" :disabled="isUpdating"
                                class="flex-1 btn border-0 bg-gradient-to-r from-[#3b5bdb] to-[#228be6] text-white font-bold rounded-lg disabled:opacity-50">
                                {{ isUpdating ? '保存中...' : '保存' }}
                            </button>
                        </div>
                    </form>
                </div>
            </div>

        </div>
    </AuroraBackground>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import type { Playlist, Song, PlaylistUpdateReq, Result } from '~/types/music'
import { usePlayerStore } from '~/stores/usePlayer'

const route = useRoute()
const router = useRouter()
const playerStore = usePlayerStore()

const playlistId = computed(() => Number(route.params.id))

// 数据状态
const playlist = ref<Playlist | null>(null)
const songs = ref<Song[]>([])
const isLoading = ref(true)
const currentUserId = ref<number | null>(null)

// 编辑相关
const showEditModal = ref(false)
const isUpdating = ref(false)
const coverFileInput = ref<HTMLInputElement | null>(null)
const editForm = ref({
    name: '',
    description: '',
    isPublic: false,
    coverUrl: ''  // ✅ 新增：封面URL
})

// ✅ 新增：封面上传相关
const selectedCoverFile = ref<File | null>(null)
const coverPreviewUrl = ref('')

const defaultCover = 'https://images.unsplash.com/photo-1493225255756-d9584f8606e9?q=80&w=400&auto=format&fit=crop'

// 判断是否为歌单拥有者
const isOwner = computed(() => {
    if (!playlist.value || !currentUserId.value) return false
    return playlist.value.userId === currentUserId.value
})

// 获取 Token
const getToken = () => {
    return localStorage.getItem('token') || sessionStorage.getItem('token') || ''
}

const getCurrentUserIdFromToken = () => {
    const token = getToken()
    if (!token) {
        currentUserId.value = null
        return
    }

    try {
        // JWT 格式: header.payload.signature
        const payload = token.split('.')[1]
        if (!payload) {
            currentUserId.value = null
            return
        }

        const decoded = JSON.parse(atob(payload))
        currentUserId.value = decoded.userId || decoded.id || null
    } catch (error) {
        console.error('解析 Token 失败:', error)
        currentUserId.value = null
    }
}

// 获取歌单详情
const fetchPlaylistDetail = async () => {
    isLoading.value = true
    try {
        const response = await $fetch<Result<Playlist>>(`/api/playlist/${playlistId.value}`)
        if (response.code === 200 && response.data) {
            playlist.value = response.data
            editForm.value = {
                name: response.data.name,
                description: response.data.description || '',
                isPublic: response.data.isPublic === 1,
                coverUrl: response.data.coverUrl || ''
            }
            coverPreviewUrl.value = response.data.coverUrl || ''
        }
    } catch (error) {
        console.error('获取歌单详情失败:', error)
    }
}

// 获取歌单歌曲
const fetchPlaylistSongs = async () => {
    try {
        const response = await $fetch<Result<Song[]>>(`/api/playlist/${playlistId.value}/songs`)
        if (response.code === 200 && response.data) {
            songs.value = response.data
        }
    } catch (error) {
        console.error('获取歌单歌曲失败:', error)
    } finally {
        isLoading.value = false
    }
}

// ✅ 新增：处理封面选择
const handleCoverSelected = (event: Event) => {
    const input = event.target as HTMLInputElement
    const file = input.files?.[0]

    if (!file) return

    // 释放旧的预览 URL
    if (coverPreviewUrl.value && coverPreviewUrl.value !== editForm.value.coverUrl) {
        URL.revokeObjectURL(coverPreviewUrl.value)
    }

    selectedCoverFile.value = file
    coverPreviewUrl.value = URL.createObjectURL(file)
}

// ✅ 新增：移除封面
const removeCover = () => {
    if (coverPreviewUrl.value && coverPreviewUrl.value !== editForm.value.coverUrl) {
        URL.revokeObjectURL(coverPreviewUrl.value)
    }
    selectedCoverFile.value = null
    coverPreviewUrl.value = ''
    editForm.value.coverUrl = ''
}

// ✅ 新增：上传封面到 MinIO
const uploadCoverToMinio = async (file: File): Promise<string | null> => {
    try {
        const formData = new FormData()
        formData.append('cover', file)

        // 复用音乐上传的封面上传逻辑，或创建专门的接口
        // 这里假设使用临时方案：直接在更新时上传
        const response = await fetch('/api/playlist/upload-cover', {
            method: 'POST',
            body: formData
        })

        if (response.ok) {
            const data = await response.json()
            return data.data.url
        }

        return null
    } catch (error) {
        console.error('上传封面失败:', error)
        return null
    }
}

// 更新歌单
const handleUpdatePlaylist = async () => {
    isUpdating.value = true
    try {
        const token = getToken()

        // ✅ 如果有新封面，先上传
        let coverUrl = editForm.value.coverUrl
        if (selectedCoverFile.value) {
            const uploadedUrl = await uploadCoverToMinio(selectedCoverFile.value)
            if (uploadedUrl) {
                coverUrl = uploadedUrl
            }
        }

        const reqData: PlaylistUpdateReq = {
            name: editForm.value.name,
            description: editForm.value.description || undefined,
            isPublic: editForm.value.isPublic ? 1 : 0,
            coverUrl: coverUrl  // ✅ 新增：包含封面URL
        }

        const response = await $fetch<Result<string>>(`/api/playlist/${playlistId.value}`, {
            method: 'PUT',
            headers: {
                'Authorization': token,
                'Content-Type': 'application/json'
            },
            body: reqData
        })

        if (response.code === 200) {
            await fetchPlaylistDetail()
            showEditModal.value = false
            // ✅ 清理临时文件
            if (selectedCoverFile.value && coverPreviewUrl.value) {
                URL.revokeObjectURL(coverPreviewUrl.value)
                selectedCoverFile.value = null
            }
        } else {
            alert(response.msg || '更新失败')
        }
    } catch (error) {
        console.error('更新歌单失败:', error)
    } finally {
        isUpdating.value = false
    }
}

// 删除歌单
const handleDeletePlaylist = async () => {
    if (!confirm('确定要删除这个歌单吗？此操作不可撤销。')) return

    try {
        const token = getToken()
        const response = await $fetch<Result<string>>(`/api/playlist/${playlistId.value}`, {
            method: 'DELETE',
            headers: { 'Authorization': token }
        })

        if (response.code === 200) {
            router.push('/my-music')
        } else {
            alert(response.msg || '删除失败')
        }
    } catch (error) {
        console.error('删除歌单失败:', error)
    }
}

// 移除歌曲
const handleRemoveSong = async (songId: number) => {
    if (!confirm('确定要从歌单中移除这首歌吗？')) return

    try {
        const token = getToken()
        const response = await $fetch<Result<string>>(`/api/playlist/${playlistId.value}/songs/${songId}`, {
            method: 'DELETE',
            headers: { 'Authorization': token }
        })

        if (response.code === 200) {
            songs.value = songs.value.filter(s => s.id !== songId)
        } else {
            alert(response.msg || '移除失败')
        }
    } catch (error) {
        console.error('移除歌曲失败:', error)
    }
}

// 播放全部
// 播放全部
const playAll = () => {
    if (songs.value.length === 0) {
        console.warn('歌单中没有歌曲')
        return
    }

    // ✅ 设置播放列表并从第一首开始播放
    playerStore.setPlaylist(songs.value, 0)
}

// ✅ 新增：播放单首歌曲
const playSong = (song: Song) => {
    if (!song.fileUrl) {
        console.error('歌曲缺少音频文件 URL:', song)
        return
    }

    playerStore.play(song)
}

// 格式化时长
const formatDuration = (seconds?: number) => {
    if (!seconds) return '0:00'
    const mins = Math.floor(seconds / 60)
    const secs = seconds % 60
    return `${mins}:${String(secs).padStart(2, '0')}`
}

// 格式化日期
const formatDate = (dateStr?: string) => {
    if (!dateStr) return '-'
    return new Date(dateStr).toLocaleDateString('zh-CN')
}

// 获取当前用户ID (简化处理)
const checkCurrentUser = () => {
    const token = getToken()

    if (!token) {
        currentUserId.value = null
        return
    }

    try {
        const payload = token.split('.')[1]
        if (!payload) {
            currentUserId.value = null
            return
        }

        const decoded = JSON.parse(atob(payload))


        currentUserId.value = decoded.userId || decoded.id || decoded.uid || decoded.sub || null

    } catch (error) {
        console.error('❌ 解析 Token 失败:', error)
        currentUserId.value = null
    }
}

onMounted(async () => {
    await checkCurrentUser()
    await fetchPlaylistDetail()
    await fetchPlaylistSongs()
})
</script>