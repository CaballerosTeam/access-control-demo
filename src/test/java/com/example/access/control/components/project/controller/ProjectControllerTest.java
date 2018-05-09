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

import static com.example.access.control.TestHelper.getRequest;
import static com.example.access.control.TestHelper.prepareRequest;
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
    private final String simpleUserName = "user";
    private final String superUserName = "superuser";

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {

        RestAssured.port = localServerPort;

        project = make(a(PROJECT));
    }

    @Test
    public void getAll() {

        getRequest(simpleUserName, baseUrl)
                .then()
                .statusCode(SC_OK)
                .body("collect { it.title }", hasItem(project.getTitle()));
    }

    @Test
    public void get() {

        getRequest(simpleUserName, baseUrl + "/" + project.getId())
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

        prepareRequest(superUserName, newProject)
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

        prepareRequest(superUserName, project)
                .statusCode(SC_NO_CONTENT)
                .when()
                .put(baseUrl);

        Project actualProject = projectService.get(project.getId());
        assertEquals("Looks like a project was not updated", project, actualProject);
    }

    @Test
    public void update_forbidden() {

        prepareRequest(simpleUserName, project)
                .statusCode(SC_FORBIDDEN)
                .when()
                .put(baseUrl);
    }

    @Test
    public void delete() {

        prepareRequest(superUserName)
                .statusCode(SC_NO_CONTENT)
                .when()
                .delete(baseUrl + "/" + project.getId());

        Set<Project> projects = projectService.getAll();
        assertThat("Looks like a project was not removed", projects, not(hasItem(project)));
    }
}
