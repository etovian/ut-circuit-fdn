package com.utcfdn.healthcheck;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthCheckService {

    private final HealthCheckRepository repository;

    @Transactional
    public void recordHealthCheck(String status) {
        HealthCheckEntity entity = new HealthCheckEntity(LocalDateTime.now(), status);
        repository.save(entity);
    }

    public List<HealthCheckEntity> getRecentHealthChecks(int limit) {
        return repository.findAllByOrderByIdDesc(PageRequest.of(0, limit));
    }
}
