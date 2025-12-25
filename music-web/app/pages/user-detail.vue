<template>
    <AuroraBackground class="min-h-screen w-full bg-[#050A14] block">
        <div class="relative z-10 w-full min-h-screen pt-24 pb-32 px-4 lg:px-8 max-w-6xl mx-auto space-y-10">

            <div class="flex items-center justify-between border-b border-white/5 pb-4">
                <div class="flex items-center gap-2">
                    <span class="w-1.5 h-1.5 bg-blue-500 rounded-full animate-pulse"></span>
                    <span class="text-xs font-mono text-blue-300 tracking-[0.2em] uppercase">个人信息</span>
                </div>
            </div>

            <section
                class="relative rounded-[2rem] overflow-hidden border border-white/10 bg-[#151925]/80 backdrop-blur-2xl shadow-[0_0_50px_rgba(0,0,0,0.5)] group">

                <div
                    class="absolute top-0 left-0 w-full h-1 bg-gradient-to-r from-transparent via-blue-500 to-transparent opacity-50">
                </div>
                <div
                    class="absolute -right-20 -top-20 w-64 h-64 bg-blue-600/10 rounded-full blur-[80px] group-hover:bg-blue-600/20 transition-all duration-700">
                </div>
                <div
                    class="absolute -left-20 -bottom-20 w-64 h-64 bg-purple-600/10 rounded-full blur-[80px] group-hover:bg-purple-600/20 transition-all duration-700">
                </div>

                <div
                    class="absolute inset-0 bg-[url('https://www.transparenttextures.com/patterns/stardust.png')] opacity-10 mix-blend-overlay pointer-events-none">
                </div>

                <div class="relative z-10 p-8 md:p-12 flex flex-col md:flex-row gap-10 items-start md:items-center">

                    <div class="relative flex-shrink-0 group/avatar">
                        <div
                            class="w-36 h-36 md:w-44 md:h-44 rounded-full p-1 bg-gradient-to-br from-blue-500/30 to-purple-500/30 border border-white/10 relative z-20">
                            <div class="w-full h-full rounded-full overflow-hidden relative">
                                <img :src="userDetail?.avatar || '/defaultAvatar.png'"
                                    :alt="userDetail?.nickname || 'Avatar'"
                                    class="w-full h-full object-cover transition-transform duration-700 group-hover/avatar:scale-110"
                                    @error="handleImageError" />

                                <div class="absolute inset-0 bg-black/40 opacity-0 group-hover/avatar:opacity-100 transition-all duration-300 flex items-center justify-center cursor-pointer backdrop-blur-[2px]"
                                    @click="showAvatarModal = true">
                                    <div class="text-center">
                                        <svg class="w-6 h-6 mx-auto text-white mb-1" fill="none" stroke="currentColor"
                                            viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M3 9a2 2 0 012-2h.93a2 2 0 001.664-.89l.812-1.22A2 2 0 0110.07 4h3.86a2 2 0 011.664.89l.812 1.22A2 2 0 0018.07 7H19a2 2 0 012 2v9a2 2 0 01-2 2H5a2 2 0 01-2-2V9z" />
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M15 13a3 3 0 11-6 0 3 3 0 016 0z" />
                                        </svg>
                                        <span
                                            class="text-[10px] uppercase font-bold tracking-wider text-white">更换头像</span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div
                            class="absolute inset-0 rounded-full border border-white/5 scale-110 animate-spin-slow pointer-events-none border-dashed opacity-30">
                        </div>
                    </div>

                    <div class="flex-1 space-y-6 w-full">
                        <div class="space-y-3">
                            <div class="flex flex-col md:flex-row md:items-end gap-3 md:gap-4">
                                <h1 class="text-4xl md:text-5xl font-black text-white tracking-tight">
                                    {{ userDetail?.nickname || 'Loading...' }}
                                </h1>
                                <div class="flex items-center gap-2 pb-1.5">
                                    <span
                                        class="px-2 py-0.5 rounded bg-blue-500/20 border border-blue-400/20 text-[10px] font-mono text-blue-300 font-bold uppercase tracking-wider">
                                        LV.{{ Math.floor((userStats?.playlistCount || 0) / 2) + 1 }}
                                    </span>
                                    <span class="text-sm font-mono text-gray-500">@{{ userDetail?.username }}</span>
                                </div>
                            </div>

                            <div
                                class="relative p-4 rounded-xl bg-white/5 border-l-2 border-blue-500/50 backdrop-blur-sm">
                                <span class="absolute -top-2 left-2 text-2xl text-blue-500/20">"</span>
                                <p class="text-gray-300 text-sm leading-relaxed italic pl-2">
                                    {{ userDetail?.bio || 'Like a stray star, waiting to be found.' }}
                                </p>
                            </div>
                        </div>

                        <div
                            class="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-6 pt-4 border-t border-white/5">
                            <div class="flex gap-4">
                                <div
                                    class="group/stat flex items-center gap-3 px-4 py-2 rounded-xl bg-[#0a0f1d] border border-white/5 hover:border-purple-500/30 transition-colors">
                                    <div class="text-2xl group-hover/stat:scale-110 transition-transform">💿</div>
                                    <div>
                                        <div class="text-lg font-bold text-white leading-none">{{
                                            userStats?.playlistCount || 0 }}</div>
                                        <div class="text-[10px] text-gray-500 uppercase tracking-wide mt-1">创建的歌单
                                        </div>
                                    </div>
                                </div>
                                <div
                                    class="group/stat flex items-center gap-3 px-4 py-2 rounded-xl bg-[#0a0f1d] border border-white/5 hover:border-pink-500/30 transition-colors">
                                    <div class="text-2xl group-hover/stat:scale-110 transition-transform">❤</div>
                                    <div>
                                        <div class="text-lg font-bold text-white leading-none">{{
                                            userStats?.likedSongCount || 0 }}</div>
                                        <div class="text-[10px] text-gray-500 uppercase tracking-wide mt-1">喜欢的歌曲数</div>
                                    </div>
                                </div>
                            </div>

                            <button @click="showEditModal = true"
                                class="btn btn-outline border-white/20 text-white hover:bg-white/10 hover:text-blue-300 hover:border-blue-400/50 rounded-full px-6 transition-all group/btn w-full sm:w-auto">
                                <span>编辑资料</span>
                                <svg class="w-4 h-4 group-hover/btn:translate-x-1 transition-transform" fill="none"
                                    stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M14 5l7 7m0 0l-7 7m7-7H3"></path>
                                </svg>
                            </button>
                        </div>
                    </div>
                </div>

                <div class="absolute bottom-4 right-6 text-[10px] font-mono text-white/10 select-none">
                    ID: {{ userDetail?.id ? String(userDetail.id).padStart(8, '0') : '00000000' }} // JOINED: {{
                        formatDate(userDetail?.createTime) }}
                </div>
            </section>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6 opacity-60 pointer-events-none grayscale">
                <div class="h-32 rounded-2xl border border-white/5 bg-white/[0.02] flex items-center justify-center">
                    <span class="text-xs font-mono text-gray-600">[ MODULE: RECENT_ACTIVITY / OFFLINE ]</span>
                </div>
                <div class="h-32 rounded-2xl border border-white/5 bg-white/[0.02] flex items-center justify-center">
                    <span class="text-xs font-mono text-gray-600">[ MODULE: FAVORITE_ARTIST / OFFLINE ]</span>
                </div>
            </div>


            <Transition name="fade">
                <div v-if="showEditModal"
                    class="fixed inset-0 z-[100] flex items-center justify-center bg-black/80 backdrop-blur-md p-4"
                    @click.self="showEditModal = false">
                    <div
                        class="bg-[#151925] rounded-3xl w-full max-w-lg border border-white/10 shadow-[0_0_60px_rgba(0,0,0,0.6)] overflow-hidden transform transition-all">
                        <div class="bg-gradient-to-r from-blue-900/20 to-transparent p-6 border-b border-white/5">
                            <h3 class="text-2xl font-bold text-white">编辑资料</h3>
                            <p class="text-xs text-gray-500 mt-1 uppercase tracking-widest">更新你的个人信息</p>
                        </div>

                        <form @submit.prevent="handleUpdateProfile" class="p-8 space-y-6">
                            <div class="space-y-2 group">
                                <label
                                    class="text-xs font-bold text-gray-400 uppercase tracking-wider group-focus-within:text-blue-400 transition-colors">昵称</label>
                                <input v-model="editForm.nickname" type="text"
                                    class="w-full bg-[#0a0f1d] border border-white/10 rounded-xl px-4 py-3.5 text-white placeholder-gray-600 focus:border-blue-500/50 focus:bg-[#0a0f1d]/80 outline-none transition-all" />
                            </div>

                            <div class="space-y-2 group">
                                <label
                                    class="text-xs font-bold text-gray-400 uppercase tracking-wider group-focus-within:text-blue-400 transition-colors">简介</label>
                                <textarea v-model="editForm.bio" rows="4"
                                    class="w-full bg-[#0a0f1d] border border-white/10 rounded-xl px-4 py-3.5 text-white placeholder-gray-600 focus:border-blue-500/50 focus:bg-[#0a0f1d]/80 outline-none transition-all resize-none"></textarea>
                            </div>

                            <div class="flex gap-4 pt-4">
                                <button type="button" @click="showEditModal = false"
                                    class="flex-1 btn btn-ghost text-gray-400 hover:text-white hover:bg-white/5 rounded-xl">
                                    Cancel
                                </button>
                                <button type="submit" :disabled="isUpdating"
                                    class="flex-1 btn border-0 bg-gradient-to-r from-blue-600 to-cyan-500 hover:from-blue-500 hover:to-cyan-400 text-white font-bold rounded-xl shadow-lg shadow-blue-500/20 disabled:opacity-50">
                                    {{ isUpdating ? 'Saving...' : '保存' }}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </Transition>

            <Transition name="fade">
                <div v-if="showAvatarModal"
                    class="fixed inset-0 z-[100] flex items-center justify-center bg-black/80 backdrop-blur-md p-4"
                    @click.self="showAvatarModal = false">
                    <div
                        class="bg-[#151925] rounded-3xl w-full max-w-md border border-white/10 shadow-[0_0_60px_rgba(0,0,0,0.6)] overflow-hidden">
                        <div class="bg-gradient-to-r from-purple-900/20 to-transparent p-6 border-b border-white/5">
                            <h3 class="text-2xl font-bold text-white">更改头像</h3>
                            <p class="text-xs text-gray-500 mt-1 uppercase tracking-widest">上传头像</p>
                        </div>

                        <form @submit.prevent="handleUpdateAvatar" class="p-8 space-y-6">
                            <div class="space-y-2 group">
                                <label
                                    class="text-xs font-bold text-gray-400 uppercase tracking-wider group-focus-within:text-purple-400 transition-colors">
                                    选择头像图片
                                </label>

                                <div class="file-input-wrapper">
                                    <input type="file" ref="avatarFileInput" accept="image/*"
                                        @change="handleAvatarFileSelected" class="hidden" />

                                    <button type="button" @click="avatarFileInput?.click()"
                                        class="w-full px-4 py-3.5 rounded-xl bg-[#0a0f1d]/60 border border-white/10 text-gray-300 hover:border-purple-400/50 hover:bg-[#0a0f1d]/80 transition-all flex items-center justify-center gap-2">
                                        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                        </svg>
                                        <span>{{ avatarFile ? avatarFile.name : '选择图片文件' }}</span>
                                    </button>
                                </div>
                            </div>

                            <div v-if="avatarPreview" class="flex justify-center py-4">
                                <div
                                    class="w-32 h-32 rounded-full overflow-hidden border-2 border-purple-500/30 shadow-[0_0_20px_rgba(168,85,247,0.2)]">
                                    <img :src="avatarPreview" alt="Preview" class="w-full h-full object-cover" />
                                </div>
                            </div>

                            <div class="flex gap-4 pt-4">
                                <button type="button" @click="closeAvatarModal"
                                    class="flex-1 btn btn-ghost text-gray-400 hover:text-white hover:bg-white/5 rounded-xl">
                                    Cancel
                                </button>
                                <button type="submit" :disabled="isUpdatingAvatar || !avatarFile"
                                    class="flex-1 btn border-0 bg-gradient-to-r from-purple-600 to-pink-500 hover:from-purple-500 hover:to-pink-400 text-white font-bold rounded-xl shadow-lg shadow-purple-500/20 disabled:opacity-50">
                                    {{ isUpdatingAvatar ? 'Uploading...' : 'Confirm' }}
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
import { ref, onMounted } from 'vue'

