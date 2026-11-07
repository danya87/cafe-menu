package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Тести логічної рівності MenuItem та поведінки у HashSet.
 */
class MenuItemEqualityTest {

    @Test
    void equalDishesAreEqual() {
        MenuItem first = new Dish(
                "Борщ",
                "Основні страви",
                145.50,
                350,
                25);

        MenuItem second = new Dish(
                "Борщ",
                "Основні страви",
                145.50,
                350,
                25);

        assertEquals(first, second);
        assertEquals(second, first);
    }

    @Test
    void equalObjectsHaveEqualHashCodes() {
        MenuItem first = new Drink(
                "Капучино",
                "Напої",
                75.00,
                250,
                7);

        MenuItem second = new Drink(
                "Капучино",
                "Напої",
                75.00,
                250,
                7);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void differentNamesMakeObjectsUnequal() {
        MenuItem first = new Dessert(
                "Сирники",
                "Десерти",
                120.00,
                220,
                15);

        MenuItem second = new Dessert(
                "Тірамісу",
                "Десерти",
                120.00,
                220,
                15);

        assertNotEquals(first, second);
    }

    @Test
    void differentPricesMakeObjectsUnequal() {
        MenuItem first = new Dish(
                "Борщ",
                "Основні страви",
                145.50,
                350,
                25);

        MenuItem second = new Dish(
                "Борщ",
                "Основні страви",
                150.00,
                350,
                25);

        assertNotEquals(first, second);
    }

    @Test
    void differentSubtypesAreNotEqual() {
        MenuItem dish = new Dish(
                "Позиція",
                "Основні страви",
                100.00,
                200,
                10);

        MenuItem drink = new Drink(
                "Позиція",
                "Напої",
                100.00,
                200,
                10);

        assertNotEquals(dish, drink);
    }

    @Test
    void objectIsEqualToItself() {
        MenuItem item = new Dish(
                "Борщ",
                "Основні страви",
                145.50,
                350,
                25);

        assertEquals(item, item);
    }

    @Test
    void objectIsNotEqualToNull() {
        MenuItem item = new Drink(
                "Капучино",
                "Напої",
                75.00,
                250,
                7);

        assertFalse(item.equals(null));
    }

    @Test
    void objectIsNotEqualToDifferentClass() {
        MenuItem item = new Dessert(
                "Сирники",
                "Десерти",
                120.00,
                220,
                15);

        assertNotEquals(item, "Сирники");
    }

    @Test
    void hashSetDoesNotDuplicateEqualObjects() {
        Set<MenuItem> items = new HashSet<>();

        items.add(new Dish(
                "Борщ",
                "Основні страви",
                145.50,
                350,
                25));

        items.add(new Dish(
                "Борщ",
                "Основні страви",
                145.50,
                350,
                25));

        assertEquals(1, items.size());
    }

    @Test
    void hashSetKeepsDifferentSubtypes() {
        Set<MenuItem> items = new HashSet<>();

        items.add(new Dish(
                "Позиція",
                "Основні страви",
                100.00,
                200,
                10));

        items.add(new Drink(
                "Позиція",
                "Напої",
                100.00,
                200,
                10));

        assertEquals(2, items.size());
    }

    @Test
    void hashSetContainsEquivalentObject() {
        Set<MenuItem> items = new HashSet<>();

        MenuItem first = new Dessert(
                "Сирники",
                "Десерти",
                120.00,
                220,
                15);

        MenuItem equivalent = new Dessert(
                "Сирники",
                "Десерти",
                120.00,
                220,
                15);

        items.add(first);

        assertTrue(items.contains(equivalent));
    }
}