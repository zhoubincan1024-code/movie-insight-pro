package com.example.demo.service;

import com.example.demo.entity.Movie;
import com.example.demo.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    // 1. 保存电影 (含简单的数据清洗)
    public Movie saveMovie(Movie movie) {
        // 数据清洗：如果评分不合法，修正为 0
        if (movie.getRating() == null || movie.getRating() < 0) {
            movie.setRating(0.0);
        }
        // 数据清洗：去除评论首尾空格
        if (movie.getReview() != null) {
            movie.setReview(movie.getReview().trim());
        }
        return movieRepository.save(movie);
    }

    // 2. 获取所有电影
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    // 3. 搜索电影 (按标题或标签)
    public List<Movie> search(String keyword) {
        return movieRepository.findByTitleContainingOrTagsContaining(keyword, keyword);
    }

    // 4. 数据分析接口 (满足任务书 2.2: 统计某部电影的好评/差评比例)
    public Map<String, Object> analyzeMovieSentiment(String title) {
        List<Movie> movies = movieRepository.findByTitle(title);

        int total = movies.size();
        long positiveCount = movies.stream().filter(m -> "正面".equals(m.getSentiment())).count();
        long negativeCount = movies.stream().filter(m -> "负面".equals(m.getSentiment())).count();

        Map<String, Object> result = new HashMap<>();
        result.put("movieTitle", title);
        result.put("totalReviews", total);
        result.put("positiveRate", total == 0 ? 0 : (double)positiveCount / total);
        result.put("negativeRate", total == 0 ? 0 : (double)negativeCount / total);

        return result;
    }
}