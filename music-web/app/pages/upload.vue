<template>
    <Navbar />
    <AuroraBackground>
        <div class="relative z-10 h-full min-h-screen flex items-center justify-center p-4 pt-20">

            <div class="upload-card w-full max-w-[500px] p-8 md:p-10 rounded-3xl animate-fade-in-up">
                <h1 class="upload-title text-3xl font-bold text-center mb-8">上传音乐</h1>

                <form @submit.prevent="handleSubmit" class="flex flex-col gap-5">

                    <div class="flex flex-col gap-2">
                        <label class="text-sm font-medium text-gray-300">
                            音频文件 <span class="text-red-400">*</span>
                            <span v-if="songList.length > 0" class="text-xs text-blue-400 ml-2">
                                (已选 {{ songList.length }} 首)
                            </span>
                        </label>

                        <div class="file-input-wrapper">
                            <input type="file" ref="audioFileInput" accept="audio/*" multiple
                                @change="handleMultipleAudioSelected" class="hidden" />

                            <button type="button" @click="audioFileInput?.click()"
                                class="w-full px-4 py-3 rounded-xl bg-[#0a0f1d]/60 border border-white/10 text-gray-300 hover:border-blue-400/50 hover:bg-[#0a0f1d]/80 transition-all flex items-center justify-center gap-2">
                                <Icon icon="solar:upload-linear" class="text-xl" />
                                <span>选择音频文件（支持多选）</span>
                            </button>
                        </div>

                        <div v-if="songList.length > 0" class="mt-3 space-y-2 max-h-40 overflow-y-auto">
                            <div v-for="(song, index) in songList" :key="song.id"
                                class="flex items-center gap-3 p-2 rounded-lg bg-[#0a0f1d]/40 border border-white/5">
                                <span class="text-xs text-gray-500 font-mono">{{ index + 1 }}</span>
                                <Icon icon="solar:music-note-2-linear" class="text-blue-400" />
                                <span class="text-sm text-gray-300 flex-1 truncate">{{ song.audioFile.name }}</span>
                                <button type="button" @click="removeSong(song.id)"
                                    class="text-gray-500 hover:text-red-400 transition-colors">
                                    <Icon icon="solar:close-circle-linear" class="text-lg" />
                                </button>
                            </div>
                        </div>
                    </div>

                    <div v-if="songList.length > 0" class="space-y-4 mt-6">
                        <div class="flex items-center justify-between border-b border-white/10 pb-3">
                            <h2 class="text-lg font-semibold text-white">
                                配置歌曲信息 ({{ songList.length }} 首)
                            </h2>
                            <button type="button" @click="clearAllSongs"
                                class="text-sm text-red-400 hover:text-red-300 transition-colors">
                                清空全部
                            </button>
                        </div>

                        <div class="space-y-4 max-h-[500px] overflow-y-auto pr-2">
                            <div v-for="(song, index) in songList" :key="song.id"
                                class="p-5 rounded-xl bg-[#0a0f1d]/40 border border-white/10 hover:border-white/20 transition-all">

                                <div class="flex items-center justify-between mb-4">
                                    <div class="flex items-center gap-3">
                                        <span class="text-lg font-bold text-blue-400">{{ index + 1 }}</span>
                                        <Icon icon="solar:music-note-2-bold" class="text-xl text-gray-500" />
                                        <span class="text-sm text-gray-400 truncate max-w-[200px]">
                                            {{ song.audioFile.name }}
                                        </span>
                                    </div>
                                    <button type="button" @click="removeSong(song.id)"
                                        class="text-gray-500 hover:text-red-400 transition-colors">
                                        <Icon icon="solar:trash-bin-trash-linear" class="text-xl" />
                                    </button>
                                </div>

                                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                    <div class="flex flex-col gap-2">
                                        <label class="text-xs font-medium text-gray-400">
                                            歌曲标题 <span class="text-red-400">*</span>
                                        </label>
                                        <input v-model="song.metadata.title" type="text" placeholder="请输入歌曲标题"
                                            class="form-input-small w-full px-3 py-2 rounded-lg outline-none text-sm" />
                                    </div>

                                    <div class="flex flex-col gap-2">
                                        <label class="text-xs font-medium text-gray-400">歌手</label>
                                        <input v-model="song.metadata.artist" type="text" placeholder="请输入歌手名称"
                                            class="form-input-small w-full px-3 py-2 rounded-lg outline-none text-sm" />
                                    </div>

                                    <div class="flex flex-col gap-2">
                                        <label class="text-xs font-medium text-gray-400">专辑</label>
                                        <input v-model="song.metadata.album" type="text" placeholder="请输入专辑名称"
                                            class="form-input-small w-full px-3 py-2 rounded-lg outline-none text-sm" />
                                    </div>

                                    <div class="flex flex-col gap-2">
                                        <label class="text-xs font-medium text-gray-400">流派</label>
                                        <select v-model="song.metadata.genre"
                                            class="form-input-small form-select w-full px-3 py-2 rounded-lg outline-none cursor-pointer appearance-none text-sm">
                                            <option value="">请选择流派</option>
                                            <option value="pop">流行</option>
                                            <option value="rock">摇滚</option>
                                            <option value="jazz">爵士</option>
                                            <option value="classical">古典</option>
                                            <option value="electronic">电子</option>
                                            <option value="hiphop">嘻哈</option>
                                            <option value="folk">民谣</option>
                                            <option value="other">其他</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="flex flex-col gap-2 mt-4">
                                    <label class="text-xs font-medium text-gray-400">封面图片</label>
                                    <div class="flex items-center gap-3">
                                        <div v-if="song.coverPreview"
                                            class="relative w-16 h-16 rounded-lg overflow-hidden border border-white/20">
                                            <img :src="song.coverPreview" class="w-full h-full object-cover" />
                                            <button type="button" @click="removeCover(song.id)"
                                                class="absolute top-0 right-0 w-5 h-5 bg-black/60 text-white flex items-center justify-center text-xs hover:bg-red-500 transition-colors">
                                                ✕
                                            </button>
                                        </div>

                                        <div v-if="!song.coverPreview" class="flex-1">
                                            <input type="file" :ref="`coverInput-${song.id}`" accept="image/*"
                                                @change="handleCoverSelected(song.id, $event)" class="hidden" />
                                            <button type="button"
                                                @click="($refs[`coverInput-${song.id}`] as HTMLInputElement[])?.[0]?.click()"
                                                class="w-full px-3 py-2 rounded-lg bg-[#0a0f1d]/60 border border-white/10 text-gray-400 hover:border-blue-400/50 text-sm transition-all">
                                                选择封面
                                            </button>
                                        </div>
                                        <span v-else class="text-xs text-gray-500">封面已设置</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>


                    <button type="submit"
                        class="submit-btn w-full py-3.5 mt-4 rounded-xl text-white font-semibold text-lg shadow-lg shadow-indigo-500/30 disabled:opacity-50 disabled:cursor-not-allowed transform active:scale-95 transition-all"
                        :disabled="isUploading">
                        {{ isUploading ? '正在上传...' : '上传音乐' }}
                    </button>
                </form>
            </div>
        </div>
    </AuroraBackground>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Icon } from '@iconify/vue'

