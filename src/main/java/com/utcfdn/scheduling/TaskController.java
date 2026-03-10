package com.utcfdn.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final EventSchedulingTask eventSchedulingTask;

    /**
     * Manually triggers the event scheduling task to materialize occurrences for the next 30 days.
     * Mapped to /run/event-scheduling as requested.
     */
    @PostMapping("/run/event-scheduling")
    public ResponseEntity<Map<String, String>> runEventScheduling() {
        log.info("Manual trigger of event scheduling task via TaskController.");
        try {
            eventSchedulingTask.scheduleEventsForNext30Days();
            return ResponseEntity.ok(Map.of("message", "Event scheduling task completed successfully."));
        } catch (Exception e) {
            log.error("Error during manual event scheduling", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to run scheduling task: " + e.getMessage()));
        }
    }
}
