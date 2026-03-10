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
}
