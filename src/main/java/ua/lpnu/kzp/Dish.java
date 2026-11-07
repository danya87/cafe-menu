package ua.lpnu.kzp;

/**
 * Основна страва.
 */
public final class Dish extends MenuItem {

    /**
     * Створює страву.
     */
    public Dish(
            String name,
            String category,
            double price,
            int weightG,
            int prepMin) {

        super(
                validateName(name),
                validateCategory(category),
                validatePrice(price),
                validateWeight(weightG),
                validatePrepTime(prepMin),
                validateKind(MenuKind.DISH));
    }

    /**
     * Для страв вартість приготування становить 30 % ціни.
     */
    @Override
    public double preparationCost() {
        return getPrice() * 0.30;
    }
}