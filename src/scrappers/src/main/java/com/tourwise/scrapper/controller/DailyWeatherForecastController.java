package com.tourwise.scrapper.controller;

import com.tourwise.scrapper.model.DailyWeatherForecastData;
import com.tourwise.scrapper.service.DailyWeatherForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/daily_weather_forecast")
public class DailyWeatherForecastController {
    @Autowired
    private DailyWeatherForecastService dailyWeatherForecastService;

    @GetMapping("/30_day")
    public List<DailyWeatherForecastData> getWeather() {
        return dailyWeatherForecastService.getDailyForecasts();
    }
}
