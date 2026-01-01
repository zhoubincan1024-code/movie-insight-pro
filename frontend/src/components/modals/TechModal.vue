<template>
  <teleport to="body">
    <transition name="fade">
      <div v-if="modelValue" class="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-lg" @click.self="modelValue = false">
        <div class="glass-panel p-10 max-w-2xl w-full mx-6">
          <h2 class="text-3xl font-bold mb-6">系统架构</h2>
          <div class="space-y-6">
            <div class="flex items-start gap-4">
              <div class="w-8 h-8 rounded-full bg-blue-500 flex items-center justify-center font-bold text-black">1</div>
              <div>
                <h4 class="font-bold">数据层 (Python Spider)</h4>
                <p class="text-gray-400 text-sm">利用 Requests & BeautifulSoup 抓取非结构化影评数据，模拟分布式清洗逻辑。</p>
              </div>
            </div>
            <div class="flex items-start gap-4">
              <div class="w-8 h-8 rounded-full bg-emerald-500 flex items-center justify-center font-bold text-black">2</div>
              <div>
                <h4 class="font-bold">服务层 (Spring Boot)</h4>
                <p class="text-gray-400 text-sm">Java 后端处理 CRUD 及统计分析，利用 JPA 进行数据持久化。</p>
              </div>
            </div>
            <div class="flex items-start gap-4">
              <div class="w-8 h-8 rounded-full bg-purple-500 flex items-center justify-center font-bold text-black">3</div>
              <div>
                <h4 class="font-bold">表现层 (Tailwind & ECharts)</h4>
                <p class="text-gray-400 text-sm">极致简约的 Apple 风格前端，集成标签云及情感分布图表。</p>
              </div>
            </div>
          </div>
          <button @click="modelValue = false" class="mt-10 w-full py-3 bg-white text-black rounded-xl font-bold">
            关闭
          </button>
        </div>
      </div>
    </transition>
  </teleport>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{ modelValue: boolean }>()
const emit = defineEmits(['update:modelValue'])

const modelValue = computed({
  get: () => props.modelValue,
  set: (v: boolean) => emit('update:modelValue', v)
})
</script>

<style scoped>
.fade-enter-active, .fade-leave-active { transition: opacity 0.3s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>