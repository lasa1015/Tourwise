package com.tourwise.backend.service;

import com.tourwise.backend.model.Attraction;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// 从 attractions.csv 文件中读取所有景点数据，然后根据用户筛选条件进行过滤、排序，并返回结果。

// 【CSV 没有数据类型和格式限制, 才导致本类里有大量代码。 如果将景点存为数据库的表，就会少很多代码！】
// 使用数据库代替 CSV 可以让数据拥有类型约束、支持增删改查、提高查询效率、减少内存占用，并能更好地支持多用户协作和系统扩展。

@Service
public class AttractionService {

    // 景点列表，位于内存中
    private List<Attraction> attractions = new ArrayList<>();

    // 构造函数
    // 调用 loadAttractions()，程序一启动就把 CSV 里面的所有数据读进内存。
    public AttractionService() {
        loadAttractions();
    }

    // 加载 CSV 文件的方法
    private void loadAttractions() {
        try {
            // ClassPathResource to load file
            Resource resource = new ClassPathResource("attractions.csv");
            InputStream inputStream = resource.getInputStream();
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));

            // 跳过第一行标题
            reader.readNext();

            String[] line;
            int lineNumber = 1;

            // 逐行读取，每一行都创建一个 Attraction 对象
            while ((line = reader.readNext()) != null) {
                lineNumber++;
                try {

                    Attraction attraction = new Attraction();

                    attraction.setTaxi_zone(parseIntFromDouble(line[0], lineNumber, "taxiZone"));
                    attraction.setZone_name(parseString(line[1], lineNumber, "zone_name"));
                    attraction.setAttraction_place_id(parseString(line[2], lineNumber, "attraction_place_id"));
                    attraction.setIndex(parseIntFromDouble(line[3], lineNumber, "index"));
                    attraction.setAttraction_name(parseString(line[4], lineNumber, "attraction_name"));
                    attraction.setCategory(parseString(line[5], lineNumber, "category"));
                    attraction.setPrice(parseDouble(line[6], lineNumber, "price"));
                    attraction.setFree(parseBoolean(line[7], lineNumber, "isFree"));
                    attraction.setDescription(parseString(line[8], lineNumber, "description"));
                    attraction.setAttraction_latitude(parseDouble(line[9], lineNumber, "attraction_latitude"));
                    attraction.setAttraction_longitude(parseDouble(line[10], lineNumber, "attraction_longitude"));
                    attraction.setAttraction_vicinity(parseString(line[11], lineNumber, "attraction_vicinity"));
                    attraction.setAttraction_rating(parseDouble(line[12], lineNumber, "attraction_rating"));
                    attraction.setUser_ratings_total(parseIntFromDouble(line[13], lineNumber, "user_ratings_total"));
                    attraction.setAttraction_phone_number(parseString(line[14], lineNumber, "attraction_phone_number"));
                    attraction.setAttractionWebsite(parseString(line[15], lineNumber, "attraction_website"));
                    attraction.setOpening_hours(parseString(line[16], lineNumber, "opening_hours"));
                    attraction.setPrice_level(parseIntFromDouble(line[17], lineNumber, "price_level"));
                    attraction.setTypes(parseList(line[18], lineNumber, "types"));
                    attraction.setInternational_phone_number(parseString(line[19], lineNumber, "international_phone_number"));
                    attraction.setUrl(parseString(line[20], lineNumber, "url"));
                    attraction.setIcon(parseString(line[21], lineNumber, "icon"));
                    attraction.setFormatted_hours(parseString(line[22], lineNumber, "formatted_hours"));
                    attraction.setPopular_times(parseString(line[23], lineNumber, "popular_times"));
                    attraction.setTime_spent(parseList(line[24], lineNumber, "time_spent"));

                    // 把所有景点存入内存的 List<Attraction>
                    // 后面要用时，直接从这个 attractions 列表中查、筛、排
                    attractions.add(attraction);
                } catch (Exception e) {
                    System.err.println("Error processing line " + lineNumber + ": " + e.getMessage());
                }
            }
            reader.close();
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===========代码里的这些 parseXxx() 方法，全部都是专门用来处理 CSV 文件中读出来的字符串数据的========
    // 因为从 CSV 文件里读取的数据全是字符串
    // 如果改用数据库来存储和读取这些景点数据，所有这些 parseXxx() 方法都可以删掉

    private int parseInt(String value, int lineNumber, String columnName) {
        try {
            return value.isEmpty() ? 0 : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing int from value '" + value + "' in column '" + columnName + "' at line " + lineNumber);
            return 0;
        }
    }

    private int parseIntFromDouble(String value, int lineNumber, String columnName) {
        try {
            return value.isEmpty() ? 0 : (int) Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing int from double value '" + value + "' in column '" + columnName + "' at line " + lineNumber);
            return 0;
        }
    }

