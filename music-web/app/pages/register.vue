<template>
    <Transition name="fade" appear>
        <AuroraBackground>
            <div class="min-h-screen flex items-center justify-center p-4 lg:p-8">
                <div class="w-full max-w-7xl grid grid-cols-1 lg:grid-cols-12 gap-8 items-center relative">

                    <div
                        class="absolute -top-20 -left-20 text-[12rem] font-black text-white/5 select-none pointer-events-none hidden xl:block overflow-hidden">
                        MoonDrop
                    </div>
                    <div class="lg:col-span-7 flex flex-col justify-center h-full relative z-10 order-2 lg:order-1">
                        <div class="relative w-full max-w-lg mx-auto lg:mx-0 group">
                            <div
                                class="relative z-20 transition-transform duration-700 transform group-hover:scale-[1.02]">
                                <img src="/loginpage/login.jpg" alt="MyGO Character"
                                    class="rounded-2xl shadow-2xl opacity-90 mask-image-gradient"
                                    style="mask-image: linear-gradient(to bottom, black 60%, transparent 100%);" />
                            </div>
                            <div class="absolute -bottom-10 -right-4 z-30 text-right">
                                <h1
                                    class="text-6xl lg:text-8xl font-black text-white tracking-tighter drop-shadow-[0_0_15px_rgba(59,130,246,0.5)] italic">
                                    MoonDrop
                                </h1>
                                <h2
                                    class="text-4xl lg:text-5xl font-bold text-blue-200/80 -mt-2 tracking-widest uppercase">
                                    Music
                                </h2>
                            </div>
                        </div>
                        <div class="mt-12 space-y-4 pl-4 border-l-4 border-blue-500/50">
                            <p class="text-2xl font-bold text-white tracking-widest">
                                加入 MoonDrop
                            </p>
                            <p class="text-blue-200/60 font-mono text-sm">
                                Create your account and start your journey<br>
                                MoonDrop MusicWeb
                            </p>
                        </div>
                    </div>

                    <!-- 右侧：注册卡片 -->
                    <div class="lg:col-span-5 w-full relative z-20">
                        <div class="card bg-[#151925]/80 backdrop-blur-2xl ...">
                            <div class="h-1 w-full bg-gradient-to-r from-blue-600 via-cyan-400 to-indigo-600"></div>

                            <div class="card-body p-8">
                                <div class="mb-6">
                                    <h3 class="text-3xl font-bold text-white mb-1">Create Account</h3>
                                    <p class="text-blue-300/50 text-sm">注册 MoonDrop 账号</p>
                                </div>

                                <div class="space-y-5 w-full">
                                    <div class="w-full">
                                        <label class="label pl-1 py-1">
                                            <span
                                                class="label-text text-blue-200/60 font-medium text-xs uppercase tracking-wider">Username</span>
                                        </label>
                                        <Usernameinput v-model="username" />
                                    </div>

                                    <div class="w-full">
                                        <label class="label pl-1 py-1">
                                            <span
                                                class="label-text text-blue-200/60 font-medium text-xs uppercase tracking-wider">Nickname</span>
                                        </label>
                                        <Usernameinput v-model="nickname" placeholder="昵称" />
                                    </div>

                                    <div class="w-full">
                                        <label class="label pl-1 py-1">
                                            <span
                                                class="label-text text-blue-200/60 font-medium text-xs uppercase tracking-wider">Password</span>
                                        </label>
                                        <Passwordinput v-model="password" />
                                    </div>

                                    <div class="w-full">
                                        <label class="label pl-1 py-1">
                                            <span
                                                class="label-text text-blue-200/60 font-medium text-xs uppercase tracking-wider">Confirm
                                                Password</span>
                                        </label>
                                        <Passwordinput v-model="confirmPassword" />
                                    </div>

                                    <div class="space-y-3 pt-4">
                                        <button @click="handleRegister" :disabled="isLoading"
                                            class="btn w-full h-12 border-0 bg-gradient-to-r from-[#3b5bdb] to-[#228be6] hover:from-[#364fc7] hover:to-[#1c7ed6] text-white font-bold tracking-wide shadow-[0_0_20px_rgba(34,139,230,0.3)] hover:shadow-[0_0_30px_rgba(34,139,230,0.5)] transition-all duration-300 rounded-lg relative overflow-hidden group/btn">
                                            <span class="relative z-10">{{ isLoading ? '注册中...' : '注 册' }}</span>
                                            <div
                                                class="absolute inset-0 bg-white/20 translate-x-[-100%] group-hover/btn:translate-x-[100%] transition-transform duration-700 skew-x-12">
                                            </div>
                                        </button>

                                        <button @click="goToLogin"
                                            class="btn w-full h-12 bg-transparent hover:bg-white/5 text-blue-100/80 border border-white/10 hover:border-white/30 font-medium transition-all duration-300 rounded-lg">
                                            已有账号？去登录
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </AuroraBackground>
    </Transition>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

definePageMeta({
    layout: false
})

const router = useRouter()
const username = ref('')
const nickname = ref('')
const password = ref('')
const confirmPassword = ref('')
const isLoading = ref(false)

// 注册处理
const handleRegister = async () => {
    if (!username.value || !password.value) {
        alert('请输入用户名和密码')
        return
    }

    if (!nickname.value) {
        alert('请输入昵称')
        return
    }

    if (password.value !== confirmPassword.value) {
        alert('两次输入的密码不一致')
        return
    }

    if (password.value.length < 6) {
        alert('密码长度至少为 6 位')
        return
    }

    isLoading.value = true

    try {
        const response = await $fetch<{ code: number; message: string; data: string }>('/api/auth/register', {
            method: 'POST',
            body: {
                username: username.value,
                password: password.value,
                nickname: nickname.value
            }
        })

        if (response.code === 200) {
            alert('注册成功！即将跳转到登录页...')
            await router.push('/login')
        } else {
            alert(response.message || '注册失败')
        }
    } catch (error: any) {
        console.error('注册错误:', error)
        alert(error?.data?.message || '网络错误，请稍后重试')
    } finally {
        isLoading.value = false
    }
}

const goToLogin = () => {
    router.push('/login')
}
</script>

<style scoped>
.mask-image-gradient {
    mask-image: linear-gradient(to bottom, black 70%, transparent 100%);
    -webkit-mask-image: linear-gradient(to bottom, black 70%, transparent 100%);
}
</style>

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