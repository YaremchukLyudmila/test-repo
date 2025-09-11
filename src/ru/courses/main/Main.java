package ru.courses.main;

import ru.courses.LogEntry;
import ru.courses.Statistics;
import ru.courses.VeryLongStringException;

import java.io.*;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String path = args[0];
        File file = new File(path);
        boolean fileExists = file.exists();
        if (!fileExists) {
            System.out.println("По указанному пути файл не найден");
            return;
        }
        boolean isDirectory = file.isDirectory();
        if (isDirectory) {
            System.out.println("Введен путь к директории, а не файлу");
            return;
        }
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader reader = new BufferedReader(fileReader);
            Statistics statistics = new Statistics();
            long lineCount = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                int length = line.length();
                if (length > 1024) {
                    throw new VeryLongStringException("Прочитана слишком длинная строка. Ее длина: " + length + " символов");
                }
                LogEntry logEntry = new LogEntry(line);
                statistics.addEntry(logEntry);
            }
            System.out.println("Количество строк в файле: " + lineCount);
            System.out.println("Трафик: " + statistics.getTrafficRate() + " байт/ч");

            System.out.println("Список существующих url:");
            statistics.getExistsPaths().forEach(url -> System.out.println("   " + url));
            System.out.println();

            System.out.println("Список запрошенных, но отсутствующих url:");
            statistics.getNotFoundPaths().forEach(url -> System.out.println("   " + url));
            System.out.println();

            double sumStat = 0;
            Map<String, Double> osKindStatistic = statistics.getOsKindStatistic();
            System.out.println("Статистика ОС:");
            for (String os: osKindStatistic.keySet()) {
                System.out.printf("   %s: %f%n", os, osKindStatistic.get(os));
                sumStat += osKindStatistic.get(os);
            }
            System.out.printf("Сумма долей ОС: %f%n", sumStat);
            System.out.println();

            System.out.println("Среднее число запросов не от ботов в час: " + statistics.getRequestPerHour());
            System.out.println();

            double sumStatByIp = 0;
            Map<String, Double> ipVisitCounter = statistics.getIpVisitCounter();
            System.out.println("Статистика посещений по пользователям:");
            for (String ip: ipVisitCounter.keySet()) {
                System.out.printf("   %s: %f%n", ip, ipVisitCounter.get(ip));
                sumStatByIp += ipVisitCounter.get(ip);
            }
            System.out.printf("Сумма долей ОС: %f%n", sumStatByIp);
            System.out.println();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