    private double parseDouble(String value, int lineNumber, String columnName) {
        try {
            value = value.trim(); // 去掉空格
            return value.isEmpty() ? 0.0 : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing double from value '" + value + "' in column '" + columnName + "' at line " + lineNumber);
            return 0.0;
        }
    }


    private String parseString(String value, int lineNumber, String columnName) {
        return value.isEmpty() ? "" : value;
    }

    private List<String> parseList(String value, int lineNumber, String columnName) {
        if (value.isEmpty()) {
            return new ArrayList<>();
        }
        return List.of(value.replace("[", "").replace("]", "").split(", "));
    }

    private boolean parseBoolean(String value, int lineNumber, String columnName) {
        return value.isEmpty() ? false : Boolean.parseBoolean(value);
    }


    // ===========使用数据库的话，以下方法可以直接用 Repository + JPQL 替代掉================
    // ============不需要再手写 Java 的 stream 和 comparator==================


    // 返回所有景点数据
    public List<Attraction> getAttractions() {
        return attractions;
    }


    // 从内存中找到“index 等于某个值”的景点。
    public Attraction getAttractionByIndex(int index) {

        return attractions.stream()
                .filter(attraction -> attraction.getIndex() == index)
                .findFirst()
                .orElse(null);
    }


    // 不带时间范围的筛选 + 排序
    public List<Attraction> filterAndSortAttractions(String name, Boolean isFree, List<String> categoryList, String sortBy, String order) {

        List<String> keywords = name != null ? List.of(name.toLowerCase().split("\\s+")) : List.of();

        return attractions.stream()
                .filter(attraction -> (name == null || keywords.stream()
                        .allMatch(keyword -> attraction.getAttraction_name().toLowerCase().contains(keyword))) &&
                        (isFree == null || attraction.isFree() == isFree) &&
                        (categoryList == null || categoryList.isEmpty() || categoryList.stream()
                                .anyMatch(category -> category.equalsIgnoreCase(attraction.getCategory()))))
                .sorted(getComparator(sortBy, order))
                .collect(Collectors.toList());
    }

    // 带时间范围的筛选 + 排序
    // 多了一步判断：这个景点在某些日期是否开放
    public List<Attraction> filterAndSortAttractionsWithDate(String name, Boolean isFree, List<String> categoryList, String sortBy, String order, LocalDate startDate, LocalDate endDate) {
        List<Integer> daysOfWeek = startDate.datesUntil(endDate.plusDays(1))
                .map(LocalDate::getDayOfWeek)
                .map(day -> day.getValue() - 1)
                .collect(Collectors.toList());

        List<String> keywords = name != null ? List.of(name.toLowerCase().split("\\s+")) : List.of();

        return attractions.stream()
                .filter(attraction -> (name == null || keywords.stream()
                        .allMatch(keyword -> attraction.getAttraction_name().toLowerCase().contains(keyword))) &&
                        (isOpenOnDays(attraction.getFormatted_hours(), daysOfWeek)) &&
                        (isFree == null || attraction.isFree() == isFree) &&
                        (categoryList == null || categoryList.isEmpty() || categoryList.stream()
                                .anyMatch(category -> category.equalsIgnoreCase(attraction.getCategory()))))
                .sorted(getComparator(sortBy, order))
                .collect(Collectors.toList());
    }

    // 这个景点在某些日期是否开放
    private boolean isOpenOnDays(String formattedHours, List<Integer> daysOfWeek) {
        if (formattedHours == null || formattedHours.trim().isEmpty()) return false;
    
        for (String entry : formattedHours.split(",")) {
            String[] parts = entry.trim().split(":");
            if (parts.length == 0 || parts[0].trim().isEmpty()) continue;
    
            try {
                int day = Integer.parseInt(parts[0].trim());
                if (daysOfWeek.contains(day)) {
                    return true;
                }
            } catch (NumberFormatException e) {
                System.err.println("⚠ 无法解析日期部分: " + parts[0]);
            }
        }
        return false;
    }
    
    // 动态返回一个 Comparator，用来给 Java Stream 排序
    private Comparator<Attraction> getComparator(String sortBy, String order) {
        Comparator<Attraction> comparator;

        if ("price".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparingDouble(Attraction::getPrice);
            if ("desc".equalsIgnoreCase(order)) {
                comparator = comparator.reversed();
            }
        } else if ("user_ratings_total".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparingDouble(Attraction::getUser_ratings_total).reversed();
            if ("asc".equalsIgnoreCase(order)) {
                comparator = Comparator.comparingDouble(Attraction::getUser_ratings_total);
            }
        } else {
            comparator = Comparator.comparingDouble(Attraction::getAttraction_rating).reversed();
            if ("asc".equalsIgnoreCase(order)) {
                comparator = Comparator.comparingDouble(Attraction::getAttraction_rating);
            }
        }

        return comparator;
    }
}