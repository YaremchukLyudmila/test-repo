package ru.courses;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {

    private static final Pattern pattern = Pattern.compile("^(\\d+\\.\\d+\\.\\d+\\.\\d+) -? -? \\[(.+)] \"(.+)\" (\\d+) (\\d+) \"(.+)\" \"(.+)\"");

    private String ip;
    private String date;
    private String methodAndPath;
    private int responseCode;
    private long responseSize;
    private String path;
    private String userAgent;

    public LogEntry(String ip, String date, String methodAndPath, int responseCode, long responseSize, String path, String userAgent) {
        this.ip = ip;
        this.date = date;
        this.methodAndPath = methodAndPath;
        this.responseCode = responseCode;
        this.responseSize = responseSize;
        this.path = path;
        this.userAgent = userAgent;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public static LogEntry parse(String line) {
        Matcher matcher = pattern.matcher(line);
        if (!matcher.find()) {
            throw new RuntimeException("Ошибка разбора строки: " + line);
        }
        return new LogEntry(
                matcher.group(1),
                matcher.group(2),
                matcher.group(3),
                Integer.parseInt(matcher.group(4)),
                Long.parseLong(matcher.group(5)),
                matcher.group(6),
                matcher.group(7)
        );
    }

}
