package ua.lpnu.kzp;

import java.nio.file.Path;

/**
 * Зберігає параметри запуску програми.
 *
 * @param input шлях до вхідного файла
 * @param output шлях до файла звіту
 * @param help чи потрібно показати довідку
 * @param version чи потрібно показати версію
 */
public record AppArguments(
        Path input,
        Path output,
        boolean help,
        boolean version) {
}