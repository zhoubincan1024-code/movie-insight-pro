package com.example.demo.common.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 分页请求参数
 */
@Data
public class PageRequest {
    
    @Min(value = 0, message = "页码不能小于0")
    private Integer page = 0;
    
    @Min(value = 1, message = "每页大小不能小于1")
    private Integer size = 10;
}

