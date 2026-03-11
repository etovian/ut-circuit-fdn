package com.utcfdn.person;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/congregations/{congregationId}/persons")
@RequiredArgsConstructor
public class CongregationPersonController {

    private final CongregationPersonService congregationPersonService;
    private final PersonMapper personMapper;

    @PostMapping
    public PersonRelationDto addPersonToCongregation(@PathVariable Long congregationId, @RequestBody CongregationPersonRequest request) {
        PersonEntity personEntity = personMapper.toEntity(request.getPerson());
        return personMapper.toPersonRelationDto(congregationPersonService.addPersonToCongregation(congregationId, personEntity, request.getPosition()));
    }

    @PutMapping("/{personId}")
    public PersonRelationDto updatePersonPosition(@PathVariable Long congregationId, @PathVariable Long personId, @RequestBody String position) {
        return personMapper.toPersonRelationDto(congregationPersonService.updatePersonPosition(congregationId, personId, position));
    }

    @DeleteMapping("/{personId}")
    public ResponseEntity<Void> removePersonFromCongregation(@PathVariable Long congregationId, @PathVariable Long personId) {
        congregationPersonService.removePersonFromCongregation(congregationId, personId);
        return ResponseEntity.noContent().build();
    }
}
