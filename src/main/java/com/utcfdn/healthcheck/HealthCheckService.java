package com.utcfdn.healthcheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HealthCheckService {

    private final HealthCheckRepository repository;

    @Autowired
    public HealthCheckService(HealthCheckRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void recordHealthCheck(String status) {
        HealthCheckEntity entity = new HealthCheckEntity(LocalDateTime.now(), status);
        repository.save(entity);
    }

    public List<HealthCheckEntity> getRecentHealthChecks(int limit) {
        return repository.findAllByOrderByIdDesc(PageRequest.of(0, limit));
    }
}
