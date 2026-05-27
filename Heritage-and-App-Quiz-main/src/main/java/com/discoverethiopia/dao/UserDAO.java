package com.discoverethiopia.dao;

import com.discoverethiopia.db.DatabaseConnection;
import com.discoverethiopia.model.Role;
import com.discoverethiopia.model.User;
import com.discoverethiopia.util.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserDAO {
    private final DatabaseConnection databaseConnection;

    public UserDAO() {
        this(DatabaseConnection.getInstance());
    }

    public UserDAO(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public User createUser(String username, String email, String plainPassword) {
        validateRequired(username, "Username");
        validateRequired(email, "Email");
        validateRequired(plainPassword, "Password");

        String sql = """
                INSERT INTO users (username, email, password_hash, role)
                VALUES (?, ?, ?, 'user')
                """;
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, username.trim());
            statement.setString(2, email.trim());
            statement.setString(3, PasswordHasher.hash(plainPassword));
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return findById(keys.getInt(1)).orElseThrow();
                }
            }
            throw new DaoException("User was created but no generated key was returned.", null);
        } catch (SQLException e) {
            throw new DaoException("Could not create user. Username or email may already exist.", e);
        }
    }

    public Optional<User> login(String email, String plainPassword) {

        validateRequired(email, "Email");
        validateRequired(plainPassword, "Password");

        String sql = """
            SELECT user_id, username, email, password_hash, role, created_at
            FROM users
            WHERE email = ? AND password_hash = ?
            """;

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email.trim());

            statement.setString(2,
                    PasswordHasher.hash(plainPassword));

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return Optional.of(mapUser(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {

            throw new DaoException("Could not log in user.", e);
        }
    }

    public Optional<User> findById(int userId) {
        String sql = """
                SELECT user_id, username, email, password_hash, role, created_at
                FROM users
                WHERE user_id = ?
                """;
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapUser(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Could not find user.", e);
        }
    }

    public boolean isAdmin(int userId) {
        return findById(userId).map(User::isAdmin).orElse(false);
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        return new User(
                resultSet.getInt("user_id"),
                resultSet.getString("username"),
                resultSet.getString("email"),
                resultSet.getString("password_hash"),
                Role.fromDatabaseValue(resultSet.getString("role")),
                createdAt);
    }

    private void validateRequired(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
    }
}

