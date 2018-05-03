package com.example.access.control.components.project.controller;

import com.example.access.control.components.core.controller.CrudController;
import com.example.access.control.components.core.service.CrudService;
import com.example.access.control.components.project.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/project")
public class ProjectController extends CrudController<Project> {

    @Autowired
    public ProjectController(CrudService<Project> service) {
        super(service);
    }
}
