package com.utcfdn.congregation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CongregationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CongregationService congregationService;

    @Test
    void testGetAllCongregations() throws Exception {
        // Seed some data
        congregationService.saveCongregation(new CongregationEntity("Test Congregation", "Desc", "Mission"));

        mockMvc.perform(get("/api/congregations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].name", notNullValue()))
                .andExpect(jsonPath("$[0].addresses", notNullValue()));
    }

    @Test
    void testUpdateDescription() throws Exception {
        // Seed some data
        CongregationEntity entity = congregationService.saveCongregation(new CongregationEntity("Update Test", "Old Desc", "Mission"));
        Long id = entity.getId();

        String newDescription = "New Updated Description";

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/congregations/" + id + "/description")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(newDescription))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(newDescription)));

        // Verify in database
        mockMvc.perform(get("/api/congregations/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(newDescription)));
    }
}
