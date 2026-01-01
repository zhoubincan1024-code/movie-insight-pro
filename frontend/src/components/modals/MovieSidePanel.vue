<template>
  <teleport to="body">
    <transition name="slide">
      <div v-if="modelValue" class="fixed inset-0 z-50 flex justify-end">
        <div class="absolute inset-0 bg-black/70" @click="modelValue = false"></div>
        <div class="relative w-full max-w-lg bg-black/95 backdrop-blur-3xl border-l border-white/10 overflow-y-auto">
          <div class="p-8 pb-20">
            <div class="flex justify-between items-center mb-10">
              <h3 class="text-3xl font-bold">{{ movie?.title || '电影分析' }}</h3>
              <button @click="modelValue = false" class="text-gray-500 hover:text-white">
                <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                </svg>
              </button>
            </div>

            <WordCloudChart :tags="movie?.tags" />

            <div class="mt-10 space-y-6">
              <p class="text-sm text-gray-500 uppercase tracking-widest">详细情报</p>
              <div class="bg-white/5 p-4 rounded-xl">
                <p class="text-xs text-gray-500 mb-1">当前评分</p>
                <p class="text-2xl font-bold text-blue-500">{{ movie?.rating.toFixed(1) || '0.0' }} / 10.0</p>
              </div>
              <div class="bg-white/5 p-4 rounded-xl">
                <p class="text-xs text-gray-500 mb-1">最新热评</p>
                <p class="text-sm italic text-gray-400">"{{ movie?.review || '该电影暂无详细影评内容。' }}"</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </teleport>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useMovieStore } from '@/stores/movieStore'
import WordCloudChart from '@/components/charts/WordCloudChart.vue'

const props = defineProps<{ modelValue: boolean }>()
const emit = defineEmits(['update:modelValue'])

const modelValue = computed({
  get: () => props.modelValue,
  set: (v) => emit('update:modelValue', v)
})

const store = useMovieStore()
const movie = computed(() => store.selectedMovie)
</script>

<style scoped>
.slide-enter-active, .slide-leave-active {
  transition: all 0.5s cubic-bezier(0.16, 1, 0.3, 1);
}
.slide-enter-from, .slide-leave-to {
  transform: translateX(100%);
}
</style>