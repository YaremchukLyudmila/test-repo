package ru.courses;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {

    private static final Pattern pattern = Pattern.compile("^(\\d+\\.\\d+\\.\\d+\\.\\d+) -? -? \\[(.+)] \"(.+?) (.+) (.+?)\" (\\d+) (\\d+) \"(.+)\" \"(.+)\"");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    final private String ip;
    final private ZonedDateTime date; // Предпочла формат с хранением счещения так как исходные данные тоже содержат смезение, возможно будет полезно в дальнейшей аналитике
    final private HttpMethod method;
    final private String path;
    final private String protocol;
    final short responseCode;
    final private long responseSize;
    final private String referer;
    final private UserAgent userAgent;

    public String getIp() {
        return ip;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public short getResponseCode() {
        return responseCode;
    }

    public long getResponseSize() {
        return responseSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }

    public LogEntry(String line) {
        Matcher matcher = pattern.matcher(line);
        if (!matcher.find()) {
            throw new RuntimeException("Ошибка разбора строки: " + line);
        }
        ip = matcher.group(1);
        date = ZonedDateTime.from(DATE_TIME_FORMATTER.parse(matcher.group(2)));
        method = HttpMethod.valueOf(matcher.group(3));
        path = matcher.group(4);
        protocol = matcher.group(5);
        responseCode = Short.parseShort(matcher.group(6));
        responseSize = Long.parseLong(matcher.group(7));
        referer = matcher.group(8);
        userAgent = new UserAgent(matcher.group(9));
    }

    enum HttpMethod {
        GET,
        POST,
        PATH,
        PUT,
        OPTIONS,
        CONNECT,
        HEAD,
        DELETE;
    }

}
