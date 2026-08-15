package com.usta.models.documentations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentationImplDAO {
    private Connection connection;

    public DocumentationImplDAO() {
        try {
            String url = "jdbc:mysql://localhost:3306/ecogrow";
            String user = "root";
            String password = "";
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Documentation> getAllDocumentation() throws SQLException {
        List<Documentation> documentationList = new ArrayList<>();
        String query = "SELECT * FROM documentation ORDER BY issue_date DESC"; 
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            Documentation doc = new Documentation(
                rs.getInt("id"),
                rs.getString("document_type"),
                rs.getString("document_number"),
                rs.getDate("issue_date"),
                rs.getString("issuing_authority"),
                rs.getObject("living_area_id", Integer.class) 
            );
            documentationList.add(doc);
        }
        return documentationList;
    }

    public void insertDocumentation(Documentation documentation) throws SQLException {
        String query = "INSERT INTO documentation (document_type, document_number, issue_date, issuing_authority, living_area_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, documentation.getDocumentType());
        stmt.setString(2, documentation.getDocumentNumber());
        stmt.setDate(3, documentation.getIssueDate());
        stmt.setString(4, documentation.getIssuingAuthority());
        if (documentation.getLivingAreaId() != null) {
            stmt.setInt(5, documentation.getLivingAreaId());
        } else {
            stmt.setNull(5, Types.INTEGER);  
        }
        stmt.executeUpdate();
    }

    public void deleteDocumentationById(int id) throws SQLException {
        String query = "DELETE FROM documentation WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public void updateDocumentation(Documentation documentation) throws SQLException {
        String query = "UPDATE documentation SET document_type = ?, document_number = ?, issue_date = ?, issuing_authority = ?, living_area_id = ? WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, documentation.getDocumentType());
        stmt.setString(2, documentation.getDocumentNumber());
        stmt.setDate(3, documentation.getIssueDate());
        stmt.setString(4, documentation.getIssuingAuthority());
        if (documentation.getLivingAreaId() != null) {
            stmt.setInt(5, documentation.getLivingAreaId());
        } else {
            stmt.setNull(5, Types.INTEGER); 
        }
        stmt.setInt(6, documentation.getId());
        stmt.executeUpdate();
    }
}
