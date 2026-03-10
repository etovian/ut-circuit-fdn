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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventTemplateRepository templateRepository;

    @Autowired
    private EventOccurrenceRepository occurrenceRepository;

    @Autowired
    private CongregationRepository congregationRepository;

    private CongregationEntity testCongregation;

    @BeforeEach
    void setUp() {
        testCongregation = congregationRepository.save(CongregationEntity.builder()
                .name("Test Church")
                .description("Test Description")
                .mission("Test Mission")
                .build());
    }

    @Test
    void testGenerateWeeklyOccurrences() {
        // Create a weekly Sunday service template starting Jan 1, 2024 (Monday)
        // First Sunday will be Jan 7, 2024
        EventTemplateEntity template = templateRepository.save(EventTemplateEntity.builder()
                .name("Sunday Service")
                .congregation(testCongregation)
                .startDate(LocalDate.of(2024, 1, 1))
                .startTime(LocalTime.of(10, 0))
                .durationMinutes(60)
                .recurrenceRule("FREQ=WEEKLY;BYDAY=SU")
                .isActive(true)
                .build());

        // Fetch occurrences for January 2024
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 31, 23, 59);
        
        List<EventOccurrenceDto> occurrences = eventService.getOccurrences(testCongregation.getId(), start, end);

        // There are 4 Sundays in January 2024: 7, 14, 21, 28
        assertEquals(4, occurrences.size());
        assertEquals(LocalDateTime.of(2024, 1, 7, 10, 0), occurrences.get(0).getStartTime());
        assertEquals(LocalDateTime.of(2024, 1, 28, 10, 0), occurrences.get(3).getStartTime());
    }

    @Test
    void testOccurrenceOverride() {
        // 1. Create Template
        EventTemplateEntity template = templateRepository.save(EventTemplateEntity.builder()
                .name("Standard Service")
                .congregation(testCongregation)
                .startDate(LocalDate.of(2024, 1, 1))
                .startTime(LocalTime.of(10, 0))
                .durationMinutes(60)
                .recurrenceRule("FREQ=WEEKLY;BYDAY=SU")
                .isActive(true)
                .build());

        // 2. Create Override for the first occurrence (Jan 7)
        LocalDateTime originalStart = LocalDateTime.of(2024, 1, 7, 10, 0);
        occurrenceRepository.save(EventOccurrenceEntity.builder()
                .template(template)
                .originalStartTime(originalStart)
                .newStartTime(LocalDateTime.of(2024, 1, 7, 9, 30)) // Move to 9:30
                .overrideName("Special Service")
                .build());

        // 3. Fetch
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 10, 23, 59);
        List<EventOccurrenceDto> occurrences = eventService.getOccurrences(testCongregation.getId(), start, end);

        assertEquals(1, occurrences.size());
        EventOccurrenceDto occ = occurrences.get(0);
        assertEquals("Special Service", occ.getName());
        assertEquals(LocalDateTime.of(2024, 1, 7, 9, 30), occ.getStartTime());
        assertEquals(originalStart, occ.getOriginalStartTime());
        assertTrue(occ.isOverride());
    }

    @Test
    void testOccurrenceCancellation() {
        // 1. Create Template
        EventTemplateEntity template = templateRepository.save(EventTemplateEntity.builder()
                .name("Will Be Cancelled")
                .congregation(testCongregation)
                .startDate(LocalDate.of(2024, 1, 1))
                .startTime(LocalTime.of(10, 0))
                .durationMinutes(60)
                .recurrenceRule("FREQ=WEEKLY;BYDAY=SU")
                .isActive(true)
                .build());

        // 2. Cancel the first occurrence
        occurrenceRepository.save(EventOccurrenceEntity.builder()
                .template(template)
                .originalStartTime(LocalDateTime.of(2024, 1, 7, 10, 0))
                .isCancelled(true)
                .build());

        // 3. Fetch for first two weeks
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 14, 23, 59);
        List<EventOccurrenceDto> occurrences = eventService.getOccurrences(testCongregation.getId(), start, end);

        // Jan 7 is cancelled, so only Jan 14 remains
        assertEquals(1, occurrences.size());
        assertEquals(LocalDateTime.of(2024, 1, 14, 10, 0), occurrences.get(0).getStartTime());
    }
}
