<template>
  <div class="glass-panel p-8">
    <h4 class="text-xl font-medium mb-8">全局情感占比</h4>
    <VChart :option="option" autoresize class="h-96" />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useMovieStore } from '@/stores/movieStore'

const store = useMovieStore()

const option = computed(() => ({
  series: [{
    type: 'pie',
    radius: ['60%', '80%'],
    itemStyle: { borderRadius: 5, borderColor: '#000', borderWidth: 2 },
    label: { show: false },
    data: [
      { value: store.movies.filter(m => m.sentiment === '正面').length, name: '正面', itemStyle: { color: '#10b981' } },
      { value: store.movies.filter(m => m.sentiment === '中性').length, name: '中性', itemStyle: { color: '#f59e0b' } },
      { value: store.movies.filter(m => m.sentiment === '负面').length, name: '负面', itemStyle: { color: '#ef4444' } },
    ]
  }]
}))
</script>