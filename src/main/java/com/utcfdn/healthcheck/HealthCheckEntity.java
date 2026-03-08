package com.utcfdn.healthcheck;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_health_check")
public class HealthCheckEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_time", nullable = false)
    private LocalDateTime checkTime;

    @Column(name = "status", nullable = false)
    private String status;

    public HealthCheckEntity() {
    }

    public HealthCheckEntity(LocalDateTime checkTime, String status) {
        this.checkTime = checkTime;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(LocalDateTime checkTime) {
        this.checkTime = checkTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
