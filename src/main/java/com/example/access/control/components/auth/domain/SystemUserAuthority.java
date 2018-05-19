package com.example.access.control.components.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.StringJoiner;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemUserAuthority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private SystemUser systemUser;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Enumerated(EnumType.STRING)
    private Permission permission;

    @Override
    public String getAuthority() {
        StringJoiner join = new StringJoiner("_");
        join.add(permission.getKey());
        join.add(contentType.getKey());

        return join.toString();
    }
}
