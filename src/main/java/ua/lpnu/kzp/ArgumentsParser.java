package ua.lpnu.kzp;

import java.nio.file.Path;

/**
 * Розбирає аргументи командного рядка.
 */
public final class ArgumentsParser {

    private static final Path DEFAULT_INPUT =
            Path.of("data", "input.csv");

    private static final Path DEFAULT_OUTPUT =
            Path.of("out", "report.txt");

    private ArgumentsParser() {
        // Utility class.
    }

    /**
     * Розбирає аргументи командного рядка.
     *
     * @param args аргументи програми
     * @return параметри запуску
     * @throws IllegalArgumentException якщо аргументи некоректні
     */
    public static AppArguments parse(String[] args) {
        Path input = DEFAULT_INPUT;
        Path output = DEFAULT_OUTPUT;
        boolean help = false;
        boolean version = false;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--help" -> help = true;

                case "--version" -> version = true;

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

        return new AppArguments(
                input,
                output,
                help,
                version);
    }
}