package com.discoverethiopia.model;

public enum Role {
    USER("user"),
    ADMIN("admin");

    private final String databaseValue;

    Role(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public String getDatabaseValue() {
        return databaseValue;
    }

    public static Role fromDatabaseValue(String value) {
        if ("admin".equalsIgnoreCase(value)) {
            return ADMIN;
        }
        return USER;
    }
}

