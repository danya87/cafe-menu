package ua.lpnu.kzp;

/**
 * Тип позиції меню.
 */
public enum MenuKind {

    DISH("Страва"),
    DRINK("Напій"),
    DESSERT("Десерт");

    private final String label;

    MenuKind(String label) {
        this.label = label;
    }

    /**
     * Повертає текстове представлення типу.
     *
     * @return назва типу
     */
    public String label() {
        return label;
    }

    /**
     * Визначає тип позиції меню за категорією.
     *
     * <p>Відомі категорії напоїв і десертів відображаються
     * у відповідні типи. Інші непорожні категорії вважаються
     * стравами для збереження сумісності з даними Lab 2.</p>
     *
     * @param category категорія позиції меню
     * @return тип позиції меню
     * @throws IllegalArgumentException якщо категорія порожня
     */
    public static MenuKind fromCategory(String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException(
                    "поле category не може бути порожнім");
        }

        String normalized = category.trim();

        if (normalized.equalsIgnoreCase("Напої")) {
            return DRINK;
        }

        if (normalized.equalsIgnoreCase("Десерти")) {
            return DESSERT;
        }

        return DISH;
    }
}