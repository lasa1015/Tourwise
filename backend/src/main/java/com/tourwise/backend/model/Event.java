package com.tourwise.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "events")
public class Event {

    @Id
    private UUID id;

    // 给可能很长的字符串字段设定长度限制，避免数据库默认 VARCHAR(255) 截断
    @Column(name = "name", length = 512)
    private String name;

    private Double latitude;

    private Double longitude;

    private String category;

    private String combined_category;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "event_site_url", length = 1000)
    private String event_site_url;

    @Column(name = "image_url", length = 1000)
    private String image_url;

    private Boolean is_free;

    private String time_start;

    private String time_end;

    private Integer attending_count;

    private Integer interested_count;

    private Boolean is_canceled;

    private Boolean is_official;

    private LocalDateTime fetchTime;

    private String address;

    private String city;

    private String state;

    private String zip_code;

    public Event() {}

}
