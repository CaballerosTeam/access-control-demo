package com.example.access.control.components.project.controller;

import com.example.access.control.AccessControlDemoApplication;
import com.example.access.control.components.person.domain.Person;
import com.example.access.control.components.project.domain.Membership;
import com.example.access.control.components.project.domain.Project;
import com.example.access.control.components.project.dto.MembershipDto;
import com.example.access.control.components.project.service.ProjectService;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.access.control.TestHelper.*;
import static com.example.access.control.components.person.maker.PersonMaker.PERSON;
import static com.example.access.control.components.project.maker.MembershipMaker.*;
import static com.example.access.control.components.project.maker.ProjectMaker.PROJECT;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = AccessControlDemoApplication.class,
        properties = "spring.config.name=application"
)
@ActiveProfiles("test")
public class MembershipControllerTest {

    @LocalServerPort
    private int localServerPort;

    @Autowired
    private ProjectService projectService;

    private final String baseUrl = "/v1/membership";
    private Person person;
    private Project project;
    private Long projectId, personId;
    private MembershipDto membershipDto;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {

        RestAssured.port = localServerPort;

        person = make(a(PERSON));
        project = make(a(PROJECT));

        projectId = project.getId();
        personId = person.getId();

        membershipDto = MembershipDto.builder()
                .personId(personId)
                .projectId(projectId)
                .build();
    }

    @Test
    public void begin() {

        prepareRequest(CREATE_USER_NAME, membershipDto)
                .statusCode(SC_CREATED)
                .when()
                .post(baseUrl);

        Project updatedProject = projectService.get(projectId);
        Set<Membership> team = updatedProject.getTeam();

        List<Membership> personMemberships = team.stream()
                .filter(membership -> person.equals(membership.getPerson()))
                .collect(Collectors.toList());

        assertEquals("Person should be a team member exactly 1 time", 1, personMemberships.size());

        Membership membership = personMemberships.get(0);

        assertEquals("Date of creation doesn't match", LocalDate.now(), membership.getStartDate());
    }

    @Test
    public void begin_forbidden() {

        prepareRequest(SIMPLE_USER_NAME, membershipDto)
                .statusCode(SC_FORBIDDEN)
                .when()
                .post(baseUrl);

        prepareRequest(UPDATE_USER_NAME, membershipDto)
                .statusCode(SC_FORBIDDEN)
                .when()
                .post(baseUrl);
    }

    @Test
    public void begin_superuser() {

        prepareRequest(SUPERUSER_NAME, membershipDto)
                .statusCode(SC_CREATED)
                .when()
                .post(baseUrl);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void complete() {

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        make(a(MEMBERSHIP, with(TEAMMATE, person), with(ACTIVITY, project), with(START_DATE, yesterday)));

        prepareRequest(UPDATE_USER_NAME, membershipDto)
                .statusCode(SC_NO_CONTENT)
                .when()
                .put(baseUrl);

        Project updatedProject = projectService.get(projectId);
        Set<Membership> team = updatedProject.getTeam();
        List<Membership> personMembership = team.stream()
                .filter(i -> person.equals(i.getPerson()))
                .collect(Collectors.toList());

        Membership updatedMembership = personMembership.get(0);

        assertEquals("Date of finish does't match", today, updatedMembership.getFinishDate());
    }

    @Test
    public void complete_forbidden() {

        prepareRequest(SIMPLE_USER_NAME, membershipDto)
                .statusCode(SC_FORBIDDEN)
                .when()
                .put(baseUrl);

        prepareRequest(CREATE_USER_NAME, membershipDto)
                .statusCode(SC_FORBIDDEN)
                .when()
                .put(baseUrl);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void complete_superuser() {

        make(a(MEMBERSHIP, with(TEAMMATE, person), with(ACTIVITY, project)));

        prepareRequest(SUPERUSER_NAME, membershipDto)
                .statusCode(SC_NO_CONTENT)
                .when()
                .put(baseUrl);
    }
}
