<template>
    <!-- 外部容器：控制整体宽度和边框光效 -->
    <div class="group relative w-full">
        <!-- 背景层与边框 -->
        <div class="flex items-center gap-3 w-full bg-[#0a0f1d]/60 border border-white/10 rounded-xl px-4 py-2.5 
                    transition-all duration-300 
                    group-focus-within:border-blue-400/50 
                    group-focus-within:bg-[#0a0f1d]/80 
                    group-focus-within:shadow-[0_0_15px_rgba(59,130,246,0.15)]">

            <!-- 搜索图标：聚焦时变亮 -->
            <Icon icon="solar:magnifer-linear"
                class="text-lg text-blue-300/40 transition-colors duration-300 group-focus-within:text-blue-400" />

            <!-- 输入框本体：去边框、去背景 -->
            <input type="text" placeholder="搜索歌曲、歌手、专辑..."
                class="w-full bg-transparent border-none outline-none text-blue-50 placeholder-blue-200/20 font-medium h-full"
                :value="modelValue" @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
                @keyup.enter="handleSearch" />

            <!-- 清除按钮（有内容时显示） -->
            <button v-if="modelValue" @click="handleClear"
                class="text-gray-500 hover:text-white transition-colors flex-shrink-0" type="button">
                <Icon icon="solar:close-circle-linear" class="text-lg" />
            </button>
        </div>
    </div>
</template>

<script setup lang="ts">
import { Icon } from '@iconify/vue'

defineProps<{
    modelValue: string
}>()

const emit = defineEmits<{
    'update:modelValue': [value: string]
    'search': []
}>()

// 处理搜索
const handleSearch = () => {
    emit('search')
}

// 处理清除
const handleClear = () => {
    emit('update:modelValue', '')
}
</script>