package com.tourwise.backend.model;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "itinerary_saved")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItinerarySaved {

    // 主键自增
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 说明一个行程属于一个用户
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  //数据库外键字段叫 user_id
    private User user;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    // 一个ItinerarySaved 包含多个行程项
    // 使用 @OneToMany 时，属性必须是集合类型（如 List<T> 或 Set<T>），因为它表达的是“一对多”，不能用单个对象。
    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItinerarySavedItems> items;
}