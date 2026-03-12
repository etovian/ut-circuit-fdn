package com.utcfdn.person;

import com.utcfdn.congregation.CongregationEntity;
import com.utcfdn.congregation.CongregationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CongregationPersonService {

    private final CongregationPersonRepository congregationPersonRepository;
    private final PersonRepository personRepository;
    private final CongregationRepository congregationRepository;

    @Transactional
    public CongregationPersonEntity addPersonToCongregation(Long congregationId, Long personId, String position) {
        CongregationEntity congregation = congregationRepository.findById(congregationId)
                .orElseThrow(() -> new IllegalArgumentException("Congregation not found with id: " + congregationId));
        PersonEntity person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person not found with id: " + personId));

        CongregationPersonId id = new CongregationPersonId(congregationId, personId);
        CongregationPersonEntity relation = congregationPersonRepository.findById(id)
                .orElseGet(() -> CongregationPersonEntity.builder()
                        .id(id)
                        .congregation(congregation)
                        .person(person)
                        .sortOrdinalValue(0)
                        .build());

        relation.setPosition(position);
        return congregationPersonRepository.save(relation);
    }

    @Transactional
    public void reorderPersons(Long congregationId, List<Long> personIds) {
        for (int i = 0; i < personIds.size(); i++) {
            Long personId = personIds.get(i);
            CongregationPersonId id = new CongregationPersonId(congregationId, personId);
            CongregationPersonEntity relation = congregationPersonRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Relationship not found for congregation " + congregationId + " and person " + personId));
            relation.setSortOrdinalValue(i);
            congregationPersonRepository.save(relation);
        }
    }

    @Transactional
    public CongregationPersonEntity updatePersonPosition(Long congregationId, Long personId, String position) {
        CongregationPersonId id = new CongregationPersonId(congregationId, personId);
        CongregationPersonEntity relation = congregationPersonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Relationship not found for congregation " + congregationId + " and person " + personId));

        relation.setPosition(position);
        return congregationPersonRepository.save(relation);
    }

    @Transactional
    public void removePersonFromCongregation(Long congregationId, Long personId) {
        CongregationPersonId id = new CongregationPersonId(congregationId, personId);
        congregationPersonRepository.deleteById(id);
    }
}
