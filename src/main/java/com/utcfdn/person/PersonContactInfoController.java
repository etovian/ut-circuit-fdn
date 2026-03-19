package com.utcfdn.person;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/person-contact-info")
@RequiredArgsConstructor
public class PersonContactInfoController {

    private final PersonContactInfoService contactInfoService;
    private final PersonContactInfoMapper contactInfoMapper;

    @GetMapping("/person/{personId}")
    public List<PersonContactInfoDto> getByPersonId(@PathVariable Long personId) {
        return contactInfoService.findByPersonId(personId).stream()
                .map(contactInfoMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonContactInfoDto> getById(@PathVariable Long id) {
        return contactInfoService.findById(id)
                .map(contactInfoMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PersonContactInfoDto create(@RequestBody PersonContactInfoDto contactInfoDto) {
        PersonContactInfoEntity entity = contactInfoMapper.toEntity(contactInfoDto);
        return contactInfoMapper.toDto(contactInfoService.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonContactInfoDto> update(@PathVariable Long id, @RequestBody PersonContactInfoDto contactInfoDto) {
        return contactInfoService.findById(id)
                .map(existing -> {
                    PersonContactInfoEntity updatedEntity = contactInfoMapper.toEntity(contactInfoDto);
                    updatedEntity.setId(id);
                    return ResponseEntity.ok(contactInfoMapper.toDto(contactInfoService.save(updatedEntity)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (contactInfoService.findById(id).isPresent()) {
            contactInfoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
