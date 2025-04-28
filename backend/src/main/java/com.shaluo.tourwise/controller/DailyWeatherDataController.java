package com.shaluo.tourwise.controller;

import com.shaluo.tourwise.service.DailyWeatherDataService;
import com.shaluo.tourwise.model.DailyForecastData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;


// 专门给前端提供天气预报数据的接口。它不处理任何逻辑，只接收日期参数，然后转发给 DailyWeatherDataService。
@RestController
@RequestMapping("/api/weather")   //这个控制器的所有接口路径都以 /weather 开头
public class DailyWeatherDataController {

    private final DailyWeatherDataService service;

    @Autowired
    public DailyWeatherDataController(DailyWeatherDataService service) {
        this.service = service;
    }

    // 获取某一天的天气!
    @GetMapping("/by_date/{date}")
    public List<DailyForecastData> getForecastByDate(@PathVariable String date) {

        // 将字符串 date 转换成 LocalDate
        LocalDate localDate = LocalDate.parse(date);

        return service.getForecastByDate(localDate);
    }

    // 获取一段时间范围内的天气
    @GetMapping("/by_date_range/{startDate}/{endDate}")
    public List<DailyForecastData> getForecastByDateRange(
            @PathVariable String startDate, @PathVariable String endDate) {

        // 把字符串 start、end 转成 LocalDate
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        return service.getForecastByDateRange(start, end);
    }
}