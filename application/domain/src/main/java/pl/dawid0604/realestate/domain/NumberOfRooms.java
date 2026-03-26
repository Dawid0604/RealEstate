/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public record NumberOfRooms(Integer value) {
    public NumberOfRooms {
        if (value != null && value < 0) {
            throw new InvalidArgumentValueException("NumberOfRooms cannot be negative");
        }
    }
}
