package com.utcfdn.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CongregationPersonRepository extends JpaRepository<CongregationPersonEntity, CongregationPersonId> {
    List<CongregationPersonEntity> findByCongregationId(Long congregationId);
    List<CongregationPersonEntity> findByPersonId(Long personId);

    @Query(value = "SELECT * FROM congregation_person WHERE congregation_id = :congregationId ORDER BY sort_ordinal_value ASC", nativeQuery = true)
    List<CongregationPersonEntity> findAllByCongregationIdOrdered(Long congregationId);

    @Query("SELECT COALESCE(MAX(cp.sortOrdinalValue), -1) FROM CongregationPersonEntity cp WHERE cp.id.congregationId = :congregationId")
    int findMaxSortOrdinalByCongregationId(Long congregationId);
}
