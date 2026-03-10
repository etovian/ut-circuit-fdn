package com.utcfdn.scheduling;

import com.utcfdn.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
            // 1. Try to find by Pattern Position (originalStartTime)
            Optional<ScheduledEventInstanceEntity> byOriginal = instanceRepository.findByTemplateIdAndOriginalStartTime(dto.getTemplateId(), dto.getOriginalStartTime());
            
            if (byOriginal.isPresent()) {
                ScheduledEventInstanceEntity existing = byOriginal.get();
                // Only update if it's NOT a manual override and NOT cancelled
                if (!existing.isOverride() && !existing.isCancelled()) {
                    existing.setStartTime(dto.getStartTime());
                    existing.setEndTime(dto.getStartTime().plusMinutes(dto.getDurationMinutes()));
                    existing.setName(dto.getName());
                    existing.setDescription(dto.getDescription());
                    existing.setLocation(dto.getLocation());
                    instanceRepository.save(existing);
                }
            } else {
                // 2. No record for this pattern position. 
                // BUT, check if there's already a record for this ACTUAL time!
                // (e.g. from an override of a different pattern position that shifted into this one, 
                // or if the pattern shifted and we are seeing a "new" slot at an "old" override's time)
                Optional<ScheduledEventInstanceEntity> atTime = instanceRepository.findByTemplateIdAndStartTime(dto.getTemplateId(), dto.getStartTime());
                
                if (atTime.isPresent()) {
                    ScheduledEventInstanceEntity conflict = atTime.get();
                    if (!conflict.isOverride() && !conflict.isCancelled()) {
                        // Stale record from an old pattern position. Adopt it.
                        conflict.setOriginalStartTime(dto.getOriginalStartTime());
                        conflict.setName(dto.getName());
                        conflict.setDescription(dto.getDescription());
                        conflict.setLocation(dto.getLocation());
                        instanceRepository.save(conflict);
                    }
                    // If it IS an override/cancelled at this time, we skip this pattern occurrence 
                    // because the time slot is already manually managed.
                } else {
                    // 3. Clean slate. Create new instance.
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
            }
        }

        log.info("Synchronization complete.");
    }
}
