package com.example.demo.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 电影数据传输对象（用于接收前端数据）
 */
@Data
public class MovieDTO {
    
    @NotBlank(message = "电影名称不能为空")
    @Size(max = 200, message = "电影名称长度不能超过200个字符")
    private String title;

    @NotNull(message = "评分不能为空")
    @Min(value = 0, message = "评分不能小于0")
    @Max(value = 10, message = "评分不能大于10")
    private Double rating;

    @Size(max = 50, message = "情感倾向长度不能超过50个字符")
    private String sentiment;

    @Size(max = 500, message = "标签长度不能超过500个字符")
    private String tags;

    @Size(max = 1000, message = "评论长度不能超过1000个字符")
    private String review;
}

