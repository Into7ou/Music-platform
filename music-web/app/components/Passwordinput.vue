<template>
  <div class="group relative w-full">
    <div class="flex items-center gap-3 w-full bg-[#0a0f1d]/60 border border-white/10 rounded-xl px-4 py-3.5 
                  transition-all duration-300 
                  group-focus-within:border-blue-400/50 
                  group-focus-within:bg-[#0a0f1d]/80 
                  group-focus-within:shadow-[0_0_15px_rgba(59,130,246,0.15)]">

      <svg class="h-5 w-5 text-blue-300/40 transition-colors duration-300 group-focus-within:text-blue-400"
        xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
          d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
      </svg>

      <input type="password" required placeholder="密码"
        class="w-full bg-transparent border-none outline-none text-blue-50 placeholder-blue-200/20 font-medium h-full"
        minlength="8" :value="modelValue"
        @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)" />
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  modelValue: string
}>()

defineEmits<{
  'update:modelValue': [value: string]
}>()
</script>