package com.discoverethiopia.db;

public class DatabaseConfig {
    private final String url;
    private final String username;
    private final String password;

    public DatabaseConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DatabaseConfig fromEnvironment() {
        String url = firstNonBlank(System.getProperty("db.url"), System.getenv("DB_URL"),
                "jdbc:mysql://localhost:3306/myprojectdb");
        String username = firstNonBlank(System.getProperty("db.user"), System.getenv("DB_USER"), "root");
        String password = firstNonBlank(System.getProperty("db.password"), System.getenv("DB_PASSWORD"), "");
        return new DatabaseConfig(url, username, password);
    }

    private static String firstNonBlank(String first, String second, String fallback) {
        if (first != null && !first.isBlank()) {
            return first;
        }
        if (second != null && !second.isBlank()) {
            return second;
        }
        return fallback;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
