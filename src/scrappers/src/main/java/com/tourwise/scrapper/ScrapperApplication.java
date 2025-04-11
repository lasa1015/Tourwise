package com.tourwise.scrapper;

import com.tourwise.scrapper.environment.environmentLoader;
import com.tourwise.scrapper.service.DailyWeatherForecastService;
import com.tourwise.scrapper.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication

@EnableScheduling
public class ScrapperApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ScrapperApplication.class);

    @Autowired
    private DailyWeatherForecastService dailyWeatherForecastService;

    @Autowired
    private EventService eventService;

    public static void main(String[] args) {
        environmentLoader.load(); // Load environment variables from .env file
        SpringApplication.run(ScrapperApplication.class, args);

    }

    @Override
    public void run(String... args){
        logger.info("\uD83D\uDE80 App started. Running weather and event scrapers.");
        dailyWeatherForecastService.getDailyForecasts();
        System.out.println("Daily weather forecast data is stored in tables");
        eventService.fetchAndSaveEvents();
        System.out.println("Event data is fetched and stored in events table");
    }
}
