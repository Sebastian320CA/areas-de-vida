package com.usta.ecogrow.dao;

import com.usta.ecogrow.db.DataAccessException;
import com.usta.ecogrow.db.Database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Pequeño ayudante JDBC: cada operación toma una conexión del pool y la devuelve (try-with-resources). */
final class Jdbc {

    @FunctionalInterface
    interface Binder {
        void bind(PreparedStatement ps) throws SQLException;
    }

    @FunctionalInterface
    interface Mapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    private Jdbc() {}

    static <T> List<T> query(String sql, Binder binder, Mapper<T> mapper) {
        try (Connection c = Database.dataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            binder.bind(ps);
            try (ResultSet rs = ps.executeQuery()) {
                List<T> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(mapper.map(rs));
                }
                return out;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    static <T> List<T> query(String sql, Mapper<T> mapper) {
        return query(sql, ps -> { }, mapper);
    }

    static int update(String sql, Binder binder) {
        try (Connection c = Database.dataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            binder.bind(ps);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /** Inserta y devuelve la clave generada. */
    static int insert(String sql, Binder binder) {
        try (Connection c = Database.dataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            binder.bind(ps);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    static Integer intOrNull(ResultSet rs, String column) throws SQLException {
        int v = rs.getInt(column);
        return rs.wasNull() ? null : v;
    }

    static void setIntOrNull(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.INTEGER);
        } else {
            ps.setInt(index, value);
        }
    }

    static LocalDate date(ResultSet rs, String column) throws SQLException {
        Date d = rs.getDate(column);
        return d == null ? null : d.toLocalDate();
    }

    static void setDate(PreparedStatement ps, int index, LocalDate value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.DATE);
        } else {
            ps.setDate(index, Date.valueOf(value));
        }
    }
}
