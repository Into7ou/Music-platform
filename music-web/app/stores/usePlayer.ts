// stores/usePlayer.ts
import { defineStore } from 'pinia';
import { Howl } from 'howler';
import type { Song } from '../types/music';

export type PlayMode = 'loop' | 'repeat-one' | 'shuffle'

export const usePlayerStore = defineStore('player', {
    state: () => ({
        currentSong: null as Song | null,
        isPlaying: false,
        progress: 0,
        duration: 0,
        volume: 0.8,
        howl: null as Howl | null,

        // ✅ 新增：播放列表相关
        playlist: [] as Song[],          // 播放列表
        currentIndex: -1,                // 当前播放索引
        playMode: 'loop' as PlayMode,    // 播放模式
    }),

    getters: {
        progressPercent: (state) => {
            if (state.duration === 0) return 0;
            return (state.progress / state.duration) * 100;
        },

        formattedProgress: (state) => {
            return formatTime(state.progress);
        },

        formattedDuration: (state) => {
            return formatTime(state.duration);
        },
    },

    actions: {
        // ✅ 修改：播放指定歌曲
        play(song: Song) {
            // 检查是否已在播放列表中
            const existingIndex = this.playlist.findIndex(s => s.id === song.id)

            if (existingIndex !== -1) {
                // 如果已在列表中，直接播放
                this.playAtIndex(existingIndex)
            } else {
                // 如果不在列表中，添加到列表末尾并播放
                this.playlist.push(song)
                this.playAtIndex(this.playlist.length - 1)
            }
        },

        // ✅ 新增：播放指定索引的歌曲
        playAtIndex(index: number) {
            if (index < 0 || index >= this.playlist.length) return

            const song = this.playlist[index]
            this.currentIndex = index

            // 如果正在播放同一首歌，直接恢复播放
            if (this.currentSong?.id === song.id && this.howl) {
                this.howl.play();
                this.isPlaying = true;
                this.startProgressUpdate();
                return;
            }

            // 停止当前播放
            if (this.howl) {
                this.howl.unload();
            }

            // 创建新的 Howl 实例
            this.currentSong = song;
            this.howl = new Howl({
                src: [song.fileUrl],
                html5: true,
                volume: this.volume,
                onplay: () => {
                    this.isPlaying = true;
                    this.duration = this.howl?.duration() || 0;
                    this.startProgressUpdate();
                },
                onpause: () => {
                    this.isPlaying = false;
                },
                onend: () => {
                    this.isPlaying = false;
                    this.progress = 0;
                    this.playNext(); // ✅ 播放完毕后自动播放下一首
                },
                onloaderror: (_id: number, error: unknown) => {
                    console.error('加载失败:', error);
                },
            });

            this.howl.play();
        },

        // ✅ 新增：播放下一首（根据播放模式）
        playNext() {
            if (this.playlist.length === 0) return

            let nextIndex = this.currentIndex

            switch (this.playMode) {
                case 'repeat-one':
                    // 单曲循环：重复当前歌曲
                    nextIndex = this.currentIndex
                    break

                case 'shuffle':
                    // 随机播放
                    nextIndex = Math.floor(Math.random() * this.playlist.length)
                    break

                case 'loop':
                default:
                    // 列表循环
                    nextIndex = (this.currentIndex + 1) % this.playlist.length
                    break
            }

            this.playAtIndex(nextIndex)
        },

        // ✅ 新增：播放上一首
        playPrevious() {
            if (this.playlist.length === 0) return

            let prevIndex = this.currentIndex

            if (this.playMode === 'shuffle') {
                // 随机模式下也随机选择
                prevIndex = Math.floor(Math.random() * this.playlist.length)
            } else {
                // 列表循环/单曲循环都回到上一首
                prevIndex = (this.currentIndex - 1 + this.playlist.length) % this.playlist.length
            }

            this.playAtIndex(prevIndex)
        },

        // ✅ 新增：设置播放模式
        setPlayMode(mode: PlayMode) {
            this.playMode = mode
        },

        // ✅ 新增：循环切换播放模式
        togglePlayMode() {
            const modes: PlayMode[] = ['loop', 'repeat-one', 'shuffle']
            const currentIdx = modes.indexOf(this.playMode)
            const nextIdx = (currentIdx + 1) % modes.length
            this.playMode = modes[nextIdx]
        },

        // ✅ 新增：设置播放列表（用于"播放全部"）
        setPlaylist(songs: Song[], startIndex = 0) {
            this.playlist = [...songs]
            this.playAtIndex(startIndex)
        },

        // ✅ 新增：从播放列表移除歌曲
        removeFromPlaylist(index: number) {
            if (index < 0 || index >= this.playlist.length) return

            // 如果移除的是正在播放的歌曲
            if (index === this.currentIndex) {
                this.playNext() // 播放下一首
                this.playlist.splice(index, 1)
                if (this.currentIndex > index) {
                    this.currentIndex--
                }
            } else {
                this.playlist.splice(index, 1)
                if (this.currentIndex > index) {
                    this.currentIndex--
                }
            }
        },

        // ✅ 新增：清空播放列表
        clearPlaylist() {
            this.playlist = []
            this.currentIndex = -1
            this.pause()
            if (this.howl) {
                this.howl.unload()
            }
            this.currentSong = null
        },

        pause() {
            if (this.howl) {
                this.howl.pause();
                this.isPlaying = false;
            }
        },

        toggle() {
            if (this.isPlaying) {
                this.pause();
            } else if (this.currentSong) {
                this.play(this.currentSong);
            }
        },

        seek(seconds: number) {
            if (this.howl) {
                this.howl.seek(seconds);
                this.progress = seconds;
            }
        },

        setVolume(vol: number) {
            this.volume = Math.max(0, Math.min(1, vol));
            if (this.howl) {
                this.howl.volume(this.volume);
            }
        },

        startProgressUpdate() {
            const updateProgress = () => {
                if (this.isPlaying && this.howl) {
                    this.progress = this.howl.seek() as number;
                    requestAnimationFrame(updateProgress);
                }
            };
            requestAnimationFrame(updateProgress);
        },
    },
});

function formatTime(seconds: number): string {
    const mins = Math.floor(seconds / 60);
    const secs = Math.floor(seconds % 60);
    return `${mins}:${secs.toString().padStart(2, '0')}`;
}