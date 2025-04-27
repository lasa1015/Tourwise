package com.tourwise.scrapper.service;

import com.tourwise.scrapper.model.DailyWeatherForecastData;
import com.tourwise.scrapper.repository.DailyWeatherForecastRepository;
import com.tourwise.scrapper.scrapers.DailyWeatherForecastScraper;
import com.tourwise.scrapper.scrapers.DailyWeatherForecastScraper.WeatherDataRaw;
import com.tourwise.scrapper.scrapers.DailyWeatherForecastScraper.DailyForecastDataRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// 每小时定时抓取一次天气预报数据；
// 清空旧数据，保存新数据到数据库；
// 提供方法用于返回数据库中的天气数据


@Service
public class DailyWeatherForecastService {

    private static final Logger logger = LoggerFactory.getLogger(DailyWeatherForecastService.class);

    private final DailyWeatherForecastRepository dailyWeatherForecastRepository;
    private final DailyWeatherForecastScraper dailyWeatherForecastScraper;

    @Autowired
    public DailyWeatherForecastService(DailyWeatherForecastRepository dailyWeatherForecastRepository,
                                       DailyWeatherForecastScraper dailyWeatherForecastScraper) {
        this.dailyWeatherForecastRepository = dailyWeatherForecastRepository;
        this.dailyWeatherForecastScraper = dailyWeatherForecastScraper;
    }

    // 定时任务方法
    @Scheduled(fixedRate = 3600000) // 每隔一小时
    public void updateDailyWeather() {
        logger.info("\uD83C\uDF24\uFE0F [WeatherService] Starting scheduled weather forecast update...");

        // 抓数据
        WeatherDataRaw weatherDataRaw = dailyWeatherForecastScraper.fetchWeatherData();

        // 校验数据是否为空
        if (weatherDataRaw != null && weatherDataRaw.getList() != null && !weatherDataRaw.getList().isEmpty()) {
            logger.info("\uD83C\uDF24\uFE0F [WeatherService] Clearing old weather forecast data...");

            // 清空旧数据
            dailyWeatherForecastRepository.deleteAll();

            // 转换并保存
            List<DailyWeatherForecastData> dailyForecasts = weatherDataRaw.getList().stream()
                    .map(this::convertToDailyForecastData)
                    .collect(Collectors.toList());

            logger.info("\uD83C\uDF24\uFE0F [WeatherService] trying to save {} forecast records into the database...", dailyForecasts.size());
            dailyWeatherForecastRepository.saveAll(dailyForecasts);
            logger.info("✅ [WeatherService] Weather forecast data successfully saved into the database.");
        } else {
            logger.warn("⚠️ [WeatherService] No weather forecast data received or list is empty. Skipping update.");
        }
    }

    private DailyWeatherForecastData convertToDailyForecastData(DailyForecastDataRaw raw) {
        DailyWeatherForecastData data = new DailyWeatherForecastData();
        data.setId(UUID.randomUUID());
        data.setFetchTime(LocalDateTime.now());
        data.setDt(raw.getDt());
        data.setSunrise(raw.getSunrise());
        data.setSunset(raw.getSunset());
        data.setPressure(raw.getPressure());
        data.setHumidity(raw.getHumidity());
        data.setClouds(raw.getClouds());
        data.setRain(raw.getRain());
        data.setSnow(raw.getSnow());
        data.setWindSpeed(raw.getSpeed());
        data.setWindDeg(raw.getDeg());
        data.setTemp(raw.getTemp());
        data.setFeels_like(raw.getFeels_like());
        if (raw.getWeather() != null && !raw.getWeather().isEmpty()) {
            DailyWeatherForecastData.Weather weather = raw.getWeather().get(0);
            data.setWeather(weather);
        }
        return data;
    }

    // 读取天气数据
    public List<DailyWeatherForecastData> getDailyForecasts() {
        return dailyWeatherForecastRepository.findAll();
    }
}
