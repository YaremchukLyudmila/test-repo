package ru.courses.main;

import ru.courses.VeryLongStringException;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int correctPathCount = 0;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String path = scanner.nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            if (!fileExists) {
                System.out.println("По указанному пути файл не найден");
                continue;
            }
            boolean isDirectory = file.isDirectory();
            if (isDirectory) {
                System.out.println("Введен путь к директории, а не файлу");
                continue;
            }
            correctPathCount++;
            System.out.println("Это файл номер " + correctPathCount);
            System.out.println();

            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);
                long lineCount = 0;
                String line;
                String shortLine = null;
                String longLine = null;
                while ((line = reader.readLine()) != null) {
                    lineCount++;
                    int length = line.length();
                    if (length > 1024) {
                        throw new VeryLongStringException("Прочитана слишком длинная строка. Ее длина: " + length + " символов");
                    }
                    if (longLine == null || length > longLine.length()) {
                        longLine = line;
                    }
                    if (shortLine == null || length < shortLine.length()) {
                        shortLine = line;
                    }
                }
                System.out.println("Количество строк в файле: " + lineCount);
                System.out.println("Длина самой длинной строки в файле: " + longLine.length());
                System.out.println("Длина самой короткой строки в файле: " + shortLine.length());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println();
        }
    }
}
