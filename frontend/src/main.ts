import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import './styles/tailwind.css'

import VChart from 'vue-echarts'
import 'echarts-wordcloud'

const app = createApp(App)
app.use(createPinia())
app.component('VChart', VChart)
app.mount('#app')