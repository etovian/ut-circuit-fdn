package com.utcfdn.person;

import com.utcfdn.congregation.CongregationEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "congregation_person")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CongregationPersonEntity {

    @EmbeddedId
    private CongregationPersonId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("congregationId")
    @JoinColumn(name = "congregation_id")
    private CongregationEntity congregation;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("personId")
    @JoinColumn(name = "person_id")
    private PersonEntity person;

    @Column(nullable = false)
    private String position;
}
