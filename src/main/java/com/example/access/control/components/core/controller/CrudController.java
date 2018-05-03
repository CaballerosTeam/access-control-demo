package com.example.access.control.components.core.controller;

import com.example.access.control.components.core.service.CrudService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

public abstract class CrudController<T> {

    private CrudService<T> service;

    public CrudController(CrudService<T> service) {
        this.service = service;
    }

    @GetMapping
    public Set<T> getAll() {

        return service.getAll();
    }

    @GetMapping(path = "/{entityId}")
    public T get(@PathVariable Long entityId) {

        return service.get(entityId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public T create(@RequestBody @Valid T entity) {

        return service.create(entity);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid T entity) {

        service.update(entity);
    }

    @DeleteMapping(path = "/{entityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long entityId) {

        service.remove(entityId);
    }
}
