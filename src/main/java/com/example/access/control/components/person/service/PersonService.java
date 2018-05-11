package com.example.access.control.components.person.service;

import com.example.access.control.components.core.service.CrudService;
import com.example.access.control.components.person.domain.Person;
import com.example.access.control.components.person.repo.PersonRepository;
import com.example.access.control.components.auth.annotations.CreatePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Service
public class PersonService implements CrudService<Person> {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Set<Person> getAll() {

        return personRepository.findAll();
    }

    @Override
    public Person get(@NotNull Long personId) {

        return personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person with ID " + personId + " does not exist"));
    }

    @Override
    @Transactional
    @CreatePermission
    public Person create(Person person) {

        if (person.getId() != null) {
            throw new RuntimeException("Person object must not contain ID");
        }

        return personRepository.save(person);
    }

    @Override
    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'update')")
    public Person update(Person person) {

        Long personId = person.getId();
        if (!exists(personId)) {
            throw new RuntimeException("Person with ID " + personId + " does not exist");
        }

        return personRepository.save(person);
    }

    @Override
    @Transactional
    @PostAuthorize("hasPermission(#personId, 'Person', 'delete')")
    public void remove(Long personId) {

        if (!exists(personId)) {
            throw new RuntimeException("Person with ID " + personId + " does not exist");
        }

        personRepository.deleteById(personId);
    }

    @Override
    public boolean exists(Long personId) {

        return personRepository.existsById(personId);
    }
}
