/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.math.BigDecimal;

public record Area(BigDecimal value) {
    public Area {
        if (value != null && value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidArgumentValueException("Area must be greater than zero");
        }
    }
}
