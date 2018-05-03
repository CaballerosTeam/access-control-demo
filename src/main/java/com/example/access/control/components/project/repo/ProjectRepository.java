package com.example.access.control.components.project.repo;

import com.example.access.control.components.project.domain.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ProjectRepository extends CrudRepository<Project, Long> {

    Set<Project> findAll();
}
