package com.utcfdn.congregation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "congregation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CongregationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "mission", columnDefinition = "TEXT")
    private String mission;

    public CongregationEntity(String name, String description, String mission) {
        this.name = name;
        this.description = description;
        this.mission = mission;
    }

}
