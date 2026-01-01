package com.example.demo.repository;

import com.example.demo.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * 满足任务书 2.1: 支持按条件或标签查询（分页版本）
     * 自动生成 SQL: select * from movie where title like %keyword% OR tags like %keyword%
     */
    Page<Movie> findByTitleContainingOrTagsContaining(String title, String tags, Pageable pageable);

    /**
     * 满足任务书 2.1: 支持按条件或标签查询（不分页版本，保留兼容性）
     */
    List<Movie> findByTitleContainingOrTagsContaining(String title, String tags);

    /**
     * 查找特定电影的所有记录（用于分析特定电影的好评比例）
     */
    List<Movie> findByTitle(String title);
}