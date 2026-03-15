/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import com.github.f4b6a3.uuid.UuidCreator;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

import java.util.UUID;

public final class Identifier {
    private final UUID value;

    private Identifier(final UUID value) {
        if (value == null) {
            throw new InvalidArgumentValueException("Value cannot be null");
        }

        this.value = value;
    }

    public static Identifier of(final UUID id) {
        return new Identifier(id);
    }

    public static Identifier generate() {
        return new Identifier(UuidCreator.getTimeOrderedEpoch());
    }

    public UUID getValue() {
        return value;
    }
}
