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

        PersonEntity entity = PersonEntity.builder()
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

        if (dto.getContactInfos() != null) {
            entity.setContactInfos(dto.getContactInfos().stream()
                    .map(contactInfoMapper::toEntity)
                    .peek(contactInfo -> contactInfo.setPerson(entity))
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    public void updateEntity(PersonEntity entity, PersonDto dto) {
        if (entity == null || dto == null) {
            return;
        }

        entity.setTitle(dto.getTitle());
        entity.setFirstName(dto.getFirstName());
        entity.setMiddleName(dto.getMiddleName());
        entity.setLastName(dto.getLastName());
        entity.setSuffix(dto.getSuffix());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setPhotoFileName(dto.getPhotoFileName());
        entity.setBiography(dto.getBiography());

        entity.getContactInfos().clear();
        if (dto.getContactInfos() != null) {
            dto.getContactInfos().forEach(ciDto -> {
                PersonContactInfoEntity ciEntity = contactInfoMapper.toEntity(ciDto);
                ciEntity.setPerson(entity);
                entity.getContactInfos().add(ciEntity);
            });
        }
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
                .biography(relation.getPerson().getBiography())
                .sortOrdinalValue(relation.getSortOrdinalValue())
                .contactInfos(relation.getPerson().getContactInfos() != null ?
                        relation.getPerson().getContactInfos().stream()
                                .map(contactInfoMapper::toDto)
                                .collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }
}
