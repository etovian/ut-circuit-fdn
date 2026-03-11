package com.utcfdn.person;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public List<PersonEntity> findAll() {
        return personRepository.findAllOrdered();
    }

    public Optional<PersonEntity> findById(Long id) {
        return personRepository.findById(id);
    }

    @Transactional
    public PersonEntity save(PersonEntity person) {
        return personRepository.save(person);
    }

    @Transactional
    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }
}
