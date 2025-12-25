<template>
    <Transition name="fade" appear>
        <AuroraBackground>
            <Loginrig />
        </AuroraBackground>
    </Transition>
</template>

<script setup lang="ts">
definePageMeta({
    layout: false
})
</script>

<style>
.fade-enter-active,
.fade-leave-active {
    transition: opacity 1s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}
</style>