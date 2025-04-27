package com.shaluo.tourwise.controller;

import com.shaluo.tourwise.model.BusynessPrediction;
import com.shaluo.tourwise.repository.BusynessPredictionRepository;
import com.shaluo.tourwise.service.AttractionService;
import com.shaluo.tourwise.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/api/busyness")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private AttractionService attractionService;

    @Autowired
    private BusynessPredictionRepository busynessPredictionRepository;

    @PostMapping("/predict_by_attraction_id")
    public float predict(@RequestParam int attractionIndex, @RequestParam String dateTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
            int attractionZone = attractionService.getAttractionByIndex(attractionIndex).getTaxi_zone();
            return predictionService.getBusynessByZoneFromMemory(attractionZone, localDateTime);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid dateTime format. Please use ISO_LOCAL_DATE_TIME format.", e);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @PostMapping("/predict_by_taxi_zone")
    public float predictByTaxiZone(@RequestParam int taxiZone, @RequestParam String dateTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
            return predictionService.getBusynessByZoneFromMemory(taxiZone, localDateTime);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid dateTime format. Please use ISO_LOCAL_DATE_TIME format.", e);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @PostMapping("/predict_all_sort_by_date_range")
    public Map<String, Map<Integer, Float>> predictByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        Map<String, Map<Integer, Float>> result = new TreeMap<>();
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.atTime(23, 0);

            List<BusynessPrediction> predictions = busynessPredictionRepository.findByDatetimeBetween(startDateTime, endDateTime);

            for (BusynessPrediction p : predictions) {
                String timeKey = p.getDatetime().toString();
                int taxiZone = p.getTaxiZone();
                float busyness = p.getBusyness();

                result.computeIfAbsent(timeKey, k -> new TreeMap<>()).put(taxiZone, busyness);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/predict_all_sort_by_zone")
    public Map<Integer, Map<String, Map<String, Float>>> predictAllSortByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        Map<Integer, Map<String, Map<String, Float>>> result = new TreeMap<>();
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.atTime(23, 0);

            List<BusynessPrediction> predictions = busynessPredictionRepository.findByDatetimeBetween(startDateTime, endDateTime);

            for (BusynessPrediction p : predictions) {
                int taxiZone = p.getTaxiZone();
                String dateKey = p.getDatetime().toLocalDate().toString();
                String timeKey = p.getDatetime().toString();

                result
                        .computeIfAbsent(taxiZone, k -> new TreeMap<>())
                        .computeIfAbsent(dateKey, k -> new TreeMap<>())
                        .put(timeKey, p.getBusyness());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
