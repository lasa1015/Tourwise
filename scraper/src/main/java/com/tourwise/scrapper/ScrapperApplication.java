package com.tourwise.scrapper;

import com.tourwise.scrapper.service.DailyWeatherForecastService;
import com.tourwise.scrapper.service.EventService;
import io.github.cdimascio.dotenv.Dotenv;
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
        // 检查系统环境变量是否存在关键字段
        // 本地靠 .env 文件，生产靠 Docker Compose 注入环境变量
        if (System.getenv("DB_HOST") == null) {

            // 如果系统变量没有，说明是本地开发，需要手动加载 .env 文件
            Dotenv dotenv = Dotenv.configure()
                    .directory("./scraper") // 指定本地 .env 文件所在目录
                    .load();

            // 将变量设置到系统属性中，Spring Boot 才能识别 ${}
            System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
            System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
            System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
            System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
            System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

            // 从.env中获取 openweather 的 api
            System.setProperty("openweather.api.key", dotenv.get("OPENWEATHER_API_KEY"));

            // 从.env中获取 yelp 的 api
            System.setProperty("yelp.api.key", dotenv.get("YELP_API_KEY"));
        }

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
