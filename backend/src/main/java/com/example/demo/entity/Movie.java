package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 对应数据库中的 movie 表
 * 包含电影基本信息、情感分析结果、标签和短评
 */
@Entity
@Table(name = "movie", indexes = {
    @Index(name = "idx_title", columnList = "title"),
    @Index(name = "idx_tags", columnList = "tags"),
    @Index(name = "idx_sentiment", columnList = "sentiment")
})
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 电影名称
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * 评分 (如 9.5)
     */
    @Column(nullable = false)
    private Double rating;

    /**
     * 情感倾向 (正面/负面/中性)
     */
    @Column(length = 50)
    private String sentiment;

    /**
     * 标签 (如 "科幻,悬疑,反转") - 满足任务书 2.1 & 2.3
     */
    @Column(length = 500)
    private String tags;

    /**
     * 短评内容 (非结构化文本) - 满足任务书 1.2
     */
    @Column(length = 1000) // 允许存储较长的评论
    private String review;

    /**
     * 入库时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 在插入数据库前自动记录时间
     */
    @PrePersist
    public void prePersist() {
        if (this.createTime == null) {
            this.createTime = LocalDateTime.now();
        }
    }
}