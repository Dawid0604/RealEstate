/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

import static org.apache.commons.lang3.StringUtils.isBlank;

import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;

public record Locality(Identifier id, String name) {

    public Locality {
        if (id == null) {
            throw new InvalidArgumentValueException("Id cannot be null");
        }

        if (isBlank(name)) {
            throw new InvalidArgumentValueException("Name cannot be blank");
        }
    }
}
