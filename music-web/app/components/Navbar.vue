<template>
    <div
        class="navbar fixed top-0 w-full z-50 bg-[#050A14]/80 backdrop-blur-md border-b border-white/5 transition-all duration-300">
        <div class="navbar-start">
            <div class="dropdown">
                <div tabindex="0" role="button" class="btn btn-ghost lg:hidden text-white">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M4 6h16M4 12h8m-8 6h16" />
                    </svg>
                </div>
                <!-- 移动端菜单：也改为深色背景 -->
                <ul tabindex="-1"
                    class="menu menu-sm dropdown-content bg-[#151925] text-gray-200 rounded-box z-10 mt-3 w-52 p-2 shadow-2xl border border-white/10">
                    <li>
                        <NuxtLink to="/">首页</NuxtLink>
                    </li>
                    <li>
                        <NuxtLink to="/playlists">歌单</NuxtLink>
                    </li>
                    <li>
                        <NuxtLink to="/rankings">排行榜</NuxtLink>
                    </li>
                    <li>
                        <NuxtLink to="/artists">歌手</NuxtLink>
                    </li>
                    <li>
                        <NuxtLink to="/daily">今日推荐</NuxtLink>
                    </li>
                    <li>
                        <NuxtLink to="/my-music">我的音乐</NuxtLink>
                    </li>
                    <li>
                        <NuxtLink to="/upload">上传音乐</NuxtLink>
                    </li>
                </ul>
            </div>
            <NuxtLink to="/"
                class="btn btn-ghost text-2xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-blue-200 via-cyan-200 to-white hover:opacity-80 transition-opacity">
                🌙 MoonDrop
            </NuxtLink>
        </div>

        <!-- 桌面端菜单 -->
        <div class="navbar-center hidden lg:flex">
            <ul class="menu menu-horizontal px-1 gap-1">
                <li v-for="(item, index) in navItems" :key="index">
                    <NuxtLink :to="item.path"
                        class="text-gray-400 hover:text-white hover:bg-white/5 transition-all duration-300 font-medium tracking-wide">
                        {{ item.name }}
                    </NuxtLink>
                </li>
            </ul>
        </div>

        <div class="navbar-end">
            <div class="hidden md:block w-64 lg:w-80">
                <SearchInput v-model="searchKeyword" @search="handleSearch" />
            </div>
            <NuxtLink v-if="!isLoggedIn" to="/login"
                class="btn h-10 min-h-0 border-0 bg-gradient-to-r from-[#3b5bdb] to-[#228be6] hover:from-[#364fc7] hover:to-[#1c7ed6] text-white font-bold tracking-wide shadow-[0_0_15px_rgba(34,139,230,0.3)] hover:shadow-[0_0_25px_rgba(34,139,230,0.5)] transition-all duration-300 rounded-lg">
                登录 / 注册
            </NuxtLink>

            <div v-else class="dropdown dropdown-end">
                <!-- 触发按钮 -->
                <div tabindex="0" role="button"
                    class="flex items-center gap-2 btn btn-ghost hover:bg-white/10 text-white group">
                    <div class="avatar">
                        <div
                            class="w-8 rounded-full ring ring-blue-400/30 ring-offset-base-100 ring-offset-1 bg-gradient-to-br from-blue-500/20 to-cyan-400/20 backdrop-blur-sm">
                            <img :src="userInfo?.avatar || '/defaultAvatar.png'" :alt="userInfo?.nickname || '用户头像'"
                                @error="handleImageError" />
                        </div>
                    </div>
                    <span class="font-normal group-hover:text-blue-200 transition-colors">
                        {{ userInfo?.nickname || '个人中心' }}
                    </span>
                    <!-- 下拉箭头图标 -->
                    <svg class="w-4 h-4 transition-transform group-hover:translate-y-0.5" fill="none"
                        stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                    </svg>
                </div>

                <!-- 下拉菜单内容 -->
                <ul tabindex="0"
                    class="dropdown-content menu bg-[#151925]/95 backdrop-blur-xl rounded-box z-50 w-52 p-2 shadow-2xl border border-white/10 mt-3">
                    <!-- 用户信息头部 -->
                    <li class="menu-title px-4 py-2">
                        <span class="text-blue-200/80 text-xs">{{ userInfo?.username }}</span>
                    </li>
                    <div class="divider my-0"></div>

                    <!-- 资料选项 -->
                    <li>
                        <NuxtLink to="/user-detail"
                            class="text-gray-300 hover:text-white hover:bg-blue-500/20 transition-all duration-200">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                            </svg>
                            个人资料
                        </NuxtLink>
                    </li>

                    <!-- 登出选项 -->
                    <li>
                        <a @click="handleLogout"
                            class="text-gray-300 hover:text-red-400 hover:bg-red-500/10 transition-all duration-200">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                            </svg>
                            退出登录
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import SearchInput from './SearchInput.vue'

const router = useRouter()
const isLoggedIn = ref(false)
const searchKeyword = ref('')
const userInfo = ref<{
    username: string
    nickname: string
    avatar: string
} | null>(null)

const navItems = [
    { name: '首页', path: '/' },
    { name: '歌单', path: '/playlists' },
    { name: '排行榜', path: '/rankings' },
    { name: '歌手', path: '/artists' },
    { name: '今日推荐', path: '/daily' },
    { name: '我的音乐', path: '/my-music' },
    { name: '上传音乐', path: '/upload' },
]

const checkLoginStatus = async () => {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token')
    isLoggedIn.value = !!token

    // ⭐ 如果已登录，从后端获取最新用户信息
    if (token) {
        try {
            const response = await $fetch<{
                code: number
                data: {
                    username: string
                    nickname: string
                    avatar: string
                }
            }>('/api/user/me', {
                headers: { 'Authorization': token }
            })

            if (response.code === 200 && response.data) {
                userInfo.value = {
                    username: response.data.username,
                    nickname: response.data.nickname,
                    avatar: response.data.avatar || '/defaultAvatar.png'
                }

                // 可选：更新本地存储
                const storage = localStorage.getItem('token') ? localStorage : sessionStorage
                storage.setItem('userInfo', JSON.stringify(userInfo.value))
            }
        } catch (error) {
            console.error('获取用户信息失败:', error)
            // 降级：尝试从本地存储读取
            const storedUserInfo = localStorage.getItem('userInfo') || sessionStorage.getItem('userInfo')
            if (storedUserInfo) {
                try {
                    userInfo.value = JSON.parse(storedUserInfo)
                } catch (e) {
                    console.error('解析用户信息失败:', e)
                }
            }
        }
    } else {
        userInfo.value = null
    }
}

const handleImageError = (e: Event) => {
    const target = e.target as HTMLImageElement
    target.src = '/defaultAvatar.png'  // 加载失败时使用默认头像
}

const handleLogout = () => {
    if (confirm('确定要退出登录吗？')) {
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        sessionStorage.removeItem('token')
        sessionStorage.removeItem('userInfo')

        isLoggedIn.value = false
        userInfo.value = null

        window.dispatchEvent(new Event('storage'))
        router.push('/')
    }
}

const handleSearch = () => {
    if (searchKeyword.value.trim()) {
        router.push(`/search?q=${encodeURIComponent(searchKeyword.value)}`)
    }
}

onMounted(() => {
    checkLoginStatus()
    window.addEventListener('storage', checkLoginStatus)
    window.addEventListener('userInfoUpdated', checkLoginStatus)
})
</script>