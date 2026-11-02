package com.usta.ecogrow.dao;

import com.usta.ecogrow.model.TreeSpecies;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class TreeSpeciesDao implements CrudDao<TreeSpecies> {

    private static TreeSpecies map(ResultSet rs) throws SQLException {
        return new TreeSpecies(rs.getInt("id"), rs.getString("species"), rs.getString("genus"),
                rs.getString("family"), rs.getString("common_name"));
    }

    private static void bind(PreparedStatement ps, TreeSpecies t) throws SQLException {
        ps.setString(1, t.species());
        ps.setString(2, t.genus());
        ps.setString(3, t.family());
        ps.setString(4, t.commonName());
    }

    @Override
    public List<TreeSpecies> findAll() {
        return Jdbc.query("SELECT id, species, genus, family, common_name FROM tree_species ORDER BY common_name, species",
                TreeSpeciesDao::map);
    }

    @Override
    public TreeSpecies save(TreeSpecies t) {
        if (t.id() == 0) {
            int id = Jdbc.insert("INSERT INTO tree_species (species, genus, family, common_name) VALUES (?,?,?,?)",
                    ps -> bind(ps, t));
            return t.withId(id);
        }
        Jdbc.update("UPDATE tree_species SET species=?, genus=?, family=?, common_name=? WHERE id=?", ps -> {
            bind(ps, t);
            ps.setInt(5, t.id());
        });
        return t;
    }

    @Override
    public void delete(int id) {
        Jdbc.update("DELETE FROM tree_species WHERE id = ?", ps -> ps.setInt(1, id));
    }
}
