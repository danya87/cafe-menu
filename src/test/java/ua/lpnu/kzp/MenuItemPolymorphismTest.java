package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Тести підтипів, enum і поліморфної поведінки MenuItem.
 */
class MenuItemPolymorphismTest {

    @Test
    void fromCsvCreatesDish() {
        MenuItem item = MenuItem.fromCsv(
                "Борщ український;Основні страви;145.50;350;25");

        assertInstanceOf(Dish.class, item);
        assertEquals(MenuKind.DISH, item.getKind());
    }

    @Test
    void fromCsvCreatesDrink() {
        MenuItem item = MenuItem.fromCsv(
                "Капучино;Напої;75.00;250;7");

        assertInstanceOf(Drink.class, item);
        assertEquals(MenuKind.DRINK, item.getKind());
    }

    @Test
    void fromCsvCreatesDessert() {
        MenuItem item = MenuItem.fromCsv(
                "Сирники;Десерти;120.00;220;15");

        assertInstanceOf(Dessert.class, item);
        assertEquals(MenuKind.DESSERT, item.getKind());
    }

    @Test
    void dishCalculatesPreparationCost() {
        MenuItem item = new Dish(
                "Борщ",
                "Основні страви",
                100.00,
                350,
                25);

        assertEquals(30.00, item.preparationCost(), 0.001);
    }

    @Test
    void drinkCalculatesPreparationCost() {
        MenuItem item = new Drink(
                "Капучино",
                "Напої",
                100.00,
                250,
                7);

        assertEquals(15.00, item.preparationCost(), 0.001);
    }

    @Test
    void dessertCalculatesPreparationCost() {
        MenuItem item = new Dessert(
                "Сирники",
                "Десерти",
                100.00,
                220,
                15);

        assertEquals(25.00, item.preparationCost(), 0.001);
    }

    @Test
    void subtypesHaveDifferentPolymorphicBehavior() {
        MenuItem dish = new Dish(
                "Страва",
                "Основні страви",
                100.00,
                300,
                20);

        MenuItem drink = new Drink(
                "Напій",
                "Напої",
                100.00,
                250,
                5);

        MenuItem dessert = new Dessert(
                "Десерт",
                "Десерти",
                100.00,
                200,
                10);

        assertNotEquals(
                dish.preparationCost(),
                drink.preparationCost());

        assertNotEquals(
                drink.preparationCost(),
                dessert.preparationCost());
    }

    @Test
    void polymorphicCollectionProcessesDifferentSubtypes() {
        List<MenuItem> items = List.of(
                new Dish(
                        "Страва",
                        "Основні страви",
                        100.00,
                        300,
                        20),
                new Drink(
                        "Напій",
                        "Напої",
                        100.00,
                        250,
                        5),
                new Dessert(
                        "Десерт",
                        "Десерти",
                        100.00,
                        200,
                        10));

        List<Double> costs = items.stream()
                .map(MenuItem::preparationCost)
                .toList();

        assertEquals(
                List.of(30.0, 15.0, 25.0),
                costs);
    }

    @Test
    void menuKindProvidesLabels() {
        assertEquals("Страва", MenuKind.DISH.label());
        assertEquals("Напій", MenuKind.DRINK.label());
        assertEquals("Десерт", MenuKind.DESSERT.label());
    }

    @Test
    void oldCategoryRemainsCompatibleAsDish() {
        MenuItem item = MenuItem.fromCsv(
                "Салат овочевий;Закуски;95.00;180;10");

        assertInstanceOf(Dish.class, item);
        assertEquals(MenuKind.DISH, item.getKind());
    }
}