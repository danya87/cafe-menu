package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Базові тести лабораторної роботи №1.
 */
class MainTest {

    /**
     * Перевіряє, що JUnit правильно підключений до Maven.
     */
    @Test
    void sampleCalculationIsCorrect() {
        assertEquals(4, 2 + 2);
    }
}