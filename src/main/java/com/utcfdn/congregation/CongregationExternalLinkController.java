package com.utcfdn.congregation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/congregations/{congregationId}/links")
@RequiredArgsConstructor
public class CongregationExternalLinkController {

    private final CongregationExternalLinkService congregationExternalLinkService;
    private final CongregationExternalLinkMapper congregationExternalLinkMapper;

    @GetMapping
    public List<CongregationExternalLinkDto> getCongregationLinks(@PathVariable Long congregationId) {
        return congregationExternalLinkMapper.toDtoList(
                congregationExternalLinkService.findAllByCongregationId(congregationId));
    }

    @PostMapping
    public CongregationExternalLinkDto createLink(@PathVariable Long congregationId, 
                                                 @RequestBody CongregationExternalLinkDto dto) {
        CongregationExternalLinkEntity entity = congregationExternalLinkMapper.toEntity(dto, null);
        return congregationExternalLinkMapper.toDto(
                congregationExternalLinkService.createLink(congregationId, entity));
    }

    @PutMapping("/{linkId}")
    public CongregationExternalLinkDto updateLink(@PathVariable Long linkId,
                                                 @RequestBody CongregationExternalLinkDto dto) {
        CongregationExternalLinkEntity entity = congregationExternalLinkMapper.toEntity(dto, null);
        return congregationExternalLinkMapper.toDto(
                congregationExternalLinkService.updateLink(linkId, entity));
    }

    @DeleteMapping("/{linkId}")
    public ResponseEntity<Void> deleteLink(@PathVariable Long linkId) {
        congregationExternalLinkService.deleteLink(linkId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reorder")
    public ResponseEntity<Void> reorderLinks(@PathVariable Long congregationId, @RequestBody List<Long> linkIds) {
        congregationExternalLinkService.reorderLinks(congregationId, linkIds);
        return ResponseEntity.noContent().build();
    }
}
