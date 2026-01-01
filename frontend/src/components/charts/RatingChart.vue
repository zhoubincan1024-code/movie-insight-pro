<template>
  <div class="glass-panel p-8">
    <h4 class="text-xl font-medium mb-8">评分分布趋势</h4>
    <VChart :option="option" autoresize class="h-96" />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useMovieStore } from '@/stores/movieStore'

const store = useMovieStore()

const option = computed(() => {
  // 计算评分分布（1-10分）
  const ratingDistribution = Array.from({ length: 10 }, (_, i) => {
    const rating = i + 1
    return store.movies.filter(m => Math.round(m.rating) === rating).length
  })

  return {
    xAxis: {
      type: 'category',
      data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'],
      axisLine: { lineStyle: { color: '#333' } }
    },
    yAxis: {
      type: 'value',
      splitLine: { lineStyle: { color: '#222' } }
    },
    series: [{
      data: ratingDistribution,
      type: 'line',
      smooth: true,
      symbol: 'none',
      lineStyle: { color: '#007AFF', width: 3 },
      areaStyle: { color: 'rgba(0,122,255,0.1)' }
    }]
  }
})
</script>