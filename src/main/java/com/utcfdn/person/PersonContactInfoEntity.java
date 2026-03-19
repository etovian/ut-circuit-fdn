package com.utcfdn.person;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "person_contact_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonContactInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private PersonEntity person;

    @Enumerated(EnumType.STRING)
    @Column(name = "contact_info_type", nullable = false)
    private ContactInfoType contactInfoType;

    @Column(name = "contact_value", nullable = false)
    private String contactValue;
}
