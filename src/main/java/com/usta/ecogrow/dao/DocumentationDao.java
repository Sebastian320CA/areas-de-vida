package com.usta.ecogrow.dao;

import com.usta.ecogrow.model.Documentation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class DocumentationDao implements CrudDao<Documentation> {

    private static final String SELECT = """
            SELECT d.id, d.document_type, d.document_number, d.issue_date, d.issuing_authority,
                   d.living_area_id, a.name AS area_name
              FROM documentation d
              LEFT JOIN living_areas a ON a.id = d.living_area_id
            """;

    private static Documentation map(ResultSet rs) throws SQLException {
        return new Documentation(rs.getInt("id"), rs.getString("document_type"), rs.getString("document_number"),
                Jdbc.date(rs, "issue_date"), rs.getString("issuing_authority"),
                Jdbc.intOrNull(rs, "living_area_id"), rs.getString("area_name"));
    }

    private static void bind(PreparedStatement ps, Documentation d) throws SQLException {
        ps.setString(1, d.type());
        ps.setString(2, d.number());
        Jdbc.setDate(ps, 3, d.issueDate());
        ps.setString(4, d.authority());
        Jdbc.setIntOrNull(ps, 5, d.areaId());
    }

    @Override
    public List<Documentation> findAll() {
        return Jdbc.query(SELECT + " ORDER BY d.issue_date DESC, d.id DESC", DocumentationDao::map);
    }

    @Override
    public Documentation save(Documentation d) {
        if (d.id() == 0) {
            int id = Jdbc.insert("INSERT INTO documentation "
                    + "(document_type, document_number, issue_date, issuing_authority, living_area_id) "
                    + "VALUES (?,?,?,?,?)", ps -> bind(ps, d));
            return d.withId(id);
        }
        Jdbc.update("UPDATE documentation SET document_type=?, document_number=?, issue_date=?, "
                + "issuing_authority=?, living_area_id=? WHERE id=?", ps -> {
            bind(ps, d);
            ps.setInt(6, d.id());
        });
        return d;
    }

    @Override
    public void delete(int id) {
        Jdbc.update("DELETE FROM documentation WHERE id = ?", ps -> ps.setInt(1, id));
    }
}
