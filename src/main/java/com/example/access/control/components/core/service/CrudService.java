package com.example.access.control.components.core.service;

import javax.validation.constraints.NotNull;
import java.util.Set;

public interface CrudService<T> {

    Set<T> getAll();

    T get(Long entityId);

    T create(@NotNull T entity);

    T update(@NotNull T entity);

    void remove(Long entityId);

    boolean exists(Long entityId);
}
