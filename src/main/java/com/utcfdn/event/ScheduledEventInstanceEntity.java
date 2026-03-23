package com.utcfdn.event;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_event_instance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduledEventInstanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_template_id", nullable = false)
    private EventTemplateEntity template;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "original_start_time", nullable = false)
    private LocalDateTime originalStartTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String location;

    @Column(name = "is_override", nullable = false)
    private boolean isOverride;

    @Column(name = "is_cancelled", nullable = false)
    private boolean isCancelled;

    @Column(name = "is_circuit_event", nullable = false)
    private boolean isCircuitEvent;
}
