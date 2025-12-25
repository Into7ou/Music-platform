<template>
    <div class="relative min-h-screen w-full overflow-hidden bg-[#050A14] font-sans">
        <!-- 背景底层渐变：模拟深夜城市光害 -->
        <div class="absolute inset-0 bg-gradient-to-b from-[#02040a] via-[#0a1124] to-[#0f1c36]"></div>

        <!-- 极光效果层 (调整为冷色调) -->
        <div class="aurora-container absolute inset-0 opacity-60">
            <div class="aurora aurora-1"></div>
            <div class="aurora aurora-2"></div>
            <div class="aurora aurora-3"></div>
        </div>

        <!-- 星尘粒子 (模拟迷路时的星光) -->
        <div class="stars absolute inset-0"></div>

        <!-- 装饰性网格线 (增加一点科技/舞台感) -->
        <div class="grid-overlay absolute inset-0 opacity-[0.03]"></div>

        <!-- 插槽内容 -->
        <div class="relative z-10 h-full">
            <slot />
        </div>
    </div>
</template>

<style scoped>
/* 极光基础样式 - 更深邃的流动 */
.aurora {
    position: absolute;
    width: 140%;
    height: 140%;
    top: -20%;
    left: -20%;
    filter: blur(80px);
    mix-blend-mode: color-dodge;
    opacity: 0.7;
}

/* MyGO Theme Colors: Blue, Cool Grey, Deep Indigo */
.aurora-1 {
    background: radial-gradient(ellipse 50% 50% at 50% 50%, rgba(60, 100, 200, 0.4), transparent 70%);
    animation: aurora-float 20s ease-in-out infinite alternate;
}

.aurora-2 {
    background: radial-gradient(ellipse 60% 60% at 20% 30%, rgba(90, 80, 180, 0.3), transparent 70%);
    animation: aurora-float 25s ease-in-out infinite alternate-reverse;
}

.aurora-3 {
    background: radial-gradient(ellipse 40% 60% at 80% 80%, rgba(40, 160, 220, 0.2), transparent 70%);
    animation: aurora-float 18s ease-in-out infinite alternate;
}

/* 星空效果 - 更加细碎 */
.stars {
    background-image:
        radial-gradient(1px 1px at 10% 10%, white, transparent),
        radial-gradient(1px 1px at 20% 30%, rgba(200, 220, 255, 0.8), transparent),
        radial-gradient(2px 2px at 40% 70%, white, transparent),
        radial-gradient(1px 1px at 60% 40%, rgba(255, 255, 255, 0.6), transparent),
        radial-gradient(2px 2px at 80% 20%, rgba(180, 200, 255, 0.7), transparent),
        radial-gradient(1px 1px at 90% 90%, white, transparent);
    background-size: 500px 500px;
    opacity: 0.4;
    animation: twinkle 6s ease-in-out infinite;
}

.grid-overlay {
    background-size: 40px 40px;
    background-image: linear-gradient(to right, rgba(255, 255, 255, 0.1) 1px, transparent 1px),
        linear-gradient(to bottom, rgba(255, 255, 255, 0.1) 1px, transparent 1px);
}

@keyframes aurora-float {
    0% {
        transform: translate(0, 0) scale(1);
    }

    100% {
        transform: translate(5%, 5%) scale(1.1);
    }
}

@keyframes twinkle {

    0%,
    100% {
        opacity: 0.3;
        transform: scale(1);
    }

    50% {
        opacity: 0.7;
        transform: scale(1.05);
    }
}
</style>