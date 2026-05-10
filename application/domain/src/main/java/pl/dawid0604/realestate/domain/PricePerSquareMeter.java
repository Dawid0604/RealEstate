/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public final class PricePerSquareMeter {
    private static final int PRICE_SCALE = 2;

    private final BigDecimal value;
    private final MoneyCurrency currency;

    private PricePerSquareMeter(
            final BigDecimal pricePerSquareMeter, final MoneyCurrency currency) {

        BigDecimal value = pricePerSquareMeter;

        if (value != null) {
            value = pricePerSquareMeter.setScale(PRICE_SCALE, RoundingMode.HALF_UP);
        }

        this.value = value;
        this.currency = currency;
    }

    public static PricePerSquareMeter reconstitute(
            final BigDecimal value, final MoneyCurrency currency) {

        if (value != null && value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidArgumentValueException("PricePerSquareMeter cannot be negative");
        }

        return new PricePerSquareMeter(value, currency);
    }

    public static PricePerSquareMeter create(final Area area, final Price price) {
        if (area != null && price != null) {
            return new PricePerSquareMeter(calculate(area, price), price.currency());
        }

        return new PricePerSquareMeter(null, null);
    }

    public BigDecimal getValue() {
        return value;
    }

    private static BigDecimal calculate(final Area area, final Price price) {
        if (area.value() == null || price.value() == null) {
            return null;
        }

        return price.value().divide(area.value(), PRICE_SCALE, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof final PricePerSquareMeter that
                && Objects.equals(value, that.value)
                && Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, currency);
    }
}
