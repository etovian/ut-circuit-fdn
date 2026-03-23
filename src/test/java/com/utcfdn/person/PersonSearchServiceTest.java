package com.utcfdn.person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class PersonSearchServiceTest {

    @Autowired
    private PersonSearchService personSearchService;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository.save(PersonEntity.builder().firstName("Martin").lastName("Luther").build());
        personRepository.save(PersonEntity.builder().firstName("Philip").lastName("Melanchthon").build());
        personRepository.save(PersonEntity.builder().firstName("Johann").lastName("Gerhard").build());
    }

    @Test
    void testSearchByFirstName() {
        List<PersonEntity> results = personSearchService.search("Martin", null);
        assertEquals(1, results.size());
        assertEquals("Luther", results.get(0).getLastName());
    }

    @Test
    void testSearchByLastName() {
        List<PersonEntity> results = personSearchService.search(null, "Melanchthon");
        assertEquals(1, results.size());
        assertEquals("Philip", results.get(0).getFirstName());
    }

    @Test
    void testSearchByPartialName() {
        List<PersonEntity> results = personSearchService.search("Mar", null);
        assertEquals(1, results.size());
        assertEquals("Martin", results.get(0).getFirstName());
    }

    @Test
    void testSearchCaseInsensitive() {
        List<PersonEntity> results = personSearchService.search("martin", null);
        assertEquals(1, results.size());
        assertEquals("Martin", results.get(0).getFirstName());
    }

    @Test
    void testSearchMatchingAny() {
        // "Martin" matches Martin Luther's first name
        // "Gerhard" matches Johann Gerhard's last name
        List<PersonEntity> results = personSearchService.search("Martin", "Gerhard");
        // matchingAny() means it should return both Johann Gerhard and Martin Luther
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(p -> p.getLastName().equals("Gerhard")));
        assertTrue(results.stream().anyMatch(p -> p.getLastName().equals("Luther")));
    }
}
