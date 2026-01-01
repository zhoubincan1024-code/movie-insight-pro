package com.example.demo.controller;

import com.example.demo.entity.Movie;
import com.example.demo.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin // 允许前端跨域访问
public class MovieController {

    @Autowired
    private MovieService movieService;

    // 获取所有数据
    @GetMapping
    public List<Movie> getAll() {
        return movieService.findAll();
    }

    // 接收爬虫数据
    @PostMapping
    public Movie save(@RequestBody Movie movie) {
        return movieService.saveMovie(movie);
    }

    // 搜索接口 (任务书 2.1)
    // 用法: GET /api/movies/search?keyword=科幻
    @GetMapping("/search")
    public List<Movie> search(@RequestParam String keyword) {
        return movieService.search(keyword);
    }

    // 单个电影深度分析 (任务书 2.2)
    // 用法: GET /api/movies/analysis?title=流浪地球2
    @GetMapping("/analysis")
    public Map<String, Object> analyze(@RequestParam String title) {
        return movieService.analyzeMovieSentiment(title);
    }
}