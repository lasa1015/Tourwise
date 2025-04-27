package com.tourwise.scrapper.service;

import com.tourwise.scrapper.model.EventData;
import com.tourwise.scrapper.repository.EventRepository;
import com.tourwise.scrapper.scrapers.EventScraper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// 曼哈顿多边形可以移到配置文件中，避免硬编码；

// 定期调用 EventScraper（你自己写的爬虫类）从 Yelp API 获取活动数据；
// 进行数据过滤、转换与整理；去重；存入数据库表 events

@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventScraper eventScraper;

    @Autowired
    private EventRepository eventRepository;

    @Value("${yelp.limit}")
    private int limit;

    // 区域过滤的坐标数组
    // 这是曼哈顿地区的多边形坐标，用来判断一个活动是否在曼哈顿区域内
    private static final double[][] ManhattanArea = {
            {-74.4900261866312, 40.68686056618563},
            {-74.02247557630322, 40.68060952571861},
            {-74.00673931790989, 40.69061065576909},
            {-74.00158758353581, 40.70353031583642},
            {-73.9742063962419, 40.709181659436126},
            {-73.96306764293499, 40.751602379032676},
            {-73.9381111465915, 40.77824950423954},
            {-73.93571826054003, 40.781756164126875},
            {-73.93835978383461, 40.78482868075099},
            {-73.93197657063641, 40.791936029479984},
            {-73.92872339624913, 40.79449340657828},
            {-73.92810629188814, 40.80216517440272},
            {-73.93381326053665, 40.80916985685798},
            {-73.9329464094588, 40.81800623927862},
            {-73.93445994426258, 40.834713196012274},
            {-73.92834261566213, 40.84628029333956},
            {-73.92006444104246, 40.85808855226034},
            {-73.91165332965423, 40.86651028933875},
            {-73.9095237173859, 40.87232949008461},
            {-73.91295888114882, 40.87465683738827},
            {-73.91793162880116, 40.87546532210828},
            {-73.92124698194723, 40.8763637510383},
            {-73.92318565996821, 40.87815766160858},
            {-73.9291707921706, 40.8787628614993},
            {-73.95417515817823, 40.85108001891453},
            {-74.01551813710326, 40.75627158256748},
            {-74.03478207315514, 40.70272068669027},
            {-74.04416204237901, 40.70052281665727},
            {-74.04903271144373, 40.68685333783202}
    };

    // 判断一个点是否在多边形中（用于地理过滤）
    private boolean isPointInPolygon(double latitude, double longitude, double[][] polygon) {
        int intersectCount = 0;
        for (int i = 0; i < polygon.length - 1; i++) {
            double[] p1 = polygon[i];
            double[] p2 = polygon[i + 1];
            if ((p1[1] > latitude) != (p2[1] > latitude)) {
                double x = (latitude - p1[1]) * (p2[0] - p1[0]) / (p2[1] - p1[1]) + p1[0];
                if (x > longitude) {
                    intersectCount++;
                }
            }
        }
        return (intersectCount % 2) == 1;
    }

    // 定时任务,每 3 小时自动执行一次
    @Scheduled(fixedRate = 10800000, initialDelay = 10800000)
    @Transactional  // @Transactional 确保批量操作时要么全部成功，要么回滚；
    public void fetchAndSaveEvents() {

        logger.info("🗓️ [EventService] Starting event update process...");

        int retryCount = 0;
        boolean success = false;

        while (retryCount < 3 && !success) {
            try {
                logger.info("📅 [EventService] Clearing existing events from database...");

                // 清空数据库
                eventRepository.deleteAllInBatch();

                ZoneId newYorkZone = ZoneId.of("America/New_York");
                LocalDateTime nowInNewYork = LocalDateTime.now(newYorkZone);

                // 构造请求时间范围（当前时间到未来 30 天）
                long startDate = nowInNewYork.toEpochSecond(ZoneOffset.UTC);
                long endDate = nowInNewYork.plusDays(30).toEpochSecond(ZoneOffset.UTC);
                logger.info("📅 [EventService] Fetching events from {} to {}", startDate, endDate);

                // 调用爬虫抓取数据
                List<EventData> events = eventScraper.fetchYelpEvents(0, startDate, endDate);

                // 进行数据过滤
                if (events != null && !events.isEmpty()) {
                    logger.info("📅 [EventService] {} events fetched from Yelp API.", events.size());

                    // 不要取消的活动,要有名称、图片、链接，起止时间要合理（最多跨一天），要在曼哈顿区域
                    List<EventData> newEvents = events.stream()
                            .filter(event -> !event.getIs_canceled())
                            .filter(event -> event.getName() != null && !event.getName().isEmpty())
                            .filter(event -> event.getEvent_site_url() != null && !event.getEvent_site_url().isEmpty())
                            .filter(event -> event.getImage_url() != null && !event.getImage_url().isEmpty())
                            .filter(event -> {
                                LocalDateTime timeStart = convertToLocalDateTime(event.getTime_start());
                                LocalDateTime timeEnd = event.getTime_end() != null ?
                                        convertToLocalDateTime(event.getTime_end()) :
                                        timeStart.plusHours(2);
                                if (!timeStart.toLocalDate().equals(timeEnd.toLocalDate())) {
                                    if (timeStart.plusHours(2).isAfter(timeStart.toLocalDate().atTime(23, 59))) {
                                        timeEnd = timeStart.toLocalDate().atTime(23, 59);
                                    } else {
                                        timeEnd = timeStart.plusHours(2);
                                    }
                                }
                                return timeStart.isAfter(nowInNewYork) &&
                                        timeStart.isBefore(nowInNewYork.plusDays(30)) &&
                                        timeEnd.isBefore(nowInNewYork.plusDays(30));
                            })
                            .filter(event -> isPointInPolygon(event.getLatitude(), event.getLongitude(), ManhattanArea))
                            .collect(Collectors.toList());

                    int savedCount = 0;
                    for (EventData event : newEvents) {
                        boolean exists = eventRepository.existsEventByNameOrUrl(
                                event.getName(), event.getEvent_site_url(), event.getImage_url()
                        );

                        if (!exists) {
                            event.setId(UUID.randomUUID());
                            event.setFetchTime(LocalDateTime.now());
                            LocalDateTime timeStart = convertToLocalDateTime(event.getTime_start());
                            LocalDateTime timeEnd = event.getTime_end() != null ?
                                    convertToLocalDateTime(event.getTime_end()) :
                                    timeStart.plusHours(2);
                            if (!timeStart.toLocalDate().equals(timeEnd.toLocalDate())) {
                                if (timeStart.plusHours(2).isAfter(timeStart.toLocalDate().atTime(23, 59))) {
                                    timeEnd = timeStart.toLocalDate().atTime(23, 59);
                                } else {
                                    timeEnd = timeStart.plusHours(2);
                                }
                            }
                            event.setTime_start(formatLocalDateTime(timeStart));
                            event.setTime_end(formatLocalDateTime(timeEnd));

                            // 保存入库
                            eventRepository.save(event);
                            savedCount++;
                        }


                    }
                    logger.info("✅ [EventService] Successfully saved {} new events to the database.", savedCount);
                } else {
                    logger.warn("⚠️ [EventService] No events received from Yelp API.");
                }

                success = true;
            } catch (Exception e) {
                retryCount++;
                logger.error("❌ [EventService] Error while fetching or saving events (attempt {}): {}", retryCount, e.getMessage());
                if (retryCount >= 3) {
                    throw e; // Rethrow the exception if maximum retry count is reached
                }
            }
        }
    }

    // 时间转换工具方法
    private LocalDateTime convertToLocalDateTime(String dateTimeWithOffset) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateTimeWithOffset, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return offsetDateTime.atZoneSameInstant(ZoneId.of("America/New_York")).toLocalDateTime();
    }

    // 把 API 返回的时间（带偏移）转成纽约本地时间
    private String formatLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public List<EventData> getAllEvents() {
        return eventRepository.findAll();
    }
}