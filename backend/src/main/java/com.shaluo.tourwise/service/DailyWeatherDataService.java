package com.shaluo.tourwise.service;

import com.shaluo.tourwise.repository.DailyWeatherDataRepository;
import com.shaluo.tourwise.model.DailyForecastData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// 从数据库中查出未来的天气数据，并做一些单位转换和排序，最后以 Java 对象形式返回给前端。
@Service
public class DailyWeatherDataService {

    @Autowired
    private DailyWeatherDataRepository repository;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    // 从数据库中查询最新的 30 条天气预报数据
    public List<DailyForecastData> getLatestForecast() {
        return repository.findLatestForecast(PageRequest.of(0, 30));
    }

    // 根据前端传来的某一天的日期，查出该日的天气预报数据，做单位转换后返回。
    public List<DailyForecastData> getForecastByDate(LocalDate date) {

        // 把用户传进来的 LocalDate（比如 2025-04-18）变成 纽约时区这一天的中午 12 点
        ZonedDateTime startOfDayET = date.atStartOfDay(ZoneId.of("America/New_York")).plusHours(12);

        // 把刚才的纽约时间转成Unix 时间戳（单位是秒）
        long targetDt = startOfDayET.toEpochSecond();

        // 调用 JPA Repository 查询数据库中所有 dt == targetDt 的记录
        // 然后对每条记录做单位转换（调用 convertUnits 方法）
        // 最后收集为一个 List 返回给前端
        return repository.findForecastByDt(targetDt).stream()
                .map(this::convertUnits)
                .collect(Collectors.toList());
    }

    // 根据传入的起始日期和结束日期，从数据库中查出这段时间内的天气预报记录，统一做单位转换并按时间排序返回。
    public List<DailyForecastData> getForecastByDateRange(LocalDate startDate, LocalDate endDate) {

        // 把 LocalDate（没有时区）转换成 纽约时区的中午 12 点 ZonedDateTime
        ZonedDateTime startOfDayET = startDate.atStartOfDay(ZoneId.of("America/New_York")).plusHours(12);
        ZonedDateTime endOfDayET = endDate.atStartOfDay(ZoneId.of("America/New_York")).plusHours(12);

        // 将 ZonedDateTime 转换成 Unix 时间戳（秒）
        long startEpochSecond = startOfDayET.toEpochSecond();
        long endEpochSecond = endOfDayET.toEpochSecond();

        // 调用 Repository 接口查数据
        List<DailyForecastData> forecasts = repository.findForecastsByDateRange(startEpochSecond, endEpochSecond);

        // 对每条数据做单位转换（温度、风速、雪量等）、排序再返回为一个集合
        return forecasts.stream()
                .map(this::convertUnits)
                .sorted(Comparator.comparing(DailyForecastData::getDt))
                .collect(Collectors.toList());
    }

    // 将原始天气数据中的单位做转换（K → ℃，mm → cm，m/s → km/h），
    // 并格式化成小数点后四位，同时计算出 LocalDate 日期。
    private DailyForecastData convertUnits(DailyForecastData data) {

        // 温度转换：Kelvin → Celsius
        data.setTempDay(formatToFourDecimalPlaces(data.getTempDay() - 273.15));

        //  雪量转换：毫米 → 厘米
        data.setSnow(formatToFourDecimalPlaces(data.getSnow() / 10));

        // 风速转换：米/秒 → 公里/小时
        data.setSpeed(formatToFourDecimalPlaces(data.getSpeed() * 3.6));

        // 时间戳转换为日期
        data.convertDtToDate();

        return data;
    }

    //  将一个 double 类型的小数保留 4 位小数，返回处理后的结果
    private double formatToFourDecimalPlaces(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
}