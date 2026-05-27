package com.discoverethiopia.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private static DatabaseConnection instance;
    private final DatabaseConfig config;

    private DatabaseConnection(DatabaseConfig config) {
        this.config = config;
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection(DatabaseConfig.fromEnvironment());
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
    }

    public boolean testConnection() {

        try (Connection connection = getConnection()) {

            System.out.println("Connected!");

            return connection.isValid(3);

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }
}

