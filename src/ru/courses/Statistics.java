package ru.courses;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Statistics {

    private static final Pattern EXTRACT_DOMAIN_PATTERN = Pattern.compile("^.*?//(.+?)/.*$");

    private long totalTraffic = 0;
    private long totalEntryCounter = 0;
    private long notBotRequestCounter = 0;
    private long errorResponseCounter = 0;
    private ZonedDateTime minTime;
    private ZonedDateTime maxTime;
    final private Set<String> paths = new HashSet<>();
    final private Set<String> notFoundPaths = new HashSet<>();
    final private Set<String> refereeDomains = new HashSet<>();
    final private Map<String, Integer> osCounter = new HashMap<>();
    final private Map<String, Integer> ipVisitCounter = new HashMap<>();
    final private Map<String, Integer> browserCounter = new HashMap<>();
    final private Map<Long, Integer> requestPerSecond = new HashMap<>();

    public void addEntry(LogEntry logEntry) {
        totalEntryCounter++;
        if (!logEntry.getUserAgent().isBot()) {
            notBotRequestCounter++;

            if (!ipVisitCounter.containsKey(logEntry.getIp())) {
                ipVisitCounter.put(logEntry.getIp(), 0);
            }
            ipVisitCounter.put(logEntry.getIp(), ipVisitCounter.get(logEntry.getIp()) + 1);

            if (!requestPerSecond.containsKey(logEntry.getDate().toEpochSecond())) {
                requestPerSecond.put(logEntry.getDate().toEpochSecond(), 0);
            }
            requestPerSecond.put(logEntry.getDate().toEpochSecond(), requestPerSecond.get(logEntry.getDate().toEpochSecond()) + 1);

            if (!browserCounter.containsKey(logEntry.getUserAgent().getBrowser())) {
                browserCounter.put(logEntry.getUserAgent().getBrowser(), 0);
            }
            browserCounter.put(logEntry.getUserAgent().getBrowser(), browserCounter.get(logEntry.getUserAgent().getBrowser()) + 1);

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
        if (logEntry.getResponseCode() >=400) {
            errorResponseCounter++;
        }

        if (!osCounter.containsKey(logEntry.getUserAgent().getOs())) {
            osCounter.put(logEntry.getUserAgent().getOs(), 0);
        }
        osCounter.put(logEntry.getUserAgent().getOs(), osCounter.get(logEntry.getUserAgent().getOs()) + 1);

        Matcher matcher = EXTRACT_DOMAIN_PATTERN.matcher(logEntry.getReferer());
        if (matcher.find()) {
            refereeDomains.add(matcher.group(1));
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

    // Среднее число ответов 4хх и 5хх в час
    public long getErrorPerHour() {
        if (minTime == null || maxTime == null) {
            return 0;
        }
        double diffInHour = 1.0 * (maxTime.toEpochSecond() - minTime.toEpochSecond()) / (60 * 60);
        return (long) (errorResponseCounter / diffInHour);
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

    public Map<String, Double> getBrowserCounter() {
        final Map<String, Double> response = new TreeMap<>();
        browserCounter.forEach((browser, count) -> response.put(browser, 1.0 * count / notBotRequestCounter));
        return response;
    }

    public SecondAndCount getMaxRequestPerSecond() {
        if (requestPerSecond.isEmpty()) {
            return new SecondAndCount(0, 0);
        }
        return requestPerSecond.keySet().stream()
                .map(key -> new SecondAndCount(key, requestPerSecond.get(key)))
                .max(Comparator.comparingInt(SecondAndCount::getCount))
                .get();
    }

    public Set<String> getRefereeDomains() {
        return refereeDomains;
    }

    public IpAndCount getMaxRequestPerIp() {
        if (ipVisitCounter.isEmpty()) {
            return new IpAndCount("0.0.0.0", 0);
        }
        return ipVisitCounter.keySet().stream()
                .map(key -> new IpAndCount(key, ipVisitCounter.get(key)))
                .max(Comparator.comparingInt(IpAndCount::getCount))
                .get();
    }

}
