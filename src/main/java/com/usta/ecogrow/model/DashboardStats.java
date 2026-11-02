package com.usta.ecogrow.model;

import java.util.List;
import java.util.Map;

public record DashboardStats(int areas, double hectares, int species, int plantings, long treesPlanted,
                             int censuses, int documents, double averageCoverage,
                             Map<ConservationStatus, Integer> byStatus,
                             Map<String, Double> coverageByArea,
                             List<PlantingActivity> recentActivities) {
}
