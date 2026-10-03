package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

/**
 * Консольний застосунок лабораторної роботи №1.
 *
 * <p>Варіант 5 — «Меню кафе».</p>
 *
 * <p>Програма читає записи меню з UTF-8 файла, перевіряє їх,
 * обчислює показники та формує текстовий звіт.</p>
 */
public final class Main {

    private static final Path DEFAULT_INPUT =
            Path.of("data", "input.csv");

    private static final Path DEFAULT_OUTPUT =
            Path.of("out", "report.txt");

    private static final String VERSION = "1.0.0";

    private Main() {
    }

    /**
     * Точка входу програми.
     *
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        Path input = DEFAULT_INPUT;
        Path output = DEFAULT_OUTPUT;

        for (int index = 0; index < args.length; index++) {
            switch (args[index]) {
                case "--help" -> {
                    printHelp();
                    return;
                }

                case "--version" -> {
                    System.out.println(VERSION);
                    return;
                }

                case "--input" -> {
                    if (index + 1 >= args.length) {
                        System.err.println(
                                "Помилка: після --input потрібно вказати шлях.");
                        return;
                    }

                    input = Path.of(args[++index]);
                }

                case "--output" -> {
                    if (index + 1 >= args.length) {
                        System.err.println(
                                "Помилка: після --output потрібно вказати шлях.");
                        return;
                    }

                    output = Path.of(args[++index]);
                }

                default -> {
                    System.err.printf(
                            "Помилка: невідомий аргумент \"%s\".%n",
                            args[index]);

                    printHelp();
                    return;
                }
            }
        }

        processFile(input, output);
    }

    /**
     * Читає файл, перевіряє записи та накопичує показники меню.
     *
     * @param input шлях до вхідного CSV-файла
     * @param output шлях до файла звіту
     */
    private static void processFile(Path input, Path output) {
        final List<String> lines;

        try {
            lines = Files.readAllLines(
                    input,
                    StandardCharsets.UTF_8);
        } catch (IOException exception) {
            System.err.printf(
                    "Не вдалося прочитати файл \"%s\": %s%n",
                    input,
                    exception.getMessage());
            return;
        }

        int validCount = 0;
        double totalPrice = 0.0;
        int maxWeight = Integer.MIN_VALUE;
        int maxPrepTime = Integer.MIN_VALUE;

        for (int index = 0; index < lines.size(); index++) {
            String line = lines.get(index);
            int lineNumber = index + 1;

            if (line.isBlank()) {
                printSkippedLine(
                        lineNumber,
                        "порожній рядок");
                continue;
            }

            String[] fields = line.split(";", -1);

            if (fields.length != 5) {
                printSkippedLine(
                        lineNumber,
                        "очікується 5 полів, отримано "
                                + fields.length);
                continue;
            }

            try {
                requireNonBlank(fields[0], "name");
                requireNonBlank(fields[1], "category");

                double price =
                        parseNonNegativeDouble(fields[2], "price");

                int weight =
                        parseNonNegativeInt(fields[3], "weightG");

                int prepTime =
                        parseNonNegativeInt(fields[4], "prepMin");

                validCount++;
                totalPrice += price;
                maxWeight = Math.max(maxWeight, weight);
                maxPrepTime = Math.max(maxPrepTime, prepTime);

            } catch (IllegalArgumentException exception) {
                printSkippedLine(
                        lineNumber,
                        exception.getMessage());
            }
        }

        String report = buildReport(
                validCount,
                totalPrice,
                maxWeight,
                maxPrepTime);

        System.out.print(report);
        writeReport(output, report);
    }

