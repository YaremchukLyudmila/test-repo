package ru.courses;

import java.time.ZonedDateTime;

public class Statistics {

    long totalTraffic = 0;
    ZonedDateTime minTime;
    ZonedDateTime maxTime;

    public void addEntry(LogEntry logEntry) {
        totalTraffic += logEntry.getResponseSize();
        if (minTime == null || minTime.isAfter(logEntry.getDate())) {
            minTime = logEntry.getDate();
        }
        if (maxTime == null || maxTime.isBefore(logEntry.getDate())) {
            maxTime = logEntry.getDate();
        }
    }

    // Средний трафик за час
    public long getTrafficRate() {
        if (minTime == null || maxTime == null) {
            return 0;
        }
        double diffInHour = 1.0 * (maxTime.toEpochSecond() - minTime.toEpochSecond()) / (60 * 60);
        return (long) (totalTraffic / diffInHour);
    }
}
