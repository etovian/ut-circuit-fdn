package com.utcfdn.congregation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CongregationExternalLinkServiceTest {

    @Autowired
    private CongregationExternalLinkService linkService;

    @Autowired
    private CongregationRepository congregationRepository;

    @Autowired
    private CongregationExternalLinkRepository linkRepository;

    private CongregationEntity testCongregation;

    @BeforeEach
    void setUp() {
        testCongregation = congregationRepository.save(CongregationEntity.builder()
                .name("Test Congregation for Links")
                .description("Test Description")
                .mission("Test Mission")
                .build());
    }

    @Test
    void testCreateLinkIncrementsOrdinal() {
        CongregationExternalLinkEntity link1 = CongregationExternalLinkEntity.builder()
                .title("Link 1")
                .url("http://link1.com")
                .externalLinkType(ExternalLinkType.WEBSITE)
                .build();
        
        CongregationExternalLinkEntity created1 = linkService.createLink(testCongregation.getId(), link1);
        assertEquals(0, created1.getOrdinalValue());

        CongregationExternalLinkEntity link2 = CongregationExternalLinkEntity.builder()
                .title("Link 2")
                .url("http://link2.com")
                .externalLinkType(ExternalLinkType.FACEBOOK)
                .build();
        
        CongregationExternalLinkEntity created2 = linkService.createLink(testCongregation.getId(), link2);
        assertEquals(1, created2.getOrdinalValue());
    }

    @Test
    void testFindAllByCongregationId() {
        linkService.createLink(testCongregation.getId(), CongregationExternalLinkEntity.builder()
                .title("Link 1").url("http://1.com").externalLinkType(ExternalLinkType.WEBSITE).build());
        linkService.createLink(testCongregation.getId(), CongregationExternalLinkEntity.builder()
                .title("Link 2").url("http://2.com").externalLinkType(ExternalLinkType.WEBSITE).build());

        List<CongregationExternalLinkEntity> links = linkService.findAllByCongregationId(testCongregation.getId());
        assertEquals(2, links.size());
    }

    @Test
    void testUpdateLink() {
        CongregationExternalLinkEntity link = linkService.createLink(testCongregation.getId(), 
                CongregationExternalLinkEntity.builder()
                .title("Old Title")
                .url("http://old.com")
                .externalLinkType(ExternalLinkType.WEBSITE)
                .build());

        CongregationExternalLinkEntity updateRequest = CongregationExternalLinkEntity.builder()
                .title("New Title")
                .url("http://new.com")
                .externalLinkType(ExternalLinkType.YOUTUBE)
                .build();

        CongregationExternalLinkEntity updated = linkService.updateLink(link.getId(), updateRequest);

        assertEquals("New Title", updated.getTitle());
        assertEquals("http://new.com", updated.getUrl());
        assertEquals(ExternalLinkType.YOUTUBE, updated.getExternalLinkType());
    }

    @Test
    void testDeleteLink() {
        CongregationExternalLinkEntity link = linkService.createLink(testCongregation.getId(), 
                CongregationExternalLinkEntity.builder()
                .title("To Delete")
                .url("http://delete.com")
                .externalLinkType(ExternalLinkType.WEBSITE)
                .build());

        Long id = link.getId();
        linkService.deleteLink(id);

        assertFalse(linkRepository.findById(id).isPresent());
    }

    @Test
    void testReorderLinks() {
        CongregationExternalLinkEntity l1 = linkService.createLink(testCongregation.getId(), 
                CongregationExternalLinkEntity.builder().title("L1").url("http://1.com").externalLinkType(ExternalLinkType.WEBSITE).build());
        CongregationExternalLinkEntity l2 = linkService.createLink(testCongregation.getId(), 
                CongregationExternalLinkEntity.builder().title("L2").url("http://2.com").externalLinkType(ExternalLinkType.WEBSITE).build());
        CongregationExternalLinkEntity l3 = linkService.createLink(testCongregation.getId(), 
                CongregationExternalLinkEntity.builder().title("L3").url("http://3.com").externalLinkType(ExternalLinkType.WEBSITE).build());

        // Initial ordinals should be 0, 1, 2
        assertEquals(0, l1.getOrdinalValue());
        assertEquals(1, l2.getOrdinalValue());
        assertEquals(2, l3.getOrdinalValue());

        // Reorder to L3, L1, L2
        linkService.reorderLinks(testCongregation.getId(), List.of(l3.getId(), l1.getId(), l2.getId()));

        CongregationExternalLinkEntity updatedL1 = linkRepository.findById(l1.getId()).orElseThrow();
        CongregationExternalLinkEntity updatedL2 = linkRepository.findById(l2.getId()).orElseThrow();
        CongregationExternalLinkEntity updatedL3 = linkRepository.findById(l3.getId()).orElseThrow();

        assertEquals(1, updatedL1.getOrdinalValue());
        assertEquals(2, updatedL2.getOrdinalValue());
        assertEquals(0, updatedL3.getOrdinalValue());
    }

    @Test
    void testReorderLinksValidation() {
        CongregationEntity otherCongregation = congregationRepository.save(CongregationEntity.builder()
                .name("Other Congregation")
                .build());
        
        CongregationExternalLinkEntity otherLink = linkService.createLink(otherCongregation.getId(), 
                CongregationExternalLinkEntity.builder().title("Other").url("http://other.com").externalLinkType(ExternalLinkType.WEBSITE).build());

        assertThrows(IllegalArgumentException.class, () -> {
            linkService.reorderLinks(testCongregation.getId(), List.of(otherLink.getId()));
        });
    }
}
