package com.utcfdn.congregation;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CongregationExternalLinkMapper {

    public CongregationExternalLinkDto toDto(CongregationExternalLinkEntity entity) {
        if (entity == null) {
            return null;
        }

        return CongregationExternalLinkDto.builder()
                .id(entity.getId())
                .congregationId(entity.getCongregation() != null ? entity.getCongregation().getId() : null)
                .title(entity.getTitle())
                .description(entity.getDescription())
                .externalLinkType(entity.getExternalLinkType())
                .url(entity.getUrl())
                .ordinalValue(entity.getOrdinalValue())
                .build();
    }

    public List<CongregationExternalLinkDto> toDtoList(List<CongregationExternalLinkEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CongregationExternalLinkEntity toEntity(CongregationExternalLinkDto dto, CongregationEntity congregation) {
        if (dto == null) {
            return null;
        }

        return CongregationExternalLinkEntity.builder()
                .id(dto.getId())
                .congregation(congregation)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .externalLinkType(dto.getExternalLinkType())
                .url(dto.getUrl())
                .ordinalValue(dto.getOrdinalValue() != null ? dto.getOrdinalValue() : 0)
                .build();
    }
}
