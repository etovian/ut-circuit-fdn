package com.utcfdn.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {

    @Query(value = "SELECT * FROM person ORDER BY last_name ASC, first_name ASC, suffix ASC", nativeQuery = true)
    List<PersonEntity> findAllOrdered();
}
