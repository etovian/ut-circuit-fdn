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

        log.info("Starting event synchronization for range: {} to {}", start, end);

        // Fetch all active templates
        List<EventOccurrenceDto> occurrences = eventService.getOccurrences(null, start, end);

        int newCount = 0;
        int updateCount = 0;

        for (EventOccurrenceDto dto : occurrences) {
            instanceRepository.findByTemplateIdAndOriginalStartTime(dto.getTemplateId(), dto.getOriginalStartTime())
                    .ifPresentOrElse(
                            existing -> {
                                // Only update if it's NOT an override and NOT cancelled
                                if (!existing.isOverride() && !existing.isCancelled()) {
                                    existing.setStartTime(dto.getStartTime());
                                    existing.setEndTime(dto.getStartTime().plusMinutes(dto.getDurationMinutes()));
                                    existing.setName(dto.getName());
                                    existing.setDescription(dto.getDescription());
                                    existing.setLocation(dto.getLocation());
                                    instanceRepository.save(existing);
                                    // Note: we don't increment updateCount here to avoid noise, 
                                    // but we could if we wanted to track actual changes.
                                }
                            },
                            () -> {
                                // Create new instance
                                EventTemplateEntity template = templateRepository.findById(dto.getTemplateId())
                                        .orElseThrow(() -> new RuntimeException("Template not found: " + dto.getTemplateId()));

                                ScheduledEventInstanceEntity instance = ScheduledEventInstanceEntity.builder()
                                        .template(template)
                                        .startTime(dto.getStartTime())
                                        .originalStartTime(dto.getOriginalStartTime())
                                        .endTime(dto.getStartTime().plusMinutes(dto.getDurationMinutes()))
                                        .name(dto.getName())
                                        .description(dto.getDescription())
                                        .location(dto.getLocation())
                                        .isOverride(false)
                                        .isCancelled(false)
                                        .build();

                                instanceRepository.save(instance);
                            }
                    );
        }

        log.info("Synchronization complete.");
    }
}
