package com.utcfdn.healthcheck;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "app_health_check")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthCheckEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_time", nullable = false)
    private LocalDateTime checkTime;

    @Column(name = "status", nullable = false)
    private String status;

    public HealthCheckEntity(LocalDateTime checkTime, String status) {
        this.checkTime = checkTime;
        this.status = status;
    }

}
