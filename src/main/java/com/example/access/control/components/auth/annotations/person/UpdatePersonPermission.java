package com.example.access.control.components.auth.annotations.person;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasPermission(null, 'Person', T(com.example.access.control.components.auth.domain.Permission).UPDATE.getKey())")
public @interface UpdatePersonPermission {}
