package com.utcfdn.person;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonContactInfoService {

    private final PersonContactInfoRepository contactInfoRepository;

    public List<PersonContactInfoEntity> findByPersonId(Long personId) {
        return contactInfoRepository.findAllByPersonId(personId);
    }

    public Optional<PersonContactInfoEntity> findById(Long id) {
        return contactInfoRepository.findById(id);
    }

    @Transactional
    public PersonContactInfoEntity save(PersonContactInfoEntity contactInfo) {
        return contactInfoRepository.save(contactInfo);
    }

    @Transactional
    public void deleteById(Long id) {
        contactInfoRepository.deleteById(id);
    }
}
