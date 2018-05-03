package com.example.access.control.components.person.maker;

import com.example.access.control.components.person.domain.Person;
import com.example.access.control.components.person.repo.PersonRepository;
import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class PersonMaker {

    private static final AtomicLong sequence = new AtomicLong();
    private static PersonRepository personRepository;

    public static final Property<Person, String> NAME = Property.newProperty();
    public static final Property<Person, String> EMAIL = Property.newProperty();

    public static final Instantiator<Person> PERSON = propertyLookup -> {

        Long id = sequence.incrementAndGet();

        Person person = Person.builder()
                .name(propertyLookup.valueOf(NAME, "Person #" + id))
                .email(propertyLookup.valueOf(EMAIL, "person_" + id))
                .build();

        return personRepository.save(person);
    };

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        PersonMaker.personRepository = personRepository;
    }
}
