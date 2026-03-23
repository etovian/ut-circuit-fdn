package com.utcfdn.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utcfdn.congregation.CongregationEntity;
import com.utcfdn.congregation.CongregationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CongregationPersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CongregationRepository congregationRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CongregationPersonService congregationPersonService;

    @Autowired
    private ObjectMapper objectMapper;

    private CongregationEntity testCongregation;
    private PersonEntity testPerson;

    @BeforeEach
    void setUp() {
        testCongregation = congregationRepository.save(CongregationEntity.builder()
                .name("CP Test Congregation")
                .build());
        
        testPerson = personRepository.save(PersonEntity.builder()
                .firstName("Test")
                .lastName("Person")
                .build());
    }

    @Test
    void testGetCongregationPersons() throws Exception {
        congregationPersonService.addPersonToCongregation(testCongregation.getId(), testPerson.getId(), "Pastor");

        mockMvc.perform(get("/api/congregations/" + testCongregation.getId() + "/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("Test")))
                .andExpect(jsonPath("$[0].position", is("Pastor")));
    }

    @Test
    void testAddPersonToCongregation() throws Exception {
        CongregationPersonRequest request = CongregationPersonRequest.builder()
                .personId(testPerson.getId())
                .position("Elder")
                .build();

        mockMvc.perform(post("/api/congregations/" + testCongregation.getId() + "/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.position", is("Elder")));
    }

    @Test
    void testUpdatePersonPosition() throws Exception {
        congregationPersonService.addPersonToCongregation(testCongregation.getId(), testPerson.getId(), "Elder");

        mockMvc.perform(put("/api/congregations/" + testCongregation.getId() + "/persons/" + testPerson.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("New Position"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position", is("New Position")));
    }

    @Test
    void testRemovePersonFromCongregation() throws Exception {
        congregationPersonService.addPersonToCongregation(testCongregation.getId(), testPerson.getId(), "Elder");

        mockMvc.perform(delete("/api/congregations/" + testCongregation.getId() + "/persons/" + testPerson.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/congregations/" + testCongregation.getId() + "/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testReorderPersons() throws Exception {
        PersonEntity testPerson2 = personRepository.save(PersonEntity.builder().firstName("P2").lastName("L2").build());
        congregationPersonService.addPersonToCongregation(testCongregation.getId(), testPerson.getId(), "Pos 1");
        congregationPersonService.addPersonToCongregation(testCongregation.getId(), testPerson2.getId(), "Pos 2");

        List<Long> newOrder = List.of(testPerson2.getId(), testPerson.getId());

        mockMvc.perform(put("/api/congregations/" + testCongregation.getId() + "/persons/reorder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isNoContent());
    }
}
