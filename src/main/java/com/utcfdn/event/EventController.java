package com.utcfdn.event;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // --- Administrative Endpoints ---

    @GetMapping("/templates")
    public List<EventTemplateDto> getTemplates(@RequestParam(required = false) Long congregationId) {
        return eventService.getTemplates(congregationId);
    }

    @PostMapping("/templates")
    public EventTemplateDto createTemplate(@RequestBody EventTemplateDto dto) {
        return eventService.createTemplate(dto);
    }

    @PutMapping("/templates/{id}")
    public ResponseEntity<EventTemplateDto> updateTemplate(@PathVariable Long id, @RequestBody EventTemplateDto dto) {
        return eventService.updateTemplate(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/templates/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        eventService.deleteTemplate(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/scheduled/{instanceId}/cancel")
    public ResponseEntity<EventOccurrenceDto> cancelInstance(@PathVariable Long instanceId) {
        return eventService.cancelInstance(instanceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/scheduled/{instanceId}/override")
    public ResponseEntity<EventOccurrenceDto> overrideInstance(@PathVariable Long instanceId, @RequestBody EventOccurrenceDto dto) {
        return eventService.overrideInstance(instanceId, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
