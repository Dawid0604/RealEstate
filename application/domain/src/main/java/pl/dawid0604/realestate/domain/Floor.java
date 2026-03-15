/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public record Floor(Integer value) {
    public Floor {
        if (value != null && value < 0) {
            throw new InvalidArgumentValueException("Floor cannot be negative");
        }
    }
}
