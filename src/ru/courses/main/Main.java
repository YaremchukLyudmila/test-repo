package ru.courses.main;

import ru.courses.LogEntry;
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
            long lineCount = 0;
            long yandexCount1 = 0;
            long yandexCount2 = 0;
            long googleCount1 = 0;
            long googleCount2 = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                int length = line.length();
                if (length > 1024) {
                    throw new VeryLongStringException("Прочитана слишком длинная строка. Ее длина: " + length + " символов");
                }
                LogEntry logEntry = LogEntry.parse(line);
                String userAgent = logEntry.getUserAgent();
                String[] fragments = userAgent.split(";");
                if (fragments.length >= 2) {
                    String stripped = fragments[1].strip();
                    int slashPosition = stripped.indexOf('/');
                    String bot = slashPosition != -1 ? stripped.substring(0, slashPosition) : stripped;
                    if ("YandexBot".equals(bot)) {
                        yandexCount1++;
                    }
                    if (userAgent.contains("YandexBot")) {
                        yandexCount2++;
                    }
                    if ("Googlebot".equals(bot)) {
                        googleCount1++;
                    }
                    if (line.contains("Googlebot")) {
                        googleCount2++;
                    }
                }
            }
            System.out.println("Количество строк в файле: " + lineCount);
            System.out.printf("Количество запросов от бота yandex: %d, доля от общего числа запрсов: %f%n", yandexCount1, 1.0 * yandexCount1 / lineCount);
            System.out.printf("Количество запросов от бота yandex по второму алгоритму: %d, доля от общего числа запрсов: %f%n", yandexCount2, 1.0 * yandexCount2 / lineCount);
            System.out.printf("Количество запросов от бота google: %d, доля от общего числа запрсов: %f%n", googleCount1, 1.0 * googleCount1 / lineCount);
            System.out.printf("Количество запросов от бота google по второму алгоритму: %d, доля от общего числа запрсов: %f%n", googleCount2, 1.0 * googleCount2 / lineCount);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println();
    }
}
