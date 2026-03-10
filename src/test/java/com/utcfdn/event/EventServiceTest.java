package com.utcfdn.event;

import com.utcfdn.congregation.CongregationEntity;
import com.utcfdn.congregation.CongregationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private CongregationRepository congregationRepository;

    @Autowired
    private EventTemplateRepository templateRepository;

    @Autowired
    private ScheduledEventInstanceRepository instanceRepository;

    private CongregationEntity testCongregation;

    @BeforeEach
    void setUp() {
        testCongregation = congregationRepository.save(CongregationEntity.builder()
                .name("Test Congregation")
                .description("Test Description")
                .mission("Test Mission")
                .build());
    }

    @Test
    void testCreateTemplate() {
        EventTemplateDto dto = EventTemplateDto.builder()
                .name("Weekly Service")
                .description("Sunday Service")
                .congregationId(testCongregation.getId())
                .startDate(LocalDate.now())
                .startTime(LocalTime.of(10, 0))
                .durationMinutes(60)
                .recurrenceRule("FREQ=WEEKLY;BYDAY=SU")
                .build();

        EventTemplateDto created = eventService.createTemplate(dto);

        assertNotNull(created.getId());
        assertEquals("Weekly Service", created.getName());
        assertTrue(created.isActive());
    }

    @Test
    void testUpdateTemplate() {
        EventTemplateEntity template = templateRepository.save(EventTemplateEntity.builder()
                .name("Original Name")
                .congregation(testCongregation)
                .startDate(LocalDate.now())
                .startTime(LocalTime.of(10, 0))
                .recurrenceRule("FREQ=WEEKLY")
                .isActive(true)
                .build());

        EventTemplateDto updateDto = EventTemplateDto.builder()
                .name("Updated Name")
                .congregationId(testCongregation.getId())
                .startDate(LocalDate.now())
                .startTime(LocalTime.of(11, 0))
                .durationMinutes(90)
                .recurrenceRule("FREQ=DAILY")
                .isActive(true)
                .build();

        Optional<EventTemplateDto> updated = eventService.updateTemplate(template.getId(), updateDto);

        assertTrue(updated.isPresent());
        assertEquals("Updated Name", updated.get().getName());
        assertEquals(LocalTime.of(11, 0), updated.get().getStartTime());
    }

    @Test
    void testDeleteTemplateSoftDeletes() {
        EventTemplateEntity template = templateRepository.save(EventTemplateEntity.builder()
                .name("To Be Deleted")
                .congregation(testCongregation)
                .startDate(LocalDate.now())
                .startTime(LocalTime.of(10, 0))
                .recurrenceRule("FREQ=WEEKLY")
                .isActive(true)
                .build());

        eventService.deleteTemplate(template.getId());

        EventTemplateEntity found = templateRepository.findById(template.getId()).orElseThrow();
        assertFalse(found.isActive(), "Template should be soft-deleted (isActive = false)");
    }

    @Test
    void testCancelInstance() {
        EventTemplateEntity template = templateRepository.save(EventTemplateEntity.builder()
                .name("Pattern")
                .congregation(testCongregation)
                .startDate(LocalDate.now())
                .startTime(LocalTime.of(10, 0))
                .recurrenceRule("FREQ=WEEKLY")
                .build());

        ScheduledEventInstanceEntity instance = instanceRepository.save(ScheduledEventInstanceEntity.builder()
                .template(template)
                .startTime(LocalDateTime.now().plusDays(1))
                .originalStartTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .name("Instance")
                .isCancelled(false)
                .build());

        Optional<EventOccurrenceDto> result = eventService.cancelInstance(instance.getId());

        assertTrue(result.isPresent());
        assertTrue(result.get().isCancelled());

        ScheduledEventInstanceEntity updatedInstance = instanceRepository.findById(instance.getId()).orElseThrow();
        assertTrue(updatedInstance.isCancelled());
    }

    @Test
    void testOverrideInstance() {
        EventTemplateEntity template = templateRepository.save(EventTemplateEntity.builder()
                .name("Pattern")
                .congregation(testCongregation)
                .startDate(LocalDate.now())
                .startTime(LocalTime.of(10, 0))
                .recurrenceRule("FREQ=WEEKLY")
                .build());

        ScheduledEventInstanceEntity instance = instanceRepository.save(ScheduledEventInstanceEntity.builder()
                .template(template)
                .startTime(LocalDateTime.now().plusDays(1))
                .originalStartTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .name("Standard Name")
                .isOverride(false)
                .build());

        EventOccurrenceDto overrideDto = EventOccurrenceDto.builder()
                .name("Special Holiday Service")
                .startTime(instance.getStartTime().plusHours(2)) // Move it 2 hours later
                .durationMinutes(120)
                .description("Special Description")
                .build();

        Optional<EventOccurrenceDto> overridden = eventService.overrideInstance(instance.getId(), overrideDto);

        assertTrue(overridden.isPresent());
        assertEquals("Special Holiday Service", overridden.get().getName());
        assertTrue(overridden.get().isOverride());

        ScheduledEventInstanceEntity updated = instanceRepository.findById(instance.getId()).orElseThrow();
        assertEquals("Special Holiday Service", updated.getName());
        assertTrue(updated.isOverride());
    }

    @Test
    void testGetScheduledInstancesFiltersCancelled() {
        EventTemplateEntity template = templateRepository.save(EventTemplateEntity.builder()
                .name("Template")
                .congregation(testCongregation)
                .startDate(LocalDate.now())
                .startTime(LocalTime.of(10, 0))
                .recurrenceRule("FREQ=WEEKLY")
                .build());

        LocalDateTime now = LocalDateTime.now();
        
        // Save one active and one cancelled instance
        instanceRepository.save(ScheduledEventInstanceEntity.builder()
                .template(template)
                .startTime(now.plusDays(1))
                .originalStartTime(now.plusDays(1))
                .endTime(now.plusDays(1).plusHours(1))
                .name("Active Event")
                .isCancelled(false)
                .build());

        instanceRepository.save(ScheduledEventInstanceEntity.builder()
                .template(template)
                .startTime(now.plusDays(2))
                .originalStartTime(now.plusDays(2))
                .endTime(now.plusDays(2).plusHours(1))
                .name("Cancelled Event")
                .isCancelled(true)
                .build());

        List<EventOccurrenceDto> results = eventService.getScheduledInstances(
                testCongregation.getId(), 
                now.minusDays(1), 
                now.plusDays(5));

        assertEquals(1, results.size(), "Only the non-cancelled instance should be returned");
        assertEquals("Active Event", results.get(0).getName());
    }

    @Test
    void testCancelSeriesFrom() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        EventTemplateEntity template = templateRepository.save(EventTemplateEntity.builder()
                .name("Weekly Series")
                .congregation(testCongregation)
                .startDate(startDate)
                .startTime(LocalTime.of(10, 0))
                .recurrenceRule("FREQ=WEEKLY;BYDAY=MO") // Mondays
                .durationMinutes(60)
                .isActive(true)
                .build());

        // Materialize some instances
        LocalDateTime instance1Time = LocalDateTime.of(2025, 1, 6, 10, 0); // Monday
        LocalDateTime instance2Time = LocalDateTime.of(2025, 1, 13, 10, 0); // Monday
        LocalDateTime instance3Time = LocalDateTime.of(2025, 1, 20, 10, 0); // Monday

        instanceRepository.save(ScheduledEventInstanceEntity.builder()
                .template(template).startTime(instance1Time).originalStartTime(instance1Time).endTime(instance1Time.plusHours(1)).name("Instance 1").build());
        instanceRepository.save(ScheduledEventInstanceEntity.builder()
                .template(template).startTime(instance2Time).originalStartTime(instance2Time).endTime(instance2Time.plusHours(1)).name("Instance 2").build());
        instanceRepository.save(ScheduledEventInstanceEntity.builder()
                .template(template).startTime(instance3Time).originalStartTime(instance3Time).endTime(instance3Time.plusHours(1)).name("Instance 3").build());

        // Cancel series starting from Instance 2
        Optional<EventTemplateDto> result = eventService.cancelSeriesFrom(template.getId(), instance2Time);

        assertTrue(result.isPresent());
        // End date should be day before instance 2 (Sunday Jan 12)
        assertEquals(LocalDate.of(2025, 1, 12), result.get().getEndDate());

        // Check materialized instances
        List<ScheduledEventInstanceEntity> remaining = instanceRepository.findAll().stream()
                .filter(i -> i.getTemplate().getId().equals(template.getId()))
                .toList();
        
        assertEquals(1, remaining.size(), "Only instance 1 should remain");
        assertEquals("Instance 1", remaining.get(0).getName());
    }

    @Test
    void testGenerateOccurrencesRespectsEndDate() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 15);
        EventTemplateEntity template = templateRepository.save(EventTemplateEntity.builder()
                .name("Limited Series")
                .congregation(testCongregation)
                .startDate(startDate)
                .endDate(endDate)
                .startTime(LocalTime.of(10, 0))
                .recurrenceRule("FREQ=WEEKLY;BYDAY=MO") // Jan 6, 13, 20...
                .durationMinutes(60)
                .isActive(true)
                .build());

        // Request occurrences for the whole month of January
        LocalDateTime startRange = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime endRange = LocalDateTime.of(2025, 1, 31, 23, 59);

        List<EventOccurrenceDto> occurrences = eventService.getOccurrences(testCongregation.getId(), startRange, endRange);

        // Should find Jan 6 and Jan 13, but NOT Jan 20 because it's after Jan 15
        assertEquals(2, occurrences.size(), "Should only return occurrences before or on the end date");
        assertEquals(LocalDateTime.of(2025, 1, 6, 10, 0), occurrences.get(0).getStartTime());
        assertEquals(LocalDateTime.of(2025, 1, 13, 10, 0), occurrences.get(1).getStartTime());
    }
}

