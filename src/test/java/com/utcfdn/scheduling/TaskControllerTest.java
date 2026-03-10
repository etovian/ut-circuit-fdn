package com.utcfdn.scheduling;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventSchedulingTask eventSchedulingTask;

    @Test
    void testRunEventScheduling_Success() throws Exception {
        // No exception thrown by the task
        doNothing().when(eventSchedulingTask).scheduleEventsForNext30Days();

        mockMvc.perform(post("/run/event-scheduling")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Event scheduling task completed successfully."));

        verify(eventSchedulingTask, times(1)).scheduleEventsForNext30Days();
    }

    @Test
    void testRunEventScheduling_Failure() throws Exception {
        // Exception thrown by the task
        doThrow(new RuntimeException("Database error")).when(eventSchedulingTask).scheduleEventsForNext30Days();

        mockMvc.perform(post("/run/event-scheduling")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Failed to run scheduling task: Database error"));

        verify(eventSchedulingTask, times(1)).scheduleEventsForNext30Days();
    }
}
