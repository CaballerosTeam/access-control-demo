package com.example.access.control.components.person.controller;

import com.example.access.control.AccessControlDemoApplication;
import com.example.access.control.components.person.domain.Person;
import com.example.access.control.components.person.service.PersonService;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static com.example.access.control.TestHelper.getRequest;
import static com.example.access.control.TestHelper.prepareRequest;
import static com.example.access.control.components.person.maker.PersonMaker.PERSON;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = AccessControlDemoApplication.class,
        properties = "spring.config.name=application"
)
public class PersonControllerTest {

    @LocalServerPort
    private int localServerPort;

    @Autowired
    PersonService personService;

    private Person person;
    private final String baseUrl = "/v1/person";

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {

        RestAssured.port = localServerPort;

        person = make(a(PERSON));
    }

    @Test
    public void getAll() {

        getRequest(baseUrl)
                .then()
                .statusCode(SC_OK)
                .body("collect { it.email }", hasItem(person.getEmail()));
    }

    @Test
    public void get() {

        getRequest(baseUrl + "/" + person.getId())
                .then()
                .statusCode(SC_OK)
                .body("email", equalTo(person.getEmail()))
                .body("name", equalTo(person.getName()));
    }

    @Test
    public void create() {

        String personName = "Homer J. Simpson";
        String personEmail = "homer@simplson.com";
        Person newPerson = Person.builder()
                .name(personName)
                .email(personEmail)
                .build();

        prepareRequest(newPerson)
                .statusCode(SC_CREATED)
                .when()
                .post(baseUrl);

        Set<Person> people = personService.getAll();
        assertThat("Looks like a person was not created", people, hasItem(newPerson));
    }

    @Test
    public void update() {

        String newEmail = "homer.s@example.com";
        person.setEmail(newEmail);

        prepareRequest(person)
                .statusCode(SC_NO_CONTENT)
                .when()
                .put(baseUrl);

        Person actualPerson = personService.get(person.getId());
        assertEquals("Looks like a person was not updated", person, actualPerson);
    }

    @Test
    public void delete() {

        prepareRequest()
                .statusCode(SC_NO_CONTENT)
                .when()
                .delete(baseUrl + "/" + person.getId());

        Set<Person> people = personService.getAll();
        assertThat("Looks like a person was not removed", people, not(hasItem(person)));
    }
}
