package com.utcfdn.congregation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CongregationService {

    private final CongregationRepository congregationRepository;

    @Transactional(readOnly = true)
    public List<CongregationEntity> getAllCongregations() {
        return congregationRepository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Optional<CongregationEntity> getCongregationById(Long id) {
        return congregationRepository.findById(id);
    }

    public CongregationEntity saveCongregation(CongregationEntity congregation) {
        return congregationRepository.save(congregation);
    }

    public Optional<CongregationEntity> updateCongregation(Long id, CongregationEntity details) {
        return congregationRepository.findById(id)
                .map(existing -> {
                    existing.setName(details.getName());
                    existing.setDescription(details.getDescription());
                    existing.setMission(details.getMission());
                    return congregationRepository.save(existing);
                });
    }

    public Optional<CongregationEntity> updateDescription(Long id, String description) {
        return congregationRepository.findById(id)
                .map(existing -> {
                    existing.setDescription(description);
                    return congregationRepository.save(existing);
                });
    }

    public boolean deleteCongregation(Long id) {
        if (congregationRepository.existsById(id)) {
            congregationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
