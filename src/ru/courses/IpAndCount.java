package ru.courses;

public class IpAndCount {
    final private String ip;
    final private int count;

    public IpAndCount(String ip, int count) {
        this.ip = ip;
        this.count = count;
    }

    public String getIp() {
        return ip;
    }

    public int getCount() {
        return count;
    }
}
