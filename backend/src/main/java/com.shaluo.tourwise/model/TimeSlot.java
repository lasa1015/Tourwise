package com.shaluo.tourwise.model;

import lombok.Data;

import java.time.LocalDateTime;

// 它表示一个“时间段”
@Data
public class TimeSlot {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private boolean occupied;

    public TimeSlot(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }
}