package com.utcfdn.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PersonContactInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonContactInfoService contactInfoService;

    @Autowired
    private ObjectMapper objectMapper;

    private PersonEntity testPerson;

    @BeforeEach
    void setUp() {
        testPerson = personService.save(PersonEntity.builder()
                .firstName("Test")
                .lastName("Person")
                .build());
    }

    @Test
    void testCreateContactInfo() throws Exception {
        PersonContactInfoDto dto = PersonContactInfoDto.builder()
                .personId(testPerson.getId())
                .contactInfoType(ContactInfoType.EMAIL)
                .contactValue("test@example.com")
                .build();

        mockMvc.perform(post("/api/person-contact-info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.personId", is(testPerson.getId().intValue())))
                .andExpect(jsonPath("$.contactInfoType", is("EMAIL")))
                .andExpect(jsonPath("$.contactValue", is("test@example.com")));
    }

    @Test
    void testGetByPersonId() throws Exception {
        PersonContactInfoEntity entity = contactInfoService.save(PersonContactInfoEntity.builder()
                .person(testPerson)
                .contactInfoType(ContactInfoType.PHONE_NUMBER)
                .contactValue("555-1234")
                .build());

        mockMvc.perform(get("/api/person-contact-info/person/" + testPerson.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].contactValue", is("555-1234")));
    }

    @Test
    void testGetById() throws Exception {
        PersonContactInfoEntity entity = contactInfoService.save(PersonContactInfoEntity.builder()
                .person(testPerson)
                .contactInfoType(ContactInfoType.EMAIL)
                .contactValue("unique@example.com")
                .build());

        mockMvc.perform(get("/api/person-contact-info/" + entity.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contactValue", is("unique@example.com")));
    }

    @Test
    void testUpdateContactInfo() throws Exception {
        PersonContactInfoEntity entity = contactInfoService.save(PersonContactInfoEntity.builder()
                .person(testPerson)
                .contactInfoType(ContactInfoType.EMAIL)
                .contactValue("old@example.com")
                .build());

        PersonContactInfoDto updateDto = PersonContactInfoDto.builder()
                .id(entity.getId())
                .personId(testPerson.getId())
                .contactInfoType(ContactInfoType.EMAIL)
                .contactValue("new@example.com")
                .build();

        mockMvc.perform(put("/api/person-contact-info/" + entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contactValue", is("new@example.com")));
    }

    @Test
    void testDeleteContactInfo() throws Exception {
        PersonContactInfoEntity entity = contactInfoService.save(PersonContactInfoEntity.builder()
                .person(testPerson)
                .contactInfoType(ContactInfoType.PHONE_NUMBER)
                .contactValue("555-9999")
                .build());

        mockMvc.perform(delete("/api/person-contact-info/" + entity.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/person-contact-info/" + entity.getId()))
                .andExpect(status().isNotFound());
    }
}
