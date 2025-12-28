<template>
    <NuxtLayout name="admin">
        <div class="space-y-6">
            <!-- 筛选栏 -->
            <!-- 筛选栏 -->
            <div
                class="p-5 rounded-2xl bg-gradient-to-br from-[#151925]/80 to-[#0a0f1d]/60 border border-white/10 backdrop-blur-xl shadow-xl">
                <div class="flex flex-wrap gap-4 items-end">
                    <!-- 用户名搜索 -->
                    <div class="form-control flex-1 min-w-[200px]">
                        <label class="label pb-2">
                            <span
                                class="label-text text-xs font-bold text-gray-400 uppercase tracking-wider flex items-center gap-2">
                                <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                                </svg>
                                用户名
                            </span>
                        </label>
                        <input v-model="searchForm.username" type="text" placeholder="按用户名筛选..."
                            class="input input-sm bg-[#0a0f1d] border border-white/10 text-white placeholder-gray-500 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all" />
                    </div>

                    <!-- 角色筛选 -->
                    <div class="form-control min-w-[140px]">
                        <label class="label pb-2">
                            <span class="label-text text-xs font-bold text-gray-400 uppercase tracking-wider">角色</span>
                        </label>
                        <select v-model="searchForm.roleId"
                            class="select select-sm bg-[#0a0f1d] border border-white/10 text-white focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all">
                            <option :value="undefined">所有角色</option>
                            <option :value="1">管理员</option>
                            <option :value="2">用户</option>
                        </select>
                    </div>

                    <!-- 状态筛选 -->
                    <div class="form-control min-w-[140px]">
                        <label class="label pb-2">
                            <span class="label-text text-xs font-bold text-gray-400 uppercase tracking-wider">状态</span>
                        </label>
                        <select v-model="searchForm.status"
                            class="select select-sm bg-[#0a0f1d] border border-white/10 text-white focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all">
                            <option :value="undefined">所有状态</option>
                            <option :value="0">正常</option>
                            <option :value="1">禁用</option>
                        </select>
                    </div>

                    <!-- 应用按钮 -->
                    <button @click="fetchUsers"
                        class="btn btn-sm btn-primary bg-gradient-to-r from-blue-600 to-blue-500 hover:from-blue-500 hover:to-blue-400 border-none text-white shadow-lg shadow-blue-900/30 transition-all hover:scale-105 active:scale-95">
                        <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
                        </svg>
                        应用筛选
                    </button>
                </div>
            </div>

            <!-- 用户列表 -->
            <div class="rounded-2xl border border-white/5 bg-[#151925]/40 overflow-hidden">
                <div class="overflow-x-auto">
                    <table class="table w-full">
                        <thead>
                            <tr
                                class="border-b border-white/5 bg-white/5 text-gray-400 text-xs uppercase tracking-wider">
                                <th>ID</th>
                                <th>用户名</th>
                                <th>角色</th>
                                <th>状态</th>
                                <th>加入时间</th>
                                <th class="text-right">操作</th>
                            </tr>
                        </thead>
                        <tbody class="text-sm">
                            <tr v-for="user in users" :key="user.id"
                                class="border-b border-white/5 hover:bg-white/[0.02] transition-colors">
                                <td class="font-mono text-gray-500 text-xs">#{{ user.id }}</td>
                                <td>
                                    <div class="flex items-center gap-3">
                                        <div class="avatar">
                                            <div class="w-10 h-10 rounded-full border border-white/10">
                                                <img :src="user.avatar || '/defaultAvatar.png'" />
                                            </div>
                                        </div>
                                        <div>
                                            <div class="font-bold text-white">{{ user.nickname || user.username }}</div>
                                            <div class="text-xs text-gray-500">@{{ user.username }}</div>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <span class="px-2 py-1 rounded text-xs font-bold border"
                                        :class="user.roleId === 1 ? 'bg-purple-500/10 text-purple-400 border-purple-500/20' : 'bg-blue-500/10 text-blue-400 border-blue-500/20'">
                                        {{ user.roleId === 1 ? '管理员' : '用户' }}
                                    </span>
                                </td>
                                <td>
                                    <div class="flex items-center gap-2">
                                        <div class="w-2 h-2 rounded-full"
                                            :class="user.status === 0 ? 'bg-green-500 shadow-[0_0_8px_rgba(34,197,94,0.5)]' : 'bg-red-500'">
                                        </div>
                                        <span class="text-gray-300 text-xs">{{ user.status === 0 ? '正常' : '禁用'
                                        }}</span>
                                    </div>
                                </td>
                                <td class="text-gray-500 text-xs">{{ formatDate(user.createTime) }}</td>
                                <td class="text-right">
                                    <div class="dropdown dropdown-end">
                                        <div tabindex="0" role="button"
                                            class="btn btn-ghost btn-xs text-gray-400 hover:text-white">•••</div>
                                        <ul tabindex="0"
                                            class="dropdown-content z-[1] menu p-2 shadow-xl bg-[#151925] border border-white/10 rounded-xl w-32">
                                            <li><a @click="viewUserDetail(user.id)"
                                                    class="text-xs text-gray-300 hover:bg-white/10">查看</a>
                                            </li>
                                            <li><a @click="openRoleModal(user)"
                                                    class="text-xs text-blue-400 hover:bg-blue-500/10">修改角色</a>
                                            </li>
                                            <li>
                                                <a v-if="user.status === 0" @click="toggleUserStatus(user.id, 1)"
                                                    class="text-xs text-red-400 hover:bg-red-500/10">禁用</a>
                                                <a v-else @click="toggleUserStatus(user.id, 0)"
                                                    class="text-xs text-green-400 hover:bg-green-500/10">启用</a>
                                            </li>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>

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
                                    <span class="text-gray-400">页</span>
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
                            共计： <span class="text-white font-mono">{{ total }}</span> 名用户
                        </div>
                    </div>
                </div>
            </div>

            <!-- Modals (深色风格) -->
            <dialog ref="roleModal" class="modal backdrop-blur-sm">
                <div class="modal-box bg-[#151925] border border-white/10">
                    <h3 class="font-bold text-lg text-white">修改角色</h3>
                    <p class="py-4 text-gray-400">目标用户： <span class="text-white font-bold">{{ selectedUser?.username
                    }}</span></p>
                    <select v-model="newRoleId"
                        class="select select-bordered w-full bg-[#0a0f1d] border-white/10 text-white">
                        <option :value="1">管理员</option>
                        <option :value="2">普通用户</option>
                    </select>
                    <div class="modal-action">
                        <button @click="closeRoleModal" class="btn btn-ghost text-gray-400">取消</button>
                        <button @click="changeUserRole" class="btn btn-primary bg-blue-600 border-none">确定</button>
                    </div>
                </div>
                <form method="dialog" class="modal-backdrop"><button @click="closeRoleModal">close</button></form>
            </dialog>

            <!-- 详情 Modal 略 (同理修改背景颜色即可) -->
        </div>
    </NuxtLayout>
