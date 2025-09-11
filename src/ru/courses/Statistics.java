package ru.courses;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Statistics {

    private long totalTraffic = 0;
    private long totalEntryCounter = 0;
    private ZonedDateTime minTime;
    private ZonedDateTime maxTime;
    final private Set<String> paths = new HashSet<>();
    final private Map<String, Integer> osCounter = new HashMap<>();

    public void addEntry(LogEntry logEntry) {
        totalEntryCounter++;
        totalTraffic += logEntry.getResponseSize();
        if (minTime == null || minTime.isAfter(logEntry.getDate())) {
            minTime = logEntry.getDate();
        }
        if (maxTime == null || maxTime.isBefore(logEntry.getDate())) {
            maxTime = logEntry.getDate();
        }
        if (logEntry.getResponseCode() == 200) {
            paths.add(logEntry.getPath());
        }
        if (!osCounter.containsKey(logEntry.getUserAgent().getOs())) {
            osCounter.put(logEntry.getUserAgent().getOs(), 0);
        }
        osCounter.put(logEntry.getUserAgent().getOs(), osCounter.get(logEntry.getUserAgent().getOs()) + 1);
    }

    // Средний трафик за час
    public long getTrafficRate() {
        if (minTime == null || maxTime == null) {
            return 0;
        }
        double diffInHour = 1.0 * (maxTime.toEpochSecond() - minTime.toEpochSecond()) / (60 * 60);
        return (long) (totalTraffic / diffInHour);
    }

    public Set<String> getExistsPaths() {
        return paths;
    }

    public Map<String, Double> getOsKindStatistic() {
        final Map<String, Double> response = new HashMap<>();
        osCounter.forEach((os, count) -> response.put(os, 1.0 * count / totalEntryCounter));
        return response;
    }
}
