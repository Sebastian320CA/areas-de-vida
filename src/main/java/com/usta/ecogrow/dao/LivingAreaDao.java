package com.usta.ecogrow.dao;

import com.usta.ecogrow.model.ConservationStatus;
import com.usta.ecogrow.model.LivingArea;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class LivingAreaDao implements CrudDao<LivingArea> {

    private static LivingArea map(ResultSet rs) throws SQLException {
        return new LivingArea(rs.getInt("id"), rs.getString("name"), rs.getString("location"),
                rs.getDouble("hectares"), ConservationStatus.fromDb(rs.getString("conservation_status")),
                Jdbc.date(rs, "creation_date"));
    }

    private static void bind(PreparedStatement ps, LivingArea a) throws SQLException {
        ps.setString(1, a.name());
        ps.setString(2, a.location());
        ps.setDouble(3, a.hectares());
        ps.setString(4, a.status().name());
        Jdbc.setDate(ps, 5, a.creationDate());
    }

    @Override
    public List<LivingArea> findAll() {
        return Jdbc.query("SELECT id, name, location, hectares, conservation_status, creation_date "
                + "FROM living_areas ORDER BY name", LivingAreaDao::map);
    }

    @Override
    public LivingArea save(LivingArea a) {
        if (a.id() == 0) {
            int id = Jdbc.insert("INSERT INTO living_areas (name, location, hectares, conservation_status, creation_date) "
                    + "VALUES (?,?,?,?,?)", ps -> bind(ps, a));
            return a.withId(id);
        }
        Jdbc.update("UPDATE living_areas SET name=?, location=?, hectares=?, conservation_status=?, creation_date=? "
                + "WHERE id=?", ps -> {
            bind(ps, a);
            ps.setInt(6, a.id());
        });
        return a;
    }

    @Override
    public void delete(int id) {
        Jdbc.update("DELETE FROM living_areas WHERE id = ?", ps -> ps.setInt(1, id));
    }
}
