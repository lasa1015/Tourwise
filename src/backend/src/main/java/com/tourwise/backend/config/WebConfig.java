package com.tourwise.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(

                        "http://localhost:3000", // Allow requests from localhost:3000 (dev)
                        "http://frontend:3000", // Allow requests from frontend service
                        "http://backend:8080",  // Allow requests from backend service
                        "http://tourwise.site:3000",
                        "http://54.228.23.122:3000"

                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
