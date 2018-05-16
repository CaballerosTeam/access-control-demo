package com.example.access.control.components.project.controller;

import com.example.access.control.AccessControlDemoApplication;
import com.example.access.control.components.project.domain.Project;
import com.example.access.control.components.project.service.ProjectService;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static com.example.access.control.TestHelper.*;
import static com.example.access.control.components.project.maker.ProjectMaker.PROJECT;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.apache.http.HttpStatus.*;
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
public class ProjectControllerTest {

    @LocalServerPort
    private int localServerPort;

    @Autowired
    private ProjectService projectService;

    private Project project;
    private String baseUrl = "/v1/project";

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {

        RestAssured.port = localServerPort;

        project = make(a(PROJECT));
    }

    @Test
    public void getAll() {

        getRequest(SIMPLE_USER_NAME, baseUrl)
                .then()
                .statusCode(SC_OK)
                .body("collect { it.title }", hasItem(project.getTitle()));
    }

    @Test
    public void get() {

        getRequest(SIMPLE_USER_NAME, baseUrl + "/" + project.getId())
                .then()
                .statusCode(SC_OK)
                .body("title", equalTo(project.getTitle()));
    }

    @Test
    public void create() {

        String projectTitle = "The Springfield";
        Project newProject = Project.builder()
                .title(projectTitle)
                .build();

        prepareRequest(CREATE_USER_NAME, newProject)
                .statusCode(SC_CREATED)
                .when()
                .post(baseUrl);

        Set<Project> projects = projectService.getAll();
        assertThat("Looks like a project was not created", projects, hasItem(newProject));
    }

    @Test
    public void update() {

        String newTitle = "Death Star";
        project.setTitle(newTitle);

        prepareRequest(UPDATE_USER_NAME, project)
                .statusCode(SC_NO_CONTENT)
                .when()
                .put(baseUrl);

        Project actualProject = projectService.get(project.getId());
        assertEquals("Looks like a project was not updated", project, actualProject);
    }

    @Test
    public void update_forbidden() {

        String newTitle = "Manhattan";
        project.setTitle(newTitle);

        prepareRequest(SIMPLE_USER_NAME, project)
                .statusCode(SC_FORBIDDEN)
                .when()
                .put(baseUrl);

        prepareRequest(CREATE_USER_NAME, project)
                .statusCode(SC_FORBIDDEN)
                .when()
                .put(baseUrl);

        prepareRequest(DELETE_USER_NAME, project)
                .statusCode(SC_FORBIDDEN)
                .when()
                .put(baseUrl);

        Set<Project> projects = projectService.getAll();
        assertThat("Looks like a project was updated", projects, not(hasItem(project)));
    }

    @Test
    public void delete() {

        prepareRequest(DELETE_USER_NAME)
                .statusCode(SC_NO_CONTENT)
                .when()
                .delete(baseUrl + "/" + project.getId());

        Set<Project> projects = projectService.getAll();
        assertThat("Looks like a project was not removed", projects, not(hasItem(project)));
    }
}
