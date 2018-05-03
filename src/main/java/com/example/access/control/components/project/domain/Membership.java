package com.example.access.control.components.project.domain;

import com.example.access.control.components.person.domain.Person;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
public class Membership implements Serializable {

    @Id
    @ManyToOne
    private Person person;

    @Id
    @ManyToOne
    private Project project;

    @NotNull
    @NotBlank
    @CreatedDate
    private LocalDate startDate;

    private LocalDate finishDate;
}
