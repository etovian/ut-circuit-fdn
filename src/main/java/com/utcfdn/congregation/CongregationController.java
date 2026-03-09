package com.utcfdn.congregation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/congregations")
@RequiredArgsConstructor
public class CongregationController {

    private final CongregationService congregationService;
    private final CongregationMapper congregationMapper;

    @GetMapping
    public List<CongregationDto> getAll() {
        return congregationService.getAllCongregations().stream()
                .map(congregationMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CongregationDto> getById(@PathVariable Long id) {
        return congregationService.getCongregationById(id)
                .map(congregationMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CongregationDto create(@RequestBody CongregationEntity congregation) {
        return congregationMapper.toDto(congregationService.saveCongregation(congregation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CongregationDto> update(@PathVariable Long id, @RequestBody CongregationEntity congregationDetails) {
        return congregationService.updateCongregation(id, congregationDetails)
                .map(congregationMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return congregationService.deleteCongregation(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
