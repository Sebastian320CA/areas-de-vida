package com.usta.models.plantingActivities;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlantingActivitiesImplDAO {
    private final Connection connection;

    public PlantingActivitiesImplDAO(Connection connection) {
        this.connection = connection;
    }

    public List<PlantingActivities> getAll() throws SQLException {
        List<PlantingActivities> activities = new ArrayList<>();
        String query = "SELECT * FROM planting_activities";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                PlantingActivities activity = new PlantingActivities(
                    rs.getInt("id"),
                    rs.getDate("activity_date").toLocalDate(),
                    rs.getString("activity_type"),
                    rs.getString("description"),
                    rs.getInt("living_area_id")
                );
                activities.add(activity);
            }
        }
        return activities;
    }

    public void add(PlantingActivities activity) throws SQLException {
        String query = "INSERT INTO planting_activities (activity_date, activity_type, description, living_area_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(activity.getActivityDate()));
            stmt.setString(2, activity.getActivityType());
            stmt.setString(3, activity.getDescription());
            stmt.setInt(4, activity.getLivingAreaId());
            stmt.executeUpdate();
        }
    }

    public void update(PlantingActivities activity) throws SQLException {
        String query = "UPDATE planting_activities SET activity_date = ?, activity_type = ?, description = ?, living_area_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(activity.getActivityDate()));
            stmt.setString(2, activity.getActivityType());
            stmt.setString(3, activity.getDescription());
            stmt.setInt(4, activity.getLivingAreaId());
            stmt.setInt(5, activity.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM planting_activities WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Verificar si el livingAreaId existe en la tabla living_areas
    public boolean isLivingAreaIdValid(int livingAreaId) throws SQLException {
        String query = "SELECT id FROM living_areas WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, livingAreaId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();  // Si existe, devuelve true
        }
    }
}
