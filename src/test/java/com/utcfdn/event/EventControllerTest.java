package com.utcfdn.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utcfdn.congregation.CongregationEntity;
import com.utcfdn.congregation.CongregationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CongregationRepository congregationRepository;

    @Autowired
    private EventTemplateRepository templateRepository;

    @Autowired
    private ScheduledEventInstanceRepository instanceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private CongregationEntity testCongregation;

    @BeforeEach
    void setUp() {
        testCongregation = congregationRepository.save(CongregationEntity.builder()
                .name("Controller Test Congregation")
                .description("Desc")
                .mission("Mission")
                .build());
    }

    @Test
    void testCreateTemplate() throws Exception {
        EventTemplateDto dto = EventTemplateDto.builder()
                .name("New Template")
                .description("Description")
                .congregationId(testCongregation.getId())
                .startDate(LocalDate.now())
                .startTime(LocalTime.of(9, 0))
                .durationMinutes(60)
                .recurrenceRule("FREQ=WEEKLY;BYDAY=SU")
                .build();

        mockMvc.perform(post("/api/events/templates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Template"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void testGetTemplates() throws Exception {
        templateRepository.save(EventTemplateEntity.builder()
                .name("Existing Template")
                .congregation(testCongregation)
                .startDate(LocalDate.now())
                .startTime(LocalTime.of(10, 0))
                .recurrenceRule("FREQ=WEEKLY")
                .isActive(true)
                .build());

        mockMvc.perform(get("/api/events/templates")
                .param("congregationId", testCongregation.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Existing Template"));
    }

    @Test
    void testCancelInstance() throws Exception {
        EventTemplateEntity template = templateRepository.save(EventTemplateEntity.builder()
                .name("Template")
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
                .name("To Cancel")
                .isCancelled(false)
                .build());

        mockMvc.perform(patch("/api/events/scheduled/" + instance.getId() + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cancelled").value(true));
    }

    @Test
    void testOverrideInstance() throws Exception {
        EventTemplateEntity template = templateRepository.save(EventTemplateEntity.builder()
                .name("Template")
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
                .name("Original Name")
                .isOverride(false)
                .build());

        EventOccurrenceDto overrideDto = EventOccurrenceDto.builder()
                .name("Overridden Name")
                .startTime(instance.getStartTime().plusHours(1))
                .durationMinutes(60)
                .build();

        mockMvc.perform(put("/api/events/scheduled/" + instance.getId() + "/override")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(overrideDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Overridden Name"))
                .andExpect(jsonPath("$.override").value(true));
    }
}
