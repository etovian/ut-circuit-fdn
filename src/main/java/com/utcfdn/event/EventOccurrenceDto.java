package com.utcfdn.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a single instance of an event, either from the pattern
 * or as a modified occurrence.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventOccurrenceDto {
    private Long id;
    private Long templateId;
    private LocalDateTime startTime;
    private LocalDateTime originalStartTime;
    private Integer durationMinutes;
    private String name;
    private String description;
    private String location;

    @JsonProperty("isCancelled")
    private boolean isCancelled;

    @JsonProperty("isOverride")
    private boolean isOverride;

    @JsonProperty("isCircuitEvent")
    private boolean isCircuitEvent;
}
