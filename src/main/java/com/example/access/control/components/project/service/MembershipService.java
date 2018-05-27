package com.example.access.control.components.project.service;

import com.example.access.control.components.auth.annotations.project.CreateMembershipPermission;
import com.example.access.control.components.auth.annotations.project.UpdateMembershipPermission;
import com.example.access.control.components.person.domain.Person;
import com.example.access.control.components.person.service.PersonService;
import com.example.access.control.components.project.domain.Membership;
import com.example.access.control.components.project.domain.Project;
import com.example.access.control.components.project.dto.MembershipDto;
import com.example.access.control.components.project.repo.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class MembershipService {

    private final PersonService personService;
    private final ProjectService projectService;
    private final MembershipRepository membershipRepository;

    @Autowired
    public MembershipService(PersonService personService, ProjectService projectService,
                             MembershipRepository membershipRepository) {
        this.personService = personService;
        this.projectService = projectService;
        this.membershipRepository = membershipRepository;
    }

    @Transactional
    @CreateMembershipPermission
    public Membership begin(MembershipDto membershipDto) {

        Long personId = membershipDto.getPersonId();
        Person person = personService.get(personId);

        Long projectId = membershipDto.getProjectId();
        Project project = projectService.get(projectId);

        Membership membership = Membership.builder()
                .person(person)
                .project(project)
                .build();

        return membershipRepository.save(membership);
    }

    @Transactional
    @UpdateMembershipPermission
    public void complete(MembershipDto membershipDto) {

        Long projectId = membershipDto.getProjectId();
        Long personId = membershipDto.getPersonId();

        Membership membership = membershipRepository.findByProjectIdAndPersonId(projectId, personId);
        if (membership == null) {
            throw new RuntimeException("Person with ID " + personId + " not found on project with ID " + projectId);
        }

        membership.setFinishDate(LocalDate.now());

        membershipRepository.save(membership);
    }
}
