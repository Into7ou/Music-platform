<template>
  <div>
    <h1 class="text-3xl font-bold mb-6">音乐列表</h1>

    <!-- 加载状态 -->
    <div v-if="pending" class="flex justify-center py-8">
      <span class="loading loading-spinner loading-lg"></span>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="alert alert-error">
      <span>加载失败: {{ error.message }}</span>
    </div>

    <!-- 歌曲列表 -->
    <div v-else-if="data" class="overflow-x-auto">
      <table class="table table-zebra">
        <thead>
          <tr>
            <th>#</th>
            <th>歌曲名</th>
            <th>艺术家</th>
            <th>专辑</th>
            <th>时长</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(song, index) in data?.data?.list" :key="song.id">
            <td>{{ index + 1 }}</td>
            <td>{{ song.title }}</td>
            <td>{{ song.artist }}</td>
            <td>{{ song.album || '-' }}</td>
            <td>{{ formatDuration(song.duration) }}</td>
            <td>
              <button @click="playSong(song)" class="btn btn-sm btn-primary">
                播放
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useFetch } from '#app';
import type { Song, PageResult, Result } from '../types/music';
import { usePlayerStore } from '../stores/usePlayer';

const playerStore = usePlayerStore();

const { data, pending, error } = await useFetch<Result<PageResult<Song>>>('/api/music/list', {
  query: {
    page: 1,
    size: 20
  }
});

function playSong(song: Song) {
  playerStore.play(song);
}

function formatDuration(seconds: number): string {
  const mins = Math.floor(seconds / 60);
  const secs = Math.floor(seconds % 60);
  return `${mins}:${secs.toString().padStart(2, '0')}`;
}
</script>