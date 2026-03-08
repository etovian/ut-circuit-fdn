package com.utcfdn.congregation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CongregationRepository extends JpaRepository<CongregationEntity, Long> {
    List<CongregationEntity> findAllByOrderByNameAsc();
}
