/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public record Price(BigDecimal value, MoneyCurrency currency) {
    private static final BigDecimal MINIMUM_PRICE = BigDecimal.valueOf(10_000);
    private static final BigDecimal MAXIMUM_PRICE = BigDecimal.valueOf(1_000_000_000);

    public Price {
        if (currency == null) {
            throw new InvalidArgumentValueException("Currency cannot be null");
        }

        if (value != null) {
            if (value.compareTo(MINIMUM_PRICE) < 0) {
                throw new InvalidArgumentValueException(
                        "Value cannot be less than " + MINIMUM_PRICE);
            }

            if (value.compareTo(MAXIMUM_PRICE) > 0) {
                throw new InvalidArgumentValueException(
                        "Value cannot be greater than " + MAXIMUM_PRICE);
            }

            value = value.setScale(2, HALF_UP);
        }
    }
}
