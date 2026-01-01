package com.example.demo.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI 配置
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("电影洞察系统 API 文档")
                        .version("1.0.0")
                        .description("电影数据分析和情感分析系统 API 接口文档")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("dev@example.com")));
    }
}

