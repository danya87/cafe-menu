package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Консольний застосунок для обробки меню кафе.
 */
public final class Main {

    private static final Path DEFAULT_INPUT =
            Path.of("data", "input.csv");

    private static final Path DEFAULT_OUTPUT =
            Path.of("out", "report.txt");

    private static final String VERSION = "1.0.0";

    private Main() {
        // Utility class.
    }

    /**
     * Точка входу в програму.
     *
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        try {
            run(args);
        } catch (IOException | IllegalArgumentException exception) {
            System.err.println("Помилка: " + exception.getMessage());
        }
    }

    private static void run(String[] args) throws IOException {
        Path input = DEFAULT_INPUT;
        Path output = DEFAULT_OUTPUT;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--help" -> {
                    printHelp();
                    return;
                }

                case "--version" -> {
                    System.out.println(VERSION);
                    return;
                }

                case "--input" -> {
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException(
                                "для --input потрібно вказати шлях");
                    }

                    input = Path.of(args[++i]);
                }

                case "--output" -> {
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException(
                                "для --output потрібно вказати шлях");
                    }

                    output = Path.of(args[++i]);
                }

                default -> throw new IllegalArgumentException(
                        "невідомий параметр: " + args[i]);
            }
        }

        List<String> lines =
                Files.readAllLines(input, StandardCharsets.UTF_8);

        List<MenuItem> items = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            try {
                MenuItem item = MenuItem.fromCsv(line);
                items.add(item);
            } catch (IllegalArgumentException exception) {
                System.err.printf(
                        "Пропущено рядок %d: %s%n",
                        i + 1,
                        exception.getMessage());
            }
        }

        String report = createReport(items);

        System.out.print(report);

        Path parent = output.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.writeString(
                output,
                report,
                StandardCharsets.UTF_8);
    }

    /**
     * Формує підсумковий звіт для коректних позицій меню.
     *
     * @param items коректні позиції меню
     * @return текстовий звіт
     */
    private static String createReport(List<MenuItem> items) {
        String newline = System.lineSeparator();

        StringBuilder report = new StringBuilder();

        report.append("Меню кафе — варіант 5")
                .append(newline);

        report.append("Коректних записів: ")
                .append(items.size())
                .append(newline);

        if (items.isEmpty()) {
            report.append(
                    "Показники не обчислено: немає коректних записів.")
                    .append(newline);

            return report.toString();
        }

        double averagePrice = items.stream()
                .mapToDouble(MenuItem::getPrice)
                .average()
                .orElse(0.0);

        int maxWeight = items.stream()
                .mapToInt(MenuItem::getWeightG)
                .max()
                .orElse(0);

        int maxPrepTime = items.stream()
                .mapToInt(MenuItem::getPrepMin)
                .max()
                .orElse(0);

        report.append(String.format(
                Locale.ROOT,
                "Середня ціна: %.2f%s",
                averagePrice,
                newline));

        report.append("Найбільша вага: ")
                .append(maxWeight)
                .append(" г")
                .append(newline);

        report.append("Найдовший час приготування: ")
                .append(maxPrepTime)
                .append(" хв")
                .append(newline);

        return report.toString();
    }

    private static void printHelp() {
        System.out.println("""
                Використання:
                  java -jar lab01-1.0.0.jar [options]

                Параметри:
                  --help          показати довідку
                  --version       показати версію програми
                  --input FILE    шлях до вхідного файла
                  --output FILE   шлях до файла звіту
                """);
    }
}