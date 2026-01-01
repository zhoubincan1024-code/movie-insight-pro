<template>
  <div class="mt-10">
    <p class="text-sm text-gray-500 mb-4 uppercase tracking-widest">影评标签云</p>
    <div ref="chartRef" class="h-80 glass-panel bg-white/5 flex items-center justify-center">
      <span v-if="!tags" class="text-gray-600 italic">暂无标签</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import * as echarts from 'echarts'

const props = defineProps<{ tags?: string }>()

const chartRef = ref<HTMLDivElement>()
let chart: echarts.ECharts | null = null

const renderChart = () => {
  if (!chartRef.value) return
  if (chart) chart.dispose()
  chart = echarts.init(chartRef.value)

  const tags = props.tags?.split(',')?.map(t => t.trim()) || ['暂无标签']
  const data = tags.map(t => ({
    name: t,
    value: Math.floor(Math.random() * 50) + 20
  }))

  chart.setOption({
    series: [{
      type: 'wordCloud',
      shape: 'circle',
      left: 'center',
      top: 'center',
      width: '90%',
      height: '90%',
      sizeRange: [16, 45],
      rotationRange: [0, 0],
      textStyle: {
        fontFamily: 'Inter',
        fontWeight: 'bold',
        color: () => `rgb(${[Math.round(Math.random()*100+100), Math.round(Math.random()*100+150), 255].join(',')})`
      },
      emphasis: { textStyle: { shadowBlur: 10, shadowColor: '#333' } },
      data
    }]
  })
}

watch(() => props.tags, renderChart)
onMounted(() => {
  renderChart()
  window.addEventListener('resize', () => chart?.resize())
})
</script>