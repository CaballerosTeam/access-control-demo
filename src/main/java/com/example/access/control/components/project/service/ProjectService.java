package com.example.access.control.components.project.service;

import com.example.access.control.components.auth.annotations.project.CreateProjectPermission;
import com.example.access.control.components.auth.annotations.project.DeleteProjectPermission;
import com.example.access.control.components.auth.annotations.project.UpdateProjectPermission;
import com.example.access.control.components.core.service.CrudService;
import com.example.access.control.components.project.domain.Project;
import com.example.access.control.components.project.repo.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Service
public class ProjectService implements CrudService<Project> {

    private ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Set<Project> getAll() {

        return projectRepository.findAll();
    }

    @Override
    public Project get(@NotNull Long projectId) {

        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project with ID " + projectId + " does not exist"));
    }

    @Override
    @Transactional
    @CreateProjectPermission
    public Project create(@NotNull Project project) {

        Long projectId = project.getId();
        if (projectId != null) {
            throw new RuntimeException("Project object must not contain ID");
        }

        return projectRepository.save(project);
    }

    @Override
    @Transactional
    @UpdateProjectPermission
    public Project update(@NotNull Project project) {

        Long projectId = project.getId();
        if (!exists(projectId)) {
            throw new RuntimeException("Project with ID " + projectId + " does not exist");
        }

        return projectRepository.save(project);
    }

    @Override
    @Transactional
    @DeleteProjectPermission
    public void remove(Long projectId) {

        if (!exists(projectId)) {
            throw new RuntimeException("Project with ID " + projectId + " does not exist");
        }

        projectRepository.deleteById(projectId);
    }

    @Override
    public boolean exists(Long projectId) {

        return projectRepository.existsById(projectId);
    }
}
