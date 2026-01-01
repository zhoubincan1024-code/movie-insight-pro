<template>
  <section class="relative h-screen flex items-center justify-center text-center px-6">
    <div class="absolute inset-0 -z-10">
      <img
        src="https://images.unsplash.com/photo-1478720568477-152d9b164e26?auto=format&fit=crop&w=2000&q=80"
        class="w-full h-full object-cover opacity-30"
        alt="Hero"
      />
      <div class="absolute inset-0 bg-gradient-to-b from-transparent to-black"></div>
    </div>
    <div class="max-w-4xl">
      <h1 class="text-6xl md:text-8xl font-bold tracking-tighter mb-4">
        Movie Insight <span class="text-blue-500">Pro</span>
      </h1>
      <p class="text-xl text-gray-400 font-light mb-10">重塑电影舆情数据的视觉美学。</p>
      <div class="flex flex-col md:flex-row gap-4 justify-center">
        <button
          @click="scrollToDashboard"
          class="bg-white text-black px-10 py-3 rounded-full font-medium hover:bg-gray-200 transition"
        >
          立即开始
        </button>
        <button
          @click="controls.showTechModal = true"
          class="text-blue-500 hover:underline flex items-center gap-1 mx-auto md:mx-0"
        >
          了解技术架构
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
          </svg>
        </button>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { inject } from 'vue'

const controls = inject('appControls') as { showTechModal: boolean }

const scrollToDashboard = () => {
  document.getElementById('dashboard')?.scrollIntoView({ behavior: 'smooth' })
}
</script>