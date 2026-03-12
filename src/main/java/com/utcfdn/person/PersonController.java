package com.utcfdn.person;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final PersonSearchService personSearchService;
    private final PersonMapper personMapper;

    @GetMapping
    public List<PersonDto> getAll() {
        return personService.findAll().stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<PersonDto> search(@RequestParam(required = false) String firstName,
                                 @RequestParam(required = false) String lastName) {
        return personSearchService.search(firstName, lastName).stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getById(@PathVariable Long id) {
        return personService.findById(id)
                .map(personMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PersonDto create(@RequestBody PersonDto personDto) {
        PersonEntity entity = personMapper.toEntity(personDto);
        return personMapper.toDto(personService.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> update(@PathVariable Long id, @RequestBody PersonDto personDto) {
        return personService.findById(id)
                .map(existingPerson -> {
                    PersonEntity updatedEntity = personMapper.toEntity(personDto);
                    updatedEntity.setId(id); // Ensure ID is preserved
                    return ResponseEntity.ok(personMapper.toDto(personService.save(updatedEntity)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (personService.findById(id).isPresent()) {
            personService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
