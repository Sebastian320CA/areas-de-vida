package com.usta.ecogrow.dao;

import com.usta.ecogrow.model.PlantingActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class PlantingActivityDao implements CrudDao<PlantingActivity> {

    static final String SELECT = """
            SELECT p.id, p.activity_date, p.activity_type, p.description, p.trees_planted,
                   p.living_area_id, a.name AS area_name, p.tree_id,
                   COALESCE(NULLIF(t.common_name, ''), t.species) AS tree_name
              FROM planting_activities p
              JOIN living_areas a ON a.id = p.living_area_id
              LEFT JOIN tree_species t ON t.id = p.tree_id
            """;

    static PlantingActivity map(ResultSet rs) throws SQLException {
        return new PlantingActivity(rs.getInt("id"), Jdbc.date(rs, "activity_date"), rs.getString("activity_type"),
                rs.getString("description"), rs.getInt("trees_planted"), rs.getInt("living_area_id"),
                rs.getString("area_name"), Jdbc.intOrNull(rs, "tree_id"), rs.getString("tree_name"));
    }

    private static void bind(PreparedStatement ps, PlantingActivity p) throws SQLException {
        Jdbc.setDate(ps, 1, p.date());
        ps.setString(2, p.type());
        ps.setString(3, p.description());
        ps.setInt(4, p.treesPlanted());
        ps.setInt(5, p.areaId());
        Jdbc.setIntOrNull(ps, 6, p.treeId());
    }

    @Override
    public List<PlantingActivity> findAll() {
        return Jdbc.query(SELECT + " ORDER BY p.activity_date DESC, p.id DESC", PlantingActivityDao::map);
    }

    @Override
    public PlantingActivity save(PlantingActivity p) {
        if (p.id() == 0) {
            int id = Jdbc.insert("INSERT INTO planting_activities "
                    + "(activity_date, activity_type, description, trees_planted, living_area_id, tree_id) "
                    + "VALUES (?,?,?,?,?,?)", ps -> bind(ps, p));
            return p.withId(id);
        }
        Jdbc.update("UPDATE planting_activities SET activity_date=?, activity_type=?, description=?, "
                + "trees_planted=?, living_area_id=?, tree_id=? WHERE id=?", ps -> {
            bind(ps, p);
            ps.setInt(7, p.id());
        });
        return p;
    }

    @Override
    public void delete(int id) {
        Jdbc.update("DELETE FROM planting_activities WHERE id = ?", ps -> ps.setInt(1, id));
    }
}
