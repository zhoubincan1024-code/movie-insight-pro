package com.example.demo.service;

import com.example.demo.common.dto.MovieDTO;
import com.example.demo.common.dto.PageResponse;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.entity.Movie;
import com.example.demo.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电影服务类
 */
@Service
public class MovieService {

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);
    
    private final MovieRepository movieRepository;

    // 使用构造函数注入（推荐方式）
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * 保存电影 (含简单的数据清洗)
     */
    @Transactional
    public Movie saveMovie(MovieDTO movieDTO) {
        logger.info("保存电影: {}", movieDTO.getTitle());
        
        // 将 DTO 转换为 Entity
        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setRating(movieDTO.getRating());
        movie.setSentiment(movieDTO.getSentiment());
        movie.setTags(movieDTO.getTags());
        movie.setReview(movieDTO.getReview());
        
        // 数据清洗：如果评分不合法，修正为 0
        if (movie.getRating() == null || movie.getRating() < 0) {
            logger.warn("电影 {} 的评分不合法，已修正为 0", movie.getTitle());
            movie.setRating(0.0);
        }
        // 数据清洗：去除评论首尾空格
        if (movie.getReview() != null) {
            movie.setReview(movie.getReview().trim());
        }
        
        Movie saved = movieRepository.save(movie);
        logger.info("电影保存成功，ID: {}", saved.getId());
        return saved;
    }

    /**
     * 获取所有电影（分页）
     */
    public PageResponse<Movie> findAll(Integer page, Integer size) {
        logger.debug("查询所有电影，页码: {}, 每页大小: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> moviePage = movieRepository.findAll(pageable);
        
        return PageResponse.of(
            moviePage.getContent(),
            page,
            size,
            moviePage.getTotalElements()
        );
    }

    /**
     * 获取所有电影（不分页）
     */
    public List<Movie> findAllWithoutPaging() {
        logger.debug("查询所有电影（不分页）");
        return movieRepository.findAll();
    }

    /**
     * 搜索电影 (按标题或标签，支持分页)
     */
    public PageResponse<Movie> search(String keyword, Integer page, Integer size) {
        logger.info("搜索电影，关键词: {}, 页码: {}, 每页大小: {}", keyword, page, size);
        
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BusinessException("搜索关键词不能为空");
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> moviePage = movieRepository.findByTitleContainingOrTagsContaining(
            keyword.trim(), keyword.trim(), pageable);
        
        logger.debug("搜索到 {} 条结果", moviePage.getTotalElements());
        return PageResponse.of(
            moviePage.getContent(),
            page,
            size,
            moviePage.getTotalElements()
        );
    }

    /**
     * 数据分析接口 (满足任务书 2.2: 统计某部电影的好评/差评比例)
     */
    public Map<String, Object> analyzeMovieSentiment(String title) {
        logger.info("分析电影情感: {}", title);
        
        if (title == null || title.trim().isEmpty()) {
            throw new BusinessException("电影名称不能为空");
        }
        
        List<Movie> movies = movieRepository.findByTitle(title.trim());
        
        if (movies.isEmpty()) {
            logger.warn("未找到电影: {}", title);
            throw new BusinessException("未找到电影: " + title);
        }

        int total = movies.size();
        long positiveCount = movies.stream()
            .filter(m -> "正面".equals(m.getSentiment()))
            .count();
        long negativeCount = movies.stream()
            .filter(m -> "负面".equals(m.getSentiment()))
            .count();
        long neutralCount = total - positiveCount - negativeCount;

        Map<String, Object> result = new HashMap<>();
        result.put("movieTitle", title);
        result.put("totalReviews", total);
        result.put("positiveCount", positiveCount);
        result.put("negativeCount", negativeCount);
        result.put("neutralCount", neutralCount);
        result.put("positiveRate", total == 0 ? 0.0 : (double) positiveCount / total);
        result.put("negativeRate", total == 0 ? 0.0 : (double) negativeCount / total);
        result.put("neutralRate", total == 0 ? 0.0 : (double) neutralCount / total);
        
        logger.info("电影 {} 分析完成，总评论数: {}, 正面: {}, 负面: {}", 
            title, total, positiveCount, negativeCount);
        
        return result;
    }
}