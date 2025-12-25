<template>
    <div class="min-h-screen flex items-center justify-center p-4 lg:p-8">
        <!-- 主要容器 -->
        <div class="w-full max-w-7xl grid grid-cols-1 lg:grid-cols-12 gap-8 items-center relative">

            <!-- 背景装饰字：MYGO -->
            <div
                class="absolute -top-20 -left-20 text-[12rem] font-black text-white/5 select-none pointer-events-none hidden xl:block overflow-hidden">
                MoonDrop
            </div>

            <!-- 左侧：角色立绘 (保持不变) -->
            <div class="lg:col-span-7 flex flex-col justify-center h-full relative z-10 order-2 lg:order-1">
                <div class="relative w-full max-w-lg mx-auto lg:mx-0 group">
                    <div class="relative z-20 transition-transform duration-700 transform group-hover:scale-[1.02]">
                        <img src="/loginpage/login.jpg" alt="MyGO Character"
                            class="rounded-2xl shadow-2xl opacity-90 mask-image-gradient"
                            style="mask-image: linear-gradient(to bottom, black 60%, transparent 100%);" />
                    </div>
                    <div class="absolute -bottom-10 -right-4 z-30 text-right">
                        <h1
                            class="text-6xl lg:text-8xl font-black text-white tracking-tighter drop-shadow-[0_0_15px_rgba(59,130,246,0.5)] italic">
                            MoonDrop
                        </h1>
                        <h2 class="text-4xl lg:text-5xl font-bold text-blue-200/80 -mt-2 tracking-widest uppercase">
                            Music
                        </h2>
                    </div>
                </div>
                <div class="mt-12 space-y-4 pl-4 border-l-4 border-blue-500/50">
                    <p class="text-2xl font-bold text-white tracking-widest">
                        加入 MoonDrop
                    </p>
                    <p class="text-blue-200/60 font-mono text-sm">
                        It's okay to get lost. Just keep moving forward.<br>
                        MoonDrop MusicWeb
                    </p>
                </div>
            </div>

            <!-- 右侧：登录卡片 -->
            <div class="lg:col-span-5 w-full relative z-20 order-1 lg:order-2">
                <div
                    class="card bg-[#151925]/80 backdrop-blur-2xl shadow-[0_0_40px_rgba(0,0,0,0.5)] border border-white/10 w-full rounded-none lg:rounded-2xl overflow-hidden group">

                    <div class="h-1 w-full bg-gradient-to-r from-blue-600 via-cyan-400 to-indigo-600"></div>

                    <!-- 调整了 padding，让内容更紧凑 -->
                    <div class="card-body p-8">
                        <div class="mb-6">
                            <h3 class="text-3xl font-bold text-white mb-1">Welcome Back</h3>
                            <p class="text-blue-300/50 text-sm">登录 MoonDrop 账号以继续</p>
                        </div>

                        <!-- 表单区域 -->
                        <div class="space-y-5 w-full">

                            <!-- 账号输入 -->
                            <div class="w-full">
                                <label class="label pl-1 py-1">
                                    <span
                                        class="label-text text-blue-200/60 font-medium text-xs uppercase tracking-wider">Username</span>
                                </label>
                                <Usernameinput v-model="username" />
                            </div>

                            <!-- 密码输入 -->
                            <div class="w-full">
                                <label class="label pl-1 py-1">
                                    <span
                                        class="label-text text-blue-200/60 font-medium text-xs uppercase tracking-wider">Password</span>
                                </label>
                                <Passwordinput v-model="password" />
                            </div>

                            <!-- 记住我 & 忘记密码 -->
                            <div class="flex justify-between items-center pt-1">
                                <!-- 自定义 Checkbox：带 ✔ 符号动画 -->
                                <label class="cursor-pointer flex items-center gap-2 group select-none">
                                    <div class="relative flex items-center">
                                        <input type="checkbox" class="peer sr-only" v-model="rememberMe" />
                                        <div
                                            class="w-5 h-5 rounded border border-white/30 bg-white/5 peer-checked:bg-blue-500 peer-checked:border-blue-500 peer-checked:shadow-[0_0_10px_rgba(59,130,246,0.5)] transition-all duration-300">
                                        </div>
                                        <svg class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-3.5 h-3.5 text-white opacity-0 peer-checked:opacity-100 transition-all duration-300 transform scale-50 peer-checked:scale-100"
                                            fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="3">
                                            <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7" />
                                        </svg>
                                    </div>
                                    <span
                                        class="text-xs text-blue-200/60 group-hover:text-blue-200 transition-colors">记住我</span>
                                </label>

                                <a
                                    class="link link-hover text-xs text-blue-400 hover:text-blue-300 transition-colors">忘记密码？</a>
                            </div>

                            <!-- 按钮组 -->
                            <div class="space-y-3 pt-4">
                                <button @click="handleLogin" :disabled="isLoading"
                                    class="btn w-full h-12 border-0 bg-gradient-to-r from-[#3b5bdb] to-[#228be6] hover:from-[#364fc7] hover:to-[#1c7ed6] text-white font-bold tracking-wide shadow-[0_0_20px_rgba(34,139,230,0.3)] hover:shadow-[0_0_30px_rgba(34,139,230,0.5)] transition-all duration-300 rounded-lg relative overflow-hidden group/btn">
                                    <span class="relative z-10">{{ isLoading ? '登录中...' : '登 录' }}</span>
                                    <div
                                        class="absolute inset-0 bg-white/20 translate-x-[-100%] group-hover/btn:translate-x-[100%] transition-transform duration-700 skew-x-12">
                                    </div>
                                </button>

                                <button @click="handleRegister"
                                    class="btn w-full h-12 bg-transparent hover:bg-white/5 text-blue-100/80 border border-white/10 hover:border-white/30 font-medium transition-all duration-300 rounded-lg">
                                    注册新账号
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('')
const password = ref('')
const rememberMe = ref(false)
const isLoading = ref(false)

