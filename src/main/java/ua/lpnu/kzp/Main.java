package ua.lpnu.kzp;

import java.io.IOException;

/**
 * Точка входу консольного застосунку «Меню кафе».
 */
public final class Main {

    private Main() {
        // Utility class.
    }

    /**
     * Запускає програму.
     *
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        try {
            AppArguments arguments =
                    ArgumentsParser.parse(args);

            CafeMenuController controller =
                    new CafeMenuController();

            controller.run(arguments);

        } catch (IOException | IllegalArgumentException
                | IllegalStateException exception) {

            System.err.println(
                    "Помилка: " + exception.getMessage());
        }
    }
}