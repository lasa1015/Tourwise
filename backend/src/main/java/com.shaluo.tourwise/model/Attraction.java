package com.shaluo.tourwise.model;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.List;

@Data
public class Attraction {

    private int taxi_zone;
    private String zone_name;
    private String attraction_place_id;
    private int index;
    private String attraction_name;
    private String category;
    private double price;
    private boolean isFree;
    private String description;
    private double attraction_latitude;
    private double attraction_longitude;
    private String attraction_vicinity;
    private double attraction_rating;
    private int user_ratings_total;
    private String attraction_phone_number;
    private String attractionWebsite;
    private String opening_hours;
    private int price_level;
    private List<String> types;
    private String international_phone_number;
    private String url;
    private String icon;
    private String formatted_hours;
    private String popular_times;
    private List<String> time_spent;
    private double current_popularity;

}