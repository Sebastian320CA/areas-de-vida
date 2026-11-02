package com.usta.ecogrow.dao;

import com.usta.ecogrow.model.Role;
import com.usta.ecogrow.model.User;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public final class UserDao implements CrudDao<User> {

    private static final String COLS = "id, name, email, password_hash, role, document, phone";

    private static User map(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
                rs.getString("password_hash"), Role.fromDb(rs.getString("role")),
                rs.getString("document"), rs.getString("phone"));
    }

    @Override
    public List<User> findAll() {
        return Jdbc.query("SELECT " + COLS + " FROM users ORDER BY name", UserDao::map);
    }

    public Optional<User> findByEmail(String email) {
        return Jdbc.query("SELECT " + COLS + " FROM users WHERE email = ?", ps -> ps.setString(1, email), UserDao::map)
                .stream().findFirst();
    }

    /** Si passwordHash viene vacío en una actualización, se conserva la contraseña actual. */
    @Override
    public User save(User u) {
        if (u.id() == 0) {
            int id = Jdbc.insert(
                    "INSERT INTO users (name, email, password_hash, role, document, phone) VALUES (?,?,?,?,?,?)",
                    ps -> {
                        ps.setString(1, u.name());
                        ps.setString(2, u.email());
                        ps.setString(3, u.passwordHash());
                        ps.setString(4, u.role().name());
                        ps.setString(5, u.document());
                        ps.setString(6, u.phone());
                    });
            return u.withId(id);
        }
        boolean changePassword = u.passwordHash() != null && !u.passwordHash().isBlank();
        String sql = changePassword
                ? "UPDATE users SET name=?, email=?, role=?, document=?, phone=?, password_hash=? WHERE id=?"
                : "UPDATE users SET name=?, email=?, role=?, document=?, phone=? WHERE id=?";
        Jdbc.update(sql, ps -> {
            ps.setString(1, u.name());
            ps.setString(2, u.email());
            ps.setString(3, u.role().name());
            ps.setString(4, u.document());
            ps.setString(5, u.phone());
            if (changePassword) {
                ps.setString(6, u.passwordHash());
                ps.setInt(7, u.id());
            } else {
                ps.setInt(6, u.id());
            }
        });
        return u;
    }

    @Override
    public void delete(int id) {
        Jdbc.update("DELETE FROM users WHERE id = ?", ps -> ps.setInt(1, id));
    }

    public int countByRole(Role role) {
        return Jdbc.query("SELECT COUNT(*) FROM users WHERE role = ?", ps -> ps.setString(1, role.name()),
                rs -> rs.getInt(1)).getFirst();
    }
}
