package com.tourwise.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// 用来配置 全局的跨域资源共享（CORS）策略，让前端（比如 React）可以访问你的 Spring Boot 后端接口。

// 实现了 WebMvcConfigurer 接口，覆盖了其中的一个方法 addCorsMappings，
// 这就是 Spring Boot 提供的全局跨域配置接口。
@Configuration  // @Configuration 表示这是一个配置类，Spring 会自动加载它。
public class WebConfig implements WebMvcConfigurer {


    // 对所有接口路径启用跨域设置，比如 /users/**、/weather/**、/itinerary/** 全部都包含
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",       // 开发时的前端端口
                        "http://frontend:3000",        // Docker Compose 里的 frontend 容器
                        "http://backend:8080",         // 后端容器互相请求（也允许）
                        "http://tourwise.site:3000",   // 你部署的正式站点
                        "http://54.228.23.122:3000"    // 公网 IP 的前端地址
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
