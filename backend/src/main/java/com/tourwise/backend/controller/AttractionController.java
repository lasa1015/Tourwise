package com.tourwise.backend.controller;

import com.tourwise.backend.model.Attraction;
import com.tourwise.backend.service.AttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Arrays;

// 用于处理前端发来的请求，提供旅游景点（Attractions）相关的接口。
// 它的职责非常单一清晰 —— 接收参数 → 调用服务类 → 返回数据
// 这个类本身不做任何数据处理，一切交给 Service 层。

@RestController
@RequestMapping("/attractions")
public class AttractionController {

    @Autowired
    private AttractionService attractionService;

    // 根据 名称 、 是否免费 、 分类列表 筛选景点
    // 支持按 评分（默认降序）或价格（升序） 排序
    @GetMapping("/filter")
    public List<Attraction> filterAttractions(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isFree,
            @RequestParam(required = false) String categories,
            @RequestParam(required = false, defaultValue = "rating") String sortBy,
            @RequestParam(required = false) String order) {

        // 如果传了 categories，就把它用 , 分割成列表（比如 "museum,park" → ["museum", "park"]）
        List<String> categoryList = categories != null ? Arrays.asList(categories.split(",")) : null;

        // 如果没传排序方式，默认asc
        if (order == null) {
            if ("price".equalsIgnoreCase(sortBy)) {
                order = "asc";
            } else {
                order = "desc";
            }
        }

        return attractionService.filterAndSortAttractions(name, isFree, categoryList, sortBy, order);
    }


    // 跟上面差不多，但额外加了“时间范围”过滤条件，比如用户只想看某天到某天之间开放的景点
    @GetMapping("/filter_within_date")
    public List<Attraction> filterAttractions(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isFree,
            @RequestParam(required = false) String categories,
            @RequestParam(required = false, defaultValue = "rating") String sortBy,
            @RequestParam(required = false) String order) {


        if (order == null) {
            if ("price".equalsIgnoreCase(sortBy)) {
                order = "asc";
            } else {
                order = "desc";
            }
        }

        List<String> categoryList = categories != null ? Arrays.asList(categories.split(",")) : null;

        return attractionService.filterAndSortAttractionsWithDate(name, isFree, categoryList, sortBy, order, startDate, endDate);
    }
}
