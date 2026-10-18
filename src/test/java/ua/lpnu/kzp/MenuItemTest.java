package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Тести класів MenuItem та PriceWeight.
 */
class MenuItemTest {

    @Test
    void constructorCreatesValidMenuItem() {
        MenuItem item = new MenuItem(
                "Борщ український",
                "Основні страви",
                145.50,
                350,
                25);

        assertEquals("Борщ український", item.getName());
        assertEquals("Основні страви", item.getCategory());
        assertEquals(145.50, item.getPrice(), 0.001);
        assertEquals(350, item.getWeightG());
        assertEquals(25, item.getPrepMin());
    }

    @Test
    void constructorRejectsNullName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(
                        null,
                        "Напої",
                        75.00,
                        250,
                        7));
    }

    @Test
    void constructorRejectsBlankName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(
                        "   ",
                        "Напої",
                        75.00,
                        250,
                        7));
    }

    @Test
    void constructorRejectsBlankCategory() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(
                        "Капучино",
                        "   ",
                        75.00,
                        250,
                        7));
    }

    @Test
    void constructorAcceptsZeroBoundaryValues() {
        MenuItem item = new MenuItem(
                "Вода",
                "Напої",
                0.0,
                0,
                0);

        assertEquals(0.0, item.getPrice(), 0.001);
        assertEquals(0, item.getWeightG());
        assertEquals(0, item.getPrepMin());
    }

    @Test
    void constructorRejectsNegativePrice() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(
                        "Кава",
                        "Напої",
                        -1.0,
                        200,
                        5));
    }

    @Test
    void constructorRejectsNegativeWeight() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(
                        "Сирники",
                        "Десерти",
                        120.00,
                        -1,
                        15));
    }

    @Test
    void constructorRejectsNegativePrepTime() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MenuItem(
                        "Салат",
                        "Закуски",
                        95.00,
                        180,
                        -1));
    }

    @Test
    void fromCsvParsesValidLine() {
        MenuItem item = MenuItem.fromCsv(
                "Капучино;Напої;75.00;250;7");

        assertEquals("Капучино", item.getName());
        assertEquals("Напої", item.getCategory());
        assertEquals(75.00, item.getPrice(), 0.001);
        assertEquals(250, item.getWeightG());
        assertEquals(7, item.getPrepMin());
    }

    @Test
    void fromCsvRejectsWrongFieldCount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> MenuItem.fromCsv(
                        "Салат;Закуски;95.00;180"));
    }

    @Test
    void fromCsvRejectsNonNumericValue() {
        assertThrows(
                IllegalArgumentException.class,
                () -> MenuItem.fromCsv(
                        "Вареники;Основні страви;помилка;300;20"));
    }

    @Test
    void fromCsvRejectsInvariantViolation() {
        assertThrows(
                IllegalArgumentException.class,
                () -> MenuItem.fromCsv(
                        "Лимонад;Напої;60.00;-400;5"));
    }

    @Test
    void priceWeightProvidesComponents() {
        PriceWeight value = new PriceWeight(145.50, 350);

        assertEquals(145.50, value.price(), 0.001);
        assertEquals(350, value.weightG());
    }

    @Test
    void priceWeightObjectsWithSameValuesAreEqual() {
        PriceWeight first = new PriceWeight(145.50, 350);
        PriceWeight second = new PriceWeight(145.50, 350);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void getPriceWeightReturnsItemPriceAndWeight() {
        MenuItem item = new MenuItem(
                "Борщ",
                "Основні страви",
                145.50,
                350,
                25);

        PriceWeight value = item.getPriceWeight();

        assertEquals(145.50, value.price(), 0.001);
        assertEquals(350, value.weightG());
    }

    @Test
    void priceWeightAcceptsZeroBoundaryValues() {
        PriceWeight value = new PriceWeight(0.0, 0);

        assertEquals(0.0, value.price(), 0.001);
        assertEquals(0, value.weightG());
    }

    @Test
    void priceWeightRejectsNegativePrice() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PriceWeight(-1.0, 100));
    }

    @Test
    void priceWeightRejectsNegativeWeight() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PriceWeight(10.0, -1));
    }

    @Test
    void builderCreatesValidMenuItem() {
        MenuItem item = MenuItem.builder()
                .name("Капучино")
                .category("Напої")
                .price(75.00)
                .weightG(250)
                .prepMin(7)
                .build();

        assertEquals("Капучино", item.getName());
        assertEquals("Напої", item.getCategory());
        assertEquals(75.00, item.getPrice(), 0.001);
        assertEquals(250, item.getWeightG());
        assertEquals(7, item.getPrepMin());
    }

    @Test
    void builderCannotBypassConstructorValidation() {
        assertThrows(
                IllegalArgumentException.class,
                () -> MenuItem.builder()
                        .name("Капучино")
                        .category("Напої")
                        .price(-10.00)
                        .weightG(250)
                        .prepMin(7)
                        .build());
    }
}