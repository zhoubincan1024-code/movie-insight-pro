<template>
  <div class="min-h-screen overflow-x-hidden">
    <NavBar />
    <HeroSection />
    <main id="dashboard" class="max-w-7xl mx-auto px-6 py-24">
      <DashboardCards />
      <div id="analysis" class="grid lg:grid-cols-2 gap-8 my-20">
        <RatingChart />
        <SentimentChart />
      </div>
      <MovieTable @open-side-panel="showSidePanel = true" />
    </main>
    <MovieSidePanel v-model="showSidePanel" />
    <TechModal v-model="showTechModal" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, provide } from 
import NavBar from '@/components/layout/NavBar.vue'
import HeroSection from '@/components/layout/HeroSection.vue'
import DashboardCards from '@/components/DashboardCards.vue'
import RatingChart from '@/components/charts/RatingChart.vue'
import SentimentChart from '@/components/charts/SentimentChart.vue'
import MovieTable from '@/components/MovieTable.vue'
import MovieSidePanel from '@/components/modals/MovieSidePanel.vue'
import TechModal from '@/components/modals/TechModal.vue'
import { useMovieStore } from '@/stores/movieStore'

const store = useMovieStore()
onMounted(() => store.fetchMovies())

const showSidePanel = ref(false)
const showTechModal = ref(false)

// 关键：提供给子组件（HeroSection）使用，使用 getter/setter 保持响应性
provide('appControls', {
  get showTechModal() {
    return showTechModal.value
  },
  set showTechModal(value: boolean) {
    showTechModal.value = value
  }
})
</script>