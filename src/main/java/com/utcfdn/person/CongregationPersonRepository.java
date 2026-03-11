package com.utcfdn.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CongregationPersonRepository extends JpaRepository<CongregationPersonEntity, CongregationPersonId> {
    List<CongregationPersonEntity> findByCongregationId(Long congregationId);
    List<CongregationPersonEntity> findByPersonId(Long personId);
}
