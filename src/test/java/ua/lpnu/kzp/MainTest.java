package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Тести консольного застосунку лабораторної роботи №1.
 */
class MainTest {

    @TempDir
    Path temporaryDirectory;

    /**
     * Перевіряє обробку одного коректного запису.
     *
     * @throws IOException якщо не вдалося працювати з тестовими файлами
     */
    @Test
    void correctLineProducesExpectedReport() throws IOException {
        String report = runProgram(
                "Борщ український;Основні страви;145.50;350;25");

        assertTrue(report.contains("Коректних записів: 1"));
        assertTrue(report.contains("Середня ціна: 145.50"));
        assertTrue(report.contains("Найбільша вага: 350 г"));
        assertTrue(report.contains(
                "Найдовший час приготування: 25 хв"));
    }

    /**
     * Перевіряє пропуск порожнього рядка.
     *
     * @throws IOException якщо не вдалося працювати з тестовими файлами
     */
    @Test
    void blankLineIsSkipped() throws IOException {
        String report = runProgram(
                "",
                "Капучино;Напої;75.00;250;7");

        assertTrue(report.contains("Коректних записів: 1"));
        assertTrue(report.contains("Середня ціна: 75.00"));
    }

    /**
     * Перевіряє пропуск запису з неправильною кількістю полів.
     *
     * @throws IOException якщо не вдалося працювати з тестовими файлами
     */
    @Test
    void wrongFieldCountIsSkipped() throws IOException {
        String report = runProgram(
                "Салат;Закуски;95.00;180",
                "Капучино;Напої;75.00;250;7");

        assertTrue(report.contains("Коректних записів: 1"));
        assertTrue(report.contains("Середня ціна: 75.00"));
    }

    /**
     * Перевіряє пропуск запису з нечисловою ціною.
     *
     * @throws IOException якщо не вдалося працювати з тестовими файлами
     */
    @Test
    void nonNumericPriceIsSkipped() throws IOException {
        String report = runProgram(
                "Вареники;Основні страви;помилка;300;20",
                "Сирники;Десерти;120.00;220;15");

        assertTrue(report.contains("Коректних записів: 1"));
        assertTrue(report.contains("Середня ціна: 120.00"));
    }

    /**
     * Перевіряє пропуск запису з від'ємним числовим значенням.
     *
     * @throws IOException якщо не вдалося працювати з тестовими файлами
     */
    @Test
    void negativeValueIsSkipped() throws IOException {
        String report = runProgram(
                "Лимонад;Напої;60.00;-400;5",
                "Капучино;Напої;75.00;250;7");

        assertTrue(report.contains("Коректних записів: 1"));
        assertTrue(report.contains("Найбільша вага: 250 г"));
    }

    /**
     * Перевіряє пропуск запису з порожнім обов'язковим полем.
     *
     * @throws IOException якщо не вдалося працювати з тестовими файлами
     */
    @Test
    void blankRequiredFieldIsSkipped() throws IOException {
        String report = runProgram(
                ";Десерти;100.00;200;10",
                "Сирники;Десерти;120.00;220;15");

        assertTrue(report.contains("Коректних записів: 1"));
        assertTrue(report.contains("Середня ціна: 120.00"));
    }

    /**
     * Перевіряє поведінку, коли всі записи некоректні.
     *
     * @throws IOException якщо не вдалося працювати з тестовими файлами
     */
    @Test
    void noValidRecordsProducesNoStatistics() throws IOException {
        String report = runProgram(
                "Лимонад;Напої;60.00;-400;5",
                "Вареники;Основні страви;помилка;300;20");

        assertTrue(report.contains("Коректних записів: 0"));
        assertTrue(report.contains(
                "Показники не обчислено: немає коректних записів."));
    }

    /**
     * Перевіряє обчислення середньої ціни для кількох записів.
     *
     * @throws IOException якщо не вдалося працювати з тестовими файлами
     */
    @Test
    void averagePriceIsCalculatedCorrectly() throws IOException {
        String report = runProgram(
                "Страва 1;Основні страви;100.00;200;10",
                "Страва 2;Десерти;200.00;300;20",
                "Страва 3;Закуски;150.00;250;15");

        assertTrue(report.contains("Коректних записів: 3"));
        assertTrue(report.contains("Середня ціна: 150.00"));
        assertTrue(report.contains("Найбільша вага: 300 г"));
        assertTrue(report.contains(
                "Найдовший час приготування: 20 хв"));
    }

    /**
     * Перевіряє читання і запис українського тексту у UTF-8.
     *
     * @throws IOException якщо не вдалося працювати з тестовими файлами
     */
    @Test
    void reportIsWrittenInUtf8() throws IOException {
        String report = runProgram(
                "Борщ український;Українська кухня;145.50;350;25");

        assertTrue(report.contains("Меню кафе"));
        assertTrue(report.contains("Середня ціна"));
        assertTrue(report.contains("Найбільша вага"));
    }

    /**
     * Перевіряє параметр командного рядка --version.
     */
    @Test
    void versionOptionPrintsProgramVersion() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {
            System.setOut(
                    new PrintStream(
                            buffer,
                            true,
                            StandardCharsets.UTF_8));

            Main.main(new String[] {"--version"});
        } finally {
            System.setOut(originalOut);
        }

        assertEquals(
                "1.0.0",
                buffer.toString(StandardCharsets.UTF_8).trim());
    }

    /**
     * Запускає програму з тимчасовими вхідним і вихідним файлами.
     *
     * @param lines рядки тестового CSV-файла
     * @return сформований програмою звіт
     * @throws IOException якщо не вдалося працювати з файлами
     */
    private String runProgram(String... lines) throws IOException {
        Path input = temporaryDirectory.resolve("input.csv");
        Path output = temporaryDirectory.resolve("report.txt");

        Files.write(
                input,
                List.of(lines),
                StandardCharsets.UTF_8);

        Main.main(new String[] {
                "--input", input.toString(),
                "--output", output.toString()
        });

        return Files.readString(
                output,
                StandardCharsets.UTF_8);
    }
}