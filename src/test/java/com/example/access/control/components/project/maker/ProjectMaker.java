package com.example.access.control.components.project.maker;

import com.example.access.control.components.project.domain.Membership;
import com.example.access.control.components.project.domain.Project;
import com.example.access.control.components.project.repo.ProjectRepository;
import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ProjectMaker {

    private static final AtomicLong sequence = new AtomicLong();
    private static ProjectRepository projectRepository;

    public static final Property<Project, String> TITLE = Property.newProperty();
    public static final Property<Project, Set<Membership>> TEAM = Property.newProperty();

    public static final Instantiator<Project> PROJECT = propertyLookup -> {

        Long id = sequence.incrementAndGet();

        Project project = Project.builder()
                .title(propertyLookup.valueOf(TITLE, "Project #" + id))
                .build();

        Set<Membership> team = propertyLookup.valueOf(TEAM, new HashSet<>());
        if (team.size() > 0) {
            project.setTeam(team);
        }

        return projectRepository.save(project);
    };

    @Autowired
    public void setProjectRepository(ProjectRepository projectRepository) {
        ProjectMaker.projectRepository = projectRepository;
    }
}
