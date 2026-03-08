package com.utcfdn.congregation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CongregationService {

    private final CongregationRepository congregationRepository;

    @Autowired
    public CongregationService(CongregationRepository congregationRepository) {
        this.congregationRepository = congregationRepository;
    }

    public List<CongregationEntity> getAllCongregations() {
        return congregationRepository.findAllByOrderByNameAsc();
    }

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

    public boolean deleteCongregation(Long id) {
        if (congregationRepository.existsById(id)) {
            congregationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
