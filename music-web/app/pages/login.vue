<template>
    <Transition name="fade" appear>
        <AuroraBackground>
            <Loginrig />
        </AuroraBackground>
    </Transition>
</template>

<script setup lang="ts">
// 设置页面布局为 false
definePageMeta({
    layout: false
})
</script>

<style>
/* 全局页面过渡动画 */
.fade-enter-active,
.fade-leave-active {
    transition: opacity 1s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}
</style>