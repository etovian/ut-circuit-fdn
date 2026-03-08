package com.utcfdn.healthcheck;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

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
