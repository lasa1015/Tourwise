package com.shaluo.tourwise.controller;

import com.shaluo.tourwise.model.Event;
import com.shaluo.tourwise.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

// 负责接收前端关于 活动查询和筛选 的 HTTP 请求，然后调用 EventService 来返回活动数据

@RestController
@RequestMapping("/api/events")  // 这个控制器下的所有接口路径前缀都是 /events
public class EventController {

    @Autowired
    private EventService eventService;

    // 不带时间过滤的活动筛选
    // 提供一个模糊搜索接口,
    // 根据：活动是否免费（isFree）,活动所属分类（combined_categories，可多个）, 活动名称关键字（name）
    @GetMapping("/filter")
    public List<Event> getAllEvents(
            @RequestParam(value = "isFree", required = false) Boolean isFree,
            @RequestParam(value = "combined_categories", required = false) String combined_categories,
            @RequestParam(value = "name", required = false) String name) {

        // 把名字处理成模糊搜索格式
        if (name != null && !name.isEmpty()) {
            name = "%" + String.join("%", name.trim().toLowerCase().split("\\s+")) + "%";
        }

        // 把分类处理成List
        List<String> categoryList = (combined_categories != null && !combined_categories.isEmpty()) ?
                Arrays.asList(combined_categories.split(",")) : null;

        // 新增：今天纽约时间
        ZoneOffset zoneOffset = ZoneOffset.of("-04:00"); // 纽约夏令时
        String today = LocalDate.now().atStartOfDay().atOffset(zoneOffset).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        // 调用 service 时带上 "今天之后" 的条件
        return eventService.getFilteredEventsAfterDate(today, isFree, categoryList, name);
    }


    // 带时间范围的活动筛选
    @GetMapping("/filter_within_date")
    public List<Event> getFilteredEventsWithDates(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "isFree", required = false) Boolean isFree,
            @RequestParam(value = "combined_categories", required = false) String combined_categories,
            @RequestParam(value = "name", required = false) String name) {

        // 将日期转换为带时区的字符串（例如 2025-04-17T00:00:00-04:00）
        ZoneOffset zoneOffset = ZoneOffset.of("-04:00");
        String startDateTime = startDate.atStartOfDay().atOffset(zoneOffset).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String endDateTime = endDate.atTime(23, 59, 59).atOffset(zoneOffset).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        if (name != null && !name.isEmpty()) {
            name = "%" + String.join("%", name.trim().toLowerCase().split("\\s+")) + "%";
        }
        List<String> categoryList = (combined_categories != null && !combined_categories.isEmpty()) ? Arrays.asList(combined_categories.split(",")) : null;
        return eventService.getFilteredEventsWithinDateRange(startDateTime, endDateTime, isFree, categoryList, name);
    }
}