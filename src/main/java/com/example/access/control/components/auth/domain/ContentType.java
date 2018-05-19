package com.example.access.control.components.auth.domain;

import lombok.Getter;

@Getter
public enum ContentType {
    PERSON("person"),
    PROJECT("project"),
    MEMBERSHIP("membership");

    private String key;

    ContentType(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
