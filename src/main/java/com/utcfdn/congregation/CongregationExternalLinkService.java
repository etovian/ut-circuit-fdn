package com.utcfdn.congregation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CongregationExternalLinkService {

    private final CongregationExternalLinkRepository congregationExternalLinkRepository;
    private final CongregationRepository congregationRepository;

    @Transactional(readOnly = true)
    public List<CongregationExternalLinkEntity> findAllByCongregationId(Long congregationId) {
        return congregationExternalLinkRepository.findAllByCongregationId(congregationId);
    }

    @Transactional
    public CongregationExternalLinkEntity createLink(Long congregationId, CongregationExternalLinkEntity entity) {
        CongregationEntity congregation = congregationRepository.findById(congregationId)
                .orElseThrow(() -> new IllegalArgumentException("Congregation not found with id: " + congregationId));
        
        entity.setCongregation(congregation);
        
        if (entity.getOrdinalValue() == null || entity.getOrdinalValue() == 0) {
            Integer maxOrdinal = congregationExternalLinkRepository.findMaxOrdinalValueByCongregationId(congregationId);
            entity.setOrdinalValue(maxOrdinal + 1);
        }
        
        return congregationExternalLinkRepository.save(entity);
    }

    @Transactional
    public CongregationExternalLinkEntity updateLink(Long id, CongregationExternalLinkEntity updatedEntity) {
        CongregationExternalLinkEntity existing = congregationExternalLinkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Link not found with id: " + id));
        
        existing.setTitle(updatedEntity.getTitle());
        existing.setDescription(updatedEntity.getDescription());
        existing.setExternalLinkType(updatedEntity.getExternalLinkType());
        existing.setUrl(updatedEntity.getUrl());
        if (updatedEntity.getOrdinalValue() != null) {
            existing.setOrdinalValue(updatedEntity.getOrdinalValue());
        }
        
        return congregationExternalLinkRepository.save(existing);
    }

    @Transactional
    public void deleteLink(Long id) {
        congregationExternalLinkRepository.deleteById(id);
    }

    @Transactional
    public void reorderLinks(Long congregationId, List<Long> linkIds) {
        for (int i = 0; i < linkIds.size(); i++) {
            Long linkId = linkIds.get(i);
            CongregationExternalLinkEntity link = congregationExternalLinkRepository.findById(linkId)
                    .orElseThrow(() -> new IllegalArgumentException("Link not found with id: " + linkId));
            
            if (!link.getCongregation().getId().equals(congregationId)) {
                throw new IllegalArgumentException("Link " + linkId + " does not belong to congregation " + congregationId);
            }
            
            link.setOrdinalValue(i);
            congregationExternalLinkRepository.save(link);
        }
    }
}
