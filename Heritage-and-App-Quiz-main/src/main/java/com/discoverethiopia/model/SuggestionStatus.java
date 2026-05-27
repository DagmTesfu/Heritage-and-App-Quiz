package com.discoverethiopia.model;

public enum SuggestionStatus {
    PENDING("pending"),
    APPROVED("approved"),
    REJECTED("rejected");

    private final String databaseValue;

    SuggestionStatus(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public String getDatabaseValue() {
        return databaseValue;
    }

    public static SuggestionStatus fromDatabaseValue(String value) {
        if ("approved".equalsIgnoreCase(value)) {
            return APPROVED;
        }
        if ("rejected".equalsIgnoreCase(value)) {
            return REJECTED;
        }
        return PENDING;
    }
}