    /**
     * Перевіряє, що текстове поле не є порожнім.
     *
     * @param value значення поля
     * @param fieldName назва поля
     */
    private static void requireNonBlank(
            String value,
            String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "поле " + fieldName + " порожнє");
        }
    }

    /**
     * Перетворює текст у невід'ємне скінченне число double.
     *
     * @param value текстове значення
     * @param fieldName назва поля
     * @return перевірене число
     */
    private static double parseNonNegativeDouble(
            String value,
            String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "поле " + fieldName + " порожнє");
        }

        final double result;

        try {
            result = Double.parseDouble(value.trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "поле " + fieldName
                            + " має бути числом",
                    exception);
        }

        if (!Double.isFinite(result)) {
            throw new IllegalArgumentException(
                    "поле " + fieldName
                            + " має бути скінченним числом");
        }

        if (result < 0) {
            throw new IllegalArgumentException(
                    "поле " + fieldName
                            + " не може бути від'ємним");
        }

        return result;
    }

    /**
     * Перетворює текст у невід'ємне ціле число.
     *
     * @param value текстове значення
     * @param fieldName назва поля
     * @return перевірене ціле число
     */
    private static int parseNonNegativeInt(
            String value,
            String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "поле " + fieldName + " порожнє");
        }

        final int result;

        try {
            result = Integer.parseInt(value.trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "поле " + fieldName
                            + " має бути цілим числом",
                    exception);
        }

        if (result < 0) {
            throw new IllegalArgumentException(
                    "поле " + fieldName
                            + " не може бути від'ємним");
        }

        return result;
    }

    /**
     * Формує підсумковий текст звіту.
     *
     * @param validCount кількість коректних записів
     * @param totalPrice сума цін
     * @param maxWeight найбільша вага
     * @param maxPrepTime найдовший час приготування
     * @return текст звіту
     */
    private static String buildReport(
            int validCount,
            double totalPrice,
            int maxWeight,
            int maxPrepTime) {

        if (validCount == 0) {
            return String.format(
                    Locale.ROOT,
                    "Меню кафе — варіант 5%n"
                            + "Коректних записів: 0%n"
                            + "Показники не обчислено: "
                            + "немає коректних записів.%n");
        }

        double averagePrice = totalPrice / validCount;

        return String.format(
                Locale.ROOT,
                "Меню кафе — варіант 5%n"
                        + "Коректних записів: %d%n"
                        + "Середня ціна: %.2f%n"
                        + "Найбільша вага: %d г%n"
                        + "Найдовший час приготування: %d хв%n",
                validCount,
                averagePrice,
                maxWeight,
                maxPrepTime);
    }

    /**
     * Записує сформований звіт у UTF-8 файл.
     *
     * @param output шлях до файла
     * @param report текст звіту
     */
    private static void writeReport(
            Path output,
            String report) {

        try {
            Path parent = output.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.writeString(
                    output,
                    report,
                    StandardCharsets.UTF_8);

            System.out.printf(
                    "Звіт записано у: %s%n",
                    output);

        } catch (IOException exception) {
            System.err.printf(
                    "Не вдалося записати звіт \"%s\": %s%n",
                    output,
                    exception.getMessage());
        }
    }

    /**
     * Виводить причину пропуску некоректного рядка.
     *
     * @param lineNumber номер рядка
     * @param reason причина
     */
    private static void printSkippedLine(
            int lineNumber,
            String reason) {

        System.err.printf(
                "Пропущено рядок %d: %s%n",
                lineNumber,
                reason);
    }

    /**
     * Виводить довідку щодо параметрів командного рядка.
     */
    private static void printHelp() {
        System.out.printf(
                "Лабораторна робота №1, варіант 5 — Меню кафе%n"
                        + "%n"
                        + "Використання:%n"
                        + "  java ua.lpnu.kzp.Main [параметри]%n"
                        + "%n"
                        + "Параметри:%n"
                        + "  --help             показати довідку%n"
                        + "  --version          показати версію програми%n"
                        + "  --input <файл>     вхідний UTF-8 файл%n"
                        + "  --output <файл>    файл звіту%n"
                        + "%n"
                        + "За замовчуванням:%n"
                        + "  input:  data/input.csv%n"
                        + "  output: out/report.txt%n");
    }
}