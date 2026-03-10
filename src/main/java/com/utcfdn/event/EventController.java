package com.utcfdn.event;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventOccurrenceDto> getEvents(
            @RequestParam(required = false) Long congregationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        return eventService.getOccurrences(congregationId, start, end);
    }

    @GetMapping("/scheduled")
    public List<EventOccurrenceDto> getScheduledEvents(
            @RequestParam(required = false) Long congregationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return eventService.getScheduledInstances(congregationId, start, end);
    }

    @GetMapping("/scheduled/next-seven-days")
    public List<EventOccurrenceDto> getScheduledEventsNextSevenDays(
            @RequestParam Long congregationId) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(7);
        return eventService.getScheduledInstances(congregationId, start, end);
    }
}
