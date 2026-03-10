package com.utcfdn.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventSchedulingTaskRunner {

    private final EventSchedulingTask eventSchedulingTask;

    /**
     * Runs the event scheduling task nightly at 2:00 AM.
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void runNightlyTasks() {
        log.info("Running nightly scheduled tasks...");
        try {
            eventSchedulingTask.scheduleEventsForNext30Days();
            log.info("Nightly scheduled tasks completed successfully.");
        } catch (Exception e) {
            log.error("Error occurred during nightly scheduled tasks", e);
        }
    }
}
