package com.example.demo;

import com.example.demo.entity.Movie;
import com.example.demo.service.MovieService;
import com.example.demo.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MovieAnalysisTest {

    @Autowired
    private MovieService movieService;

    @MockBean
    private MovieRepository movieRepository;

    @Test
    void testSentimentAnalysis() {
        // 1. 准备模拟数据 (Mock Data)
        Movie m1 = new Movie(); m1.setTitle("测试电影"); m1.setSentiment("正面");
        Movie m2 = new Movie(); m2.setTitle("测试电影"); m2.setSentiment("正面");
        Movie m3 = new Movie(); m3.setTitle("测试电影"); m3.setSentiment("负面");
        Movie m4 = new Movie(); m4.setTitle("测试电影"); m4.setSentiment("中性");

        // 告诉测试系统：当去数据库找 "测试电影" 时，直接返回上面这4条假数据，不要真去查数据库
        Mockito.when(movieRepository.findByTitle("测试电影"))
                .thenReturn(Arrays.asList(m1, m2, m3, m4));

        // 2. 执行业务逻辑
        Map<String, Object> result = movieService.analyzeMovieSentiment("测试电影");

        // 3. 验证结果 (Assert)
        // 总数应该是 4
        assertEquals(4, result.get("totalReviews"));
        // 正面评论是 2，所以好评率应该是 0.5 (50%)
        assertEquals(0.5, result.get("positiveRate"));

        System.out.println("✅ 测试通过：好评率计算逻辑正确！");
    }
}