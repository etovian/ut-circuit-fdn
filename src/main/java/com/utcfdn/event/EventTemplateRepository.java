package com.utcfdn.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventTemplateRepository extends JpaRepository<EventTemplateEntity, Long> {
    /**
     * Find all active event templates for a specific congregation.
     */
    List<EventTemplateEntity> findByCongregationIdAndIsActiveTrue(Long congregationId);

    /**
     * Find all active event templates for the entire circuit.
     */
    List<EventTemplateEntity> findByIsActiveTrue();
}
