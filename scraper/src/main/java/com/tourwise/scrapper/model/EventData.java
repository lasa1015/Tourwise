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



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    private UUID id;

    @JsonProperty("name")
    @Column(name = "name", length = 512)
    private String name;

    @Embedded
    private Location location;

    @JsonProperty("latitude")
    @Column(name = "latitude")
    private Double latitude;

    @JsonProperty("longitude")
    @Column(name = "longitude")
    private Double longitude;

    @JsonProperty("category")
    @Column(name = "category")
    private String category;

    @Column(name = "combined_category")
    private String combinedCategory;

    @JsonProperty("description")
    @Column(name = "description", length = 2000)
    private String description;

    @JsonProperty("event_site_url")
    @Column(name = "event_site_url", length = 1000)
    private String eventSiteUrl;

    @JsonProperty("image_url")
    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @JsonProperty("is_free")
    @Column(name = "is_free")
    private Boolean is_free;

    @JsonProperty("time_start")
    @Column(name = "time_start")
    private String time_start;

    @JsonProperty("time_end")
    @Column(name = "time_end")
    private String time_end;

    @JsonProperty("attending_count")
    @Column(name = "attending_count")
    private Integer attending_count;

    @JsonProperty("interested_count")
    @Column(name = "interested_count")
    private Integer interested_count;

    @JsonProperty("is_canceled")
    @Column(name = "is_canceled")
    private Boolean is_canceled;

    @JsonProperty("is_official")
    @Column(name = "is_official")
    private Boolean is_official;

    @Column(name = "fetch_time")
    private LocalDateTime fetchTime;

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

    @PrePersist
    @PreUpdate
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
