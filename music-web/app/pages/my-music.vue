<template>
    <AuroraBackground class="min-h-screen w-full bg-[#050A14] block">
        <div class="relative z-10 w-full min-h-screen pt-24 pb-32 px-4 lg:px-8 max-w-7xl mx-auto space-y-12">

            <div
                class="flex flex-col md:flex-row items-start md:items-end justify-between gap-4 border-b border-white/5 pb-6">
                <div class="space-y-2">
                    <div
                        class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-blue-500/10 border border-blue-400/20 mb-2">
                        <span class="w-1.5 h-1.5 rounded-full bg-blue-400 animate-pulse"></span>
                        <span class="text-xs font-mono text-blue-300 tracking-widest uppercase">My Library</span>
                    </div>
                    <h1 class="text-4xl font-black text-white tracking-tight">
                        我的音乐空间
                    </h1>
                </div>
                <button @click="showCreateModal = true"
                    class="btn border-0 bg-white text-black hover:bg-gray-200 font-bold px-6 rounded-full shadow-[0_0_15px_rgba(255,255,255,0.1)] transition-transform hover:scale-105 active:scale-95 flex items-center gap-2">
                    <span class="text-lg">+</span> 新建歌单
                </button>
            </div>

            <div v-if="!isLoggedIn" class="flex flex-col items-center justify-center py-32 space-y-6">
                <div class="w-20 h-20 rounded-full bg-white/5 flex items-center justify-center text-4xl mb-2">🔒</div>
                <p class="text-gray-400 text-lg">请登录以访问您的云端歌单</p>
                <NuxtLink to="/login"
                    class="btn btn-wide bg-blue-600 hover:bg-blue-500 text-white border-0 rounded-full shadow-lg shadow-blue-900/20">
                    立即登录
                </NuxtLink>
            </div>

            <template v-else>
                <div v-if="isLoading" class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-6">
                    <div v-for="i in 5" :key="i" class="h-64 bg-white/5 rounded-2xl animate-pulse"></div>
                </div>

                <template v-else>
                    <section v-if="defaultPlaylist"
                        class="group relative w-full overflow-hidden rounded-3xl border border-white/10 cursor-pointer"
                        @click="navigateToPlaylist(defaultPlaylist.id)">
                        <div
                            class="absolute inset-0 bg-gradient-to-r from-pink-900/80 via-purple-900/60 to-[#050A14] transition-all duration-500 group-hover:scale-105">
                        </div>
                        <div
                            class="absolute inset-0 bg-[url('https://www.transparenttextures.com/patterns/stardust.png')] opacity-30 mix-blend-overlay">
                        </div>

                        <div class="relative z-10 p-8 md:p-10 flex items-center justify-between">
                            <div class="flex items-center gap-8">
                                <div
                                    class="relative w-28 h-28 md:w-32 md:h-32 rounded-2xl overflow-hidden shadow-2xl border border-white/20 group-hover:rotate-3 transition-transform duration-500">
                                    <img :src="defaultPlaylist.coverUrl || 'https://images.unsplash.com/photo-1493225255756-d9584f8606e9?q=80&w=400'"
                                        class="w-full h-full object-cover" />
                                    <div class="absolute inset-0 flex items-center justify-center bg-black/20">
                                        <span class="text-4xl text-white drop-shadow-md">❤</span>
                                    </div>
                                </div>
                                <div class="space-y-2">
                                    <h2
                                        class="text-3xl font-black text-white group-hover:text-pink-200 transition-colors">
                                        我喜欢的音乐
                                    </h2>
                                    <div class="flex items-center gap-3 text-sm text-pink-200/60 font-mono">
                                        <span
                                            class="px-2 py-0.5 rounded bg-white/10 border border-white/10">DEFAULT</span>
                                        <span>{{ defaultPlaylist.songCount || 0 }} Songs</span>
                                        <span>·</span>
                                        <span>Created by You</span>
                                    </div>
                                </div>
                            </div>
                            <div
                                class="hidden md:flex w-16 h-16 rounded-full border-2 border-white/20 items-center justify-center group-hover:bg-white group-hover:border-white transition-all duration-300">
                                <span
                                    class="text-white group-hover:text-pink-900 text-2xl ml-1 transition-colors">▶</span>
                            </div>
                        </div>
                    </section>

                    <section class="space-y-6">
                        <div class="flex items-center justify-between px-1">
                            <h2 class="text-xl font-bold text-white flex items-center gap-2">
                                <span class="w-1 h-6 bg-blue-500 rounded-full"></span>
                                创建的歌单
                            </h2>
                            <span class="text-xs font-mono text-gray-500 bg-white/5 px-2 py-1 rounded">{{
                                customPlaylists.length }} PLAYLISTS</span>
                        </div>

                        <div v-if="customPlaylists.length === 0"
                            class="flex flex-col items-center justify-center py-20 border-2 border-dashed border-white/10 rounded-3xl bg-white/[0.02]">
                            <div class="text-6xl mb-4 opacity-50">📂</div>
                            <p class="text-gray-400 mb-6">这里还很空旷，去创造属于你的旋律吧</p>
                            <button @click="showCreateModal = true"
                                class="btn btn-sm btn-ghost text-blue-300 hover:text-white hover:bg-blue-500/10">
                                创建第一个歌单
                            </button>
                        </div>

                        <div v-else class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-4 lg:gap-6">
                            <PlaylistCard v-for="playlist in customPlaylists" :key="playlist.id" :playlist="playlist" />
                        </div>
                    </section>
                </template>
            </template>

            <Transition name="fade">
                <div v-if="showCreateModal"
                    class="fixed inset-0 z-[100] flex items-center justify-center bg-black/80 backdrop-blur-sm p-4"
                    @click.self="showCreateModal = false">

                    <div
                        class="bg-[#151925] rounded-3xl w-full max-w-lg border border-white/10 shadow-[0_0_50px_rgba(0,0,0,0.5)] overflow-hidden transform transition-all">
                        <div class="bg-gradient-to-r from-blue-900/20 to-transparent p-6 border-b border-white/5">
                            <h3 class="text-2xl font-bold text-white">Create Playlist</h3>
                            <p class="text-sm text-gray-500 mt-1">给你的新收藏起个名字吧</p>
                        </div>

                        <form @submit.prevent="handleCreatePlaylist" class="p-8 space-y-6">
                            <div class="space-y-2 group">
                                <label
                                    class="text-xs font-bold text-gray-400 uppercase tracking-wider group-focus-within:text-blue-400 transition-colors">Name</label>
                                <input v-model="createForm.name" type="text" placeholder="My Emotional Mix..."
                                    class="w-full bg-[#0a0f1d] border border-white/10 rounded-xl px-4 py-3.5 text-white placeholder-gray-600 focus:border-blue-500/50 focus:bg-[#0a0f1d]/80 focus:shadow-[0_0_15px_rgba(59,130,246,0.1)] outline-none transition-all"
                                    required />
                            </div>

                            <div class="space-y-2 group">
                                <label
                                    class="text-xs font-bold text-gray-400 uppercase tracking-wider group-focus-within:text-blue-400 transition-colors">Description</label>
                                <textarea v-model="createForm.description" placeholder="关于这个歌单的故事..." rows="3"
                                    class="w-full bg-[#0a0f1d] border border-white/10 rounded-xl px-4 py-3.5 text-white placeholder-gray-600 focus:border-blue-500/50 focus:bg-[#0a0f1d]/80 outline-none transition-all resize-none"></textarea>
                            </div>

                            <div
                                class="flex items-center justify-between p-4 rounded-xl bg-white/5 border border-white/5">
                                <div class="flex flex-col">
                                    <span class="text-sm font-bold text-gray-200">公开可见</span>
                                    <span class="text-xs text-gray-500">允许其他用户搜索并查看此歌单</span>
                                </div>
                                <input v-model="createForm.isPublic" type="checkbox"
                                    class="toggle toggle-info toggle-sm" />
                            </div>

                            <div class="flex gap-4 pt-4">
                                <button type="button" @click="showCreateModal = false"
                                    class="flex-1 btn btn-ghost text-gray-400 hover:text-white hover:bg-white/5 rounded-xl">
                                    取消
                                </button>
                                <button type="submit" :disabled="isCreating"
                                    class="flex-1 btn border-0 bg-gradient-to-r from-blue-600 to-cyan-500 hover:from-blue-500 hover:to-cyan-400 text-white font-bold rounded-xl shadow-lg shadow-blue-500/20 disabled:opacity-50 disabled:shadow-none">
                                    {{ isCreating ? 'Creating...' : 'Confirm' }}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </Transition>

        </div>
    </AuroraBackground>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import type { Playlist, PlaylistCreateReq, Result } from '~/types/music'
