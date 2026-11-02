package com.usta.ecogrow.dao;

import com.usta.ecogrow.model.ConservationStatus;
import com.usta.ecogrow.model.DashboardStats;
import com.usta.ecogrow.model.PlantingActivity;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class StatsDao {

    private static int count(String table) {
        return Jdbc.query("SELECT COUNT(*) FROM " + table, rs -> rs.getInt(1)).getFirst();
    }

    public DashboardStats load() {
        double hectares = Jdbc.query("SELECT COALESCE(SUM(hectares), 0) FROM living_areas", rs -> rs.getDouble(1)).getFirst();
        long trees = Jdbc.query("SELECT COALESCE(SUM(trees_planted), 0) FROM planting_activities", rs -> rs.getLong(1)).getFirst();
        double avgCoverage = Jdbc.query("SELECT COALESCE(AVG(vegetation_coverage), 0) FROM forest_censuses",
                rs -> rs.getDouble(1)).getFirst();

        Map<ConservationStatus, Integer> byStatus = new EnumMap<>(ConservationStatus.class);
        for (ConservationStatus s : ConservationStatus.values()) {
            byStatus.put(s, 0);
        }
        Jdbc.query("SELECT conservation_status, COUNT(*) AS n FROM living_areas GROUP BY conservation_status",
                rs -> byStatus.merge(ConservationStatus.fromDb(rs.getString(1)), rs.getInt(2), Integer::sum));

        Map<String, Double> coverage = new LinkedHashMap<>();
        Jdbc.query("""
                SELECT a.name, AVG(c.vegetation_coverage) AS avg_cov
                  FROM forest_censuses c JOIN living_areas a ON a.id = c.living_area_id
                 GROUP BY a.id, a.name ORDER BY avg_cov DESC LIMIT 8
                """, rs -> coverage.put(rs.getString(1), rs.getDouble(2)));

        List<PlantingActivity> recent = Jdbc.query(
                PlantingActivityDao.SELECT + " ORDER BY p.activity_date DESC, p.id DESC LIMIT 5",
                PlantingActivityDao::map);

        return new DashboardStats(count("living_areas"), hectares, count("tree_species"), count("planting_activities"),
                trees, count("forest_censuses"), count("documentation"), avgCoverage, byStatus, coverage, recent);
    }
}
