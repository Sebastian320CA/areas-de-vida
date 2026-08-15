package com.usta.models.treetaxonomy;

import com.usta.utils.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TreeTaxonomyImplDao {
    private final Connection connection;

    public TreeTaxonomyImplDao() {
        connection = ConnectionManager.getInstance().getConnection();
        if (connection == null) {
            throw new RuntimeException("Failed to initialize connection in TreeTaxonomyImplDao.");
        }
    }

    public void addTreeTaxonomy(String species, String genus, String family, String commonName) {
        String query = "INSERT INTO tree_taxonomy (species, genus, family, common_name) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, species);
            preparedStatement.setString(2, genus);
            preparedStatement.setString(3, family);
            preparedStatement.setString(4, commonName);
            preparedStatement.executeUpdate();
            System.out.println("Tree taxonomy added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding tree taxonomy: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateTreeTaxonomy(TreeTaxonomy tree) {
        String query = "UPDATE tree_taxonomy SET species = ?, genus = ?, family = ?, common_name = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, tree.getSpecies());
            preparedStatement.setString(2, tree.getGenus());
            preparedStatement.setString(3, tree.getFamily());
            preparedStatement.setString(4, tree.getCommonName());
            preparedStatement.executeUpdate();
            System.out.println("Tree taxonomy updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating tree taxonomy: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteTreeTaxonomy(TreeTaxonomy tree) {
        String query = "DELETE FROM tree_taxonomy WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
            System.out.println("Tree taxonomy deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting tree taxonomy: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public List<TreeTaxonomy> getAllTreeTaxonomies() {
    List<TreeTaxonomy> trees = new ArrayList<>();
    String query = "SELECT species, genus, family, common_name FROM tree_taxonomy";

    try (PreparedStatement preparedStatement = connection.prepareStatement(query);
         ResultSet resultSet = preparedStatement.executeQuery()) {

        while (resultSet.next()) {
            String species = resultSet.getString("species");
            String genus = resultSet.getString("genus");
            String family = resultSet.getString("family");
            String commonName = resultSet.getString("common_name");

            trees.add(new TreeTaxonomy(species, genus, family, commonName));
        }
    } catch (SQLException e) {
        System.err.println("Error fetching tree taxonomies: " + e.getMessage());
        e.printStackTrace();
    }
    return trees;
}
}
