import { defineStore } from 'pinia'
import axios from 'axios'

const API_BASE = 'http://localhost:8081/api/movies'

export interface Movie {
  title: string
  rating: number
  sentiment: '正面' | '中性' | '负面'
  review?: string
  tags?: string
}

export const useMovieStore = defineStore('movie', {
  state: () => ({
    movies: [] as Movie[],
    selectedMovie: null as Movie | null,
    loading: true,
    searchKeyword: ''
  }),
  getters: {
    totalCount: (state) => state.movies.length,
    avgRating: (state): string => {
      if (!state.movies.length) return '0.0'
      const sum = state.movies.reduce((s, m) => s + m.rating, 0)
      return (sum / state.movies.length).toFixed(1)
    },
    posRate: (state): string => {
      const pos = state.movies.filter(m => m.sentiment === '正面').length
      return state.movies.length ? Math.round((pos / state.movies.length) * 100) + '%' : '0%'
    }
  },
  actions: {
    async fetchMovies(keyword = '') {
      this.loading = true
      try {
        const url = keyword
          ? `${API_BASE}/search?keyword=${encodeURIComponent(keyword)}`
          : API_BASE
        const res = await axios.get<Movie[]>(url)
        this.movies = res.data
      } catch (e) {
        console.error('fetch error', e)
      } finally {
        this.loading = false
      }
    },
    selectMovie(title: string) {
      this.selectedMovie = this.movies.find(m => m.title === title) || null
    }
  }
})