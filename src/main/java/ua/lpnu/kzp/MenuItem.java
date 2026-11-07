package ua.lpnu.kzp;

import java.util.Locale;

/**
 * Базовий тип для позицій меню кафе.
 */
public abstract class MenuItem {

    private static final int FIELD_COUNT = 5;

    private final String name;
    private final String category;
    private final double price;
    private final int weightG;
    private final int prepMin;
    private final MenuKind kind;

    /**
     * Створює позицію меню.
     *
     * @param name назва
     * @param category категорія
     * @param price ціна
     * @param weightG вага у грамах
     * @param prepMin базовий час приготування
     * @param kind тип позиції меню
     */
    protected MenuItem(
            String name,
            String category,
            double price,
            int weightG,
            int prepMin,
            MenuKind kind) {

        this.name = name;
        this.category = category;
        this.price = price;
        this.weightG = weightG;
        this.prepMin = prepMin;
        this.kind = kind;
    }

    /**
     * Створює відповідний підтип MenuItem з CSV-рядка.
     *
     * @param line CSV-рядок
     * @return позиція меню
     */
    public static MenuItem fromCsv(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("порожній рядок");
        }

        String[] fields = line.split(";", -1);

        if (fields.length != FIELD_COUNT) {
            throw new IllegalArgumentException(
                    "очікується 5 полів, отримано " + fields.length);
        }

        String name = fields[0].trim();
        String category = fields[1].trim();

        double price =
                parseDouble(fields[2].trim(), "price");

        int weightG =
                parseInt(fields[3].trim(), "weightG");

        int prepMin =
                parseInt(fields[4].trim(), "prepMin");

        MenuKind kind = MenuKind.fromCategory(category);

        return switch (kind) {
            case DISH ->
                    new Dish(name, category, price, weightG, prepMin);

            case DRINK ->
                    new Drink(name, category, price, weightG, prepMin);

            case DESSERT ->
                    new Dessert(name, category, price, weightG, prepMin);
        };
    }

    /**
     * Повертає назву.
     *
     * @return назва
     */
    public final String getName() {
        return name;
    }

    /**
     * Повертає категорію.
     *
     * @return категорія
     */
    public final String getCategory() {
        return category;
    }

    /**
     * Повертає ціну.
     *
     * @return ціна
     */
    public final double getPrice() {
        return price;
    }

    /**
     * Повертає вагу.
     *
     * @return вага
     */
    public final int getWeightG() {
        return weightG;
    }

    /**
     * Повертає базовий час приготування.
     *
     * @return хвилини
     */
    public final int getPrepMin() {
        return prepMin;
    }

    /**
     * Повертає тип позиції меню.
     *
     * @return тип
     */
    public final MenuKind getKind() {
        return kind;
    }

    /**
     * Поліморфно обчислює вартість приготування позиції.
     *
     * @return розрахункова вартість приготування
     */
    public abstract double preparationCost();

    /**
     * Повертає допоміжне значення ціни та ваги.
     *
     * @return ціна та вага
     */
    public final PriceWeight getPriceWeight() {
        return new PriceWeight(price, weightG);
    }

    @Override
    public String toString() {
        return String.format(
                Locale.ROOT,
                "%s;%s;%.2f;%d;%d",
                name,
                category,
                price,
                weightG,
                prepMin);
    }

    /**
     * Створює Builder для позиції меню.
     *
     * @return новий Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder для створення конкретного підтипу MenuItem.
     */
    public static final class Builder {

        private String name;
        private String category;
        private double price;
        private int weightG;
        private int prepMin;

        public Builder name(String value) {
            this.name = value;
            return this;
        }

        public Builder category(String value) {
            this.category = value;
            return this;
        }

        public Builder price(double value) {
            this.price = value;
            return this;
        }

        public Builder weightG(int value) {
            this.weightG = value;
            return this;
        }

        public Builder prepMin(int value) {
            this.prepMin = value;
            return this;
        }

        /**
         * Створює конкретний підтип відповідно до категорії.
         *
         * @return створена позиція меню
         */
        public MenuItem build() {
            MenuKind kind = MenuKind.fromCategory(category);

            return switch (kind) {
                case DISH ->
                        new Dish(name, category, price, weightG, prepMin);
                case DRINK ->
                        new Drink(name, category, price, weightG, prepMin);
                case DESSERT ->
                        new Dessert(name, category, price, weightG, prepMin);
            };
        }
    }

    private static double parseDouble(
            String value,
            String fieldName) {

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "поле " + fieldName + " має бути числом",
                    exception);
        }
    }

    private static int parseInt(
            String value,
            String fieldName) {

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "поле " + fieldName + " має бути цілим числом",
                    exception);
        }
    }

    protected static String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "поле name не може бути порожнім");
        }

        return name.trim();
    }

    protected static String validateCategory(String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException(
                    "поле category не може бути порожнім");
        }

        return category.trim();
    }

    protected static double validatePrice(double price) {
        if (!Double.isFinite(price)) {
            throw new IllegalArgumentException(
                    "поле price має бути скінченним числом");
        }

        if (price < 0) {
            throw new IllegalArgumentException(
                    "поле price не може бути від'ємним");
        }

        return price;
    }

    protected static int validateWeight(int weightG) {
        if (weightG < 0) {
            throw new IllegalArgumentException(
                    "поле weightG не може бути від'ємним");
        }

        return weightG;
    }

    protected static int validatePrepTime(int prepMin) {
        if (prepMin < 0) {
            throw new IllegalArgumentException(
                    "поле prepMin не може бути від'ємним");
        }

        return prepMin;
    }

    protected static MenuKind validateKind(MenuKind kind) {
        if (kind == null) {
            throw new IllegalArgumentException(
                    "поле kind не може бути null");
        }

        return kind;
    }
}