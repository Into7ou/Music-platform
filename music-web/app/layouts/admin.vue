<template>
    <div class="min-h-screen bg-[#050A14] text-gray-300 font-sans selection:bg-blue-500/30 selection:text-white">
        <!-- 背景装饰 -->
        <div class="fixed inset-0 pointer-events-none z-0">
            <div class="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] bg-blue-900/10 rounded-full blur-[120px]"></div>
            <div class="absolute bottom-[10%] right-[-5%] w-[30%] h-[30%] bg-purple-900/10 rounded-full blur-[100px]">
            </div>
        </div>

        <!-- 引入侧边栏组件 -->
        <DashboardSidebar />

        <!-- 主内容区 -->
        <main class="ml-64 min-h-screen relative z-10 flex flex-col">
            <!-- 顶部栏 -->
            <header
                class="h-16 px-8 flex items-center justify-between border-b border-white/5 bg-[#050A14]/80 backdrop-blur-sm sticky top-0 z-40">
                <h2 class="text-xl font-bold text-white flex items-center gap-2">
                    <span class="w-1 h-6 bg-blue-500 rounded-full mr-2"></span>
                    {{ pageTitle }}
                </h2>
                <div class="flex items-center gap-4">
                    <button class="btn btn-circle btn-sm btn-ghost text-gray-400 hover:text-white">
                        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
                        </svg>
                    </button>
                </div>
            </header>

            <!-- 页面内容插槽 -->
            <div class="p-8">
                <slot />
            </div>
        </main>
    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import DashboardSidebar from '~/components/DashboardSidebar.vue'

const route = useRoute()

// 页面标题映射
const pageTitles: Record<string, string> = {
    '/admin': 'Dashboard',
    '/admin/users': 'User Management',
    '/admin/music': 'Music Library'
}

const pageTitle = computed(() => pageTitles[route.path] || 'Admin Portal')
</script>