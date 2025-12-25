<template>
  <div class="min-h-screen flex flex-col bg-[#050A14]">

    <Navbar />

    <main class="flex-1 relative w-full">
      <slot />
    </main>

    <PlayerBar />

  </div>
</template>

<script setup lang="ts">
// 这里可以添加全局逻辑
</script>