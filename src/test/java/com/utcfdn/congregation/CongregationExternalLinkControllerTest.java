package com.utcfdn.congregation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CongregationExternalLinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CongregationRepository congregationRepository;

    @Autowired
    private CongregationExternalLinkService linkService;

    @Autowired
    private ObjectMapper objectMapper;

    private CongregationEntity testCongregation;

    @BeforeEach
    void setUp() {
        testCongregation = congregationRepository.save(CongregationEntity.builder()
                .name("Controller Test Congregation")
                .description("Desc")
                .mission("Mission")
                .build());
    }

    @Test
    void testGetCongregationLinks() throws Exception {
        linkService.createLink(testCongregation.getId(), CongregationExternalLinkEntity.builder()
                .title("Link 1").url("http://1.com").externalLinkType(ExternalLinkType.WEBSITE).build());

        mockMvc.perform(get("/api/congregations/" + testCongregation.getId() + "/links"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].title", is("Link 1")));
    }

    @Test
    void testCreateLink() throws Exception {
        CongregationExternalLinkDto dto = CongregationExternalLinkDto.builder()
                .title("New Link")
                .url("http://newlink.com")
                .externalLinkType(ExternalLinkType.WEBSITE)
                .build();

        mockMvc.perform(post("/api/congregations/" + testCongregation.getId() + "/links")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Link")))
                .andExpect(jsonPath("$.url", is("http://newlink.com")));
    }

    @Test
    void testUpdateLink() throws Exception {
        CongregationExternalLinkEntity existing = linkService.createLink(testCongregation.getId(), 
                CongregationExternalLinkEntity.builder()
                .title("Old Title").url("http://old.com").externalLinkType(ExternalLinkType.WEBSITE).build());

        CongregationExternalLinkDto updateDto = CongregationExternalLinkDto.builder()
                .title("Updated Title")
                .url("http://updated.com")
                .externalLinkType(ExternalLinkType.FACEBOOK)
                .build();

        mockMvc.perform(put("/api/congregations/" + testCongregation.getId() + "/links/" + existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.externalLinkType", is("FACEBOOK")));
    }

    @Test
    void testDeleteLink() throws Exception {
        CongregationExternalLinkEntity existing = linkService.createLink(testCongregation.getId(), 
                CongregationExternalLinkEntity.builder()
                .title("To Delete").url("http://delete.com").externalLinkType(ExternalLinkType.WEBSITE).build());

        mockMvc.perform(delete("/api/congregations/" + testCongregation.getId() + "/links/" + existing.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testReorderLinks() throws Exception {
        CongregationExternalLinkEntity l1 = linkService.createLink(testCongregation.getId(), 
                CongregationExternalLinkEntity.builder().title("L1").url("http://1.com").externalLinkType(ExternalLinkType.WEBSITE).build());
        CongregationExternalLinkEntity l2 = linkService.createLink(testCongregation.getId(), 
                CongregationExternalLinkEntity.builder().title("L2").url("http://2.com").externalLinkType(ExternalLinkType.WEBSITE).build());

        List<Long> newOrder = List.of(l2.getId(), l1.getId());

        mockMvc.perform(put("/api/congregations/" + testCongregation.getId() + "/links/reorder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isNoContent());
    }
}
