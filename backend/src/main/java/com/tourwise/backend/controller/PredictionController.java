package com.tourwise.backend.controller;

import com.tourwise.backend.service.PredictionScheduler;
import com.tourwise.backend.service.AttractionService;
import com.tourwise.backend.service.PredictionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

// 【Controller 里写了太多业务逻辑和数据处理，这些逻辑和“HTTP 请求处理”无关，应该在 PredictionService 里封装好。】

@RestController  // 表示这是一个控制器类，返回的是 JSON 数据
@RequestMapping("/busyness")    // 所有接口路径前缀为 /busyness
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private AttractionService attractionService;

    // 根据景点 index 和时间预测
    @PostMapping("/predict_by_attraction_id")
    public float predict(@RequestParam int attractionIndex, @RequestParam String dateTime) {

        try {
            // 把时间字符串解析成 LocalDateTime 对象
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);

            // 通过 index 找到景点对象，再获取它的 taxiZone。
            int attractionZone = attractionService.getAttractionByIndex(attractionIndex).getTaxi_zone();

            // 从 PredictionService 的内存缓存中拿预测值
            return predictionService.getBusynessByZoneFromMemory(attractionZone, localDateTime);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid dateTime format. Please use ISO_LOCAL_DATE_TIME format.", e);
        } catch (Exception e) {
            e.printStackTrace();

            // 报错时返回 -1，避免服务直接崩掉
            return -1;
        }
    }

    // 根据 taxi zone 和时间预测
    // 功能和上面一样，不过是直接传入 taxiZone 而不是景点 index
    @PostMapping("/predict_by_taxi_zone")
    public float predictByTaxiZone(@RequestParam int taxiZone, @RequestParam String dateTime) {
        try {
            // Parse the dateTime string to LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);

            // Get busyness value from the JSON data
            return predictionService.getBusynessByZoneFromMemory(taxiZone, localDateTime);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid dateTime format. Please use ISO_LOCAL_DATE_TIME format.", e);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // 获取某一时间范围内所有区域、按时间排序的预测值
    @PostMapping("/predict_all_sort_by_date_range")
    public Map<String, Map<Integer, Float>> predictByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        Map<String, Map<Integer, Float>> result = new TreeMap<>();
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            // Retrieve predictions from savedResult in PredictionScheduler
            Map<Integer, Map<String, Map<String, Float>>> predictions = PredictionScheduler.getSavedResult();

            // Loop through each day in the date range
            for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                String dateKey = date.toString();

                // Loop through each hour of the day
                for (int hour = 0; hour < 24; hour++) {
                    LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(hour, 0));
                    String dateTimeKey = dateTime.toString();
                    Map<Integer, Float> hourlyPredictions = new TreeMap<>();

                    // Loop through each taxi zone
                    for (Map.Entry<Integer, Map<String, Map<String, Float>>> zoneEntry : predictions.entrySet()) {
                        int taxiZone = zoneEntry.getKey();
                        Map<String, Map<String, Float>> dateMap = zoneEntry.getValue();

                        if (dateMap.containsKey(dateKey)) {
                            Map<String, Float> timeMap = dateMap.get(dateKey);
                            if (timeMap.containsKey(dateTimeKey)) {
                                float prediction = timeMap.get(dateTimeKey);
                                hourlyPredictions.put(taxiZone, prediction);
                            } else {
                                hourlyPredictions.put(taxiZone, -1.0f);
                            }
                        } else {
                            hourlyPredictions.put(taxiZone, -1.0f);
                        }
                    }
                    result.put(dateTimeKey, hourlyPredictions);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 获取某时间段内所有区域的预测值，按 zone 来组织
    @PostMapping("/predict_all_sort_by_zone")
    public Map<Integer, Map<String, Map<String, Float>>> predictAllSortByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        Map<Integer, Map<String, Map<String, Float>>> result = new TreeMap<>();
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            Map<Integer, Map<String, Map<String, Float>>> predictions = PredictionScheduler.getSavedResult();

            // Loop through each day in the date range
            for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                String dateKey = date.toString();

                // Loop through each taxi zone
                for (Map.Entry<Integer, Map<String, Map<String, Float>>> zoneEntry : predictions.entrySet()) {
                    int taxiZone = zoneEntry.getKey();
                    Map<String, Map<String, Float>> dateMap = zoneEntry.getValue();

                    if (dateMap.containsKey(dateKey)) {
                        Map<String, Float> hourlyPredictions = dateMap.get(dateKey);

                        // Add to result map
                        result.computeIfAbsent(taxiZone, k -> new TreeMap<>())
                                .put(dateKey, hourlyPredictions);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}