</template>

<script setup lang="ts">
definePageMeta({ layout: false })

interface User {
    id: number
    username: string
    nickname: string | null
    avatar: string | null
    roleId: number
    roleName?: string
    status: number
    createTime: string
}

// 状态
const searchForm = ref({
    username: '',
    roleId: undefined as number | undefined,
    status: undefined as number | undefined
})
const users = ref<User[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = computed(() => Math.ceil(total.value / pageSize.value))
const selectedUser = ref<User | null>(null)
const roleModal = ref<HTMLDialogElement | null>(null)
const newRoleId = ref(2)

// 获取 token
const getToken = () => localStorage.getItem('token') || sessionStorage.getItem('token') || ''

// 获取用户列表
const fetchUsers = async () => {
    try {
        const params = new URLSearchParams()
        params.append('page', currentPage.value.toString())
        params.append('size', pageSize.value.toString())
        if (searchForm.value.username) params.append('username', searchForm.value.username)
        if (searchForm.value.roleId !== undefined) params.append('roleId', searchForm.value.roleId.toString())
        if (searchForm.value.status !== undefined) params.append('status', searchForm.value.status.toString())

        const response = await $fetch<{ code: number; data: { total: number; list: User[] } }>(
            `/api/admin/user/list?${params.toString()}`,
            { headers: { Authorization: getToken() } }
        )

        if (response.code === 200) {
            users.value = response.data.list
            total.value = response.data.total
        }
    } catch (error) {
        console.error('获取用户列表失败:', error)
    }
}

// 切换用户状态
const toggleUserStatus = async (userId: number, status: number) => {
    try {
        const endpoint = status === 1 ? 'disable' : 'enable'
        const response = await $fetch<{ code: number }>(`/api/admin/user/${endpoint}/${userId}`, {
            method: 'PUT',
            headers: { Authorization: getToken() }
        })

        if (response.code === 200) {
            await fetchUsers() // 刷新列表
        } else {
            alert('操作失败')
        }
    } catch (error) {
        console.error('切换状态失败:', error)
        alert('操作失败')
    }
}

// 打开角色修改弹窗
const openRoleModal = (user: User) => {
    selectedUser.value = user
    newRoleId.value = user.roleId
    roleModal.value?.showModal()
}

const closeRoleModal = () => {
    roleModal.value?.close()
}

// 修改用户角色
const changeUserRole = async () => {
    if (!selectedUser.value) return

    try {
        const response = await $fetch<{ code: number }>(`/api/admin/user/role/${selectedUser.value.id}`, {
            method: 'PUT',
            headers: { Authorization: getToken(), 'Content-Type': 'application/json' },
            body: { roleId: newRoleId.value }
        })

        if (response.code === 200) {
            closeRoleModal()
            await fetchUsers()
        } else {
            alert('修改失败')
        }
    } catch (error) {
        console.error('修改角色失败:', error)
        alert('修改失败')
    }
}

// 查看用户详情
const viewUserDetail = async (userId: number) => {
    try {
        const response = await $fetch<{ code: number; data: User }>(`/api/admin/user/detail/${userId}`, {
            headers: { Authorization: getToken() }
        })

        if (response.code === 200) {
            const user = response.data
            alert(`用户详情：\nID: ${user.id}\n用户名: ${user.username}\n昵称: ${user.nickname || '-'}\n角色: ${user.roleName || '-'}`)
        }
    } catch (error) {
        console.error('获取用户详情失败:', error)
    }
}

// 分页
const changePage = (page: number) => {
    if (page >= 1 && page <= totalPages.value) {
        currentPage.value = page
        fetchUsers()
    }
}

// 日期格式化
const formatDate = (dateStr: string) => {
    if (!dateStr) return '-'
    return new Date(dateStr).toLocaleDateString('zh-CN')
}

onMounted(fetchUsers)
</script>