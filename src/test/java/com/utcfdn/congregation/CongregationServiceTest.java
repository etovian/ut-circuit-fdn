package com.utcfdn.congregation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class CongregationServiceTest {

    @Autowired
    private CongregationService congregationService;

    @Test
    void testFindAllSortedByName() {
        // 1. Create five congregations in unsorted order
        congregationService.saveCongregation(new CongregationEntity("St. John", "Description", "Mission"));
        congregationService.saveCongregation(new CongregationEntity("Redeemer", "Description", "Mission"));
        congregationService.saveCongregation(new CongregationEntity("Cross of Christ", "Description", "Mission"));
        congregationService.saveCongregation(new CongregationEntity("Grace", "Description", "Mission"));
        congregationService.saveCongregation(new CongregationEntity("Holy Trinity", "Description", "Mission"));

        // 2. Find all congregations (this should call the sorted repository method)
        List<CongregationEntity> congregations = congregationService.getAllCongregations();

        // 3. Extract names
        List<String> names = congregations.stream()
                .map(CongregationEntity::getName)
                .toList();

        // 4. Verify sort order
        List<String> expectedOrder = List.of(
                "Cross of Christ",
                "Grace",
                "Holy Trinity",
                "Redeemer",
                "St. John"
        );

        // We filter only the ones we just added to avoid interference with any existing data 
        // if @Transactional doesn't isolate from pre-existing data (though it should handle rollback)
        // However, a better approach is to just verify the whole list if we assume a clean DB or 
        // just check that our 5 are there in order relative to each other.
        // Given the requirement, simple assertEquals on the whole list or a sublist is fine.
        
        // Let's filter to just the names we care about to be robust
        List<String> actualSortedNames = names.stream()
                .filter(expectedOrder::contains)
                .collect(Collectors.toList());

        assertEquals(expectedOrder, actualSortedNames, "Congregations should be sorted by name in ascending order");
    }
}
