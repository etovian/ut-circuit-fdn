package com.utcfdn.person;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PersonMapper {

    private final PersonContactInfoMapper contactInfoMapper;

    public PersonDto toDto(PersonEntity entity) {
        if (entity == null) {
            return null;
        }

        return PersonDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .firstName(entity.getFirstName())
                .middleName(entity.getMiddleName())
                .lastName(entity.getLastName())
                .suffix(entity.getSuffix())
                .dateOfBirth(entity.getDateOfBirth())
                .photoFileName(entity.getPhotoFileName())
                .biography(entity.getBiography())
                .congregations(entity.getCongregations() != null ?
                        entity.getCongregations().stream()
                                .map(this::toCongregationRelationDto)
                                .collect(Collectors.toList()) : Collections.emptyList())
                .contactInfos(entity.getContactInfos() != null ?
                        entity.getContactInfos().stream()
                                .map(contactInfoMapper::toDto)
                                .collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }

    public PersonEntity toEntity(PersonDto dto) {
        if (dto == null) {
            return null;
        }

        return PersonEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .lastName(dto.getLastName())
                .suffix(dto.getSuffix())
                .dateOfBirth(dto.getDateOfBirth())
                .photoFileName(dto.getPhotoFileName())
                .biography(dto.getBiography())
                .build();
    }

    public CongregationRelationDto toCongregationRelationDto(CongregationPersonEntity relation) {
        if (relation == null) {
            return null;
        }

        return CongregationRelationDto.builder()
                .id(relation.getCongregation().getId())
                .name(relation.getCongregation().getName())
                .position(relation.getPosition())
                .build();
    }

    public PersonRelationDto toPersonRelationDto(CongregationPersonEntity relation) {
        if (relation == null) {
            return null;
        }

        String photoUrl = null;
        if (relation.getPerson().getPhotoFileName() != null) {
            photoUrl = "/api/person-photos/" + relation.getPerson().getPhotoFileName();
        }

        return PersonRelationDto.builder()
                .id(relation.getPerson().getId())
                .firstName(relation.getPerson().getFirstName())
                .lastName(relation.getPerson().getLastName())
                .position(relation.getPosition())
                .photoUrl(photoUrl)
                .sortOrdinalValue(relation.getSortOrdinalValue())
                .build();
    }
}
