<template>
    <NuxtLayout name="admin">
        <div class="space-y-6">
            <div
                class="p-5 rounded-2xl bg-gradient-to-br from-[#151925]/80 to-[#0a0f1d]/60 border border-white/10 backdrop-blur-xl shadow-xl">
                <div class="flex flex-wrap gap-4 justify-between items-center">
                    <div class="flex gap-3 flex-1">
                        <div class="relative group flex-1 max-w-md">
                            <input v-model="searchKeyword" type="text" placeholder="搜索歌曲标题、艺人..."
                                class="input w-full bg-[#0a0f1d] border border-white/10 text-white placeholder-gray-500 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all pl-11 pr-4" />
                            <svg class="w-5 h-5 absolute left-3.5 top-1/2 -translate-y-1/2 text-gray-500 group-focus-within:text-blue-400 transition-colors"
                                fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                            </svg>
                        </div>
                        <button @click="fetchSongs"
                            class="btn btn-primary bg-gradient-to-r from-blue-600 to-blue-500 hover:from-blue-500 hover:to-blue-400 border-none text-white shadow-lg shadow-blue-900/30 transition-all hover:scale-105 active:scale-95">
                            <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                            </svg>
                            搜索
                        </button>
                    </div>

                    <button @click="batchDelete" :disabled="selectedIds.length === 0"
                        class="btn bg-red-500/10 text-red-400 border border-red-500/30 hover:bg-red-500 hover:text-white hover:border-red-500 disabled:opacity-50 disabled:cursor-not-allowed transition-all">
                        <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                        </svg>
                        批量删除 <span class="ml-1 font-mono">({{ selectedIds.length }})</span>
                    </button>
                </div>
            </div>

            <div class="rounded-2xl border border-white/5 bg-[#151925]/40 overflow-hidden">
                <div class="overflow-x-auto">
                    <table class="table w-full">
                        <thead>
                            <tr
                                class="border-b border-white/5 bg-white/5 text-gray-400 text-xs uppercase tracking-wider">
                                <th class="w-12 text-center">
                                    <input type="checkbox"
                                        class="checkbox checkbox-xs checkbox-primary border-white/30 rounded-sm"
                                        @change="toggleSelectAll" :checked="isAllSelected" />
                                </th>
                                <th>信息</th>
                                <th>艺人</th>
                                <th>专辑</th>
                                <th>时长</th>
                                <th>上传时间</th>
                                <th class="text-right">操作</th>
                            </tr>
                        </thead>
                        <!-- 表体 -->
                        <tbody class="text-sm">
                            <tr v-for="song in songs" :key="song.id"
                                class="border-b border-white/5 hover:bg-white/[0.02] transition-colors group">
                                <th class="text-center">
                                    <input type="checkbox"
                                        class="checkbox checkbox-xs checkbox-primary border-white/30 rounded-sm"
                                        :checked="selectedIds.includes(song.id)" @change="toggleSelect(song.id)" />
                                </th>
                                <td>
                                    <div class="flex items-center gap-4">
                                        <div class="avatar">
                                            <div class="w-12 h-12 rounded-lg border border-white/10 overflow-hidden">
                                                <img :src="song.coverUrl || '/defaultCover.png'" class="object-cover" />
                                            </div>
                                        </div>
                                        <div>
                                            <div
                                                class="font-bold text-white group-hover:text-blue-300 transition-colors">
                                                {{ song.title }}</div>
                                            <div class="text-xs text-gray-500">ID: {{ song.id }}</div>
                                        </div>
                                    </div>
                                </td>
                                <td class="text-gray-300">{{ song.artist }}</td>
                                <td class="text-gray-400">{{ song.album || '-' }}</td>
                                <td class="font-mono text-gray-400">{{ formatDuration(song.duration) }}</td>
                                <td class="text-gray-500 text-xs">{{ formatDate(song.createTime) }}</td>
                                <td class="text-right">
                                    <div
                                        class="flex justify-end gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                                        <button @click="openEditModal(song)"
                                            class="btn btn-square btn-xs btn-ghost text-blue-400 hover:bg-blue-500/20">
                                            ✏️
                                        </button>
                                        <button @click="deleteSong(song.id)"
                                            class="btn btn-square btn-xs btn-ghost text-red-400 hover:bg-red-500/20">
                                            🗑️
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <!-- 分页控件 -->
                <div class="p-4 border-t border-white/5 flex justify-center items-center gap-4">
                    <div class="flex items-center gap-2">
                        <!-- 上一页按钮 -->
                        <button @click="changePage(currentPage - 1)" :disabled="currentPage <= 1"
                            class="btn btn-sm btn-circle bg-white/5 border border-white/10 text-gray-400 hover:bg-blue-600 hover:border-blue-500 hover:text-white disabled:opacity-30 disabled:cursor-not-allowed transition-all">
                            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M15 19l-7-7 7-7" />
                            </svg>
                        </button>

                        <!-- 页码显示 -->
                        <div
                            class="px-4 py-2 rounded-lg bg-gradient-to-r from-blue-600/20 to-purple-600/20 border border-blue-500/30">
                            <span class="text-white font-mono text-sm">
                                <span class="text-gray-400">第</span>
                                <span class="text-blue-400 font-bold mx-1">{{ currentPage }}</span>
                                <span class="text-gray-400">页，共</span>
                                <span class="text-purple-400 font-bold ml-1">{{ totalPages || 1 }}</span>
                            </span>
                        </div>

                        <!-- 下一页按钮 -->
                        <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages"
                            class="btn btn-sm btn-circle bg-white/5 border border-white/10 text-gray-400 hover:bg-blue-600 hover:border-blue-500 hover:text-white disabled:opacity-30 disabled:cursor-not-allowed transition-all">
                            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M9 5l7 7-7 7" />
                            </svg>
                        </button>
                    </div>

                    <!-- 总数显示 -->
                    <div class="text-xs text-gray-500">
                        共计： <span class="text-white font-mono">{{ total }}</span> 条
                    </div>
                </div>
            </div>

            <!-- 编辑弹窗 (深色模态框) -->
            <dialog ref="editModal" class="modal backdrop-blur-sm">
                <div class="modal-box bg-[#151925] border border-white/10 shadow-2xl">
                    <h3 class="font-bold text-lg text-white mb-6">编辑歌曲详情</h3>
                    <div v-if="editForm" class="space-y-4">
                        <div class="form-control">
                            <label class="label"><span class="label-text text-gray-400">歌曲标题</span></label>
                            <input v-model="editForm.title" type="text"
                                class="input input-bordered bg-[#0a0f1d] border-white/10 text-white focus:border-blue-500" />
                        </div>
                        <div class="grid grid-cols-2 gap-4">
                            <div class="form-control">
                                <label class="label"><span class="label-text text-gray-400">艺人</span></label>
                                <input v-model="editForm.artist" type="text"
                                    class="input input-bordered bg-[#0a0f1d] border-white/10 text-white focus:border-blue-500" />
                            </div>
                            <div class="form-control">
                                <label class="label"><span class="label-text text-gray-400">专辑</span></label>
                                <input v-model="editForm.album" type="text"
                                    class="input input-bordered bg-[#0a0f1d] border-white/10 text-white focus:border-blue-500" />
                            </div>
                        </div>
                    </div>
                    <div class="modal-action mt-8">
                        <button @click="closeEditModal" class="btn btn-ghost text-gray-400 hover:text-white">取消</button>
                        <button @click="saveSong"
                            class="btn btn-primary bg-blue-600 hover:bg-blue-500 border-none text-white">保存</button>
                    </div>
                </div>
                <form method="dialog" class="modal-backdrop">
                    <button @click="closeEditModal">close</button>
                </form>
            </dialog>
        </div>
    </NuxtLayout>
