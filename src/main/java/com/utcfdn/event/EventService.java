package com.utcfdn.event;

import com.utcfdn.congregation.CongregationRepository;
import com.utcfdn.scheduling.EventSchedulingTask;
import lombok.RequiredArgsConstructor;
import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.dmfs.rfc5545.recur.RecurrenceRuleIterator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventTemplateRepository templateRepository;
    private final EventOccurrenceRepository occurrenceRepository;
    private final ScheduledEventInstanceRepository instanceRepository;
    private final CongregationRepository congregationRepository;
    private final ObjectProvider<EventSchedulingTask> schedulingTaskProvider;

    @Transactional(readOnly = true)
    public List<EventTemplateDto> getTemplates(Long congregationId) {
        List<EventTemplateEntity> templates;
        if (congregationId != null) {
            templates = templateRepository.findByCongregationIdAndIsActiveTrue(congregationId);
        } else {
            templates = templateRepository.findByIsActiveTrue();
        }
        return templates.stream().map(this::mapToTemplateDto).collect(Collectors.toList());
    }

    @Transactional
    public EventTemplateDto createTemplate(EventTemplateDto dto) {
        EventTemplateEntity entity = EventTemplateEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .congregation(congregationRepository.findById(dto.getCongregationId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid congregation ID")))
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .startTime(dto.getStartTime())
                .durationMinutes(dto.getDurationMinutes())
                .recurrenceRule(dto.getRecurrenceRule())
                .isActive(true)
                .build();
        
        EventTemplateDto result = mapToTemplateDto(templateRepository.save(entity));
        schedulingTaskProvider.ifAvailable(EventSchedulingTask::scheduleEventsForNext30Days);
        return result;
    }

    @Transactional
    public Optional<EventTemplateDto> updateTemplate(Long id, EventTemplateDto dto) {
        return templateRepository.findById(id).map(existing -> {
            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setLocation(dto.getLocation());
            existing.setStartDate(dto.getStartDate());
            existing.setEndDate(dto.getEndDate());
            existing.setStartTime(dto.getStartTime());
            existing.setDurationMinutes(dto.getDurationMinutes());
            existing.setRecurrenceRule(dto.getRecurrenceRule());
            existing.setActive(dto.isActive());
            EventTemplateDto result = mapToTemplateDto(templateRepository.save(existing));
            schedulingTaskProvider.ifAvailable(EventSchedulingTask::scheduleEventsForNext30Days);
            return result;
        });
    }

    @Transactional
    public void deleteTemplate(Long id) {
        templateRepository.findById(id).ifPresent(template -> {
            template.setActive(false);
            templateRepository.save(template);
            schedulingTaskProvider.ifAvailable(EventSchedulingTask::scheduleEventsForNext30Days);
        });
    }

    @Transactional
    public Optional<EventOccurrenceDto> cancelInstance(Long instanceId) {
        return instanceRepository.findById(instanceId).map(instance -> {
            instance.setCancelled(true);
            return mapToDto(instanceRepository.save(instance));
        });
    }

    @Transactional
    public Optional<EventOccurrenceDto> overrideInstance(Long instanceId, EventOccurrenceDto dto) {
        return instanceRepository.findById(instanceId).map(instance -> {
            instance.setName(dto.getName());
            instance.setDescription(dto.getDescription());
            instance.setLocation(dto.getLocation());
            instance.setStartTime(dto.getStartTime());
            instance.setEndTime(dto.getStartTime().plusMinutes(dto.getDurationMinutes()));
            instance.setOverride(true);
            return mapToDto(instanceRepository.save(instance));
        });
    }

    private EventTemplateDto mapToTemplateDto(EventTemplateEntity entity) {
        return EventTemplateDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .location(entity.getLocation())
                .congregationId(entity.getCongregation().getId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .startTime(entity.getStartTime())
                .durationMinutes(entity.getDurationMinutes())
                .recurrenceRule(entity.getRecurrenceRule())
                .isActive(entity.isActive())
                .build();
    }

    @Transactional(readOnly = true)
    public List<EventOccurrenceDto> getScheduledInstances(Long congregationId, LocalDateTime start, LocalDateTime end) {
        List<ScheduledEventInstanceEntity> instances;
        if (congregationId != null) {
            instances = instanceRepository.findByTemplateCongregationIdAndStartTimeBetween(congregationId, start, end);
        } else {
            instances = instanceRepository.findByStartTimeBetween(start, end);
        }

        return instances.stream()
                .filter(i -> !i.isCancelled())
                .map(this::mapToDto)
                .sorted((a, b) -> a.getStartTime().compareTo(b.getStartTime()))
                .collect(Collectors.toList());
    }

    private EventOccurrenceDto mapToDto(ScheduledEventInstanceEntity entity) {
        return EventOccurrenceDto.builder()
                .id(entity.getId())
                .templateId(entity.getTemplate().getId())
                .startTime(entity.getStartTime())
                .originalStartTime(entity.getOriginalStartTime())
                .durationMinutes((int) java.time.Duration.between(entity.getStartTime(), entity.getEndTime()).toMinutes())
                .name(entity.getName())
                .description(entity.getDescription())
                .location(entity.getLocation())
                .isCancelled(entity.isCancelled())
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
