package com.utcfdn.person;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CongregationPersonId implements Serializable {

    @Column(name = "congregation_id")
    private Long congregationId;

    @Column(name = "person_id")
    private Long personId;
}
