package com.utcfdn.scheduling;

import com.utcfdn.congregation.CongregationEntity;
import com.utcfdn.congregation.CongregationRepository;
import com.utcfdn.event.EventService;
import com.utcfdn.event.EventTemplateDto;
import com.utcfdn.event.ScheduledEventInstanceEntity;
import com.utcfdn.event.ScheduledEventInstanceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
class EventSyncConflictTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventSchedulingTask schedulingTask;

    @Autowired
    private CongregationRepository congregationRepository;

    @Autowired
    private ScheduledEventInstanceRepository instanceRepository;

    @Test
    @Transactional
    void testChangeTemplateTimeAndSync() {
        // 1. Setup Congregation and Template
        CongregationEntity church = congregationRepository.save(CongregationEntity.builder()
                .name("Conflict Test Church")
                .description("Desc")
                .mission("Mission")
                .build());

        EventTemplateDto templateDto = EventTemplateDto.builder()
                .name("Weekly Service")
                .congregationId(church.getId())
                .startDate(LocalDate.now())
                .startTime(LocalTime.of(10, 0))
                .durationMinutes(60)
                .recurrenceRule("FREQ=WEEKLY;BYDAY=SU")
                .isActive(true)
                .build();

        templateDto = eventService.createTemplate(templateDto);
        Long templateId = templateDto.getId();

        // 2. Initial Sync
        schedulingTask.scheduleEventsForNext30Days();
        
        List<ScheduledEventInstanceEntity> instancesBefore = instanceRepository.findAll();
        int countBefore = instancesBefore.size();
        System.out.println("Instances before: " + countBefore);

        // 3. Update Template Time (10:00 -> 11:00)
        templateDto.setStartTime(LocalTime.of(11, 0));
        eventService.updateTemplate(templateId, templateDto);

        // 4. Second Sync (This is where the conflict might happen)
        // In the real app, updateTemplate calls sync automatically.
        // If it didn't throw yet, let's call it manually to be sure.
        schedulingTask.scheduleEventsForNext30Days();

        List<ScheduledEventInstanceEntity> instancesAfter = instanceRepository.findAll();
        System.out.println("Instances after: " + instancesAfter.size());
    }
}
