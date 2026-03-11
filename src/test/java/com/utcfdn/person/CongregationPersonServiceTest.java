package com.utcfdn.person;

import com.utcfdn.congregation.CongregationEntity;
import com.utcfdn.congregation.CongregationRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(true)
class CongregationPersonServiceTest {

    @Autowired
    private CongregationPersonService congregationPersonService;

    @Autowired
    private CongregationRepository congregationRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CongregationPersonRepository congregationPersonRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testAddPersonToCongregation() {
        // Create a congregation
        CongregationEntity congregation = congregationRepository.save(new CongregationEntity("Test Congregation", "Desc", "Mission"));
        
        // Create person details
        PersonEntity person = PersonEntity.builder()
                .firstName("Jane")
                .lastName("Doe")
                .build();

        // Add person to congregation
        CongregationPersonEntity relation = congregationPersonService.addPersonToCongregation(
                congregation.getId(), person, "Pastor");

        assertNotNull(relation);
        assertEquals("Pastor", relation.getPosition());
        assertEquals(congregation.getId(), relation.getCongregation().getId());
        assertNotNull(relation.getPerson().getId());

        // Verify in DB
        assertTrue(congregationPersonRepository.findById(relation.getId()).isPresent());
    }

    @Test
    void testUpdatePosition() {
        CongregationEntity congregation = congregationRepository.save(new CongregationEntity("Test Congregation", "Desc", "Mission"));
        PersonEntity person = personRepository.save(PersonEntity.builder().firstName("Jane").lastName("Doe").build());
        
        congregationPersonService.addPersonToCongregation(congregation.getId(), person, "Elder");
        
        CongregationPersonEntity updated = congregationPersonService.updatePersonPosition(congregation.getId(), person.getId(), "Lead Elder");
        
        assertEquals("Lead Elder", updated.getPosition());
    }

    @Test
    void testRemoveFromCongregation() {
        CongregationEntity congregation = congregationRepository.save(new CongregationEntity("Test Congregation", "Desc", "Mission"));
        PersonEntity person = personRepository.save(PersonEntity.builder().firstName("Jane").lastName("Doe").build());
        
        congregationPersonService.addPersonToCongregation(congregation.getId(), person, "Elder");
        
        congregationPersonService.removePersonFromCongregation(congregation.getId(), person.getId());
        
        assertFalse(congregationPersonRepository.findById(new CongregationPersonId(congregation.getId(), person.getId())).isPresent());
        // Person should still exist
        assertTrue(personRepository.findById(person.getId()).isPresent());
    }

    @Test
    void testCascadingDeletePerson() {
        CongregationEntity congregation = congregationRepository.save(new CongregationEntity("Test Congregation", "Desc", "Mission"));
        PersonEntity person = personRepository.save(PersonEntity.builder().firstName("Jane").lastName("Doe").build());
        
        congregationPersonService.addPersonToCongregation(congregation.getId(), person, "Elder");
        
        // Ensure everything is written to DB and persistence context is cleared
        entityManager.flush();
        entityManager.clear();

        // Relationship should exist
        assertTrue(congregationPersonRepository.findById(new CongregationPersonId(congregation.getId(), person.getId())).isPresent());

        // Delete person via repository
        personRepository.deleteById(person.getId());
        
        // Ensure delete is propagated
        entityManager.flush();
        entityManager.clear();

        // Relationship should be gone due to cascade
        assertFalse(congregationPersonRepository.findById(new CongregationPersonId(congregation.getId(), person.getId())).isPresent());
    }
}
