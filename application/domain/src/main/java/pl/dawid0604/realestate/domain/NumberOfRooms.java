/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public record NumberOfRooms(int value) {
    public NumberOfRooms {
        if (value < 0) {
            throw new InvalidArgumentValueException("Number of rooms cannot be negative");
        }
    }
}
