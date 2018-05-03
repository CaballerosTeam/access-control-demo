package com.example.access.control.components.person.repo;

import com.example.access.control.components.person.domain.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface PersonRepository extends CrudRepository<Person, Long> {

    Set<Person> findAll();
}
