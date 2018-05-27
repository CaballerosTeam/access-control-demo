package com.example.access.control.components.project.maker;

import com.example.access.control.components.person.domain.Person;
import com.example.access.control.components.project.domain.Membership;
import com.example.access.control.components.project.domain.Project;
import com.example.access.control.components.project.repo.MembershipRepository;
import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.example.access.control.components.person.maker.PersonMaker.PERSON;
import static com.example.access.control.components.project.maker.ProjectMaker.PROJECT;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;

@Component
public class MembershipMaker {

    private static MembershipRepository membershipRepository;

    public static final Property<Membership, Person> TEAMMATE = Property.newProperty();
    public static final Property<Membership, Project> ACTIVITY = Property.newProperty();
    public static final Property<Membership, LocalDate> START_DATE = Property.newProperty();

    @SuppressWarnings("unchecked")
    public static final Instantiator<Membership> MEMBERSHIP = propertyLookup -> {

        Person person = propertyLookup.valueOf(TEAMMATE, make(a(PERSON)));
        Project project = propertyLookup.valueOf(ACTIVITY, make(a(PROJECT)));
        LocalDate startDate = propertyLookup.valueOf(START_DATE, LocalDate.now());

        Membership membership = Membership.builder()
                .person(person)
                .project(project)
                .startDate(startDate)
                .build();

        return membershipRepository.save(membership);
    };

    @Autowired
    public void setMembershipService(MembershipRepository membershipService) {

        MembershipMaker.membershipRepository = membershipService;
    }
}
