package com.discoverethiopia.dao;

import com.discoverethiopia.db.DatabaseConnection;
import com.discoverethiopia.model.ArchaeologicalSite;
import com.discoverethiopia.model.ChurchSite;
import com.discoverethiopia.model.CitySite;
import com.discoverethiopia.model.HeritageSite;
import com.discoverethiopia.model.NaturalSite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HeritageDAO {
    private final DatabaseConnection databaseConnection;

    public HeritageDAO() {
        this(DatabaseConnection.getInstance());
    }

    public HeritageDAO(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public List<HeritageSite> findAll() {
        String sql = """
                SELECT site_id, name, type, region, description, amazing_facts, image_path, added_by_admin_id
                FROM heritage_sites
                ORDER BY name
                """;
        List<HeritageSite> sites = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                sites.add(mapSite(resultSet));
            }
            return sites;
        } catch (SQLException e) {
            throw new DaoException("Could not load heritage sites.", e);
        }
    }

    public Optional<HeritageSite> findById(int siteId) {
        String sql = """
                SELECT site_id, name, type, region, description, amazing_facts, image_path, added_by_admin_id
                FROM heritage_sites
                WHERE site_id = ?
                """;
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, siteId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapSite(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Could not load heritage site.", e);
        }
    }

    public int addSite(HeritageSite site) {
        String sql = """
                INSERT INTO heritage_sites
                (name, type, region, description, amazing_facts, image_path, added_by_admin_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillSiteStatement(statement, site);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            throw new DaoException("Heritage site was created but no generated key was returned.", null);
        } catch (SQLException e) {
            throw new DaoException("Could not add heritage site.", e);
        }
    }

    public boolean updateSite(HeritageSite site) {
        String sql = """
                UPDATE heritage_sites
                SET name = ?, type = ?, region = ?, description = ?, amazing_facts = ?, image_path = ?,
                    added_by_admin_id = ?
                WHERE site_id = ?
                """;
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            fillSiteStatement(statement, site);
            statement.setInt(8, site.getSiteId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Could not update heritage site.", e);
        }
    }

    public boolean deleteSite(int siteId) {
        String sql = "DELETE FROM heritage_sites WHERE site_id = ?";
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, siteId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Could not delete heritage site.", e);
        }
    }

    private void fillSiteStatement(PreparedStatement statement, HeritageSite site) throws SQLException {
        statement.setString(1, site.getName());
        statement.setString(2, site.getType());
        statement.setString(3, site.getRegion());
        statement.setString(4, site.getDescription());
        statement.setString(5, site.getAmazingFacts());
        statement.setString(6, site.getImagePath());
        if (site.getAddedByAdminId() == null) {
            statement.setNull(7, java.sql.Types.INTEGER);
        } else {
            statement.setInt(7, site.getAddedByAdminId());
        }
    }

    private HeritageSite mapSite(ResultSet resultSet) throws SQLException {
        int siteId = resultSet.getInt("site_id");
        String name = resultSet.getString("name");
        String type = resultSet.getString("type");
        String region = resultSet.getString("region");
        String description = resultSet.getString("description");
        String amazingFacts = resultSet.getString("amazing_facts");
        String imagePath = resultSet.getString("image_path");
        Integer addedByAdminId = resultSet.getObject("added_by_admin_id", Integer.class);

        return switch (type.toLowerCase()) {
            case "church" -> new ChurchSite(siteId, name, region, description, amazingFacts, imagePath, addedByAdminId);
            case "natural" -> new NaturalSite(siteId, name, region, description, amazingFacts, imagePath, addedByAdminId);
            case "archaeological" -> new ArchaeologicalSite(siteId, name, region, description, amazingFacts, imagePath, addedByAdminId);
            case "city" -> new CitySite(siteId, name, region, description, amazingFacts, imagePath, addedByAdminId);
            default -> new CitySite(siteId, name, region, description, amazingFacts, imagePath, addedByAdminId);
        };
    }
}

