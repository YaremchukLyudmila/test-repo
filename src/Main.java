import java.io.File;
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
        }
    }
}
