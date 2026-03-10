package com.utcfdn.scheduling;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventSchedulingTaskRunnerTest {

    @Mock
    private EventSchedulingTask eventSchedulingTask;

    @InjectMocks
    private EventSchedulingTaskRunner taskRunner;

    @Test
    void testRunNightlyTasks_Success() {
        taskRunner.runNightlyTasks();
        verify(eventSchedulingTask, times(1)).scheduleEventsForNext30Days();
    }

    @Test
    void testRunNightlyTasks_HandlesException() {
        doThrow(new RuntimeException("Something went wrong")).when(eventSchedulingTask).scheduleEventsForNext30Days();
        
        // This should not throw an exception as it is logged and caught in the runner
        taskRunner.runNightlyTasks();
        
        verify(eventSchedulingTask, times(1)).scheduleEventsForNext30Days();
    }
}
