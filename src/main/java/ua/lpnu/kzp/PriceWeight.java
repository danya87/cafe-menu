package ua.lpnu.kzp;

import java.util.Locale;

/**
 * Зберігає допоміжне незмінне значення:
 * ціну та вагу позиції меню.
 *
 * @param price ціна позиції меню
 * @param weightG вага у грамах
 */
public record PriceWeight(double price, int weightG) {

    /**
     * Перевіряє коректність значень record.
     */
    public PriceWeight {
        if (!Double.isFinite(price)) {
            throw new IllegalArgumentException(
                    "ціна має бути скінченним числом");
        }

        if (price < 0) {
            throw new IllegalArgumentException(
                    "ціна не може бути від'ємною");
        }

        if (weightG < 0) {
            throw new IllegalArgumentException(
                    "вага не може бути від'ємною");
        }
    }

    /**
     * Повертає форматоване текстове представлення.
     *
     * @return ціна та вага
     */
    @Override
    public String toString() {
        return String.format(
                Locale.ROOT,
                "%.2f грн, %d г",
                price,
                weightG);
    }
}