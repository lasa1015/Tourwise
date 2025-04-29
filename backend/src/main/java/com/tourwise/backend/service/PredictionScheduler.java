package com.tourwise.backend.service;

import com.tourwise.backend.model.BusynessPrediction;
import com.tourwise.backend.repository.BusynessPredictionRepository;
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

    private static final int BATCH_SAVE_SIZE = 500;
    private static final int BATCH_DAYS = 5;
    private static final int SLEEP_TIME_MS = 60000; // 1 minute

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
        List<BusynessPrediction> buffer = new ArrayList<>();

        try {
            List<Integer> taxiZones = readTaxiZonesFromCSV();

            System.out.println("✅ Loaded " + taxiZones.size() + " taxi zones from CSV.");

            LocalDate current = startDate;
            while (!current.isAfter(endDate)) {
                LocalDate batchEnd = current.plusDays(BATCH_DAYS - 1);
                if (batchEnd.isAfter(endDate)) batchEnd = endDate;

                System.out.println("🚀 Processing batch: " + current + " to " + batchEnd);

                for (LocalDate date = current; !date.isAfter(batchEnd); date = date.plusDays(1)) {
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
                                buffer.add(buildPredictionEntity(taxiZone, dateTime, prediction));
                            } catch (IllegalArgumentException e) {
                                System.err.println("⚠️ Taxi zone not found: " + taxiZone + " at " + dateTime);
                                hourlyPredictions.put(timeKey, -1.0f);
                                buffer.add(buildPredictionEntity(taxiZone, dateTime, -1.0f));
                            } catch (XGBoostError e) {
                                System.err.println("❌ Prediction error for zone " + taxiZone + " at " + timeKey);
                                e.printStackTrace();
                                hourlyPredictions.put(timeKey, -1.0f);
                                buffer.add(buildPredictionEntity(taxiZone, dateTime, -1.0f));
                            }

                            if (buffer.size() >= BATCH_SAVE_SIZE) {
                                flushBatch(buffer);
                            }
                        }
                    }
                    addHardcodedBusyness(result, date, dateKey, buffer);
                }

                flushBatch(buffer);

                if (!batchEnd.isEqual(endDate)) {
                    System.out.println("⏸️ Sleeping for 2 minutes before next batch...");
                    Thread.sleep(SLEEP_TIME_MS);
                }

                current = batchEnd.plusDays(1);
            }

            this.savedResult = result;
            System.out.println("✅ Prediction results saved to memory and database successfully.");

        } catch (Exception e) {
            System.err.println("❌ Error occurred in calculateAndSaveBusyness(): " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addHardcodedBusyness(Map<Integer, Map<String, Map<String, Float>>> result, LocalDate date, String dateKey, List<BusynessPrediction> buffer) {
        String dayOfWeek = date.getDayOfWeek().toString();
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
                    buffer.add(buildPredictionEntity(zone, dateTime, value));

                    if (buffer.size() >= BATCH_SAVE_SIZE) {
                        flushBatch(buffer);
                    }
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

    private BusynessPrediction buildPredictionEntity(int taxiZone, LocalDateTime datetime, float busyness) {
        BusynessPrediction prediction = new BusynessPrediction();
        prediction.setTaxiZone(taxiZone);
        prediction.setDatetime(datetime);
        prediction.setBusyness(busyness);
        prediction.setUpdatedAt(LocalDateTime.now());
        return prediction;
    }

    private void flushBatch(List<BusynessPrediction> buffer) {
        for (BusynessPrediction prediction : buffer) {
            Optional<BusynessPrediction> existing = busynessPredictionRepository.findByTaxiZoneAndDatetime(
                    prediction.getTaxiZone(), prediction.getDatetime()
            );

            if (existing.isPresent()) {
                BusynessPrediction existingRecord = existing.get();
                existingRecord.setBusyness(prediction.getBusyness());
                existingRecord.setUpdatedAt(LocalDateTime.now());
                busynessPredictionRepository.save(existingRecord);
            } else {
                busynessPredictionRepository.save(prediction);
            }
        }
        buffer.clear();
    }
}