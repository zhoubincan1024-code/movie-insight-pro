<template>
  <div class="glass-panel overflow-hidden">
    <div class="p-8 border-b border-white/5 flex justify-between items-center">
      <div>
        <h4 class="text-xl font-medium">舆情数据库</h4>
        <span class="text-xs text-gray-500">点击行查看电影标签云分析</span>
      </div>
      <div class="flex items-center gap-4">
        <span class="text-xs text-gray-500">
          共 {{ store.totalElements }} 条数据
        </span>
        <div class="flex items-center gap-1">
          <span class="text-xs text-gray-500 mr-2">每页：</span>
          <button
            v-for="size in [10, 20, 50, 100]"
            :key="size"
            @click="handlePageSizeChange(size)"
            class="px-3 py-1.5 text-xs rounded transition min-w-[3rem]"
            :class="pageSize === size
              ? 'bg-blue-500 text-white font-medium'
              : 'bg-white/10 hover:bg-white/20 text-gray-300'"
          >
            {{ size }}
          </button>
        </div>
      </div>
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
          :key="movie.id || movie.title"
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
    <div v-else-if="store.movies.length === 0" class="py-20 text-center text-gray-500">
      暂无数据
    </div>
    
    <!-- 分页控件 -->
    <div v-if="store.totalPages > 1" class="p-6 border-t border-white/5 flex items-center justify-between">
      <div class="text-xs text-gray-500">
        显示第 {{ (store.currentPage * store.pageSize) + 1 }} - 
        {{ Math.min((store.currentPage + 1) * store.pageSize, store.totalElements) }} 条，
        共 {{ store.totalElements }} 条
      </div>
      <div class="flex items-center gap-2">
        <!-- 上一页按钮 -->
        <button
          @click="store.previousPage()"
          :disabled="!store.hasPrevious"
          class="px-4 py-2 text-xs rounded bg-white/10 hover:bg-white/20 disabled:opacity-30 disabled:cursor-not-allowed transition"
          :class="{ 'opacity-50 cursor-not-allowed': !store.hasPrevious }"
        >
          上一页
        </button>
        
        <!-- 页码按钮 -->
        <div class="flex items-center gap-1">
          <button
            v-for="page in visiblePages"
            :key="page"
            @click="store.goToPage(page - 1)"
            class="w-8 h-8 text-xs rounded transition"
            :class="page === store.currentPage + 1 
              ? 'bg-blue-500 text-white' 
              : 'bg-white/10 hover:bg-white/20 text-gray-300'"
          >
            {{ page }}
          </button>
        </div>
        
        <!-- 下一页按钮 -->
        <button
          @click="store.nextPage()"
          :disabled="!store.hasNext"
          class="px-4 py-2 text-xs rounded bg-white/10 hover:bg-white/20 disabled:opacity-30 disabled:cursor-not-allowed transition"
          :class="{ 'opacity-50 cursor-not-allowed': !store.hasNext }"
        >
          下一页
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { useMovieStore } from '@/stores/movieStore'

const store = useMovieStore()

// 通过 emit 通知父组件打开侧边栏
const emit = defineEmits<{
  openSidePanel: []
}>()

const pageSize = computed(() => store.pageSize)

// 计算可见的页码（最多显示7个页码）
const visiblePages = computed(() => {
  const total = store.totalPages
  const current = store.currentPage + 1
  const maxVisible = 7
  
  if (total <= maxVisible) {
    return Array.from({ length: total }, (_, i) => i + 1)
  }
  
  let start = Math.max(1, current - Math.floor(maxVisible / 2))
  let end = Math.min(total, start + maxVisible - 1)
  
  // 如果接近末尾，调整起始位置
  if (end - start < maxVisible - 1) {
    start = Math.max(1, end - maxVisible + 1)
  }
  
  return Array.from({ length: end - start + 1 }, (_, i) => start + i)
})

const handleRowClick = (movieTitle: string) => {
  store.selectMovie(movieTitle)
  emit('openSidePanel')
}

const handlePageSizeChange = (size: number) => {
  store.changePageSize(size)
}
</script>