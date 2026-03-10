package com.utcfdn.event;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a deviation or cancellation of a recurring event occurrence.
 * This is used ONLY when an occurrence is different from the pattern defined in the template.
 */
@Entity
@Table(name = "event_occurrence")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventOccurrenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_template_id", nullable = false)
    private EventTemplateEntity template;

    /**
     * The original, intended start time for this occurrence before any override.
     * This acts as the key to identify which occurrence in the pattern is being modified.
     */
    @Column(name = "original_start_time", nullable = false)
    private LocalDateTime originalStartTime;

    /**
     * The new start time for this occurrence (if moved).
     */
    @Column(name = "new_start_time")
    private LocalDateTime newStartTime;

    @Column(name = "is_cancelled")
    @Builder.Default
    private boolean isCancelled = false;

    @Column(name = "override_name")
    private String overrideName;

    @Column(name = "override_description", columnDefinition = "TEXT")
    private String overrideDescription;
}