</template>

<script setup lang="ts">
definePageMeta({ layout: false })

interface Song {
    id: number
    title: string
    artist: string
    album: string | null
    genre: string | null
    duration: number
    fileUrl: string
    coverUrl: string | null
    createTime: string
}

// 状态
const searchKeyword = ref('')
const songs = ref<Song[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = computed(() => Math.ceil(total.value / pageSize.value))
const selectedIds = ref<number[]>([])
const isAllSelected = computed(() => songs.value.length > 0 && selectedIds.value.length === songs.value.length)
const editModal = ref<HTMLDialogElement | null>(null)
const editForm = ref<{ id: number; title: string; artist: string; album: string; genre: string } | null>(null)

// 获取 token
const getToken = () => localStorage.getItem('token') || sessionStorage.getItem('token') || ''

// 获取歌曲列表
const fetchSongs = async () => {
    try {
        const params = new URLSearchParams()
        params.append('page', currentPage.value.toString())
        params.append('size', pageSize.value.toString())
        if (searchKeyword.value) params.append('keyword', searchKeyword.value)

        const response = await $fetch<{ code: number; data: { total: number; list: Song[] } }>(
            `/api/music/list?${params.toString()}`,
            { headers: { Authorization: getToken() } }
        )

        if (response.code === 200) {
            songs.value = response.data.list
            total.value = response.data.total
        }
    } catch (error) {
        console.error('获取歌曲列表失败:', error)
    }
}

// 全选/取消全选
const toggleSelectAll = () => {
    if (isAllSelected.value) {
        selectedIds.value = []
    } else {
        selectedIds.value = songs.value.map(s => s.id)
    }
}

// 单选
const toggleSelect = (id: number) => {
    const idx = selectedIds.value.indexOf(id)
    if (idx > -1) {
        selectedIds.value.splice(idx, 1)
    } else {
        selectedIds.value.push(id)
    }
}

// 打开编辑弹窗
const openEditModal = (song: Song) => {
    editForm.value = {
        id: song.id,
        title: song.title,
        artist: song.artist,
        album: song.album || '',
        genre: song.genre || ''
    }
    editModal.value?.showModal()
}

const closeEditModal = () => {
    editModal.value?.close()
}

// 保存歌曲
const saveSong = async () => {
    if (!editForm.value) return

    try {
        const response = await $fetch<{ code: number }>(`/api/admin/music/update/${editForm.value.id}`, {
            method: 'PUT',
            headers: { Authorization: getToken(), 'Content-Type': 'application/json' },
            body: {
                title: editForm.value.title,
                artist: editForm.value.artist,
                album: editForm.value.album,
                genre: editForm.value.genre
            }
        })

        if (response.code === 200) {
            closeEditModal()
            await fetchSongs()
        } else {
            alert('保存失败')
        }
    } catch (error) {
        console.error('保存失败:', error)
        alert('保存失败')
    }
}

// 删除歌曲
const deleteSong = async (id: number) => {
    if (!confirm('确定要删除这首歌曲吗？')) return

    try {
        const response = await $fetch<{ code: number }>(`/api/admin/music/delete/${id}`, {
            method: 'DELETE',
            headers: { Authorization: getToken() }
        })

        if (response.code === 200) {
            await fetchSongs()
        } else {
            alert('删除失败')
        }
    } catch (error) {
        console.error('删除失败:', error)
        alert('删除失败')
    }
}

// 批量删除
const batchDelete = async () => {
    if (!confirm(`确定要删除选中的 ${selectedIds.value.length} 首歌曲吗？`)) return

    try {
        const response = await $fetch<{ code: number }>('/api/admin/music/batch-delete', {
            method: 'DELETE',
            headers: { Authorization: getToken(), 'Content-Type': 'application/json' },
            body: { ids: selectedIds.value }
        })

        if (response.code === 200) {
            selectedIds.value = []
            await fetchSongs()
        } else {
            alert('批量删除失败')
        }
    } catch (error) {
        console.error('批量删除失败:', error)
        alert('批量删除失败')
    }
}

// 分页
const changePage = (page: number) => {
    if (page >= 1 && page <= totalPages.value) {
        currentPage.value = page
        fetchSongs()
    }
}

// 格式化时长
const formatDuration = (seconds: number) => {
    const mins = Math.floor(seconds / 60)
    const secs = seconds % 60
    return `${mins}:${secs.toString().padStart(2, '0')}`
}

// 格式化日期
const formatDate = (dateStr: string) => {
    if (!dateStr) return '-'
    return new Date(dateStr).toLocaleDateString('zh-CN')
}

onMounted(fetchSongs)
</script>