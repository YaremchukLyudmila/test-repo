package ru.courses;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAgent {
    private static final Pattern PATTERN_1 = Pattern.compile("^(\\w+?)/.*?\\((\\w+?)[;, )].*$");
    private static final Pattern PATTERN_2 = Pattern.compile("^(\\w+)/?.*$");

    final String os;
    final String browser;

    public UserAgent(String str) {
        Matcher matcher = PATTERN_1.matcher(str);
        if (matcher.find()) {
            browser = matcher.group(1);
            os = matcher.group(2);
            return;
        }
        matcher = PATTERN_2.matcher(str);
        if (matcher.find()) {
            browser = matcher.group(1);
            os = "unknown";
            return;
        }
        os = "unknown";
        browser = "unknown";
    }
}
