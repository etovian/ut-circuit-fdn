package com.utcfdn.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonContactInfoRepository extends JpaRepository<PersonContactInfoEntity, Long> {
    List<PersonContactInfoEntity> findAllByPersonId(Long personId);
}
