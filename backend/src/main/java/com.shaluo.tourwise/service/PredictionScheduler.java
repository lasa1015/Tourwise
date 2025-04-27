package com.shaluo.tourwise.service;

import com.shaluo.tourwise.model.BusynessPrediction;
import com.shaluo.tourwise.repository.BusynessPredictionRepository;
import lombok.Getter;
import ml.dmlc.xgboost4j.java.XGBoostError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class PredictionScheduler {

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private BusynessPredictionRepository busynessPredictionRepository;

    private static final Map<String, List<Integer>> HARDCODED_BUSINESS_VALUES = new HashMap<>();

    @Getter
    private static Map<Integer, Map<String, Map<String, Float>>> savedResult = new TreeMap<>();

    static {
        HARDCODED_BUSINESS_VALUES.put("MONDAY", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 63, 83, 92, 91, 80, 59, 34, 0, 0, 0, 0, 0, 0, 0));
        HARDCODED_BUSINESS_VALUES.put("TUESDAY", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 34, 58, 75, 82, 79, 69, 51, 30, 0, 0, 0, 0, 0, 0, 0));
        HARDCODED_BUSINESS_VALUES.put("WEDNESDAY", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 55, 70, 73, 65, 53, 37, 21, 0, 0, 0, 0, 0, 0, 0));
        HARDCODED_BUSINESS_VALUES.put("THURSDAY", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 31, 55, 72, 77, 73, 61, 43, 24, 0, 0, 0, 0, 0, 0, 0));
        HARDCODED_BUSINESS_VALUES.put("FRIDAY", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 36, 63, 83, 91, 88, 76, 57, 33, 0, 0, 0, 0, 0, 0, 0));
        HARDCODED_BUSINESS_VALUES.put("SATURDAY", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 66, 88, 100, 99, 91, 72, 45, 0, 0, 0, 0, 0, 0, 0));
        HARDCODED_BUSINESS_VALUES.put("SUNDAY", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 33, 58, 78, 89, 89, 82, 64, 39, 0, 0, 0, 0, 0, 0, 0));
    }

    @Scheduled(initialDelay = 1000, fixedRate = 3600000)
    public void calculateAndSaveBusyness() {
        System.out.println("✅ [Scheduler] Start running prediction scheduler...");

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(29);

        Map<Integer, Map<String, Map<String, Float>>> result = new TreeMap<>();

        try {
            List<Integer> taxiZones = readTaxiZonesFromCSV();

            System.out.println("✅ Loaded " + taxiZones.size() + " taxi zones from CSV.");

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

                String dateKey = date.toString();

                for (int taxiZone : taxiZones) {
                    Map<String, Map<String, Float>> dateMap = result.computeIfAbsent(taxiZone, k -> new TreeMap<>());
                    Map<String, Float> hourlyPredictions = dateMap.computeIfAbsent(dateKey, k -> new TreeMap<>());

                    for (int hour = 0; hour < 24; hour++) {
                        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(hour, 0));
                        String timeKey = dateTime.toString();

                        try {
                            float prediction = predictionService.predictByTaxiZone(taxiZone, dateTime);
                            hourlyPredictions.put(timeKey, prediction);

                            saveOrUpdatePrediction(taxiZone, dateTime, prediction);

                        } catch (IllegalArgumentException | XGBoostError e) {
                            hourlyPredictions.put(timeKey, -1.0f);
                            saveOrUpdatePrediction(taxiZone, dateTime, -1.0f);
                            System.err.println("❌ Prediction error for zone " + taxiZone + " at " + timeKey);
                        }
                    }
                    System.out.println("✅ TaxiZone " + taxiZone + " predictions saved to database.");
                }

                addHardcodedBusyness(result, date, dateKey);
            }

            this.savedResult = result;
            System.out.println("✅ Prediction results saved to memory and database successfully.");

        } catch (Exception e) {
            System.err.println("❌ Error occurred in calculateAndSaveBusyness(): " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addHardcodedBusyness(Map<Integer, Map<String, Map<String, Float>>> result, LocalDate date, String dateKey) {
        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toUpperCase();
        List<Integer> hourlyBusyness = HARDCODED_BUSINESS_VALUES.get(dayOfWeek);

        if (hourlyBusyness != null) {
            for (int zone : Arrays.asList(103, 104, 105)) {
                Map<String, Map<String, Float>> dateMap = result.computeIfAbsent(zone, k -> new TreeMap<>());
                Map<String, Float> hourlyPredictions = dateMap.computeIfAbsent(dateKey, k -> new TreeMap<>());

                for (int hour = 0; hour < hourlyBusyness.size(); hour++) {
                    LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(hour, 0));
                    String timeKey = dateTime.toString();
                    float value = hourlyBusyness.get(hour).floatValue();
                    hourlyPredictions.put(timeKey, value);

                    saveOrUpdatePrediction(zone, dateTime, value);
                }
            }
        }
    }

    private List<Integer> readTaxiZonesFromCSV() throws IOException {
        List<Integer> taxiZones = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("manhattan_taxi_zones_id.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                taxiZones.add(Integer.parseInt(line.trim()));
            }
        }
        return taxiZones;
    }

    private void saveOrUpdatePrediction(int taxiZone, LocalDateTime datetime, float busyness) {
        Optional<BusynessPrediction> existing = busynessPredictionRepository.findByTaxiZoneAndDatetime(taxiZone, datetime);
        if (existing.isPresent()) {
            BusynessPrediction prediction = existing.get();
            prediction.setBusyness(busyness);
            prediction.setUpdatedAt(LocalDateTime.now());
            busynessPredictionRepository.save(prediction);
        } else {
            BusynessPrediction prediction = new BusynessPrediction();
            prediction.setTaxiZone(taxiZone);
            prediction.setDatetime(datetime);
            prediction.setBusyness(busyness);
            busynessPredictionRepository.save(prediction);
        }
    }
}
