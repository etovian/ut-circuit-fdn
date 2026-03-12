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
        PersonEntity savedPerson = personRepository.save(person);
        CongregationPersonEntity relation = congregationPersonService.addPersonToCongregation(
                congregation.getId(), savedPerson.getId(), "Pastor");

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
        
        congregationPersonService.addPersonToCongregation(congregation.getId(), person.getId(), "Elder");
        
        CongregationPersonEntity updated = congregationPersonService.updatePersonPosition(congregation.getId(), person.getId(), "Lead Elder");
        
        assertEquals("Lead Elder", updated.getPosition());
    }

    @Test
    void testRemoveFromCongregation() {
        CongregationEntity congregation = congregationRepository.save(new CongregationEntity("Test Congregation", "Desc", "Mission"));
        PersonEntity person = personRepository.save(PersonEntity.builder().firstName("Jane").lastName("Doe").build());
        
        congregationPersonService.addPersonToCongregation(congregation.getId(), person.getId(), "Elder");
        
        congregationPersonService.removePersonFromCongregation(congregation.getId(), person.getId());
        
        assertFalse(congregationPersonRepository.findById(new CongregationPersonId(congregation.getId(), person.getId())).isPresent());
        // Person should still exist
        assertTrue(personRepository.findById(person.getId()).isPresent());
    }

    @Test
    void testCascadingDeletePerson() {
        CongregationEntity congregation = congregationRepository.save(new CongregationEntity("Test Congregation", "Desc", "Mission"));
        PersonEntity person = personRepository.save(PersonEntity.builder().firstName("Jane").lastName("Doe").build());
        
        congregationPersonService.addPersonToCongregation(congregation.getId(), person.getId(), "Elder");
        
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

    @Test
    void testAddExistingPersonToDifferentCongregationDoesNotOrphanOriginal() {
        // 1. Setup two congregations
        CongregationEntity congregation1 = congregationRepository.save(new CongregationEntity("Congregation One", "Desc 1", "Mission 1"));
        CongregationEntity congregation2 = congregationRepository.save(new CongregationEntity("Congregation Two", "Desc 2", "Mission 2"));
        
        // 2. Create a person and associate with the first congregation
        PersonEntity person = personRepository.save(PersonEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .biography("Original Bio")
                .build());
        
        congregationPersonService.addPersonToCongregation(congregation1.getId(), person.getId(), "Pastor");
        
        entityManager.flush();
        entityManager.clear();
        
        // Verify initial setup
        PersonEntity savedPerson = personRepository.findAll().stream()
                .filter(p -> p.getFirstName().equals("John"))
                .findFirst().orElseThrow();
        Long personId = savedPerson.getId();
        
        assertEquals(1, congregationPersonRepository.findAll().stream()
                .filter(cp -> cp.getPerson().getId().equals(personId)).count());

        // 3. Update the person's info and associate with the SECOND congregation
        // This simulates the frontend sending the person with their ID back to the server
        PersonEntity personUpdate = PersonEntity.builder()
                .id(personId)
                .firstName("John")
                .lastName("Doe")
                .biography("Updated Bio")
                .build();
        personRepository.save(personUpdate);
        
        congregationPersonService.addPersonToCongregation(congregation2.getId(), personUpdate.getId(), "Guest Speaker");
        
        entityManager.flush();
        entityManager.clear();

        // 4. VERIFY: The person should now be associated with BOTH congregations
        PersonEntity finalPerson = personRepository.findById(personId).orElseThrow();
        assertEquals("Updated Bio", finalPerson.getBiography());
        
        long relationshipCount = congregationPersonRepository.findAll().stream()
                .filter(cp -> cp.getPerson().getId().equals(personId))
                .count();
        
        assertEquals(2, relationshipCount, "Person should have 2 congregation relationships, not 1");
        
        assertTrue(congregationPersonRepository.findById(new CongregationPersonId(congregation1.getId(), personId)).isPresent(), 
                "Original relationship to Congregation 1 should still exist");
        assertTrue(congregationPersonRepository.findById(new CongregationPersonId(congregation2.getId(), personId)).isPresent(), 
                "New relationship to Congregation 2 should exist");
    }
}
