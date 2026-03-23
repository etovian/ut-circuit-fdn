package com.utcfdn.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventTemplateDto {
    private Long id;
    private String name;
    private String description;
    private String location;
    private Long congregationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private Integer durationMinutes;
    private String recurrenceRule;

    @JsonProperty("isActive")
    private boolean isActive;

    @JsonProperty("isCircuitEvent")
    private boolean isCircuitEvent;
}
