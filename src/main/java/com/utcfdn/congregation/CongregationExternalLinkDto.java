package com.utcfdn.congregation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CongregationExternalLinkDto {
    private Long id;
    private Long congregationId;
    private String title;
    private String description;
    private ExternalLinkType externalLinkType;
    private String url;
    private Integer ordinalValue;
}
