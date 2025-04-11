package com.tourwise.scrapper.controller;

import com.tourwise.scrapper.model.EventData;
import com.tourwise.scrapper.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/events")
    public List<EventData> getAllEvents() {
        return eventService.getAllEvents();
    }
}
