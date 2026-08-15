package com.usta.models.forestCensuses;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ForestCensusesImplDAO {

    private final Connection connection;

    public ForestCensusesImplDAO(Connection connection) {
        this.connection = connection;
    }

    public List<ForestCensuses> getAll() throws SQLException {
        List<ForestCensuses> censuses = new ArrayList<>();
        String query = "SELECT * FROM forest_censuses";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                ForestCensuses census = new ForestCensuses(
                        rs.getInt("id"),
                        rs.getDate("census_date").toLocalDate(),
                        rs.getDouble("vegetation_coverage"),
                        rs.getString("ecosystem_state"),
                        rs.getString("observations")
                );
                censuses.add(census);
            }
        }
        return censuses;
    }

    public void add(ForestCensuses census) throws SQLException {
        String query = "INSERT INTO forest_censuses (census_date, vegetation_coverage, ecosystem_state, observations) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, Date.valueOf(census.getCensusDate()));
            pstmt.setDouble(2, census.getVegetationCoverage());
            pstmt.setString(3, census.getEcosystemState());
            pstmt.setString(4, census.getObservations());
            pstmt.executeUpdate();
        }
    }

    public void update(ForestCensuses census) throws SQLException {
        String query = "UPDATE forest_censuses SET census_date = ?, vegetation_coverage = ?, ecosystem_state = ?, observations = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, Date.valueOf(census.getCensusDate()));
            pstmt.setDouble(2, census.getVegetationCoverage());
            pstmt.setString(3, census.getEcosystemState());
            pstmt.setString(4, census.getObservations());
            pstmt.setInt(5, census.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM forest_censuses WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
