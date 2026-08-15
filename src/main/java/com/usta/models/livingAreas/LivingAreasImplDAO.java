package com.usta.models.livingAreas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivingAreasImplDAO {
    private static LivingAreasImplDAO instance;
    private Connection connection;

    private LivingAreasImplDAO(Connection connection) {
        this.connection = connection;
    }

    public LivingAreasImplDAO() {
    }

    public static LivingAreasImplDAO getInstance(Connection connection) {
        if (instance == null) {
            instance = new LivingAreasImplDAO(connection);
        }
        return instance;
    }

    public void createLivingArea(LivingAreas livingArea) throws SQLException {
        String query = "INSERT INTO living_areas (location, size, conservation_status, creation_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, livingArea.getLocation());
            stmt.setDouble(2, livingArea.getSize());
            stmt.setString(3, livingArea.getConservationStatus());
            stmt.setDate(4, livingArea.getCreationDate());
            stmt.executeUpdate();
        }
    }

    public List<LivingAreas> getAllLivingAreas() throws SQLException {
        List<LivingAreas> livingAreas = new ArrayList<>();
        String query = "SELECT * FROM living_areas";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String location = rs.getString("location");
                double size = rs.getDouble("size");
                String conservationStatus = rs.getString("conservation_status");
                Date creationDate = rs.getDate("creation_date");
                livingAreas.add(new LivingAreas(id, location, size, conservationStatus, creationDate));
            }
        }
        return livingAreas;
    }

    public void updateLivingArea(LivingAreas livingArea) throws SQLException {
        String query = "UPDATE living_areas SET location = ?, size = ?, conservation_status = ?, creation_date = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, livingArea.getLocation());
            stmt.setDouble(2, livingArea.getSize());
            stmt.setString(3, livingArea.getConservationStatus());
            stmt.setDate(4, livingArea.getCreationDate());
            stmt.setInt(5, livingArea.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteLivingArea(int id) throws SQLException {
        String query = "DELETE FROM living_areas WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
