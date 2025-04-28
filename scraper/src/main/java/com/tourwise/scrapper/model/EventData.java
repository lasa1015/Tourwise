package com.tourwise.scrapper.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventData {

    @Version
    private Long version = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    private UUID id;

    @Column(name = "name", length = 512)
    private String name;

    @Embedded
    private Location location;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "category")
    private String category;

    @Column(name = "combined_category")
    private String combinedCategory;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "event_site_url", length = 1000)
    private String eventSiteUrl;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Column(name = "is_free")
    private Boolean is_free;

    @Column(name = "time_start")
    private String time_start;

    @Column(name = "time_end")
    private String time_end;

    @Column(name = "attending_count")
    private Integer attending_count;

    @Column(name = "interested_count")
    private Integer interested_count;

    @Column(name = "is_canceled")
    private Boolean is_canceled;

    @Column(name = "is_official")
    private Boolean is_official;

    @Column(name = "fetch_time")
    private LocalDateTime fetchTime;

    //  嵌套地址对象 Location
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        @JsonProperty("address1")
        @Column(name = "address")
        private String address;

        @JsonProperty("city")
        @Column(name = "city")
        private String city;

        @JsonProperty("state")
        @Column(name = "state")
        private String state;

        @JsonProperty("zip_code")
        @Column(name = "zip_code")
        private String zipCode;
    }

    // 在 实体插入或更新之前自动执行的逻辑。
    // 根据原始的 category 字段，生成一个更通用的 combinedCategory 分类
    // 原始的分类太细了，有几十种；把它们合并成几大类；
    @PrePersist  // 在执行 INSERT 语句之前调用这个方法
    @PreUpdate   //在执行 UPDATE 语句之前调用这个方法
    private void setCombinedCategory() {

        switch (this.category) {
            case "music":
                this.combinedCategory = "Music";
                break;
            case "visual-arts":
            case "performing-arts":
            case "film":
            case "fashion":
                this.combinedCategory = "Art & Fashion";
                break;
            case "lectures-books":
                this.combinedCategory = "Lecture & Books";
                break;
            case "food-and-drink":
            case "festivals-fairs":
                this.combinedCategory = "Food & Festival";
                break;
            case "sports-active-life":
                this.combinedCategory = "Sports & Active";
                break;
            case "kids-family":
                this.combinedCategory = "Kids & Family";
                break;
            case "other":
            case "nightlife":
            case "charities":
            default:
                this.combinedCategory = "Other";
                break;
        }
    }
}
