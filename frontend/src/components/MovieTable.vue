<template>
  <div class="glass-panel overflow-hidden">
    <div class="p-8 border-b border-white/5 flex justify-between items-center">
      <h4 class="text-xl font-medium">舆情数据库</h4>
      <span class="text-xs text-gray-500">点击行查看电影标签云分析</span>
    </div>
    <table class="w-full text-left">
      <thead class="text-xs text-gray-500 uppercase tracking-widest bg-white/5">
        <tr>
          <th class="py-4 px-8">电影</th>
          <th class="py-4 px-4">评分</th>
          <th class="py-4 px-4">状态</th>
          <th class="py-4 px-8 text-right">热评</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="movie in store.movies"
          :key="movie.title"
          @click="handleRowClick(movie.title)"
          class="group hover:bg-white/5 cursor-pointer transition"
        >
          <td class="py-6 px-8 font-semibold">{{ movie.title }}</td>
          <td class="py-6 px-4">
            <span class="bg-blue-500/10 text-blue-400 px-3 py-1 rounded-full text-xs font-bold">
              {{ movie.rating.toFixed(1) }}
            </span>
          </td>
          <td class="py-6 px-4">
            <div class="flex items-center text-xs">
              <span
                class="w-2 h-2 rounded-full mr-2"
                :class="{
                  'bg-emerald-500': movie.sentiment === '正面',
                  'bg-yellow-500': movie.sentiment === '中性',
                  'bg-red-500': movie.sentiment === '负面'
                }"
              ></span>
              {{ movie.sentiment }}
            </div>
          </td>
          <td class="py-6 px-8 text-right text-gray-500 text-xs italic truncate max-w-xs">
            "{{ movie.review || '暂无评论' }}"
          </td>
        </tr>
      </tbody>
    </table>
    <div v-if="store.loading" class="py-20 text-center text-gray-500">加载中...</div>
  </div>
</template>

<script setup lang="ts">
import { useMovieStore } from '@/stores/movieStore'

const store = useMovieStore()

// 通过 emit 通知父组件打开侧边栏
const emit = defineEmits<{
  openSidePanel: []
}>()

const handleRowClick = (movieTitle: string) => {
  store.selectMovie(movieTitle)
  emit('openSidePanel')
}
</script>