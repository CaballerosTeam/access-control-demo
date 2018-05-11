package com.example.access.control.components.auth.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.access.prepost.PostAuthorize;

@Retention(RetentionPolicy.RUNTIME)
@PostAuthorize("hasPermission(returnObject, 'create')")
public @interface CreatePermission() {}
