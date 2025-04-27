package com.shaluo.tourwise.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

// 这张表代表一个行程中的“单个日程项”
// 一个 itinerary item 可能是一个景点，也可能是一个活动

@Entity
@Table(name = "itinerary_saved_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItinerarySavedItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne  // 多个 ItinerarySavedItems 可以指向同一个 ItinerarySaved
    @JoinColumn(name = "itinerary_id", nullable = false)
    private ItinerarySaved itinerary;

    @Column(nullable = true)
    private Integer itemId;

    @Column(nullable = true)
    private UUID eventId;

    @Column(nullable = false)
    private boolean isEvent;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    // 在设置“是活动”还是“是景点”的时候，自动清除另一个类型的 ID
    public void setIsEvent(boolean isEventPassed) {
        isEvent = isEventPassed;

        if (isEvent) {
            this.itemId = null; // 说明这是个活动 → 把景点的 ID 清空
        } else {
            this.eventId = null; // 说明这是个景点 → 把活动的 ID 清空
        }
    }

    public boolean getIsEvent() {
        return isEvent;
    }

}
