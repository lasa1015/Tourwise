package com.tourwise.backend.repository;

import com.tourwise.backend.model.DailyForecastData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
// 这个接口是专门负责 DailyForecastData 实体类的数据库操作
// 它的主键是 UUID
public interface DailyWeatherDataRepository extends JpaRepository<DailyForecastData, UUID> {

    // 在接口里加了 3 个查询方法，分别用了 JPQL（JPA 的查询语言）

    // 查出所有 dt 等于指定时间戳的天气数据
    // :dt 是一个占位符，意思是：把传进来的参数绑定到这个位置。
    @Query("SELECT f FROM DailyForecastData f WHERE f.dt = ?1")
    List<DailyForecastData> findForecastByDt(long dt);


    // 查最新（或最旧）的一部分天气记录，支持分页
    @Query("SELECT f FROM DailyForecastData f ORDER BY f.dt ASC")
    List<DailyForecastData> findLatestForecast(Pageable pageable);


    // 查出 dt 在某个范围内的所有天气数据，并按时间升序排序
    // ?1 → 代表方法的第一个参数
    // ?2 → 代表方法的第二个参数
    @Query("SELECT d FROM DailyForecastData d WHERE d.dt BETWEEN ?1 AND ?2 ORDER BY d.dt ASC")
    List<DailyForecastData> findForecastsByDateRange(long startDt, long endDt);
}