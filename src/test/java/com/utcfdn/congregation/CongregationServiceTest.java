package com.utcfdn.congregation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Rollback(true)
class CongregationServiceTest {

    @Autowired
    private CongregationService congregationService;

    @Test
    void testFindAllSortedByName() {
        // 1. Create five congregations in unsorted order with unique names to avoid seed data collision
        congregationService.saveCongregation(new CongregationEntity("Test Z", "Description", "Mission"));
        congregationService.saveCongregation(new CongregationEntity("Test M", "Description", "Mission"));
        congregationService.saveCongregation(new CongregationEntity("Test A", "Description", "Mission"));
        congregationService.saveCongregation(new CongregationEntity("Test C", "Description", "Mission"));
        congregationService.saveCongregation(new CongregationEntity("Test B", "Description", "Mission"));

        // 2. Find all congregations (this should call the sorted repository method)
        List<CongregationEntity> congregations = congregationService.getAllCongregations();

        // 3. Extract names and filter for our test names
        List<String> actualSortedNames = congregations.stream()
                .map(CongregationEntity::getName)
                .filter(name -> name.startsWith("Test "))
                .collect(Collectors.toList());

        // 4. Verify sort order
        List<String> expectedOrder = List.of(
                "Test A",
                "Test B",
                "Test C",
                "Test M",
                "Test Z"
        );

        assertEquals(expectedOrder, actualSortedNames, "Test congregations should be sorted by name in ascending order");
    }
}
