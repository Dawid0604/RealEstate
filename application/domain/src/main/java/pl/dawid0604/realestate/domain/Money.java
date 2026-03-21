/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static java.math.RoundingMode.HALF_UP;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.math.BigDecimal;

public record Money(BigDecimal value, MoneyCurrency currency) {
    private static final BigDecimal MINIMUM_PRICE = BigDecimal.valueOf(10_000);

    public Money {
        if (value != null && value.compareTo(MINIMUM_PRICE) < 0) {
            throw new InvalidArgumentValueException("Value cannot be less than " + MINIMUM_PRICE);
        }

        if (currency == null) {
            throw new InvalidArgumentValueException("Currency cannot be null");
        }

        if (value != null) {
            value = value.setScale(2, HALF_UP);
        }
    }
}
