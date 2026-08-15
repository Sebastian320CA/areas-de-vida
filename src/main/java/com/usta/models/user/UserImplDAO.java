package com.usta.models.user;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.usta.utils.ConnectionManager;
import com.usta.utils.GenericDao;

public class UserImplDAO implements GenericDao<User> {

    private Connection connection;

    public UserImplDAO() {
        this.connection = ConnectionManager.getInstance().getConnection();
    }

    @Override
    public void add(User user) throws SQLException {
        String query = "INSERT INTO users (name, email, password, userType, document, phone) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getUserType());
            stmt.setString(5, user.getDocument());
            stmt.setString(6, user.getPhone());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error adding user to database: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error deleting user with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("userType"),
                        rs.getString("document"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving all users: " + e.getMessage(), e);
        }
        return users;
    }

    @Override
    public User getById(Integer id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("userType"),
                            rs.getString("document"),
                            rs.getString("phone")
                    );
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving user with id " + id + ": " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void update(User user) throws SQLException {
        String query = "UPDATE users SET name=?, email=?, password=?, userType=?, document=?, phone=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getUserType());
            stmt.setString(5, user.getDocument());
            stmt.setString(6, user.getPhone());
            stmt.setInt(7, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating user with id " + user.getId() + ": " + e.getMessage(), e);
        }
    }

    public User authenticate(String email, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE email=? AND password=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("userType"),
                            rs.getString("document"),
                            rs.getString("phone")
                    );
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error authenticating user: " + e.getMessage(), e);
        }
        return null;
    }
    
}