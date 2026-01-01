<template>
  <nav class="fixed top-0 w-full z-50 bg-black/80 backdrop-blur-2xl border-b border-white/10">
    <div class="max-w-7xl mx-auto px-6 h-12 flex items-center justify-between text-sm">
      <div class="flex items-center space-x-8">
        <a href="#" class="opacity-90 hover:opacity-100">
          <svg class="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 14.5v-9l6 4.5-6 4.5z"/>
          </svg>
        </a>
        <a href="#dashboard" class="opacity-70 hover:opacity-100">控制台</a>
        <a href="#analysis" class="opacity-70 hover:opacity-100">统计分析</a>
      </div>
      <input
        v-model="keyword"
        type="text"
        placeholder="搜索电影或标签..."
        class="bg-white/10 rounded-full px-4 py-1 text-xs w-48 focus:w-64 transition-all outline-none"
      />
    </div>
  </nav>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useMovieStore } from '@/stores/movieStore'

const store = useMovieStore()
const keyword = ref('')

let timeoutId: ReturnType<typeof setTimeout> | null = null

watch(keyword, (newVal) => {
  if (timeoutId) clearTimeout(timeoutId)
  timeoutId = setTimeout(() => {
    // 搜索时重置到第一页
    store.fetchMovies(newVal, 0)
  }, 500)
})
</script>