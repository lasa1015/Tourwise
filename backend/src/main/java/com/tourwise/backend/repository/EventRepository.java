package com.tourwise.backend.repository;

import com.tourwise.backend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


// 查询的语句本身（SQL/JPQL）写在 Repository 层，即使是复杂的JPQL
// 查询的使用逻辑、流程控制才写在 Service 层： 判断逻辑、是否调用哪个查询方法、如何处理返回结果。

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    // JPA 本身就有，列出所有活动，不加筛选
    List<Event> findAll();

    //  按“模糊名称 + 是否免费 + 分类 + 时间范围”组合筛选活动，并按开始时间升序排序。
    @Query("SELECT e FROM Event e WHERE " +
            "(:name IS NULL OR CAST(e.name AS string) ILIKE '%' || CAST(:name AS string) || '%') AND " +
            "(:isFree IS NULL OR e.is_free = :isFree) AND " +
            "(:combined_categories IS NULL OR e.combined_category IN :combined_categories) AND " +
            "((e.time_start >= :startDate AND e.time_start <= :endDate) OR " +
            "(e.time_start < :startDate AND e.time_end >= :startDate) OR " +
            "(e.time_start <= :endDate AND e.time_end > :endDate)) " +
            "ORDER BY e.time_start ASC")
    List<Event> findFilteredEventsWithinDateRange(@Param("startDate") String startDate,
                                                  @Param("endDate") String endDate,
                                                  @Param("isFree") Boolean isFree,
                                                  @Param("combined_categories") List<String> combined_categories,
                                                  @Param("name") String name);


    // 当用户只选了“是否免费”、“分类”、“活动名称”，但没有选时间范围，就会走这个查询
    // 根据活动名称（模糊匹配）、是否免费、活动分类进行组合筛选，并按开始时间升序排序（不包含时间范围限制）
    @Query("SELECT e FROM Event e WHERE " +
            "(:name IS NULL OR CAST(e.name AS string) ILIKE '%' || CAST(:name AS string) || '%') AND " +
            "(:isFree IS NULL OR e.is_free = :isFree) AND " +
            "(:combined_categories IS NULL OR e.combined_category IN :combined_categories) " +
            "ORDER BY e.time_start ASC")
    List<Event> findFilteredEventsWithoutDateRange(@Param("isFree") Boolean isFree,
                                                   @Param("combined_categories") List<String> combined_categories,
                                                   @Param("name") String name);


    // 按 ID 查找单个活动, 点开一个活动详情页面时用到的接口
    Optional<Event> findById(UUID id);
}