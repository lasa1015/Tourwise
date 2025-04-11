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

    @Scheduled(fixedRate = 3600000)
    public void updateDailyWeather() {
        logger.info("\uD83C\uDF24\uFE0F [WeatherService] Starting scheduled weather forecast update...");
        WeatherDataRaw weatherDataRaw = dailyWeatherForecastScraper.fetchWeatherData();

        if (weatherDataRaw != null && weatherDataRaw.getList() != null && !weatherDataRaw.getList().isEmpty()) {
            logger.info("\uD83C\uDF24\uFE0F [WeatherService] Clearing old weather forecast data...");
            dailyWeatherForecastRepository.deleteAll();

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

    public List<DailyWeatherForecastData> getDailyForecasts() {
        return dailyWeatherForecastRepository.findAll();
    }
}
