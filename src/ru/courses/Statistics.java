package ru.courses;

import java.time.ZonedDateTime;
import java.util.*;

public class Statistics {

    private long totalTraffic = 0;
    private long totalEntryCounter = 0;
    private long notBotRequestCounter = 0;
    private ZonedDateTime minTime;
    private ZonedDateTime maxTime;
    final private Set<String> paths = new HashSet<>();
    final private Set<String> notFoundPaths = new HashSet<>();
    final private Map<String, Integer> osCounter = new HashMap<>();
    final private Map<String, Integer> ipVisitCounter = new HashMap<>();

    public void addEntry(LogEntry logEntry) {
        totalEntryCounter++;
        if (!logEntry.getUserAgent().isBot()) {
            notBotRequestCounter++;
            if (!ipVisitCounter.containsKey(logEntry.getIp())) {
                ipVisitCounter.put(logEntry.getIp(), 0);
            }
            ipVisitCounter.put(logEntry.getIp(), ipVisitCounter.get(logEntry.getIp()) + 1);
        }
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
        if (logEntry.getResponseCode() == 404) {
            notFoundPaths.add(logEntry.getPath());
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

    public Set<String> getNotFoundPaths() {
        return notFoundPaths;
    }

    public Map<String, Double> getOsKindStatistic() {
        final Map<String, Double> response = new TreeMap<>();
        osCounter.forEach((os, count) -> response.put(os, 1.0 * count / totalEntryCounter));
        return response;
    }

    public long getRequestPerHour() {
        if (minTime == null || maxTime == null) {
            return 0;
        }
        double diffInHour = 1.0 * (maxTime.toEpochSecond() - minTime.toEpochSecond()) / (60 * 60);
        return (long) (notBotRequestCounter / diffInHour);
    }

    public Map<String, Double> getIpVisitCounter() {
        final Map<String, Double> response = new TreeMap<>();
        ipVisitCounter.forEach((os, count) -> response.put(os, 1.0 * count / notBotRequestCounter));
        return response;
    }
}
