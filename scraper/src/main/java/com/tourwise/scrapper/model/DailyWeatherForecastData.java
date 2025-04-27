package com.tourwise.scrapper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "daily_forecast_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyWeatherForecastData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // 自动生成 UUID
    // @JsonIgnore 是 Jackson 提供的一个注解。在把对象转成 JSON 的时候，忽略掉这个字段，不要返回给前端。
    // 常用于 密码等， 此处其实没必要写
    @JsonIgnore
    private UUID id;

    @Column(name = "fetch_time")
    private LocalDateTime fetchTime;

    @Column(name = "dt")
    private long dt;

    @Column(name = "sunrise")
    private long sunrise;

    @Column(name = "sunset")
    private long sunset;

    @Column(name = "pressure")
    private double pressure;

    @Column(name = "humidity")
    private double humidity;

    @Column(name = "clouds")
    private double clouds;

    @Column(name = "rain")
    private double rain;

    @Column(name = "snow")
    private double snow;

    @JsonProperty("speed")
    @Column(name = "speed")
    private double windSpeed;

    @JsonProperty("deg")
    @Column(name = "deg")
    private double windDeg;

    @Embedded
    private Temp temp;

    @Embedded
    private Feels_like feels_like;

    @Embedded
    private Weather weather;

    // 嵌套类: 把多个字段看作一个“逻辑上的整体”，但实际上存在同一个表里，不会创建新的表。
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Temp {

        // @JsonProperty 指定 Java 字段在序列化/反序列化 JSON 时，对应的 JSON 字段名称
        // 在对接第三方天气 API 时 json 字段一致方便处理
        @JsonProperty("day")
        @Column(name = "temp_day")
        private double day;

        @JsonProperty("min")
        @Column(name = "temp_min")
        private double min;

        @JsonProperty("max")
        @Column(name = "temp_max")
        private double max;

        @JsonProperty("night")
        @Column(name = "temp_night")
        private double night;

        @JsonProperty("eve")
        @Column(name = "temp_eve")
        private double eve;

        @JsonProperty("morn")
        @Column(name = "temp_morn")
        private double morn;
    }

    // 嵌套类
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Feels_like {
        @JsonProperty("day")
        @Column(name = "feel_day")
        private double day;

        @JsonProperty("night")
        @Column(name = "feel_night")
        private double night;

        @JsonProperty("eve")
        @Column(name = "feel_eve")
        private double eve;

        @JsonProperty("morn")
        @Column(name = "feel_morn")
        private double morn;
    }

    // 嵌套类
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Weather {
        @JsonProperty("id")
        @Column(name = "weather_id")
        private int id;

        @JsonProperty("main")
        @Column(name = "weather_main")
        private String main;

        @JsonProperty("description")
        @Column(name = "weather_description")
        private String description;

        @JsonProperty("icon")
        @Column(name = "weather_icon")
        private String icon;
    }
}