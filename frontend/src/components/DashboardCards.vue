<template>
  <div class="grid grid-cols-1 md:grid-cols-3 gap-8 mb-20">
    <div class="glass-panel p-8 text-center">
      <p class="text-gray-500 text-sm mb-1">分析影片总数</p>
      <h3 class="text-5xl font-semibold">{{ store.totalCount }}</h3>
    </div>
    <div class="glass-panel p-8 text-center">
      <p class="text-gray-500 text-sm mb-1">平均口碑评分</p>
      <h3 class="text-5xl font-semibold text-blue-500">{{ store.avgRating }}</h3>
    </div>
    <div class="glass-panel p-8 text-center">
      <p class="text-gray-500 text-sm mb-1">正面情绪占比</p>
      <h3 class="text-5xl font-semibold text-emerald-500">{{ store.posRate }}</h3>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useMovieStore } from '@/stores/movieStore'
const store = useMovieStore()
</script>