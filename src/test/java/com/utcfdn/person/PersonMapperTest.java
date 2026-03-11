package com.utcfdn.person;

import com.utcfdn.congregation.CongregationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PersonMapperTest {

    private PersonMapper personMapper;

    @BeforeEach
    void setUp() {
        personMapper = new PersonMapper();
    }

    @Test
    void testToDto() {
        PersonEntity person = PersonEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1980, 1, 1))
                .build();

        CongregationEntity congregation = CongregationEntity.builder()
                .id(2L)
                .name("Redeemer")
                .build();

        CongregationPersonEntity relation = CongregationPersonEntity.builder()
                .id(new CongregationPersonId(2L, 1L))
                .congregation(congregation)
                .person(person)
                .position("Pastor")
                .build();

        person.setCongregations(List.of(relation));

        PersonDto dto = personMapper.toDto(person);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals(1, dto.getCongregations().size());
        assertEquals("Redeemer", dto.getCongregations().get(0).getName());
        assertEquals("Pastor", dto.getCongregations().get(0).getPosition());
    }

    @Test
    void testToEntity() {
        PersonDto dto = PersonDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        PersonEntity entity = personMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
    }
}
