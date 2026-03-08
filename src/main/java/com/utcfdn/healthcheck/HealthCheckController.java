package com.utcfdn.healthcheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    @Autowired
    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping
    public Map<String, String> health() {
        healthCheckService.recordHealthCheck("UP");
        return Map.of("status", "UP");
    }

    @GetMapping("/history")
    public List<HealthCheckEntity> getHistory() {
        return healthCheckService.getRecentHealthChecks(50);
    }
}
