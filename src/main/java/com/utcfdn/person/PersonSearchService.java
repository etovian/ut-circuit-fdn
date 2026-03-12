package com.utcfdn.person;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonSearchService {

    private final PersonRepository personRepository;

    public List<PersonEntity> search(String firstName, String lastName) {
        PersonEntity person = PersonEntity.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues();

        Example<PersonEntity> example = Example.of(person, matcher);
        return personRepository.findAll(example);
    }
}
