package com.usta.ecogrow.db;

import com.usta.ecogrow.config.AppConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * Singleton que administra el pool de conexiones (reemplaza al antiguo ConnectionManager,
 * que mantenía una única Connection compartida por toda la aplicación).
 */
public final class Database {

    private static HikariDataSource dataSource;

    private Database() {}

    public static synchronized DataSource dataSource() {
        if (dataSource == null) {
            HikariConfig cfg = new HikariConfig();
            cfg.setJdbcUrl(AppConfig.get("db.url", "jdbc:mysql://localhost:3306/ecogrow_v2"));
            cfg.setUsername(AppConfig.get("db.user", "root"));
            cfg.setPassword(AppConfig.get("db.password", ""));
            cfg.setPoolName("EcoGrowPool");
            cfg.setMaximumPoolSize(6);
            cfg.setMinimumIdle(1);
            cfg.setConnectionTimeout(5_000);
            dataSource = new HikariDataSource(cfg);
        }
        return dataSource;
    }

    public static synchronized void shutdown() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }
}
