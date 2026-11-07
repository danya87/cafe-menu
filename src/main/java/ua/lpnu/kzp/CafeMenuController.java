package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Керує основним процесом обробки меню кафе.
 */
public final class CafeMenuController {

    /**
     * Виконує програму відповідно до переданих параметрів.
     *
     * @param arguments параметри запуску
     * @throws IOException якщо виникла помилка роботи з файлами
     */
    public void run(AppArguments arguments) throws IOException {

        if (arguments.help()) {
            printHelp();
            return;
        }

        if (arguments.version()) {
            System.out.println(ProjectVersion.get());
            return;
        }

        List<String> lines = Files.readAllLines(
                arguments.input(),
                StandardCharsets.UTF_8);

        List<MenuItem> items = parseItems(lines);

        String report = createReport(items);

        System.out.print(report);

        writeReport(arguments.output(), report);
    }

    private List<MenuItem> parseItems(List<String> lines) {
        List<MenuItem> items = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            try {
                items.add(MenuItem.fromCsv(lines.get(i)));
            } catch (IllegalArgumentException exception) {
                System.err.printf(
                        "Пропущено рядок %d: %s%n",
                        i + 1,
                        exception.getMessage());
            }
        }

        return items;
    }

    private String createReport(List<MenuItem> items) {
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

    private void writeReport(
            Path output,
            String report) throws IOException {

        Path parent = output.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.writeString(
                output,
                report,
                StandardCharsets.UTF_8);
    }

    private void printHelp() {
        System.out.printf(
                "Використання:%n"
                        + "  java -jar lab01-%s.jar [options]%n"
                        + "%n"
                        + "Параметри:%n"
                        + "  --help          показати довідку%n"
                        + "  --version       показати версію програми%n"
                        + "  --input FILE    шлях до вхідного файла%n"
                        + "  --output FILE   шлях до файла звіту%n",
                ProjectVersion.get());
    }
}