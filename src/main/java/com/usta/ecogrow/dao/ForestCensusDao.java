package com.usta.ecogrow.dao;

import com.usta.ecogrow.model.ForestCensus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class ForestCensusDao implements CrudDao<ForestCensus> {

    private static final String SELECT = """
            SELECT c.id, c.census_date, c.vegetation_coverage, c.ecosystem_state, c.observations,
                   c.living_area_id, a.name AS area_name
              FROM forest_censuses c
              JOIN living_areas a ON a.id = c.living_area_id
            """;

    private static ForestCensus map(ResultSet rs) throws SQLException {
        return new ForestCensus(rs.getInt("id"), Jdbc.date(rs, "census_date"), rs.getDouble("vegetation_coverage"),
                rs.getString("ecosystem_state"), rs.getString("observations"),
                rs.getInt("living_area_id"), rs.getString("area_name"));
    }

    private static void bind(PreparedStatement ps, ForestCensus c) throws SQLException {
        Jdbc.setDate(ps, 1, c.date());
        ps.setDouble(2, c.coverage());
        ps.setString(3, c.ecosystemState());
        ps.setString(4, c.observations());
        ps.setInt(5, c.areaId());
    }

    @Override
    public List<ForestCensus> findAll() {
        return Jdbc.query(SELECT + " ORDER BY c.census_date DESC, c.id DESC", ForestCensusDao::map);
    }

    @Override
    public ForestCensus save(ForestCensus c) {
        if (c.id() == 0) {
            int id = Jdbc.insert("INSERT INTO forest_censuses "
                    + "(census_date, vegetation_coverage, ecosystem_state, observations, living_area_id) "
                    + "VALUES (?,?,?,?,?)", ps -> bind(ps, c));
            return c.withId(id);
        }
        Jdbc.update("UPDATE forest_censuses SET census_date=?, vegetation_coverage=?, ecosystem_state=?, "
                + "observations=?, living_area_id=? WHERE id=?", ps -> {
            bind(ps, c);
            ps.setInt(6, c.id());
        });
        return c;
    }

    @Override
    public void delete(int id) {
        Jdbc.update("DELETE FROM forest_censuses WHERE id = ?", ps -> ps.setInt(1, id));
    }
}
