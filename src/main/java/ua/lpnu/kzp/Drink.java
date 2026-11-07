package ua.lpnu.kzp;

/**
 * Напій.
 */
public final class Drink extends MenuItem {

    /**
     * Створює напій.
     */
    public Drink(
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
                validateKind(MenuKind.DRINK));
    }

    /**
     * Для напоїв вартість приготування становить 15 % ціни.
     */
    @Override
    public double preparationCost() {
        return getPrice() * 0.15;
    }
}