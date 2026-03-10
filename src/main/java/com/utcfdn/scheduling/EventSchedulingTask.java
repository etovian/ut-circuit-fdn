package com.utcfdn.scheduling;

import com.utcfdn.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventSchedulingTask {

    private final EventService eventService;
    private final EventTemplateRepository templateRepository;
    private final ScheduledEventInstanceRepository instanceRepository;

    @Transactional
    public void scheduleEventsForNext30Days() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(30);

        log.info("Starting event scheduling task for range: {} to {}", start, end);

        // Fetch all active templates (passing null for congregationId gets all)
        List<EventOccurrenceDto> occurrences = eventService.getOccurrences(null, start, end);

        int scheduledCount = 0;
        for (EventOccurrenceDto dto : occurrences) {
            if (!instanceRepository.existsByTemplateIdAndStartTime(dto.getTemplateId(), dto.getStartTime())) {
                EventTemplateEntity template = templateRepository.findById(dto.getTemplateId())
                        .orElseThrow(() -> new RuntimeException("Template not found: " + dto.getTemplateId()));

                ScheduledEventInstanceEntity instance = ScheduledEventInstanceEntity.builder()
                        .template(template)
                        .startTime(dto.getStartTime())
                        .endTime(dto.getStartTime().plusMinutes(dto.getDurationMinutes()))
                        .name(dto.getName())
                        .description(dto.getDescription())
                        .location(dto.getLocation())
                        .isOverride(dto.isOverride())
                        .build();

                instanceRepository.save(instance);
                scheduledCount++;
            }
        }

        log.info("Scheduled {} new event instances.", scheduledCount);
    }
}
