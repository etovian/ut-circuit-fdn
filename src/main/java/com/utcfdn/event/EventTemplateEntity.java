package com.utcfdn.event;

import com.utcfdn.congregation.CongregationEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a recurring event pattern (the "Template").
 * Following RFC 5545 (iCalendar) for recurrence rules.
 */
@Entity
@Table(name = "event_template")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventTemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String location; // Optional: defaults to congregation address if null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "congregation_id", nullable = false)
    private CongregationEntity congregation;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate; // Null if it repeats indefinitely

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    /**
     * RFC 5545 Recurrence Rule (RRULE).
     * Example: "FREQ=WEEKLY;BYDAY=SU" (Every Sunday)
     * Example: "FREQ=MONTHLY;BYDAY=2TU" (Second Tuesday of every month)
     */
    @Column(name = "recurrence_rule", nullable = false)
    private String recurrenceRule;

    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true;
}
