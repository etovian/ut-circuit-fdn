package com.utcfdn.congregation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/congregations")
@RequiredArgsConstructor
public class CongregationController {

    private final CongregationService congregationService;

    @GetMapping
    public List<CongregationEntity> getAll() {
        return congregationService.getAllCongregations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CongregationEntity> getById(@PathVariable Long id) {
        return congregationService.getCongregationById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CongregationEntity create(@RequestBody CongregationEntity congregation) {
        return congregationService.saveCongregation(congregation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CongregationEntity> update(@PathVariable Long id, @RequestBody CongregationEntity congregationDetails) {
        return congregationService.updateCongregation(id, congregationDetails).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return congregationService.deleteCongregation(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
