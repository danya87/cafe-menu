package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Тести sealed-ієрархії та вичерпного switch.
 */
class MenuItemSealedTest {

    @Test
    void menuItemIsSealedWithExpectedSubtypes() {
        assertTrue(MenuItem.class.isSealed());

        Set<Class<?>> permitted = Set.of(
                MenuItem.class.getPermittedSubclasses());

        assertEquals(
                Set.of(
                        Dish.class,
                        Drink.class,
                        Dessert.class),
                permitted);
    }

    @Test
    void switchDescribesDish() {
        MenuItem item = new Dish(
                "Борщ",
                "Основні страви",
                145.50,
                350,
                25);

        assertEquals(
                "Страва",
                MenuItem.subtypeDescription(item));
    }

    @Test
    void switchDescribesDrink() {
        MenuItem item = new Drink(
                "Капучино",
                "Напої",
                75.00,
                250,
                7);

        assertEquals(
                "Напій",
                MenuItem.subtypeDescription(item));
    }

    @Test
    void switchDescribesDessert() {
        MenuItem item = new Dessert(
                "Сирники",
                "Десерти",
                120.00,
                220,
                15);

        assertEquals(
                "Десерт",
                MenuItem.subtypeDescription(item));
    }
}