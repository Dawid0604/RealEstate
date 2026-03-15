/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.math.BigDecimal;

public record Money(BigDecimal value, MoneyCurrency currency) {

    public Money {
        if (value == null) {
            throw new InvalidArgumentValueException("Value cannot be null");
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidArgumentValueException("Value cannot be less than or equal to zero");
        }

        if (currency == null) {
            throw new InvalidArgumentValueException("Currency cannot be null");
        }
    }
}
