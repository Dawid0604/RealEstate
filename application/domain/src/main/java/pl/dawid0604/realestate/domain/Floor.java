/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public record Floor(Integer value) {
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 100;

    // TODO: test it
    public Floor {
        if (value != null && (value < MIN_VALUE || value > MAX_VALUE)) {
            throw new InvalidArgumentValueException(
                    "Floor must be between " + MIN_VALUE + " and " + MAX_VALUE);
        }
    }
}
