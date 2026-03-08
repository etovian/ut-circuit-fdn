package com.utcfdn.congregation;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "congregation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @ElementCollection
    @CollectionTable(
            name = "congregation_address",
            joinColumns = @JoinColumn(name = "congregation_id")
    )
    @Builder.Default
    private List<CongregationAddress> addresses = new ArrayList<>();

    public CongregationEntity(String name, String description, String mission) {
        this.name = name;
        this.description = description;
        this.mission = mission;
    }

}