import PlaylistCard from '~/components/PlaylistCard.vue'

const isLoggedIn = ref(false)
const isLoading = ref(true)

const playlists = ref<Playlist[]>([])

const defaultPlaylist = computed(() =>
    playlists.value.find(p => p.isDefault === 1)
)
const customPlaylists = computed(() =>
    playlists.value.filter(p => p.isDefault !== 1)
)

const showCreateModal = ref(false)
const isCreating = ref(false)
const createForm = ref<{ name: string; description: string; isPublic: boolean }>({
    name: '',
    description: '',
    isPublic: false
})

const getToken = (): string => {
    if (typeof window === 'undefined') return ''
    return localStorage.getItem('token') || sessionStorage.getItem('token') || ''
}

const fetchMyPlaylists = async () => {
    const token = getToken()
    if (!token) {
        isLoggedIn.value = false
        isLoading.value = false
        return
    }

    isLoggedIn.value = true
    isLoading.value = true

    try {
        const response = await $fetch<Result<Playlist[]>>('/api/playlist/my', {
            headers: { 'Authorization': token }
        })

        if (response.code === 200 && response.data) {
            playlists.value = response.data
        }
    } catch (error) {
        console.error('获取歌单失败:', error)
    } finally {
        isLoading.value = false
    }
}

const handleCreatePlaylist = async () => {
    if (!createForm.value.name.trim()) return
    isCreating.value = true
    try {
        const token = getToken()
        const reqData: PlaylistCreateReq = {
            name: createForm.value.name,
            description: createForm.value.description || undefined,
            isPublic: createForm.value.isPublic ? 1 : 0
        }
        const response = await $fetch<Result<string>>('/api/playlist/create', {
            method: 'POST',
            headers: { 'Authorization': token, 'Content-Type': 'application/json' },
            body: reqData
        })

        if (response.code === 200) {
            await fetchMyPlaylists()
            createForm.value = { name: '', description: '', isPublic: false }
            showCreateModal.value = false
        } else {
            alert(response.msg || '创建失败')
        }
    } catch (error) {
        console.error('创建失败:', error)
    } finally {
        isCreating.value = false
    }
}

const router = useRouter()
const navigateToPlaylist = (id: number) => {
    router.push(`/playlists/${id}`)
}

onMounted(() => {
    fetchMyPlaylists()
})
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}
</style>