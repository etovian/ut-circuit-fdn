package com.utcfdn.event;

import lombok.RequiredArgsConstructor;
import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.dmfs.rfc5545.recur.RecurrenceRuleIterator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventTemplateRepository templateRepository;
    private final EventOccurrenceRepository occurrenceRepository;
    private final ScheduledEventInstanceRepository instanceRepository;

    @Transactional(readOnly = true)
    public List<EventOccurrenceDto> getScheduledInstances(Long congregationId, LocalDateTime start, LocalDateTime end) {
        List<ScheduledEventInstanceEntity> instances;
        if (congregationId != null) {
            instances = instanceRepository.findByTemplateCongregationIdAndStartTimeBetween(congregationId, start, end);
        } else {
            instances = instanceRepository.findByStartTimeBetween(start, end);
        }

        return instances.stream()
                .map(this::mapToDto)
                .sorted((a, b) -> a.getStartTime().compareTo(b.getStartTime()))
                .collect(Collectors.toList());
    }

    private EventOccurrenceDto mapToDto(ScheduledEventInstanceEntity entity) {
        return EventOccurrenceDto.builder()
                .id(entity.getId())
                .templateId(entity.getTemplate().getId())
                .startTime(entity.getStartTime())
                .originalStartTime(entity.getStartTime()) // In this simplified model, we use start_time as original
                .durationMinutes((int) java.time.Duration.between(entity.getStartTime(), entity.getEndTime()).toMinutes())
                .name(entity.getName())
                .description(entity.getDescription())
                .location(entity.getLocation())
                .isCancelled(false)
                .isOverride(entity.isOverride())
                .build();
    }

    @Transactional(readOnly = true)
    public List<EventOccurrenceDto> getOccurrences(Long congregationId, LocalDateTime start, LocalDateTime end) {
        // 1. Fetch templates
        List<EventTemplateEntity> templates = congregationId != null
                ? templateRepository.findByCongregationIdAndIsActiveTrue(congregationId)
                : templateRepository.findByIsActiveTrue();

        if (templates.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. Fetch overrides/cancellations for the range and these templates
        List<Long> templateIds = templates.stream().map(EventTemplateEntity::getId).collect(Collectors.toList());
        List<EventOccurrenceEntity> overrides = occurrenceRepository.findAll().stream() // Simplified for now, in practice use a custom query
                .filter(o -> templateIds.contains(o.getTemplate().getId()))
                .filter(o -> !o.getOriginalStartTime().isBefore(start) && !o.getOriginalStartTime().isAfter(end))
                .collect(Collectors.toList());

        // Group overrides by (templateId, originalStartTime) for easy lookup
        Map<String, EventOccurrenceEntity> overrideMap = overrides.stream()
                .collect(Collectors.toMap(
                        o -> o.getTemplate().getId() + "_" + o.getOriginalStartTime().toString(),
                        o -> o
                ));

        List<EventOccurrenceDto> allOccurrences = new ArrayList<>();

        for (EventTemplateEntity template : templates) {
            allOccurrences.addAll(generateOccurrences(template, start, end, overrideMap));
        }

        return allOccurrences.stream()
                .sorted((a, b) -> a.getStartTime().compareTo(b.getStartTime()))
                .collect(Collectors.toList());
    }

    private List<EventOccurrenceDto> generateOccurrences(
            EventTemplateEntity template,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Map<String, EventOccurrenceEntity> overrideMap) {

        List<EventOccurrenceDto> occurrences = new ArrayList<>();

        try {
            RecurrenceRule rule = new RecurrenceRule(template.getRecurrenceRule());
            DateTime startDateTime = new DateTime(
                    template.getStartDate().getYear(),
                    template.getStartDate().getMonthValue() - 1,
                    template.getStartDate().getDayOfMonth(),
                    template.getStartTime().getHour(),
                    template.getStartTime().getMinute(),
                    0
            );

            RecurrenceRuleIterator it = rule.iterator(startDateTime);
            DateTime rangeStartDT = toDateTime(rangeStart);
            DateTime rangeEndDT = toDateTime(rangeEnd);

            // Fast forward to range start
            it.fastForward(rangeStartDT);

            while (it.hasNext()) {
                DateTime next = it.nextDateTime();
                if (next.after(rangeEndDT)) {
                    break;
                }

                LocalDateTime originalStart = toLocalDateTime(next);
                String key = template.getId() + "_" + originalStart.toString();
                EventOccurrenceEntity override = overrideMap.get(key);

                if (override != null) {
                    if (!override.isCancelled()) {
                        occurrences.add(mapToDto(template, originalStart, override));
                    }
                    // If cancelled, we simply don't add it to the list
                } else {
                    occurrences.add(mapToDto(template, originalStart, null));
                }
            }
        } catch (Exception e) {
            // Log error and handle gracefully (e.g., skip this template)
            System.err.println("Error parsing RRULE for template " + template.getId() + ": " + e.getMessage());
        }

        return occurrences;
    }

    private EventOccurrenceDto mapToDto(EventTemplateEntity template, LocalDateTime originalStart, EventOccurrenceEntity override) {
        return EventOccurrenceDto.builder()
                .templateId(template.getId())
                .originalStartTime(originalStart)
                .startTime(override != null && override.getNewStartTime() != null ? override.getNewStartTime() : originalStart)
                .durationMinutes(template.getDurationMinutes())
                .name(override != null && override.getOverrideName() != null ? override.getOverrideName() : template.getName())
                .description(override != null && override.getOverrideDescription() != null ? override.getOverrideDescription() : template.getDescription())
                .location(template.getLocation())
                .isCancelled(override != null && override.isCancelled())
                .isOverride(override != null)
                .build();
    }

    private DateTime toDateTime(LocalDateTime ldt) {
        return new DateTime(ldt.getYear(), ldt.getMonthValue() - 1, ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), 0);
    }

    private LocalDateTime toLocalDateTime(DateTime dt) {
        return LocalDateTime.of(dt.getYear(), dt.getMonth() + 1, dt.getDayOfMonth(), dt.getHours(), dt.getMinutes());
    }
}
