package com.utcfdn.congregation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CongregationExternalLinkRepository extends JpaRepository<CongregationExternalLinkEntity, Long> {

    @Query(value = "SELECT * FROM congregation_external_link WHERE congregation_id = :congregationId ORDER BY ordinal_value ASC", nativeQuery = true)
    List<CongregationExternalLinkEntity> findAllByCongregationId(@Param("congregationId") Long congregationId);

    @Query(value = "SELECT COALESCE(MAX(ordinal_value), -1) FROM congregation_external_link WHERE congregation_id = :congregationId", nativeQuery = true)
    Integer findMaxOrdinalValueByCongregationId(@Param("congregationId") Long congregationId);
}
