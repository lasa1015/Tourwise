package com.tourwise.scrapper.scrapers;

import com.tourwise.scrapper.model.DailyWeatherForecastData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class DailyWeatherForecastScraper {

    private static final Logger logger = LoggerFactory.getLogger(DailyWeatherForecastScraper.class);

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.city.name}")
    private String cityName;

    @Value("${openweather.state.code}")
    private String stateCode;

    @Value("${openweather.country.code}")
    private String countryCode;

    public WeatherDataRaw fetchWeatherData() {
        logger.info("\uD83C\uDF24\uFE0F [WeatherScraper] Starting to fetch weather forecast data...");

        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("https://pro.openweathermap.org/data/2.5/forecast/climate?q=%s,%s,%s&appid=%s",
                cityName, stateCode, countryCode, apiKey);

        try {
            WeatherDataRaw response = restTemplate.getForObject(url, WeatherDataRaw.class);
            if (response != null && response.getList() != null) {
                logger.info("✅[WeatherScraper] Successfully fetched {} days of weather forecast data.", response.getList().size());
            } else {
                logger.warn(" ⚠\uFE0F【empty】[WeatherScraper] API responded, but weather data list is null or empty.");
            }
            return response;
        } catch (RestClientException e) {
            logger.error(" ❌【Failed】 [WeatherScraper] Failed to fetch weather forecast data: {}", e.getMessage());
            return null;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherDataRaw {
        private String code;
        private double message;
        private int cnt;
        private List<DailyForecastDataRaw> list;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public double getMessage() {
            return message;
        }

        public void setMessage(double message) {
            this.message = message;
        }

        public int getCnt() {
            return cnt;
        }

        public void setCnt(int cnt) {
            this.cnt = cnt;
        }

        public List<DailyForecastDataRaw> getList() {
            return list;
        }

        public void setList(List<DailyForecastDataRaw> list) {
            this.list = list;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DailyForecastDataRaw {
        private long dt;
        private long sunrise;
        private long sunset;
        private double pressure;
        private double humidity;
        private double clouds;
        private double rain;
        private double snow;
        private double speed;
        private double deg;
        private DailyWeatherForecastData.Temp temp;
        private DailyWeatherForecastData.Feels_like feels_like;
        private List<DailyWeatherForecastData.Weather> weather;

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }

        public long getSunrise() {
            return sunrise;
        }

        public void setSunrise(long sunrise) {
            this.sunrise = sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public void setSunset(long sunset) {
            this.sunset = sunset;
        }

        public double getPressure() {
            return pressure;
        }

        public void setPressure(double pressure) {
            this.pressure = pressure;
        }

        public double getHumidity() {
            return humidity;
        }

        public void setHumidity(double humidity) {
            this.humidity = humidity;
        }

        public double getClouds() {
            return clouds;
        }

        public void setClouds(double clouds) {
            this.clouds = clouds;
        }

        public double getRain() {
            return rain;
        }

        public void setRain(double rain) {
            this.rain = rain;
        }

        public double getSnow() {
            return snow;
        }

        public void setSnow(double snow) {
            this.snow = snow;
        }

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public double getDeg() {
            return deg;
        }

        public void setDeg(double deg) {
            this.deg = deg;
        }

        public DailyWeatherForecastData.Temp getTemp() {
            return temp;
        }

        public void setTemp(DailyWeatherForecastData.Temp temp) {
            this.temp = temp;
        }

        public DailyWeatherForecastData.Feels_like getFeels_like() {
            return feels_like;
        }

        public void setFeels_like(DailyWeatherForecastData.Feels_like feels_like) {
            this.feels_like = feels_like;
        }

        public List<DailyWeatherForecastData.Weather> getWeather() {
            return weather;
        }

        public void setWeather(List<DailyWeatherForecastData.Weather> weather) {
            this.weather = weather;
        }
    }
}
