package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.dto.MovieDTO;
import com.example.demo.common.dto.PageRequest;
import com.example.demo.common.dto.PageResponse;
import com.example.demo.entity.Movie;
import com.example.demo.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 电影控制器
 */
@RestController
@RequestMapping("/api/movies")
@Validated
@Tag(name = "电影管理", description = "电影数据的增删改查和情感分析接口")
public class MovieController {

    private final MovieService movieService;

    // 使用构造函数注入（推荐方式）
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * 获取所有电影（支持分页）
     * GET /api/movies?page=0&size=10
     * 如果不传 size 或 size=0，则返回所有数据（不分页）
     */
    @Operation(summary = "获取所有电影", description = "分页获取所有电影数据，size=0时返回所有数据")
    @GetMapping
    public ApiResponse<?> getAll(
            @Parameter(description = "页码，从0开始", example = "0")
            @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "每页大小，0表示不分页返回所有数据", example = "10")
            @RequestParam(defaultValue = "10") Integer size) {
        // 如果 size 为 0，返回所有数据（不分页）
        if (size == 0) {
            List<Movie> allMovies = movieService.findAllWithoutPaging();
            return ApiResponse.success(allMovies);
        }
        // 否则返回分页数据
        PageResponse<Movie> result = movieService.findAll(page, size);
        return ApiResponse.success(result);
    }

    /**
     * 接收爬虫数据
     * POST /api/movies
     */
    @Operation(summary = "保存电影", description = "接收并保存电影数据（用于爬虫数据入库）")
    @PostMapping
    public ApiResponse<Movie> save(@Valid @RequestBody MovieDTO movieDTO) {
        Movie movie = movieService.saveMovie(movieDTO);
        return ApiResponse.success("电影保存成功", movie);
    }

    /**
     * 搜索接口 (任务书 2.1)
     * GET /api/movies/search?keyword=科幻&page=0&size=10
     */
    @Operation(summary = "搜索电影", description = "根据关键词搜索电影（支持按标题或标签搜索）")
    @GetMapping("/search")
    public ApiResponse<PageResponse<Movie>> search(
            @Parameter(description = "搜索关键词", required = true, example = "科幻")
            @RequestParam @NotBlank(message = "搜索关键词不能为空") String keyword,
            @Parameter(description = "页码，从0开始", example = "0")
            @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "每页大小", example = "10")
            @RequestParam(defaultValue = "10") Integer size) {
        PageResponse<Movie> result = movieService.search(keyword, page, size);
        return ApiResponse.success(result);
    }

    /**
     * 单个电影深度分析 (任务书 2.2)
     * GET /api/movies/analysis?title=流浪地球2
     */
    @Operation(summary = "电影情感分析", description = "分析指定电影的好评/差评比例")
    @GetMapping("/analysis")
    public ApiResponse<Map<String, Object>> analyze(
            @Parameter(description = "电影名称", required = true, example = "流浪地球2")
            @RequestParam @NotBlank(message = "电影名称不能为空") String title) {
        Map<String, Object> result = movieService.analyzeMovieSentiment(title);
        return ApiResponse.success(result);
    }
}