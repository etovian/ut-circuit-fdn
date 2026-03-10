package com.utcfdn.scheduling;

import com.utcfdn.congregation.CongregationEntity;
import com.utcfdn.congregation.CongregationRepository;
import com.utcfdn.event.EventTemplateEntity;
import com.utcfdn.event.EventTemplateRepository;
import com.utcfdn.event.ScheduledEventInstanceEntity;
import com.utcfdn.event.ScheduledEventInstanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EventSchedulingTaskTest {

    @Autowired
    private EventSchedulingTask eventSchedulingTask;

    @Autowired
    private EventTemplateRepository templateRepository;

    @Autowired
    private ScheduledEventInstanceRepository instanceRepository;

    @Autowired
    private CongregationRepository congregationRepository;

    private CongregationEntity testCongregation;

    @BeforeEach
    void setUp() {
        testCongregation = congregationRepository.save(CongregationEntity.builder()
                .name("Scheduling Test Church")
                .description("Test Description")
                .mission("Test Mission")
                .build());
    }

    @Test
    void testScheduleEventsForNext30Days() {
        // 1. Create a weekly template
        templateRepository.save(EventTemplateEntity.builder()
                .name("Weekly Bible Study")
                .congregation(testCongregation)
                .startDate(LocalDate.now().minusDays(7)) // Started last week
                .startTime(LocalTime.of(19, 0))
                .durationMinutes(90)
                .recurrenceRule("FREQ=WEEKLY;BYDAY=MO,WE,FR") // 3 times a week
                .isActive(true)
                .build());

        // 2. Run the task
        eventSchedulingTask.scheduleEventsForNext30Days();

        // 3. Verify instances were created
        List<ScheduledEventInstanceEntity> instances = instanceRepository.findAll();
        
        // 3 days a week * 4+ weeks = ~12-14 instances
        assertFalse(instances.isEmpty(), "Should have scheduled some instances");
        assertTrue(instances.size() >= 12, "Should have scheduled at least 12 instances for 3x/week over 30 days");
        
        ScheduledEventInstanceEntity first = instances.get(0);
        assertEquals("Weekly Bible Study", first.getName());
        assertNotNull(first.getStartTime());
        assertNotNull(first.getEndTime());
        assertEquals(90, java.time.Duration.between(first.getStartTime(), first.getEndTime()).toMinutes());

        // 4. Run again - should not create duplicates
        int countBefore = instances.size();
        eventSchedulingTask.scheduleEventsForNext30Days();
        int countAfter = instanceRepository.findAll().size();

        assertEquals(countBefore, countAfter, "Running the task again should not create duplicate instances");
    }
}
