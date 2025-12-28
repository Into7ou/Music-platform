<template>
    <!-- 侧边栏 -->
    <aside
        class="fixed left-0 top-0 h-full w-64 bg-[#151925]/90 backdrop-blur-xl border-r border-white/5 z-50 flex flex-col">
        <!-- Logo -->
        <div class="p-6 border-b border-white/5">
            <NuxtLink to="/" class="flex items-center gap-3 group">
                <div
                    class="w-10 h-10 rounded-xl bg-gradient-to-br from-blue-600 to-cyan-400 flex items-center justify-center shadow-lg shadow-blue-500/20 group-hover:scale-105 transition-transform">
                    <span class="text-xl">🌙</span>
                </div>
                <div>
                    <h1 class="text-lg font-bold text-white tracking-wide">MoonDrop</h1>
                    <p class="text-[10px] font-mono text-blue-400 uppercase tracking-wider">Admin System</p>
                </div>
            </NuxtLink>
        </div>

        <!-- 导航菜单 -->
        <nav class="flex-1 p-4 overflow-y-auto">
            <ul class="space-y-2">
                <li v-for="item in menuItems" :key="item.path">
                    <NuxtLink :to="item.path"
                        class="flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-300 group"
                        :class="route.path === item.path ? 'bg-blue-600 text-white shadow-lg shadow-blue-900/50' : 'text-gray-400 hover:text-white hover:bg-white/5'">
                        <component :is="item.icon" class="w-5 h-5 transition-transform group-hover:scale-110" />
                        <span class="font-medium text-sm">{{ item.name }}</span>
                        <div v-if="route.path === item.path"
                            class="ml-auto w-1.5 h-1.5 rounded-full bg-white animate-pulse"></div>
                    </NuxtLink>
                </li>
            </ul>
        </nav>

        <!-- 底部用户信息 -->
        <div class="p-4 border-t border-white/5">
            <div class="flex items-center gap-3 p-3 rounded-xl bg-white/5 border border-white/5">
                <div class="avatar placeholder">
                    <div
                        class="bg-gradient-to-br from-purple-500 to-indigo-500 text-white rounded-lg w-9 h-9 flex items-center justify-center font-bold">
                        {{ userInfo?.nickname?.charAt(0) || 'A' }}
                    </div>
                </div>
                <div class="flex-1 min-w-0">
                    <p class="text-sm font-bold text-white truncate">{{ userInfo?.nickname || 'Admin' }}</p>
                    <p class="text-xs text-green-400 flex items-center gap-1">
                        <span class="w-1.5 h-1.5 rounded-full bg-green-400"></span> Online
                    </p>
                </div>
            </div>
            <NuxtLink to="/"
                class="btn btn-sm w-full mt-4 btn-outline border-white/20 text-gray-400 hover:bg-white/10 hover:text-white hover:border-white/40">
                ← Back to Site
            </NuxtLink>
        </div>
    </aside>
</template>

<script setup lang="ts">
const route = useRoute()

// 用户信息
const userInfo = ref<{ nickname?: string } | null>(null)

// 菜单图标
const DashboardIcon = h('svg', { fill: 'none', viewBox: '0 0 24 24', stroke: 'currentColor', strokeWidth: 2 }, [
    h('path', { strokeLinecap: 'round', strokeLinejoin: 'round', d: 'M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6' })
])

const UsersIcon = h('svg', { fill: 'none', viewBox: '0 0 24 24', stroke: 'currentColor', strokeWidth: 2 }, [
    h('path', { strokeLinecap: 'round', strokeLinejoin: 'round', d: 'M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z' })
])

const MusicIcon = h('svg', { fill: 'none', viewBox: '0 0 24 24', stroke: 'currentColor', strokeWidth: 2 }, [
    h('path', { strokeLinecap: 'round', strokeLinejoin: 'round', d: 'M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3' })
])

// 菜单项
const menuItems = [
    { name: 'Dashboard', path: '/admin', icon: DashboardIcon },
    { name: 'Users', path: '/admin/users', icon: UsersIcon },
    { name: 'Music Library', path: '/admin/music', icon: MusicIcon }
]

// 获取用户信息
onMounted(() => {
    if (typeof window !== 'undefined') {
        const stored = localStorage.getItem('userInfo') || sessionStorage.getItem('userInfo')
        if (stored) userInfo.value = JSON.parse(stored)
    }
})
</script>