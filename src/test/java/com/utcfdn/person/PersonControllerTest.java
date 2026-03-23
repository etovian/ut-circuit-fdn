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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    private PersonEntity testPerson;

    @BeforeEach
    void setUp() {
        testPerson = personService.save(PersonEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .build());
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[*].firstName", hasItem("John")));
    }

    @Test
    void testSearch() throws Exception {
        mockMvc.perform(get("/api/persons/search")
                        .param("firstName", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].firstName", is("John")));
    }

    @Test
    void testGetById() throws Exception {
        mockMvc.perform(get("/api/persons/" + testPerson.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/persons/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate() throws Exception {
        PersonDto dto = PersonDto.builder()
                .firstName("Jane")
                .lastName("Smith")
                .build();

        mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Jane")))
                .andExpect(jsonPath("$.lastName", is("Smith")));
    }

    @Test
    void testUpdate() throws Exception {
        PersonDto dto = PersonDto.builder()
                .firstName("Johnny")
                .lastName("Doe")
                .build();

        mockMvc.perform(put("/api/persons/" + testPerson.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Johnny")));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/persons/" + testPerson.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/persons/" + testPerson.getId()))
                .andExpect(status().isNotFound());
    }
}
