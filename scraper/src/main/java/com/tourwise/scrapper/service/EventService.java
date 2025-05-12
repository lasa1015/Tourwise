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

@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventScraper eventScraper;

    @Autowired
    private EventRepository eventRepository;

    @Value("${yelp.limit}")
    private int limit;

    @Scheduled(fixedRate = 10800000)
    @Transactional
    public void fetchAndSaveEvents() {
        logger.info("🗓️ [EventService] Starting event update process...");

        try {
            ZoneId newYorkZone = ZoneId.of("America/New_York");
            LocalDateTime nowInNewYork = LocalDateTime.now(newYorkZone);

            long startDate = nowInNewYork.toEpochSecond(ZoneOffset.UTC);
            long endDate = nowInNewYork.plusDays(30).toEpochSecond(ZoneOffset.UTC);
            logger.info("📅 [EventService] Fetching events from {} to {}", startDate, endDate);

            List<EventData> events = eventScraper.fetchYelpEvents(0, startDate, endDate);

            if (events != null && !events.isEmpty()) {
                logger.info("📅 [EventService] {} events fetched from Yelp API.", events.size());

                // 清空原有数据
                logger.info("🧹 [EventService] Clearing existing events from database...");
                eventRepository.deleteAll();

                // 插入所有新数据
                for (EventData event : events) {
                    try {
                        event.setId(UUID.randomUUID());
                        event.setFetchTime(LocalDateTime.now());
                        eventRepository.save(event);
                    } catch (Exception e) {
                        logger.warn("⚠️ Failed to save event [{}]: {}", event.getName(), e.getMessage());
                    }
                }

                logger.info("✅ [EventService] Successfully saved {} events.", events.size());
            } else {
                logger.warn("⚠️ [EventService] No events received from Yelp API.");
            }
        } catch (Exception e) {
            logger.error("❌ [EventService] Error while fetching or saving events: {}", e.getMessage());
        }
    }

    private LocalDateTime convertToLocalDateTime(String dateTimeWithOffset) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateTimeWithOffset, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return offsetDateTime.atZoneSameInstant(ZoneId.of("America/New_York")).toLocalDateTime();
    }

    public List<EventData> getAllEvents() {
        return eventRepository.findAll();
    }
}
