package ru.courses.main;

import ru.courses.LogEntry;
import ru.courses.Statistics;
import ru.courses.VeryLongStringException;

import java.io.*;

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
            System.out.println("Статистика ОС:");
            for (String os: statistics.getOsKindStatistic().keySet()) {
                System.out.printf("   %s: %f%n", os, statistics.getOsKindStatistic().get(os));
                sumStat += statistics.getOsKindStatistic().get(os);
            }
            System.out.printf("Сумма долей ОС: %f", sumStat);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
