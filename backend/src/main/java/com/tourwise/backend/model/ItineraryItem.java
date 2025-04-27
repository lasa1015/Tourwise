package com.tourwise.backend.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

// 这个类并不是要单独存在数据库的，而是拼装出来临时用的（典型的 DTO 或 View Model）
// 用来把不同来源的数据（比如景点、活动）拼成统一格式返回给前端

@Data
public class ItineraryItem {

    private Object id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double latitude;
    private double longitude;
    private boolean isEvent;
    private double busyness;
    private boolean isFree;
    private String category;
    private String address;
    private String website;
    private String description;
    private double rating;
    private int user_ratings_total;
    private String attraction_phone_number;
    private String international_phone_number;
    private String event_image;

    public ItineraryItem() {}

    // 处理 活动（Event） 时调用的构造器
    public ItineraryItem(UUID id, String name, LocalDateTime startTime, LocalDateTime endTime, double latitude, double longitude, boolean isEvent, boolean isFree, String category, String address, String website, String description, String event_image) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isEvent = isEvent;
        this.isFree = isFree;
        this.category = category;
        this.address = address;
        this.website = website;
        this.description = description;
        this.event_image = event_image;
    }

    // 处理 景点（Attraction） 时调用的构造器
    public ItineraryItem(int id, String name, LocalDateTime startTime, LocalDateTime endTime, double latitude, double longitude, boolean isEvent, boolean isFree, String category, String address, String website, String description, double rating, int userRatings_total, String attraction_phone_number, String international_phone_number) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isEvent = isEvent;
        this.isFree = isFree;
        this.category = category;
        this.address = address;
        this.website = website;
        this.description = description;
        this.rating = rating;
        this.user_ratings_total = userRatings_total;
        this.attraction_phone_number = attraction_phone_number;
        this.international_phone_number = international_phone_number;
    }
}