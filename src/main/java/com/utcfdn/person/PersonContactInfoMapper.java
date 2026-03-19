package com.utcfdn.person;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonContactInfoMapper {

    private final PersonRepository personRepository;

    public PersonContactInfoDto toDto(PersonContactInfoEntity entity) {
        if (entity == null) {
            return null;
        }

        return PersonContactInfoDto.builder()
                .id(entity.getId())
                .personId(entity.getPerson().getId())
                .contactInfoType(entity.getContactInfoType())
                .contactValue(entity.getContactValue())
                .build();
    }

    public PersonContactInfoEntity toEntity(PersonContactInfoDto dto) {
        if (dto == null) {
            return null;
        }

        PersonContactInfoEntity entity = PersonContactInfoEntity.builder()
                .id(dto.getId())
                .contactInfoType(dto.getContactInfoType())
                .contactValue(dto.getContactValue())
                .build();

        if (dto.getPersonId() != null) {
            personRepository.findById(dto.getPersonId()).ifPresent(entity::setPerson);
        }

        return entity;
    }
}
