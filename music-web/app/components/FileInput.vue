<template>
    <input type="file" class="file-input" :accept="accept" @change="handleChange" />
</template>

<script setup lang="ts">
defineProps<{
    accept?: string
}>()

const emit = defineEmits<{
    (e: 'file-selected', file: File | null): void
}>()

const handleChange = (event: Event) => {
    const target = event.target as HTMLInputElement
    const file = target.files?.[0] || null
    emit('file-selected', file)
}
</script>