definePageMeta({
    layout: false
})

interface SongUploadItem {
    id: string
    audioFile: File
    coverFile: File | null
    coverPreview: string | null
    metadata: {
        title: string
        artist: string
        album: string
        genre: string
    }
    status: 'pending' | 'uploading' | 'success' | 'error'
    progress: number
    error: string | null
}

const songList = ref<SongUploadItem[]>([])
const audioFileInput = ref<HTMLInputElement | null>(null)
const isUploading = ref(false)

const generateId = () => {
    return `song-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`
}

const extractTitleFromFilename = (filename: string): string => {
    return filename.replace(/\.[^/.]+$/, '')
}

const handleMultipleAudioSelected = (event: Event) => {
    const input = event.target as HTMLInputElement
    const files = input.files

    if (!files || files.length === 0) return

    const newSongs: SongUploadItem[] = Array.from(files).map(file => ({
        id: generateId(),
        audioFile: file,
        coverFile: null,
        coverPreview: null,
        metadata: {
            title: extractTitleFromFilename(file.name),
            artist: '',
            album: '',
            genre: ''
        },
        status: 'pending',
        progress: 0,
        error: null
    }))

    songList.value.push(...newSongs)

    input.value = ''
}

const removeSong = (songId: string) => {
    const song = songList.value.find(s => s.id === songId)
    if (song?.coverPreview) {
        URL.revokeObjectURL(song.coverPreview)
    }
    songList.value = songList.value.filter(s => s.id !== songId)
}

const clearAllSongs = () => {
    if (!confirm('确定要清空所有歌曲吗？')) return

    songList.value.forEach(song => {
        if (song.coverPreview) {
            URL.revokeObjectURL(song.coverPreview)
        }
    })

    songList.value = []
}

const handleCoverSelected = (songId: string, event: Event) => {
    const input = event.target as HTMLInputElement
    const file = input.files?.[0]

    if (!file) return

    const song = songList.value.find(s => s.id === songId)
    if (!song) return

    if (song.coverPreview) {
        URL.revokeObjectURL(song.coverPreview)
    }

    song.coverFile = file
    song.coverPreview = URL.createObjectURL(file)

    input.value = ''
}

