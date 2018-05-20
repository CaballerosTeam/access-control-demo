package com.example.access.control.components.person.controller;

import com.example.access.control.AccessControlDemoApplication;
import com.example.access.control.components.auth.domain.ContentType;
import com.example.access.control.components.auth.domain.Permission;
import com.example.access.control.components.auth.domain.SystemUser;
import com.example.access.control.components.auth.domain.SystemUserAuthority;
import com.example.access.control.components.auth.service.SystemUserDetailsService;
import com.example.access.control.components.person.domain.Person;
import com.example.access.control.components.person.service.PersonService;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.access.control.TestHelper.*;
import static com.example.access.control.components.person.maker.PersonMaker.PERSON;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static junit.framework.TestCase.assertEquals;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = AccessControlDemoApplication.class,
        properties = "spring.config.name=application"
)
@ActiveProfiles("test")
public class PersonControllerTest {

    @LocalServerPort
    private int localServerPort;

    @Autowired
    PersonService personService;

    @Autowired
    SystemUserDetailsService systemUserDetailsService;

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

        getRequest(SIMPLE_USER_NAME, baseUrl)
                .then()
                .statusCode(SC_OK)
                .body("collect { it.email }", hasItem(person.getEmail()));
    }

    @Test
    public void get() {

        getRequest(SIMPLE_USER_NAME,baseUrl + "/" + person.getId())
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

        prepareRequest(CREATE_USER_NAME, newPerson)
                .statusCode(SC_CREATED)
                .when()
                .post(baseUrl);

        Set<Person> people = personService.getAll();
        assertThat("Looks like a person was not created", people, hasItem(newPerson));
    }

    @Test
    public void create_superuser() {

        String personName = "Philip Fry";
        String personEmail = "fry@futurama.com";
        Person newPerson = Person.builder()
                .name(personName)
                .email(personEmail)
                .build();

        prepareRequest(SUPERUSER_NAME, newPerson)
                .statusCode(SC_CREATED)
                .when()
                .post(baseUrl);

        Set<Person> people = personService.getAll();
        assertThat("Looks like a person was not created", people, hasItem(newPerson));
    }

    @Test
    public void create_forbidden() {

        Person newPerson = Person.builder()
                .name("Darth Vader")
                .email("d.h@example.com")
                .build();

        prepareRequest(SIMPLE_USER_NAME, newPerson)
                .statusCode(SC_FORBIDDEN)
                .when()
                .post(baseUrl);

        prepareRequest(UPDATE_USER_NAME, newPerson)
                .statusCode(SC_FORBIDDEN)
                .when()
                .post(baseUrl);

        prepareRequest(DELETE_USER_NAME, newPerson)
                .statusCode(SC_FORBIDDEN)
                .when()
                .post(baseUrl);

        Set<Person> people = personService.getAll();
        assertThat("Looks like a person was created", people, not(hasItem(newPerson)));
    }

    @Test
    public void update() {

        String newEmail = "homer.s@example.com";
        person.setEmail(newEmail);

        prepareRequest(UPDATE_USER_NAME, person)
                .statusCode(SC_NO_CONTENT)
                .when()
                .put(baseUrl);

        Person actualPerson = personService.get(person.getId());
        assertEquals("Looks like a person was not updated", person, actualPerson);
    }

    @Test
    public void delete() {

        prepareRequest(DELETE_USER_NAME)
                .statusCode(SC_NO_CONTENT)
                .when()
                .delete(baseUrl + "/" + person.getId());

        Set<Person> people = personService.getAll();
        assertThat("Looks like a person was not removed", people, not(hasItem(person)));
    }

    @Test
    public void delete_forbidden() {

        String url = baseUrl + "/" + person.getId();

        prepareRequest(SIMPLE_USER_NAME)
                .statusCode(SC_FORBIDDEN)
                .when()
                .delete(url);

        prepareRequest(CREATE_USER_NAME)
                .statusCode(SC_FORBIDDEN)
                .when()
                .delete(url);

        prepareRequest(UPDATE_USER_NAME)
                .statusCode(SC_FORBIDDEN)
                .when()
                .delete(url);
    }

    @Test
    public void delete_superuser() {

        prepareRequest(SUPERUSER_NAME)
                .statusCode(SC_NO_CONTENT)
                .when()
                .delete(baseUrl + "/" + person.getId());

        Set<Person> people = personService.getAll();
        assertThat("Looks like a person was not removed", people, not(hasItem(person)));
    }
}
