package com.utcfdn.person;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/congregations/{congregationId}/persons")
@RequiredArgsConstructor
public class CongregationPersonController {

    private final CongregationPersonService congregationPersonService;
    private final PersonMapper personMapper;

    @GetMapping
    public List<PersonRelationDto> getCongregationPersons(@PathVariable Long congregationId) {
        return congregationPersonService.findAllByCongregationIdOrdered(congregationId).stream()
                .map(personMapper::toPersonRelationDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public PersonRelationDto addPersonToCongregation(@PathVariable Long congregationId, @RequestBody CongregationPersonRequest request) {
        return personMapper.toPersonRelationDto(congregationPersonService.addPersonToCongregation(congregationId, request.getPersonId(), request.getPosition()));
    }

    @PutMapping("/{personId}")
    public PersonRelationDto updatePersonPosition(@PathVariable Long congregationId, @PathVariable Long personId, @RequestBody String position) {
        return personMapper.toPersonRelationDto(congregationPersonService.updatePersonPosition(congregationId, personId, position));
    }

    @PutMapping("/reorder")
    public ResponseEntity<Void> reorderPersons(@PathVariable Long congregationId, @RequestBody List<Long> personIds) {
        congregationPersonService.reorderPersons(congregationId, personIds);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{personId}")
    public ResponseEntity<Void> removePersonFromCongregation(@PathVariable Long congregationId, @PathVariable Long personId) {
        congregationPersonService.removePersonFromCongregation(congregationId, personId);
        return ResponseEntity.noContent().build();
    }
}
