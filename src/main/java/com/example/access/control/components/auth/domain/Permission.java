package com.example.access.control.components.auth.domain;

import lombok.Getter;

@Getter
public enum Permission {
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete");

    private String key;

    Permission(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
