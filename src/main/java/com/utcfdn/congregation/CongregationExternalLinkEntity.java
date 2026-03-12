package com.utcfdn.congregation;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "congregation_external_link")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CongregationExternalLinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "congregation_id", nullable = false)
    private CongregationEntity congregation;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "external_link_type", nullable = false)
    private ExternalLinkType externalLinkType;

    @Column(name = "url", nullable = false)
    private String url;

    @Builder.Default
    @Column(name = "ordinal_value", nullable = false)
    private Integer ordinalValue = 0;
}
