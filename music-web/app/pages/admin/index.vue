<template>
    <NuxtLayout name="admin">
        <div class="space-y-8">
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-2 gap-6">
                <div v-for="(item, index) in statItems" :key="index"
                    class="group relative overflow-hidden rounded-2xl border border-white/5 bg-[#151925]/60 backdrop-blur-md p-6 hover:border-white/10 transition-all duration-300 hover:-translate-y-1 hover:shadow-xl">
                    <div class="relative z-10 flex justify-between items-start">
                        <div>
                            <p class="text-xs font-bold text-gray-500 uppercase tracking-widest mb-2">{{ item.label }}
                            </p>
                            <h3
                                class="text-3xl font-black text-white group-hover:text-transparent group-hover:bg-clip-text group-hover:bg-gradient-to-r group-hover:from-white group-hover:to-blue-300 transition-all">
                                {{ item.value }}
                            </h3>
                            <p class="text-[10px] text-green-400 mt-2 flex items-center gap-1" v-if="item.trend > 0">
                                <span>▲</span> {{ item.trend }}% from last week
                            </p>
                        </div>
                        <div
                            class="p-3 rounded-xl bg-white/5 group-hover:bg-blue-500/20 group-hover:text-blue-300 transition-colors">
                            <component :is="item.icon" class="w-6 h-6" />
                        </div>
                    </div>
                    <div class="absolute -right-6 -bottom-6 w-24 h-24 rounded-full blur-[50px] opacity-20 group-hover:opacity-40 transition-opacity"
                        :class="item.glowColor"></div>
                </div>
            </div>

            <!-- 快捷入口（单独居中显示） -->
            <div class="max-w-3xl mx-auto">
                <div class="rounded-3xl border border-white/5 bg-[#151925]/60 p-6 backdrop-blur-md">
                    <h3 class="text-lg font-bold text-white mb-6">Quick Actions</h3>
                    <div class="grid grid-cols-2 gap-4">
                        <NuxtLink to="/admin/users"
                            class="group flex flex-col items-center justify-center p-6 rounded-2xl border border-white/5 bg-white/[0.02] hover:bg-blue-600 hover:border-blue-500 transition-all duration-300">
                            <span class="text-3xl mb-2 grayscale group-hover:grayscale-0 transition-all">👥</span>
                            <span class="text-sm font-bold text-gray-400 group-hover:text-white">Manage Users</span>
                        </NuxtLink>

                        <NuxtLink to="/admin/music"
                            class="group flex flex-col items-center justify-center p-6 rounded-2xl border border-white/5 bg-white/[0.02] hover:bg-purple-600 hover:border-purple-500 transition-all duration-300">
                            <span class="text-3xl mb-2 grayscale group-hover:grayscale-0 transition-all">🎵</span>
                            <span class="text-sm font-bold text-gray-400 group-hover:text-white">Upload Song</span>
                        </NuxtLink>
                    </div>
                </div>
            </div>
        </div>
    </NuxtLayout>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, h } from 'vue'

definePageMeta({ layout: false })

// 模拟数据
const stats = ref({
    totalUsers: 0,
    totalSongs: 0
})

// 图标组件 (SVG)
const UserIcon = h('svg', { fill: 'none', viewBox: '0 0 24 24', stroke: 'currentColor', strokeWidth: 2 }, [h('path', { d: 'M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z' })])
const SongIcon = h('svg', { fill: 'none', viewBox: '0 0 24 24', stroke: 'currentColor', strokeWidth: 2 }, [h('path', { d: 'M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3' })])

const statItems = computed(() => [
    { label: 'Total Users', value: stats.value.totalUsers, trend: 12, icon: UserIcon, glowColor: 'bg-blue-500' },
    { label: 'Songs Library', value: stats.value.totalSongs, trend: 5, icon: SongIcon, glowColor: 'bg-purple-500' },
])

// 获取 token
const getToken = () => localStorage.getItem('token') || sessionStorage.getItem('token') || ''

onMounted(async () => {
    try {
        // 获取用户列表统计
        const userRes = await $fetch<{ code: number; data: { total: number } }>(
            '/api/admin/user/list?page=1&size=1',
            { headers: { Authorization: getToken() } }
        )

        // 获取音乐列表统计
        const musicRes = await $fetch<{ code: number; data: { total: number } }>(
            '/api/music/list?page=1&size=1',
            { headers: { Authorization: getToken() } }
        )

        stats.value = {
            totalUsers: userRes.code === 200 ? userRes.data.total : 0,
            totalSongs: musicRes.code === 200 ? musicRes.data.total : 0,
        }
    } catch (error) {
        console.error('获取统计数据失败:', error)
    }
})
</script>