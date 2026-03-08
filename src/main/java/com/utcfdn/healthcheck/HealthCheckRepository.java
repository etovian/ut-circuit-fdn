package com.utcfdn.healthcheck;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthCheckRepository extends JpaRepository<HealthCheckEntity, Long> {
    List<HealthCheckEntity> findAllByOrderByIdDesc(Pageable pageable);
}
