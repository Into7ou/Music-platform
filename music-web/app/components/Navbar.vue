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

            <NuxtLink v-else to="/user-detail"
                class="flex items-center gap-2 btn btn-ghost hover:bg-white/10 text-white group">
                <div class="avatar placeholder">
                    <div class="bg-gradient-to-br from-blue-500 to-cyan-400 text-neutral-content rounded-full w-8">
                        <span class="text-xs">U</span>
                    </div>
                </div>
                <span class="font-normal group-hover:text-blue-200 transition-colors">个人中心</span>
            </NuxtLink>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import SearchInput from './SearchInput.vue'

const router = useRouter()
const isLoggedIn = ref(false)
const searchKeyword = ref('')

const navItems = [
    { name: '首页', path: '/' },
    { name: '歌单', path: '/playlists' },
    { name: '排行榜', path: '/rankings' },
    { name: '歌手', path: '/artists' },
    { name: '今日推荐', path: '/daily' },
    { name: '我的音乐', path: '/my-music' },
    { name: '上传音乐', path: '/upload' },
]

const checkLoginStatus = () => {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token')
    isLoggedIn.value = !!token
}

const handleSearch = () => {
    if (searchKeyword.value.trim()) {
        router.push(`/search?q=${encodeURIComponent(searchKeyword.value)}`)
    }
}

onMounted(() => {
    checkLoginStatus()
    window.addEventListener('storage', checkLoginStatus)
})
</script>