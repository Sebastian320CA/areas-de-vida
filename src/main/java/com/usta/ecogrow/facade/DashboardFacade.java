package com.usta.ecogrow.facade;

import com.usta.ecogrow.dao.StatsDao;
import com.usta.ecogrow.model.DashboardStats;

/** Fachada para el panel principal: reúne en una sola llamada todas las métricas. */
public class DashboardFacade {

    private final StatsDao stats = new StatsDao();

    public DashboardStats load() {
        return stats.load();
    }
}