interface UserDetail {
    id: number
    username: string
    nickname: string
    avatar: string
    bio: string | null
    createTime: string
}

interface UserStats {
    userId: number
    playlistCount: number
    likedSongCount: number
}

interface Result<T> {
    code: number
    msg: string
    data: T
}

const userDetail = ref<UserDetail | null>(null)
const userStats = ref<UserStats | null>(null)
const showEditModal = ref(false)
const showAvatarModal = ref(false)
const isUpdating = ref(false)
const isUpdatingAvatar = ref(false)
const avatarFile = ref<File | null>(null)
const avatarPreview = ref<string>('')
const avatarFileInput = ref<HTMLInputElement | null>(null)

const editForm = ref({
    nickname: '',
    bio: ''
})

const getToken = (): string => {
    if (typeof window === 'undefined') return ''
    return localStorage.getItem('token') || sessionStorage.getItem('token') || ''
}

const handleImageError = (e: Event) => {
    const target = e.target as HTMLImageElement
    target.src = '/defaultAvatar.png' // 请确保 public 目录下有此图片，或换成网络图
}


const formatDate = (dateString?: string) => {
    if (!dateString) return 'UNKNOWN'
    const date = new Date(dateString)
    return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, '0')}.${String(date.getDate()).padStart(2, '0')}`
}

const fetchUserDetail = async () => {
    try {
        const token = getToken()
        if (!token) {
            const router = useRouter()
            router.push('/login')
            return
        }

        const response = await $fetch<Result<UserDetail>>('/api/user/me', {
            headers: { 'Authorization': token }
        })

        if (response.code === 200 && response.data) {
            userDetail.value = response.data
            editForm.value.nickname = response.data.nickname
            editForm.value.bio = response.data.bio || ''
        }
    } catch (error) {
        console.error('获取用户信息失败:', error)
    }
}

const fetchUserStats = async () => {
    try {
        const token = getToken()
        if (!token || !userDetail.value) return

        const response = await $fetch<Result<UserStats>>(`/api/user/stats/${userDetail.value.id}`, {
            headers: { 'Authorization': token }
        })

        if (response.code === 200 && response.data) {
            userStats.value = response.data
        }
    } catch (error) {
        console.error('获取用户统计失败:', error)
    }
}

// 处理头像文件选择
const handleAvatarFileSelected = (event: Event) => {
    const input = event.target as HTMLInputElement
    const file = input.files?.[0]

    if (!file) return

    // 验证文件类型
    if (!file.type.startsWith('image/')) {
        alert('请选择图片文件')
        return
    }

    // 验证文件大小（限制为5MB）
    if (file.size > 5 * 1024 * 1024) {
        alert('图片大小不能超过5MB')
        return
    }

    avatarFile.value = file

    // 生成预览
    if (avatarPreview.value) {
        URL.revokeObjectURL(avatarPreview.value)
    }
    avatarPreview.value = URL.createObjectURL(file)
}

// 关闭头像模态框
const closeAvatarModal = () => {
    showAvatarModal.value = false
    avatarFile.value = null
    if (avatarPreview.value) {
        URL.revokeObjectURL(avatarPreview.value)
    }
    avatarPreview.value = ''
}

const handleUpdateProfile = async () => {
    if (!userDetail.value) return
    isUpdating.value = true

    try {
        const token = getToken()
        const response = await $fetch<Result<string>>(`/api/user/update/${userDetail.value.id}`, {
            method: 'PUT',
            headers: {
                'Authorization': token,
                'Content-Type': 'application/json'
            },
            body: {
                nickname: editForm.value.nickname,
                bio: editForm.value.bio
            }
        })

        if (response.code === 200) {
            await fetchUserDetail()
            showEditModal.value = false
        } else {
            alert(response.msg || '更新失败')
        }
    } catch (error) {
        console.error('更新失败:', error)
        alert('更新失败，请稍后重试')
    } finally {
        isUpdating.value = false
    }
}

// 更新头像上传处理
const handleUpdateAvatar = async () => {
    if (!userDetail.value || !avatarFile.value) return
    isUpdatingAvatar.value = true

    try {
        const token = getToken()
        const formData = new FormData()
        formData.append('file', avatarFile.value)

        const response = await $fetch<Result<string>>(`/api/user/avatar/${userDetail.value.id}`, {
            method: 'POST',
            headers: { 'Authorization': token },
            body: formData
        })

        if (response.code === 200) {
            await fetchUserDetail()
            closeAvatarModal()
            alert('头像更新成功！')
        } else {
            alert(response.msg || '更新失败')
        }
    } catch (error) {
        console.error('更新头像失败:', error)
        alert('更新失败，请稍后重试')
    } finally {
        isUpdatingAvatar.value = false
    }
}

// 生命周期
onMounted(async () => {
    await fetchUserDetail()
    if (userDetail.value) {
        await fetchUserStats()
    }
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

/* 慢速旋转动画 */
.animate-spin-slow {
    animation: spin 10s linear infinite;
}

@keyframes spin {
    from {
        transform: rotate(0deg);
    }

    to {
        transform: rotate(360deg);
    }
}
</style>