package com.tourwise.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

// 提供一个简单的接口 /maps/key，让前端可以通过 HTTP 请求获取后端配置的 Google Maps API 密钥。
// 【直接把 Google API key 暴露为公开接口不是一个最佳实践，存在安全隐患】

@RestController
public class GoogleMapController {

    @Value("${google.maps.api.key}")
    private String apiKey;

    @GetMapping("/maps/key")
    public String getApiKey() {
        return apiKey;
    }
}