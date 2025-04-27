package com.shaluo.tourwise.service;

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


// 【目前预测数据只存在 Java 内存中，关掉后端后全丢，用户和开发者都无法直接访问这些数据】

// 定时任务类，它每小时自动运行一次，做的事情是：
// 对未来 30 天内，所有出租车区域（Taxi Zone）的每个小时的拥挤程度（busyness）进行预测，并把结果保存在内存中。
@Service
public class PredictionScheduler {

    @Autowired
    private PredictionService predictionService;

    // 写死的每周每天的“拥挤度”数据（busyness），用于处理一些模型预测不到的区域（103,104,105）
    private static final Map<String, List<Integer>> HARDCODED_BUSINESS_VALUES = new HashMap<>();

    // 保存所有预测结果
    @Getter
    private static Map<Integer, Map<String, Map<String, Float>>> savedResult;

    // 静态代码块（static block）+ 静态变量初始化
    // 当类 PredictionScheduler 第一次被加载的时候，JVM 会自动执行这个 static { ... } 代码块，用来初始化静态变量 HARDCODED_BUSINESS_VALUES
    static {
        HARDCODED_BUSINESS_VALUES.put("MONDAY", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 63, 83, 92, 91, 80, 59, 34, 0, 0, 0, 0, 0, 0, 0));
        HARDCODED_BUSINESS_VALUES.put("TUESDAY", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 34, 58, 75, 82, 79, 69, 51, 30, 0, 0, 0, 0, 0, 0, 0));
        HARDCODED_BUSINESS_VALUES.put("WEDNESDAY", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 55, 70, 73, 65, 53, 37, 21, 0, 0, 0, 0, 0, 0, 0));
        HARDCODED_BUSINESS_VALUES.put("THURSDAY", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 31, 55, 72, 77, 73, 61, 43, 24, 0, 0, 0, 0, 0, 0, 0));
        HARDCODED_BUSINESS_VALUES.put("FRIDAY", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 36, 63, 83, 91, 88, 76, 57, 33, 0, 0, 0, 0, 0, 0, 0));
        HARDCODED_BUSINESS_VALUES.put("SATURDAY", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 66, 88, 100, 99, 91, 72, 45, 0, 0, 0, 0, 0, 0, 0));
        HARDCODED_BUSINESS_VALUES.put("SUNDAY", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 33, 58, 78, 89, 89, 82, 64, 39, 0, 0, 0, 0, 0, 0, 0));
    }

    // 定时任务，每小时自动执行一次
    // 运行一个“未来 30 天、24 小时、每个出租车区域的拥挤程度预测任务”，将预测结果保存在一个内存变量 savedResult
    @Scheduled(initialDelay = 1000, fixedRate = 3600000)
    public void calculateAndSaveBusyness() {

        System.out.println("✅ [Scheduler] Start running prediction scheduler...");

        // 定义预测的时间范围：从今天开始, 未来共 30 天（含今天）
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(29);

        // 多层嵌套的 Map 结构用于保存结果
        Map<Integer, Map<String, Map<String, Float>>> result = new TreeMap<>();

        try {
            // 读取 CSV
            List<Integer> taxiZones = readTaxiZonesFromCSV();

            System.out.println("✅ Loaded " + taxiZones.size() + " taxi zones from CSV.");

            System.out.println("✅ Prediction is running: " + startDate + " to " + endDate);

            // 遍历这 30 天，每天生成预测
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

                String dateKey = date.toString();


                for (int taxiZone : taxiZones) {

                    Map<String, Map<String, Float>> dateMap = result.computeIfAbsent(taxiZone, k -> new TreeMap<>());
                    Map<String, Float> hourlyPredictions = dateMap.computeIfAbsent(dateKey, k -> new TreeMap<>());

                    for (int hour = 0; hour < 24; hour++) {
                        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(hour, 0));
                        String timeKey = dateTime.toString();

                        try {

                            // 调用机器学习模型做预测，并把预测值放进结果 map
                            float prediction = predictionService.predictByTaxiZone(taxiZone, dateTime);
                            hourlyPredictions.put(timeKey, prediction);

                        } catch (IllegalArgumentException e) {

                            // 如果输入有误，填 -1 作为预测失败的标志
                            hourlyPredictions.put(timeKey, -1.0f);
//                            System.err.println("⚠️  IllegalArgumentException for zone " + taxiZone + " at " + timeKey);
                        } catch (XGBoostError e) {
                            hourlyPredictions.put(timeKey, -1.0f);
                            System.err.println("❌ XGBoostError for zone " + taxiZone + " at " + timeKey + ": " + e.getMessage());
                        }
                    }
                }

                // 硬编码的区域插入
                addHardcodedBusyness(result, date, dateKey);
            }

            this.savedResult = result;

            System.out.println("✅ Prediction results saved to memory successfully.");

        } catch (Exception e) {
            System.err.println("❌ Error occurred in calculateAndSaveBusyness(): " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 为指定的几个出租车区域（103, 104, 105），根据星期几，插入“硬编码”的24小时拥挤度数据到内存中。
    private void addHardcodedBusyness(Map<Integer, Map<String, Map<String, Float>>> result, LocalDate date, String dateKey) {
        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toUpperCase();
        List<Integer> hourlyBusyness = HARDCODED_BUSINESS_VALUES.get(dayOfWeek);

        if (hourlyBusyness != null) {

            // 为 103, 104, 105 三个 zone 插入数据 (模型没训练这些区域，所以用人工值补)
            for (int zone : Arrays.asList(103, 104, 105)) {
                Map<String, Map<String, Float>> dateMap = result.computeIfAbsent(zone, k -> new TreeMap<>());
                Map<String, Float> hourlyPredictions = dateMap.computeIfAbsent(dateKey, k -> new TreeMap<>());

                for (int hour = 0; hour < hourlyBusyness.size(); hour++) {
                    LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(hour, 0));
                    String timeKey = dateTime.toString();
                    hourlyPredictions.put(timeKey, hourlyBusyness.get(hour).floatValue());
                }
            }
        }
    }

    // 从 manhattan_taxi_zones_id.csv 文件中读取出租车区域编号，一行一个，解析成 List<Integer> 返回
    private List<Integer> readTaxiZonesFromCSV() throws IOException {

        List<Integer> taxiZones = new ArrayList<>();

        // ClassPathResource 表示这个 CSV 文件放在 resources/ 目录下
        ClassPathResource resource = new ClassPathResource("manhattan_taxi_zones_id.csv");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                taxiZones.add(Integer.parseInt(line.trim()));
            }
        }
        return taxiZones;
    }
}
