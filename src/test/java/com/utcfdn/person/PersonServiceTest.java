package com.utcfdn.person;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(true)
class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Test
    void testSaveAndFindById() {
        PersonEntity person = PersonEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        PersonEntity saved = personService.save(person);
        assertNotNull(saved.getId());

        PersonEntity found = personService.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("John", found.getFirstName());
        assertEquals("Doe", found.getLastName());
    }

    @Test
    void testFindAllOrdered() {
        // Create persons in unsorted order
        personService.save(PersonEntity.builder().firstName("B").lastName("Smith").build());
        personService.save(PersonEntity.builder().firstName("A").lastName("Smith").build());
        personService.save(PersonEntity.builder().firstName("Z").lastName("Adams").build());
        personService.save(PersonEntity.builder().firstName("A").lastName("Adams").build());
        personService.save(PersonEntity.builder().firstName("A").lastName("Adams").suffix("Jr.").build());

        List<PersonEntity> persons = personService.findAll();
        
        // Filter for our test persons to avoid seed data interference
        List<String> actualNames = persons.stream()
                .filter(p -> p.getLastName().equals("Adams") || p.getLastName().equals("Smith"))
                .map(p -> p.getLastName() + ", " + p.getFirstName() + (p.getSuffix() != null ? " " + p.getSuffix() : ""))
                .collect(Collectors.toList());

        List<String> expectedOrder = List.of(
                "Adams, A",
                "Adams, A Jr.",
                "Adams, Z",
                "Smith, A",
                "Smith, B"
        );

        assertEquals(expectedOrder, actualNames);
    }

    @Test
    void testDelete() {
        PersonEntity person = PersonEntity.builder()
                .firstName("Delete")
                .lastName("Me")
                .build();
        PersonEntity saved = personService.save(person);
        Long id = saved.getId();

        personService.deleteById(id);

        assertFalse(personService.findById(id).isPresent());
    }
}
