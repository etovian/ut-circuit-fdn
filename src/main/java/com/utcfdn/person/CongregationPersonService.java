package com.utcfdn.person;

import com.utcfdn.congregation.CongregationEntity;
import com.utcfdn.congregation.CongregationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CongregationPersonService {

    private final CongregationPersonRepository congregationPersonRepository;
    private final PersonRepository personRepository;
    private final CongregationRepository congregationRepository;

    @Transactional
    public CongregationPersonEntity addPersonToCongregation(Long congregationId, PersonEntity person, String position) {
        CongregationEntity congregation = congregationRepository.findById(congregationId)
                .orElseThrow(() -> new IllegalArgumentException("Congregation not found with id: " + congregationId));

        PersonEntity savedPerson = personRepository.save(person);

        CongregationPersonId id = new CongregationPersonId(congregationId, savedPerson.getId());
        CongregationPersonEntity relation = CongregationPersonEntity.builder()
                .id(id)
                .congregation(congregation)
                .person(savedPerson)
                .position(position)
                .build();

        return congregationPersonRepository.save(relation);
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
