package ua.lpnu.kzp;

/**
 * Десерт.
 */
public final class Dessert extends MenuItem {

    /**
     * Створює десерт.
     */
    public Dessert(
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
                validateKind(MenuKind.DESSERT));
    }

    /**
     * Для десертів вартість приготування становить 25 % ціни.
     */
    @Override
    public double preparationCost() {
        return getPrice() * 0.25;
    }
}