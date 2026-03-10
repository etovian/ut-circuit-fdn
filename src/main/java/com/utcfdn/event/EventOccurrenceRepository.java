package com.utcfdn.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventOccurrenceRepository extends JpaRepository<EventOccurrenceEntity, Long> {
    /**
     * Find all overrides/cancellations for a specific template within a time range.
     */
    List<EventOccurrenceEntity> findByTemplateIdAndOriginalStartTimeBetween(
            Long templateId, LocalDateTime start, LocalDateTime end);

    /**
     * Find all overrides/cancellations for all templates within a time range.
     * Useful for building a calendar view of multiple templates.
     */
    List<EventOccurrenceEntity> findByOriginalStartTimeBetween(
            LocalDateTime start, LocalDateTime end);
}
