<template>
    <!-- 外部容器：控制整体宽度和边框光效 -->
    <div class="group relative w-full">
        <!-- 背景层与边框 -->
        <div class="flex items-center gap-3 w-full bg-[#0a0f1d]/60 border border-white/10 rounded-xl px-4 py-3.5 
                    transition-all duration-300 
                    group-focus-within:border-blue-400/50 
                    group-focus-within:bg-[#0a0f1d]/80 
                    group-focus-within:shadow-[0_0_15px_rgba(59,130,246,0.15)]">

            <!-- 图标：聚焦时变亮 -->
            <svg class="h-5 w-5 text-blue-300/40 transition-colors duration-300 group-focus-within:text-blue-400"
                xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>

            <!-- 输入框本体：去边框、去背景 -->
            <input type="text" required :placeholder="placeholder ?? '用户账号'"
                class="w-full bg-transparent border-none outline-none text-blue-50 placeholder-blue-200/20 font-medium h-full"
                :value="modelValue" @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)" />
        </div>
    </div>
</template>

<script setup lang="ts">
defineProps<{
    modelValue: string
    placeholder?: string
}>()

defineEmits<{
    'update:modelValue': [value: string]
}>()
</script>