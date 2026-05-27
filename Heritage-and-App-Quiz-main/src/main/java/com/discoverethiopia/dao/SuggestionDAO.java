package com.discoverethiopia.dao;

import com.discoverethiopia.db.DatabaseConnection;
import com.discoverethiopia.model.Suggestion;
import com.discoverethiopia.model.SuggestionStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SuggestionDAO {
    private final DatabaseConnection databaseConnection;

    public SuggestionDAO() {
        this(DatabaseConnection.getInstance());
    }

    public SuggestionDAO(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public int createSuggestion(int userId, String name, String region, String description, String reason) {
        validateRequired(name, "Heritage name");
        validateRequired(region, "Region");
        validateRequired(description, "Description");
        validateRequired(reason, "Reason");

        String sql = """
                INSERT INTO user_suggestions
                (user_id, suggested_name, suggested_region, suggested_description, reason)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userId);
            statement.setString(2, name.trim());
            statement.setString(3, region.trim());
            statement.setString(4, description.trim());
            statement.setString(5, reason.trim());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            throw new DaoException("Suggestion was created but no generated key was returned.", null);
        } catch (SQLException e) {
            throw new DaoException("Could not create suggestion.", e);
        }
    }

    public List<Suggestion> getPendingSuggestions() {
        String sql = """
                SELECT suggestion_id, user_id, suggested_name, suggested_region, suggested_description,
                       reason, status, submitted_at
                FROM user_suggestions
                WHERE status = 'pending'
                ORDER BY submitted_at
                """;
        List<Suggestion> suggestions = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                suggestions.add(mapSuggestion(resultSet));
            }
            return suggestions;
        } catch (SQLException e) {
            throw new DaoException("Could not load suggestions.", e);
        }
    }

    public int approveSuggestion(int suggestionId, int adminId, String siteType, String amazingFacts, String imagePath) {
        String selectSql = """
                SELECT suggested_name, suggested_region, suggested_description
                FROM user_suggestions
                WHERE suggestion_id = ? AND status = 'pending'
                """;
        String insertSiteSql = """
                INSERT INTO heritage_sites
                (name, type, region, description, amazing_facts, image_path, added_by_admin_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        String updateSuggestionSql = """
                UPDATE user_suggestions
                SET status = 'approved'
                WHERE suggestion_id = ?
                """;

        try (Connection connection = databaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                String name;
                String region;
                String description;
                try (PreparedStatement select = connection.prepareStatement(selectSql)) {
                    select.setInt(1, suggestionId);
                    try (ResultSet resultSet = select.executeQuery()) {
                        if (!resultSet.next()) {
                            throw new IllegalArgumentException("Suggestion not found or already reviewed.");
                        }
                        name = resultSet.getString("suggested_name");
                        region = resultSet.getString("suggested_region");
                        description = resultSet.getString("suggested_description");
                    }
                }

                int siteId;
                try (PreparedStatement insert = connection.prepareStatement(insertSiteSql,
                        Statement.RETURN_GENERATED_KEYS)) {
                    insert.setString(1, name);
                    insert.setString(2, siteType);
                    insert.setString(3, region);
                    insert.setString(4, description);
                    insert.setString(5, amazingFacts == null ? "" : amazingFacts);
                    insert.setString(6, imagePath);
                    insert.setInt(7, adminId);
                    insert.executeUpdate();
                    try (ResultSet keys = insert.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("No site id returned.");
                        }
                        siteId = keys.getInt(1);
                    }
                }

                try (PreparedStatement update = connection.prepareStatement(updateSuggestionSql)) {
                    update.setInt(1, suggestionId);
                    update.executeUpdate();
                }
                connection.commit();
                return siteId;
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DaoException("Could not approve suggestion.", e);
        }
    }

    public boolean rejectSuggestion(int suggestionId) {
        String sql = """
                UPDATE user_suggestions
                SET status = 'rejected'
                WHERE suggestion_id = ? AND status = 'pending'
                """;
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, suggestionId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Could not reject suggestion.", e);
        }
    }

    private Suggestion mapSuggestion(ResultSet resultSet) throws SQLException {
        return new Suggestion(
                resultSet.getInt("suggestion_id"),
                resultSet.getInt("user_id"),
                resultSet.getString("suggested_name"),
                resultSet.getString("suggested_region"),
                resultSet.getString("suggested_description"),
                resultSet.getString("reason"),
                SuggestionStatus.fromDatabaseValue(resultSet.getString("status")),
                resultSet.getTimestamp("submitted_at").toLocalDateTime());
    }

    private void validateRequired(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
    }


    /**
     * Creates a suggestion using only user ID and a suggestion text.
     * The other fields (name, region, etc.) are filled with placeholders.
     */
    public int createSimpleSuggestion(int userId, String suggestionText) {
        String sql = """
        INSERT INTO user_suggestions
        (user_id, suggested_name, suggested_region, suggested_description, reason)
        VALUES (?, ?, ?, ?, ?)
        """;
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setString(2, "User Suggestion");          // placeholder
            stmt.setString(3, "Unknown");                  // placeholder
            stmt.setString(4, suggestionText);             // the actual suggestion
            stmt.setString(5, suggestionText);             // reason same as description
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            throw new DaoException("No key returned", null);
        } catch (SQLException e) {
            throw new DaoException("Could not save suggestion", e);
        }
    }
}

