/** @type {import('tailwindcss').Config} */
export default {
    content: [
        "./components/**/*.{js,vue,ts}",
        "./layouts/**/*.vue",
        "./pages/**/*.vue",
        "./plugins/**/*.{js,ts}",
        "./app.vue",
    ],
    theme: {
        extend: {},
    },
    plugins: [require("daisyui")],
    daisyui: {
        themes: ["light", "dark", "cupcake"], // 可选主题
        darkTheme: "dark", // 默认暗色主题
        base: true,
        styled: true,
        utils: true,
        logs: true,
    },
}