const removeCover = (songId: string) => {
    const song = songList.value.find(s => s.id === songId)
    if (!song) return

    if (song.coverPreview) {
        URL.revokeObjectURL(song.coverPreview)
    }

    song.coverFile = null
    song.coverPreview = null
}

const validateForm = (): boolean => {
    if (songList.value.length === 0) {
        alert('请至少选择一个音频文件')
        return false
    }

    const invalidSongs = songList.value.filter(song => !song.metadata.title.trim())
    if (invalidSongs.length > 0) {
        alert('请为所有歌曲填写标题')
        return false
    }

    return true
}

const handleSubmit = async () => {
    if (!validateForm()) return

    isUploading.value = true

    try {
        let successCount = 0
        let failCount = 0

        for (const song of songList.value) {
            song.status = 'uploading'

            try {
                const formData = new FormData()
                formData.append('file', song.audioFile)
                formData.append('title', song.metadata.title)
                formData.append('artist', song.metadata.artist)
                formData.append('album', song.metadata.album)
                formData.append('genre', song.metadata.genre)

                if (song.coverFile) {
                    formData.append('cover', song.coverFile)
                }

                const response = await fetch('/api/music/upload', {
                    method: 'POST',
                    body: formData
                })

                if (response.ok) {
                    song.status = 'success'
                    song.progress = 100
                    successCount++
                } else {
                    song.status = 'error'
                    song.error = `上传失败: ${response.statusText}`
                    failCount++
                }
            } catch (error) {
                song.status = 'error'
                song.error = '上传失败'
                failCount++
                console.error(`上传歌曲 ${song.metadata.title} 失败:`, error)
            }
        }

        if (failCount === 0) {
            alert(`上传成功！\n成功: ${successCount} 首`)
            clearAllSongs()
        } else {
            alert(`上传完成！\n成功: ${successCount} 首\n失败: ${failCount} 首\n\n失败的歌曲已标记为红色`)
        }
    } catch (error) {
        console.error('批量上传错误:', error)
        alert('上传失败，请稍后重试')
    } finally {
        isUploading.value = false
    }
}
</script>

<style scoped>
.upload-card {
    background: rgba(13, 20, 35, 0.4);
    backdrop-filter: blur(40px);
    border: 1px solid rgba(255, 255, 255, 0.08);
    box-shadow:
        0 20px 50px rgba(0, 0, 0, 0.5),
        inset 0 0 0 1px rgba(255, 255, 255, 0.05);
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.upload-card:hover {
    box-shadow:
        0 25px 60px rgba(0, 0, 0, 0.6),
        inset 0 0 0 1px rgba(255, 255, 255, 0.1);
}

.upload-title {
    background: linear-gradient(135deg, #a5b4fc 0%, #e0c3fc 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    text-shadow: 0 0 20px rgba(165, 180, 252, 0.3);
}

.form-input {
    background: rgba(0, 0, 0, 0.2) !important;
    border: 1px solid rgba(255, 255, 255, 0.1) !important;
    color: #e2e8f0 !important;
    transition: all 0.3s ease;
}

.form-input:focus {
    background: rgba(0, 0, 0, 0.4) !important;
    border-color: #818cf8 !important;
    box-shadow: 0 0 0 3px rgba(129, 140, 248, 0.15) !important;
}

.form-input::placeholder {
    color: rgba(255, 255, 255, 0.3);
}

.submit-btn {
    background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%);
    position: relative;
    overflow: hidden;
}

.submit-btn::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(rgba(255, 255, 255, 0.1), rgba(255, 255, 255, 0));
    opacity: 0;
    transition: opacity 0.3s;
}

.submit-btn:hover::after {
    opacity: 1;
}

:deep(.file-input)::file-selector-button {
    background: rgba(255, 255, 255, 0.1) !important;
    color: #c7d2fe !important;
    border: 1px solid rgba(255, 255, 255, 0.1) !important;
    transition: all 0.3s;
}

:deep(.file-input):hover::file-selector-button {
    background: rgba(255, 255, 255, 0.15) !important;
    color: #fff !important;
}

.form-input-small {
    background: rgba(0, 0, 0, 0.2) !important;
    border: 1px solid rgba(255, 255, 255, 0.1) !important;
    color: #e2e8f0 !important;
    transition: all 0.3s ease;
}

.form-input-small:focus {
    background: rgba(0, 0, 0, 0.4) !important;
    border-color: #818cf8 !important;
    box-shadow: 0 0 0 2px rgba(129, 140, 248, 0.1) !important;
}

.form-input-small::placeholder {
    color: rgba(255, 255, 255, 0.25);
    font-size: 0.875rem;
}
</style>