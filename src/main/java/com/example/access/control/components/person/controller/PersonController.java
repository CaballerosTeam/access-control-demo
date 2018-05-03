package com.example.access.control.components.person.controller;

import com.example.access.control.components.core.controller.CrudController;
import com.example.access.control.components.person.domain.Person;
import com.example.access.control.components.person.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/v1/person")
public class PersonController extends CrudController<Person> {

    @Autowired
    public PersonController(PersonService personService) {
        super(personService);
    }
}
