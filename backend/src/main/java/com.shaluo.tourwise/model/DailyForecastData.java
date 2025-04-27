package com.shaluo.tourwise.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@Data
@Entity
@Table(name = "daily_forecast_data")
public class DailyForecastData {

    // UUID 是 Java 自带的标准类
    @Id
    private UUID id;

    private Timestamp fetch_time;
    private long dt;

    @Column(name = "temp_day")
    private double tempDay;

    private double rain;
    private double snow;
    private double speed;
    private double humidity;
    private double pressure;
    private String weather_description;
    private String weather_icon;
    private String weather_main;

    // @Transient 字段不会参与数据库映射，也不会被保存到表中
    @Transient
    private LocalDate date;

    // Convert dt to LocalDate and set it
    public void convertDtToDate() {
        this.date = Instant.ofEpochSecond(this.dt)
                .atZone(ZoneId.of("America/New_York"))
                .toLocalDate();
    }
}