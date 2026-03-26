/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public record BuiltYear(Integer value) {
    private static final int MIN_BUILT_YEAR = 1900;

    public BuiltYear {
        if (value != null && value < MIN_BUILT_YEAR) {
            throw new InvalidArgumentValueException(
                    "Built year cannot be before " + MIN_BUILT_YEAR);
        }
    }
}
