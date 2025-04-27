package com.shaluo.tourwise.service;

import com.shaluo.tourwise.repository.EventRepository;
import com.shaluo.tourwise.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


// 从数据库查询活动数据 + 加载 GeoJSON 判断坐标在哪个出租车区域（Taxi Zone）里

// 【当前的 EventService 虽然功能上可运行，但职责过多、嵌套类杂乱，应拆分 GeoJSON 相关逻辑成独立类，让服务职责单一、结构清晰。】

@Service
public class EventService {

    // 加载 GeoJSON 的数据结构变量
    private List<GeoFeature> geoFeatures;

    @Autowired
    private EventRepository eventRepository;

    // 构造方法 + 初始化 GeoJSON
    public EventService() {
        loadGeoFeatures();
    }


    public List<Event> getFilteredEventsAfterDate(String startDateTime, Boolean isFree, List<String> combinedCategories, String name) {
        return eventRepository.findEventsAfterDate(startDateTime, isFree, combinedCategories, name);
    }


    // 调用 eventRepository.findFilteredEventsWithinDateRange(...) 来查数据库中满足如下条件的活动：
    // 名字模糊匹配, 是否免费, 分类是否属于给定列表, 起始时间和结束时间范围重叠
    public List<Event> getFilteredEventsWithinDateRange(
            String startDate, String endDate, Boolean isFree, List<String> combined_categories, String name) {

        return eventRepository.findFilteredEventsWithinDateRange(startDate, endDate, isFree, combined_categories, name);
    }

    // 跟上面一样的查找方法，但不筛选时间范围（更宽松）
    public List<Event> getFilteredEventsWithoutDateRange(Boolean isFree, List<String> combined_categories, String name) {
        return eventRepository.findFilteredEventsWithoutDateRange(isFree, combined_categories, name);
    }

    // 根据 ID 查找活动
    public Event getEventById(UUID id) {
        Optional<Event> event = eventRepository.findById(id);
        return event.orElse(null);
    }

    //  加载 GeoJSON 文件内容
    private void loadGeoFeatures() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = new ClassPathResource("manhattan_taxi_zones.geojson").getInputStream();
            GeoFeatureCollection featureCollection = objectMapper.readValue(inputStream, GeoFeatureCollection.class);
            geoFeatures = featureCollection.getFeatures();
        } catch (IOException e) {
            e.printStackTrace();
            geoFeatures = new ArrayList<>();
        }
    }

    // 传入一个纬度经度, 判断一个属于哪个出租车区域
    public int getTaxiZoneIdByPosition(double latitude, double longitude) {

        for (GeoFeature feature : geoFeatures) {
            if (feature.contains(latitude, longitude)) {
                return Integer.parseInt(feature.getProperties().getLocationId());
            }
        }
        return -1;
    }

    //========== 以下是四个嵌套类=============

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GeoFeatureCollection {
        private List<GeoFeature> features;

        public List<GeoFeature> getFeatures() {
            return features;
        }

        public void setFeatures(List<GeoFeature> features) {
            this.features = features;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GeoFeature {
        private Geometry geometry;
        private Properties properties;

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }

        public boolean contains(double latitude, double longitude) {
            for (List<List<Double[]>> polygon : geometry.getCoordinates()) {
                for (List<Double[]> ring : polygon) {
                    if (isPointInPolygon(ring, latitude, longitude)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean isPointInPolygon(List<Double[]> polygon, double latitude, double longitude) {
            boolean result = false;
            int j = polygon.size() - 1;
            for (int i = 0; i < polygon.size(); i++) {
                if (polygon.get(i)[1] > latitude != polygon.get(j)[1] > latitude &&
                        longitude < (polygon.get(j)[0] - polygon.get(i)[0]) * (latitude - polygon.get(i)[1]) / (polygon.get(j)[1] - polygon.get(i)[1]) + polygon.get(i)[0]) {
                    result = !result;
                }
                j = i;
            }
            return result;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class Geometry {
            private String type;
            private List<List<List<Double[]>>> coordinates;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<List<List<Double[]>>> getCoordinates() {
                return coordinates;
            }

            public void setCoordinates(List<List<List<Double[]>>> coordinates) {
                this.coordinates = coordinates;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class Properties {
            @JsonProperty("location_id")
            private String locationId;

            public String getLocationId() {
                return locationId;
            }

            public void setLocationId(String locationId) {
                this.locationId = locationId;
            }
        }
    }

}
