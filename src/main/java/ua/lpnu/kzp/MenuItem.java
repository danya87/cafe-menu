package ua.lpnu.kzp;

import java.util.Locale;

/**
 * Представляє одну позицію меню кафе.
 */
public final class MenuItem {

    private static final int FIELD_COUNT = 5;

    private final String name;
    private final String category;
    private final double price;
    private final int weightG;
    private final int prepMin;

    /**
     * Створює позицію меню та перевіряє коректність її значень.
     *
     * @param name назва позиції меню
     * @param category категорія позиції
     * @param price ціна
     * @param weightG вага у грамах
     * @param prepMin час приготування у хвилинах
     * @throws IllegalArgumentException якщо значення некоректні
     */
    public MenuItem(
            String name,
            String category,
            double price,
            int weightG,
            int prepMin) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "поле name не може бути порожнім");
        }

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException(
                    "поле category не може бути порожнім");
        }

        if (!Double.isFinite(price)) {
            throw new IllegalArgumentException(
                    "поле price має бути скінченним числом");
        }

        if (price < 0) {
            throw new IllegalArgumentException(
                    "поле price не може бути від'ємним");
        }

        if (weightG < 0) {
            throw new IllegalArgumentException(
                    "поле weightG не може бути від'ємним");
        }

        if (prepMin < 0) {
            throw new IllegalArgumentException(
                    "поле prepMin не може бути від'ємним");
        }

        this.name = name.trim();
        this.category = category.trim();
        this.price = price;
        this.weightG = weightG;
        this.prepMin = prepMin;
    }

    /**
     * Створює позицію меню з одного CSV-рядка.
     *
     * @param line рядок формату name;category;price;weightG;prepMin
     * @return створена позиція меню
     * @throws IllegalArgumentException якщо рядок має неправильний формат
     *                                  або містить некоректні значення
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
        double price = parseDouble(fields[2].trim(), "price");
        int weightG = parseInt(fields[3].trim(), "weightG");
        int prepMin = parseInt(fields[4].trim(), "prepMin");

        return new MenuItem(
                name,
                category,
                price,
                weightG,
                prepMin);
    }

    /**
     * Створює новий Builder для MenuItem.
     *
     * @return новий Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Повертає назву позиції меню.
     *
     * @return назва
     */
    public String getName() {
        return name;
    }

    /**
     * Повертає категорію позиції меню.
     *
     * @return категорія
     */
    public String getCategory() {
        return category;
    }

    /**
     * Повертає ціну.
     *
     * @return ціна
     */
    public double getPrice() {
        return price;
    }

    /**
     * Повертає вагу у грамах.
     *
     * @return вага
     */
    public int getWeightG() {
        return weightG;
    }

    /**
     * Повертає час приготування у хвилинах.
     *
     * @return час приготування
     */
    public int getPrepMin() {
        return prepMin;
    }

    /**
     * Повертає допоміжне незмінне представлення ціни та ваги.
     *
     * @return ціна та вага позиції меню
     */
    public PriceWeight getPriceWeight() {
        return new PriceWeight(price, weightG);
    }

    /**
     * Повертає текстове представлення позиції меню.
     *
     * @return форматований рядок
     */
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

    /**
     * Builder для поетапного створення MenuItem.
     *
     * <p>Метод build() використовує основний конструктор MenuItem,
     * тому не обходить перевірку інваріантів.</p>
     */
    public static final class Builder {

        private String name;
        private String category;
        private double price;
        private int weightG;
        private int prepMin;

        /**
         * Задає назву.
         *
         * @param value назва
         * @return цей Builder
         */
        public Builder name(String value) {
            this.name = value;
            return this;
        }

        /**
         * Задає категорію.
         *
         * @param value категорія
         * @return цей Builder
         */
        public Builder category(String value) {
            this.category = value;
            return this;
        }

        /**
         * Задає ціну.
         *
         * @param value ціна
         * @return цей Builder
         */
        public Builder price(double value) {
            this.price = value;
            return this;
        }

        /**
         * Задає вагу у грамах.
         *
         * @param value вага
         * @return цей Builder
         */
        public Builder weightG(int value) {
            this.weightG = value;
            return this;
        }

        /**
         * Задає час приготування.
         *
         * @param value час у хвилинах
         * @return цей Builder
         */
        public Builder prepMin(int value) {
            this.prepMin = value;
            return this;
        }

        /**
         * Створює MenuItem через основний конструктор.
         *
         * @return коректний MenuItem
         */
        public MenuItem build() {
            return new MenuItem(
                    name,
                    category,
                    price,
                    weightG,
                    prepMin);
        }
    }
}