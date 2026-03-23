package com.utcfdn.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduledEventInstanceRepository extends JpaRepository<ScheduledEventInstanceEntity, Long> {
    boolean existsByTemplateIdAndStartTime(Long templateId, LocalDateTime startTime);

    Optional<ScheduledEventInstanceEntity> findByTemplateIdAndStartTime(Long templateId, LocalDateTime startTime);

    Optional<ScheduledEventInstanceEntity> findByTemplateIdAndOriginalStartTime(Long templateId, LocalDateTime originalStartTime);

    List<ScheduledEventInstanceEntity> findByTemplateIdInAndStartTimeBetween(
            List<Long> templateIds, LocalDateTime start, LocalDateTime end);

    List<ScheduledEventInstanceEntity> findByTemplateIdAndStartTimeGreaterThanEqual(
            Long templateId, LocalDateTime start);

    List<ScheduledEventInstanceEntity> findByTemplateCongregationIdAndStartTimeBetween(
            Long congregationId, LocalDateTime start, LocalDateTime end);

    List<ScheduledEventInstanceEntity> findByStartTimeBetween(
            LocalDateTime start, LocalDateTime end);

    List<ScheduledEventInstanceEntity> findByIsCircuitEventTrueAndStartTimeBetween(
            LocalDateTime start, LocalDateTime end);
}