interface UserLoginVO {
    token: string
    username: string
    nickname?: string
    avatar?: string
}

// 登录处理
const handleLogin = async () => {
    if (!username.value || !password.value) {
        alert('请输入用户名和密码')
        return
    }

    isLoading.value = true

    try {
        const response = await $fetch<{ code: number; msg: string; data: UserLoginVO }>('/api/auth/login', {
            method: 'POST',
            body: {
                username: username.value,
                password: password.value
            }
        })

        if (response.code === 200) {
            // 登录成功，保存 token
            // ⭐ response.data 是 UserLoginVO 对象，需要取其中的 token 字段
            const token = response.data.token

            console.log('登录返回的 token:', token)  // 调试日志

            if (rememberMe.value) {
                localStorage.setItem('token', token)
                localStorage.setItem('username', response.data.username)
                if (response.data.nickname) {
                    localStorage.setItem('nickname', response.data.nickname)
                }
            } else {
                sessionStorage.setItem('token', token)
                sessionStorage.setItem('username', response.data.username)
                if (response.data.nickname) {
                    sessionStorage.setItem('nickname', response.data.nickname)
                }
            }

            alert('登录成功！')
            await router.push('/')
        } else {
            alert(response.msg || '登录失败')
        }
    } catch (error: any) {
        console.error('登录错误:', error)
        alert(error?.data?.message || '网络错误，请稍后重试')
    } finally {
        isLoading.value = false
    }

    // 登录成功后，触发 storage 事件，通知其他标签页
    window.dispatchEvent(new Event('storage'))
}

const handleRegister = () => {
    router.push('/register')
}
</script>

<style scoped>
.mask-image-gradient {
    mask-image: linear-gradient(to bottom, black 70%, transparent 100%);
    -webkit-mask-image: linear-gradient(to bottom, black 70%, transparent 100%);
}
</style>