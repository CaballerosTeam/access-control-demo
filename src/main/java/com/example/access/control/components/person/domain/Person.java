package com.example.access.control.components.person.domain;

import com.example.access.control.components.project.domain.Membership;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;
import org.springframework.util.CollectionUtils;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "name")
@EqualsAndHashCode(of = {"name", "email"})
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "person")
    private Set<Membership> participation;

    @JsonIgnore
    public boolean isBusy() {
        return !CollectionUtils.isEmpty(participation);
    }
}
