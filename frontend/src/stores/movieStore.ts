import { defineStore } from 'pinia'
import axios from 'axios'

const API_BASE = 'http://localhost:8081/api/movies'

export interface Movie {
  id?: number
  title: string
  rating: number
  sentiment: '正面' | '中性' | '负面'
  review?: string
  tags?: string
  createTime?: string
}

// 后端统一响应格式
interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

// 分页响应格式
interface PageResponse<T> {
  content: T[]
  page: number
  size: number
  totalElements: number
  totalPages: number
}

export const useMovieStore = defineStore('movie', {
  state: () => ({
    movies: [] as Movie[],
    selectedMovie: null as Movie | null,
    loading: true,
    searchKeyword: '',
    // 分页相关状态
    currentPage: 0,
    pageSize: 10,
    totalElements: 0,
    totalPages: 0,
    // 统计信息（基于所有数据，不是当前页）
    allMoviesStats: {
      totalCount: 0,
      avgRating: 0,
      positiveCount: 0
    }
  }),
  getters: {
    // 当前页的电影数量
    currentPageCount: (state) => state.movies.length,
    // 总数据量（用于显示）
    totalCount: (state) => state.totalElements || state.allMoviesStats.totalCount,
    // 平均评分（基于所有数据）
    avgRating: (state): string => {
      if (state.allMoviesStats.totalCount === 0) return '0.0'
      return state.allMoviesStats.avgRating.toFixed(1)
    },
    // 正面情绪占比（基于所有数据）
    posRate: (state): string => {
      if (state.allMoviesStats.totalCount === 0) return '0%'
      return Math.round((state.allMoviesStats.positiveCount / state.allMoviesStats.totalCount) * 100) + '%'
    },
    // 是否有上一页
    hasPrevious: (state) => state.currentPage > 0,
    // 是否有下一页
    hasNext: (state) => state.currentPage < state.totalPages - 1
  },
  actions: {
    /**
     * 获取电影数据（支持分页）
     * @param keyword 搜索关键词
     * @param page 页码（从0开始）
     * @param size 每页大小
     */
    async fetchMovies(keyword = '', page?: number, size?: number) {
      this.loading = true
      try {
        // 使用传入的参数或使用 state 中的值
        const currentPage = page !== undefined ? page : this.currentPage
        const pageSize = size !== undefined ? size : this.pageSize
        
        // 更新 state
        this.currentPage = currentPage
        this.pageSize = pageSize
        this.searchKeyword = keyword
        
        let url: string
        
        if (keyword) {
          // 搜索接口：使用分页
          url = `${API_BASE}/search?keyword=${encodeURIComponent(keyword)}&page=${currentPage}&size=${pageSize}`
        } else {
          // 获取所有电影：使用分页
          url = `${API_BASE}?page=${currentPage}&size=${pageSize}`
        }
        
        const res = await axios.get<ApiResponse<PageResponse<Movie>>>(url)
        
        // 检查响应格式
        if (res.data.code === 200 && res.data.data) {
          const pageData = res.data.data
          this.movies = pageData.content || []
          this.totalElements = pageData.totalElements || 0
          this.totalPages = pageData.totalPages || 0
          
          console.log(`成功加载第 ${currentPage + 1}/${this.totalPages} 页，共 ${this.totalElements} 条数据`)
          
          // 如果是第一页且没有搜索关键词，获取统计信息
          if (currentPage === 0 && !keyword) {
            this.fetchStats()
          }
        } else {
          console.error('API 返回错误:', res.data.message)
          this.movies = []
          this.totalElements = 0
          this.totalPages = 0
        }
      } catch (e: any) {
        console.error('fetch error', e)
        this.movies = []
        this.totalElements = 0
        this.totalPages = 0
      } finally {
        this.loading = false
      }
    },
    
    /**
     * 获取统计信息（所有数据的统计）
     */
    async fetchStats() {
      try {
        // 获取所有数据用于统计（不分页）
        const res = await axios.get<ApiResponse<Movie[]>>(`${API_BASE}?page=0&size=0`)
        
        if (res.data.code === 200 && Array.isArray(res.data.data)) {
          const allMovies = res.data.data
          const totalCount = allMovies.length
          const sumRating = allMovies.reduce((sum, m) => sum + m.rating, 0)
          const positiveCount = allMovies.filter(m => m.sentiment === '正面').length
          
          this.allMoviesStats = {
            totalCount,
            avgRating: totalCount > 0 ? sumRating / totalCount : 0,
            positiveCount
          }
        }
      } catch (e) {
        console.error('获取统计信息失败', e)
      }
    },
    
    /**
     * 上一页
     */
    async previousPage() {
      if (this.hasPrevious) {
        await this.fetchMovies(this.searchKeyword, this.currentPage - 1)
      }
    },
    
    /**
     * 下一页
     */
    async nextPage() {
      if (this.hasNext) {
        await this.fetchMovies(this.searchKeyword, this.currentPage + 1)
      }
    },
    
    /**
     * 跳转到指定页
     */
    async goToPage(page: number) {
      if (page >= 0 && page < this.totalPages) {
        await this.fetchMovies(this.searchKeyword, page)
      }
    },
    
    /**
     * 更改每页大小
     */
    async changePageSize(size: number) {
      if (size > 0) {
        this.pageSize = size
        // 重新计算当前页（避免超出范围）
        const maxPage = Math.max(0, Math.ceil(this.totalElements / size) - 1)
        const newPage = Math.min(this.currentPage, maxPage)
        await this.fetchMovies(this.searchKeyword, newPage, size)
      }
    },
    
    selectMovie(title: string) {
      this.selectedMovie = this.movies.find(m => m.title === title) || null
    }
  }
})