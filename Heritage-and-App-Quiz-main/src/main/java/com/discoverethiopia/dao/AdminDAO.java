package com.discoverethiopia.dao;

import com.discoverethiopia.db.DatabaseConnection;
import com.discoverethiopia.model.QuizAttempt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    private final DatabaseConnection databaseConnection;

    public AdminDAO() {
        this(DatabaseConnection.getInstance());
    }

    public AdminDAO(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public void logAction(int adminId, String action) {
        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException("Admin action cannot be empty.");
        }
        String sql = "INSERT INTO admin_logs (admin_id, action) VALUES (?, ?)";
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, adminId);
            statement.setString(2, action.trim());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Could not write admin log.", e);
        }
    }

    public List<String> getAdminLogs() {
        String sql = """
                SELECT CONCAT(log_time, ' - Admin #', admin_id, ': ', action) AS log_text
                FROM admin_logs
                ORDER BY log_time DESC
                """;
        List<String> logs = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                logs.add(resultSet.getString("log_text"));
            }
            return logs;
        } catch (SQLException e) {
            throw new DaoException("Could not load admin logs.", e);
        }
    }

    public List<QuizAttempt> getAllQuizAttempts() {
        String sql = """
                SELECT attempt_id, user_id, site_id, score, total_questions, percentage, taken_at
                FROM quiz_attempts
                ORDER BY taken_at DESC
                """;
        List<QuizAttempt> attempts = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                attempts.add(new QuizAttempt(
                        resultSet.getInt("attempt_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("site_id"),
                        resultSet.getInt("score"),
                        resultSet.getInt("total_questions"),
                        resultSet.getInt("percentage"),
                        resultSet.getTimestamp("taken_at").toLocalDateTime()));
            }
            return attempts;
        } catch (SQLException e) {
            throw new DaoException("Could not load all quiz attempts.", e);
        }
    }

    public int countUsers() {
        return countFrom("users");
    }

    public int countHeritageSites() {
        return countFrom("heritage_sites");
    }

    public int countPendingSuggestions() {
        String sql = "SELECT COUNT(*) AS total FROM user_suggestions WHERE status = 'pending'";
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt("total");
        } catch (SQLException e) {
            throw new DaoException("Could not count pending suggestions.", e);
        }
    }

    private int countFrom(String tableName) {
        String sql = "SELECT COUNT(*) AS total FROM " + tableName;
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt("total");
        } catch (SQLException e) {
            throw new DaoException("Could not count rows from " + tableName + ".", e);
        }
    }
}

