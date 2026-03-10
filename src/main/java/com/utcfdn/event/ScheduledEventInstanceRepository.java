package com.utcfdn.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledEventInstanceRepository extends JpaRepository<ScheduledEventInstanceEntity, Long> {
    boolean existsByTemplateIdAndStartTime(Long templateId, LocalDateTime startTime);

    List<ScheduledEventInstanceEntity> findByTemplateIdInAndStartTimeBetween(
            List<Long> templateIds, LocalDateTime start, LocalDateTime end);

    List<ScheduledEventInstanceEntity> findByTemplateCongregationIdAndStartTimeBetween(
            Long congregationId, LocalDateTime start, LocalDateTime end);

    List<ScheduledEventInstanceEntity> findByStartTimeBetween(
            LocalDateTime start, LocalDateTime end);
}
